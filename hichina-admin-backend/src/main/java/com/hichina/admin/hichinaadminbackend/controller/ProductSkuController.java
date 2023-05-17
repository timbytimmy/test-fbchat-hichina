package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.*;
import com.hichina.admin.hichinaadminbackend.model.*;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import com.hichina.admin.hichinaadminbackend.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productsku")
public class ProductSkuController {
    @Autowired
    private HichinaProductMapper hichinaProductMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired ProductSkuGroupMapper productSkuGroupMapper;

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

    @GetMapping("/withpropertybag")
    public HichinaResponse getProductSkuById(@RequestParam(value="skuId",required = true) String skuId){
        HichinaResponse ret = new HichinaResponse();

        // get the basic property
        List<HichinaProductDTO> singleSkuInList = hichinaProductMapper.findBySkuId(skuId);


        HichinaProductDTO singleSku;
        if(!singleSkuInList.isEmpty()){
            singleSku = singleSkuInList.get(0);
        }else{
            singleSku = null;
        }

        List<ProductPropertyBag> propertyBags = getPropertyBagBySkuId(skuId);

        HichinaProductWithPropertyBagDTO data = new HichinaProductWithPropertyBagDTO();
        data.setHichinaProductDTO(singleSku);
        data.setProductPropertyBag(propertyBags);

        ret.setMessage("成功获取skuId: "+skuId+"的所有属性");
        ret.setData(data);
        ret.setOk(true);

        return ret;
    }
    private boolean checkUseMineOnly(String currentUserName){
        AdminUser adminUser = userUtil.currentUser(currentUserName);
        return adminUser.getUsername().startsWith("SP_");
    }

