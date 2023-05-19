package com.hichina.main.back.hichinamainback.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.model.DTO.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.hichina.main.back.hichinamainback.mapper.HichinaProductMapper;
import com.hichina.main.back.hichinamainback.mapper.ProductSkuIntAttributeMappingMapper;
import com.hichina.main.back.hichinamainback.mapper.ProductSkuVarcharAttributeMappingMapper;
import com.hichina.main.back.hichinamainback.mapper.ProductSkuTimestampAttributeMappingMapper;

@RestController
@RequestMapping("/api/public/productsku")
public class PublicProductSkuController {
    @Autowired
    private HichinaProductMapper hichinaProductMapper;

    @Autowired
    private ProductSkuTimestampAttributeMappingMapper productSkuTimestampAttributeMappingMapper;

    @Autowired
    private ProductSkuVarcharAttributeMappingMapper productSkuVarcharAttributeMappingMapper;

    @Autowired
    private ProductSkuIntAttributeMappingMapper productSkuIntAttributeMappingMapper;

    private ProductPropertyBag changeDateStringFormat(ProductPropertyBag input){
        String dateString = input.getAttributeValue();

        //2023-03-22 00:00:00
        SimpleDateFormat inputformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = inputformatter.parse(dateString);
            SimpleDateFormat outputformatter = new SimpleDateFormat("yyyy/MM/dd");
            String formatted = outputformatter.format(date);
            input.setAttributeValue(formatted);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
        return input;
    }
    /**
     * main logic to get custom property bag, each skuId can have more then 1 property from each typed attribute table
     * @param skuId
     * @return
     */
    private List<ProductPropertyBag> getPropertyBagBySkuId(String skuId){
        List<ProductPropertyBag> propertyBags = new ArrayList<>();

        // TODO: 2023/3/18 if added other type, we should update here
        List<ProductPropertyBag> intPropertyBag = productSkuIntAttributeMappingMapper.findAttributeValueBySkuId(skuId);
        List<ProductPropertyBag> varcharPropertyBag = productSkuVarcharAttributeMappingMapper.findAttributeValueBySkuId(skuId);
        List<ProductPropertyBag> datePropertyBag = productSkuTimestampAttributeMappingMapper.findAttributeValueBySkuId(skuId);
        datePropertyBag = datePropertyBag.stream().map(r->changeDateStringFormat(r)).collect(Collectors.toList());

        propertyBags.addAll(intPropertyBag);
        propertyBags.addAll(varcharPropertyBag);
        propertyBags.addAll(datePropertyBag);

        propertyBags.sort(Comparator.comparing(ProductPropertyBag::getSequence));

        List<ProductAttributeTypeShortDTO> bindedAttributes = hichinaProductMapper.findBindedAttributesBySkuId(skuId);

        List<ProductPropertyBag> finalPropertyBag = fillEmptyPropertyBag(propertyBags, bindedAttributes);

        return finalPropertyBag;
    }

    /**
    check if all attribute Id in bindedAttributes is covered by propertyBags, otherwise add empty ProductPropertyBag to make up for the lost
     */
    private List<ProductPropertyBag> fillEmptyPropertyBag(List<ProductPropertyBag> propertyBags, List<ProductAttributeTypeShortDTO> bindedAttributes){
        List<String> existingAttributeIds = propertyBags.stream().map(r ->r.getAttributeId()).collect(Collectors.toList());

        List<ProductPropertyBag> toAdd = new ArrayList<>();

        for(ProductAttributeTypeShortDTO att : bindedAttributes){
            if(existingAttributeIds.isEmpty() || (!existingAttributeIds.isEmpty() && !existingAttributeIds.contains(att.getAttributeId()))){
                ProductPropertyBag placeHolderObj = new ProductPropertyBag();
                placeHolderObj.setAttributeId(att.getAttributeId());
                placeHolderObj.setDataType(att.getDataType());
                placeHolderObj.setAttributeValue(null);
                placeHolderObj.setAttributeName(att.getAttributeName());
                toAdd.add(placeHolderObj);
            }
        }
        propertyBags.addAll(toAdd);

        return propertyBags;
    }

    @GetMapping("/productskugrouplist")
    @EnableHichinaAutoLog(description = "Get product sku group list by page")
    public HichinaResponse getProductSkuGroupList(@RequestParam(value = "page", required = true) Integer page,
                                                  @RequestParam(value = "pageSize", required = true) Integer size,
                                                  @RequestParam(value = "query") String query,
                                                  @RequestParam(value = "productTypeId") String productTypeId){
        HichinaResponse ret = new HichinaResponse();

        if(page>0){
            PageHelper.startPage(page,size);
        }

        Integer cnt = 0;
        List<HichinaProductListDTO> productListDTOS;

        if(StringUtils.isEmpty(productTypeId)){
            if(StringUtils.isEmpty(query)){
                productListDTOS = hichinaProductMapper.getAllProductList();
                cnt = hichinaProductMapper.countAllProductList();
            }else{
                productListDTOS = hichinaProductMapper.getAllProductListByQuery(query);
                cnt = hichinaProductMapper.countAllProductListByQuery(query);
            }
        }else{
            if(StringUtils.isEmpty(query)){
                productListDTOS = hichinaProductMapper.getAllProductListByProductTypeId(productTypeId);
                cnt = hichinaProductMapper.countAllProductListByProductTypeId(productTypeId);
            }else{
                productListDTOS = hichinaProductMapper.getAllProductListByQueryAndProductTypeId(query, productTypeId);
                cnt = hichinaProductMapper.countAllProductListByQueryAndProductTypeId(query, productTypeId);
            }
        }

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(productListDTOS);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);
        ret.setMessage("Successfully get product list");

        return ret;
    }

    @GetMapping("/bygroupidwithpropertybag")
    @EnableHichinaAutoLog(description = "Get product skus by group id")
    public HichinaResponse getProductSkusByGroupId(@RequestParam(value="skuGroupId",required = true) String skuGroupId){
        HichinaResponse ret = new HichinaResponse();


        // get the basic property
        List<HichinaProductBasicDTO> skuInList = hichinaProductMapper.findBySkuGroupId(skuGroupId);



        List<HichinaProductWithPropertyBagDTO> data = new ArrayList<>();
        for(HichinaProductBasicDTO sku : skuInList){
            List<ProductPropertyBag> propertyBags = getPropertyBagBySkuId(sku.getSkuId());
            HichinaProductWithPropertyBagDTO item = new HichinaProductWithPropertyBagDTO();
            item.setHichinaProductBasicDTO(sku);
            item.setProductPropertyBag(propertyBags);
            data.add(item);
        }

        ret.setMessage(String.format("成功获取skuGroupId为%s的所有属性",skuGroupId));
        ret.setData(data);
        ret.setOk(true);

        return ret;
    }
}
