package com.hichina.admin.hichinaadminbackend.service;

import com.hichina.admin.hichinaadminbackend.mapper.*;
import com.hichina.admin.hichinaadminbackend.model.*;
import com.hichina.admin.hichinaadminbackend.model.DTO.ProductPropertyBag;
import com.hichina.admin.hichinaadminbackend.model.DTO.ProductSkuUpdateDTO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProductSkuService {

    @Autowired
    private ProductSkuIntAttributeMappingMapper productSkuIntAttributeMappingMapper;

    @Autowired
    private ProductSkuVarcharAttributeMappingMapper productSkuVarcharAttributeMappingMapper;

    @Autowired
    private HichinaProductMapper hichinaProductMapper;

    @Autowired
    private ProductSkuTimestampAttributeMappingMapper productSkuTimestampAttributeMappingMapper;

    @Autowired
    ProductSkuGroupMapper productSkuGroupMapper;

    @Transactional
    public void postProcessSkuGroup(HichinaProduct toUpdate){
        productSkuGroupMapper.updateMinPriceAndImage(toUpdate.getSkuGroupId());
        productSkuGroupMapper.deleteUnreferenced();
    }

    @Transactional
    public void postProcessSkuGroupV2(List<String> skuGroupIds){
        for(String affectedSkuGroupId: skuGroupIds){
            productSkuGroupMapper.updateMinPriceAndImage(affectedSkuGroupId);
        }
        productSkuGroupMapper.deleteUnreferenced();
    }

    @Transactional
    public void deleteProductSkus(List<String> skuIds){
        // delete all related custom properties
        productSkuTimestampAttributeMappingMapper.batchDelete(skuIds);
        productSkuIntAttributeMappingMapper.batchDelete(skuIds);
        productSkuVarcharAttributeMappingMapper.batchDelete(skuIds);
        // delete base product record
        hichinaProductMapper.batchDelete(skuIds);
    }

    @Transactional
    public void updateProductContentOnly(String skuId, String content){
        HichinaProduct ret = new HichinaProduct();
        List<HichinaProduct> hichinaProducts = hichinaProductMapper.findRawBySkuId(skuId);
        if(hichinaProducts.isEmpty()){
            throw new RuntimeException("Nothing to update");
        }
        HichinaProduct toUpdate = hichinaProducts.get(0);
        toUpdate.setProductContent(content);
        hichinaProductMapper.updateBySkuId(toUpdate);
    }

    @Transactional
    public HichinaProduct updateProducts(String skuId , ProductSkuUpdateDTO request){
        List<HichinaProduct> hichinaProducts = hichinaProductMapper.findRawBySkuId(skuId);
        if(hichinaProducts.isEmpty()){
            throw new RuntimeException("Nothing to update");
        }
        HichinaProduct toUpdate = hichinaProducts.get(0);
        if(!request.getProductName().equals(toUpdate.getProductName())){
            // indicating name change, so must update sku group id
            String skuGroupId = checkProductGroupExistence(request.getProductName(), toUpdate.getProductTypeId());
            toUpdate.setSkuGroupId(skuGroupId);
            productSkuGroupMapper.updateMinPriceAndImage(skuGroupId);
        }
        toUpdate.setProductName(request.getProductName());
        toUpdate.setProductContent(request.getProductContent());
        // update basic info
        hichinaProductMapper.updateBySkuId(toUpdate);
        // update property bags
        List<ProductPropertyBag> propertyBags = request.getPropertyBags();
        updatePropertyBagsBySkuId(skuId, propertyBags);

        return toUpdate;
    }

    /**
     * whether exist or not, return the group id, if not exist, insert first
     * @param productName
     * @param productTypeId
     * @return
     */
    public String checkProductGroupExistence(String productName, String productTypeId){
        List<ProductSkuGroup> productSkuGroups = productSkuGroupMapper.findByNameAndProductTypeId(productName, productTypeId);
        String productSkuGroupId;
        if(productSkuGroups.isEmpty()){
            productSkuGroupId = java.util.UUID.randomUUID().toString();
            productSkuGroupMapper.insert(new ProductSkuGroup(productSkuGroupId, productName, productTypeId , new Date(), true, "", 0));
        }else{
            ProductSkuGroup existingSkuGroup = productSkuGroups.get(0);
            productSkuGroupId = existingSkuGroup.getSkuGroupId();
        }
        return productSkuGroupId;
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