    @GetMapping
    public HichinaResponse getProductSkuNoCustomAttributes(@RequestParam(value = "page", required = true) Integer page,
                                                                @RequestParam(value = "pageSize", required = true) Integer size,
                                                                @RequestParam(value = "query") String query,
                                         @RequestParam(value = "productTypeId") String productTypeId){
        HichinaResponse ret = new HichinaResponse();
        String currentUser = userUtil.currentUserName();
        boolean mineOnly = checkUseMineOnly(currentUser);

        if(page>0){
            PageHelper.startPage(page,size);
        }


        List<HichinaProductDTO> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            if(!StringUtils.isEmpty(productTypeId)){
                rawData = hichinaProductMapper.findByProductTypeIdAndQuery(productTypeId, query, mineOnly, currentUser);
                cnt = hichinaProductMapper.countByProductTypeIdAndQuery(productTypeId, query, mineOnly, currentUser);
            }else{
                rawData = hichinaProductMapper.findByQuery(query, mineOnly, currentUser);
                cnt = hichinaProductMapper.countByQuery(query, mineOnly, currentUser);
            }
        }else{
            if(!StringUtils.isEmpty(productTypeId)){
                rawData = hichinaProductMapper.findByProductTypeId(productTypeId, mineOnly, currentUser);
                cnt = hichinaProductMapper.countByProductTypeId(productTypeId, mineOnly, currentUser);
            }else{
                rawData = hichinaProductMapper.findAllProduct(mineOnly, currentUser);
                cnt = hichinaProductMapper.count(mineOnly, currentUser);
            }
        }
        ret.setMessage("Successfully got all lines");
        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);
        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }

    /**
     * whether exist or not, return the group id, if not exist, insert first
     * @param productName
     * @param productTypeId
     * @return
     */
    private String checkProductGroupExistence(String productName, String productTypeId){
        List<ProductSkuGroup> productSkuGroups = productSkuGroupMapper.findByNameAndProductTypeId(productName, productTypeId);
        String productSkuGroupId;
        if(productSkuGroups.isEmpty()){
            productSkuGroupId = java.util.UUID.randomUUID().toString();
            productSkuGroupMapper.insert(new ProductSkuGroup(productSkuGroupId, productName, productTypeId , new Date(), true));
        }else{
            productSkuGroupId = productSkuGroups.get(0).getSkuGroupId();
        }
        return productSkuGroupId;
    }

    private String insertProductSkuInTransaction(ProductSkuCreateDTO request){
        HichinaProduct product = new HichinaProduct();
        product.setProductName(request.getProductName());
        product.setProductContent(request.getProductDescription());
        product.setProductTypeId(request.getProductTypeId());
        product.setSkuId(java.util.UUID.randomUUID().toString());
        product.setCreatedTime(new Date());
        product.setCreatedBy(userUtil.currentUserName());
        // check if sku group of name request.getProductName() and request.getProductTypeId() exist, if not, insert new sku group, and set corresponding sku_group_id in product
        String productSkuGroupId = checkProductGroupExistence(request.getProductName(), request.getProductTypeId());
        product.setSkuGroupId(productSkuGroupId);
        // insert common attribute
        hichinaProductMapper.insert(product);
        // note to make all in a single transaction
        // insert custom property bag into various tables
        for (Map.Entry<String, String> entry : request.getCustomPropertyBag().entrySet()) {
            String dataTypeAndAttributeId = entry.getKey();
            String value = entry.getValue();
            String dataType = dataTypeAndAttributeId.substring(dataTypeAndAttributeId.indexOf("[")+1, dataTypeAndAttributeId.indexOf("]"));
            String attributeId = dataTypeAndAttributeId.substring(dataTypeAndAttributeId.indexOf("]")+1);
            if(dataType.equals("string") || dataType.equals("image")  || dataType.equals("datestring")){
                ProductSkuVarcharAttributeMapping productSkuVarcharAttributeMapping = new ProductSkuVarcharAttributeMapping();
                productSkuVarcharAttributeMapping.setSkuId(product.getSkuId());
                productSkuVarcharAttributeMapping.setAttributeValue(value);
                productSkuVarcharAttributeMapping.setDataType(dataType);
                productSkuVarcharAttributeMapping.setAttributeId(attributeId);
                productSkuVarcharAttributeMappingMapper.insert(productSkuVarcharAttributeMapping);
            }else if(dataType.equals("integer")){
                ProductSkuIntAttributeMapping productSkuIntAttributeMapping = new ProductSkuIntAttributeMapping();
                productSkuIntAttributeMapping.setSkuId(product.getSkuId());
                productSkuIntAttributeMapping.setAttributeValue(Integer.parseInt(value));
                productSkuIntAttributeMapping.setDataType(dataType);
                productSkuIntAttributeMapping.setAttributeId(attributeId);
                productSkuIntAttributeMappingMapper.insert(productSkuIntAttributeMapping);
            }else if(dataType.equals("date")){
                ProductSkuTimestampAttributeMapping productSkuTimestampAttributeMapping = new ProductSkuTimestampAttributeMapping();
                productSkuTimestampAttributeMapping.setSkuId(product.getSkuId());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                Date date = null;
                try {
                    date = formatter.parse(value);
                    productSkuTimestampAttributeMapping.setAttributeValue(date);
                    productSkuTimestampAttributeMapping.setDataType(dataType);
                    productSkuTimestampAttributeMapping.setAttributeId(attributeId);
                    productSkuTimestampAttributeMappingMapper.insert(productSkuTimestampAttributeMapping);
                } catch (ParseException e) {
                    throw new RuntimeException("Invalid date format");
                }
            }else{
                throw new RuntimeException("Not supported data type!");
            }
        }
        return product.getSkuId();
    }
    @PostMapping
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public HichinaResponse createProductSku(@RequestBody ProductSkuCreateDTO request){
        HichinaResponse ret = new HichinaResponse();

        String skuId = insertProductSkuInTransaction(request);

        ret.setData(skuId);
        ret.setOk(true);
        ret.setMessage("成功新建产品");

        return ret;
    }

    @PutMapping("/withpropertybags/{skuId}")
    @Transactional
    public HichinaResponse updateProductWithAllProperties(@PathVariable("skuId") String skuId ,@RequestBody ProductSkuUpdateDTO request){
        HichinaResponse ret = new HichinaResponse();

        List<HichinaProduct> hichinaProducts = hichinaProductMapper.findRawBySkuId(skuId);
        if(hichinaProducts.isEmpty()){
            throw new RuntimeException("Nothing to update");
        }
        HichinaProduct toUpdate = hichinaProducts.get(0);
        if(!request.getProductName().equals(toUpdate.getProductName())){
            // indicating name change, so must update sku group id
            String skuGroupId = checkProductGroupExistence(request.getProductName(), toUpdate.getProductTypeId());
            toUpdate.setSkuGroupId(skuGroupId);
        }
        toUpdate.setProductName(request.getProductName());
        toUpdate.setProductContent(request.getProductContent());
        // update basic info
        hichinaProductMapper.updateBySkuId(toUpdate);

        productSkuGroupMapper.deleteUnreferenced();

        // update property bags
        List<ProductPropertyBag> propertyBags = request.getPropertyBags();

        updatePropertyBagsBySkuId(skuId, propertyBags);

        ret.setOk(true);
        ret.setData(skuId);
        ret.setMessage("成功更新skuId:"+skuId+"的产品");

        return ret;
    }

    @DeleteMapping("/batch")
    @Transactional
    public HichinaResponse deleteProductSkus(@RequestBody ProductSkuBatchDeleteRequest req){

        // delete all related custom properties
        productSkuTimestampAttributeMappingMapper.batchDelete(req.getToDelete());
        productSkuIntAttributeMappingMapper.batchDelete(req.getToDelete());
        productSkuVarcharAttributeMappingMapper.batchDelete(req.getToDelete());
        // delete base product record
        hichinaProductMapper.batchDelete(req.getToDelete());
        // remove unreferenced group
        productSkuGroupMapper.deleteUnreferenced();

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除product sku");

        return ret;
    }

    private void updatePropertyBagsBySkuId(String skuId, List<ProductPropertyBag> propertyBags){
        // first filter out those propertybag with no value
        List<ProductPropertyBag> nonEmptyPropertyBag  = propertyBags.stream().filter(r -> !Strings.isEmpty(r.getAttributeValue())).collect(Collectors.toList());

        for(ProductPropertyBag item: nonEmptyPropertyBag){
            if("integer".equals(item.getDataType())){
                ProductSkuIntAttributeMapping upsertObj = new ProductSkuIntAttributeMapping();
                upsertObj.setAttributeId(item.getAttributeId());
                upsertObj.setSkuId(skuId);
                upsertObj.setDataType(item.getDataType());
                upsertObj.setAttributeValue(Integer.parseInt(item.getAttributeValue()));
                productSkuIntAttributeMappingMapper.upsertBySkuIdAndAttributeId(upsertObj);
            }else if("string".equals(item.getDataType()) || "image".equals(item.getDataType()) || "datestring".equals(item.getDataType())){
                ProductSkuVarcharAttributeMapping upsertObj = new ProductSkuVarcharAttributeMapping();
                upsertObj.setAttributeId(item.getAttributeId());
                upsertObj.setSkuId(skuId);
                upsertObj.setDataType(item.getDataType());
                upsertObj.setAttributeValue(item.getAttributeValue());
                productSkuVarcharAttributeMappingMapper.upsertBySkuIdAndAttributeId(upsertObj);
            }else if("date".equals(item.getDataType())){
                ProductSkuTimestampAttributeMapping upsertObj = new ProductSkuTimestampAttributeMapping();
                upsertObj.setAttributeId(item.getAttributeId());
                upsertObj.setSkuId(skuId);
                upsertObj.setDataType(item.getDataType());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                Date date = null;
                try {
                    date = formatter.parse(item.getAttributeValue());
                    upsertObj.setAttributeValue(date);
                    productSkuTimestampAttributeMappingMapper.upsertBySkuIdAndAttributeId(upsertObj);
                }catch (ParseException e) {
                    throw new RuntimeException("Invalid date format");
                }
            }else{
                // do nothing
            }
        }
    }
}
