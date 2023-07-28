package com.hichina.admin.hichinaadminbackend.service;

import com.hichina.admin.hichinaadminbackend.config.Constants;
import com.hichina.admin.hichinaadminbackend.mapper.AdminUserMapper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductAttributeMapper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductTypeMapper;
import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import com.hichina.admin.hichinaadminbackend.model.ProductType;
import com.hichina.admin.hichinaadminbackend.model.ProductTypeAttributeMapping;
import com.hichina.admin.hichinaadminbackend.util.AtomicSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class InitDataService {
    private static final Logger LOG = LoggerFactory.getLogger(InitDataService.class);

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    private static AtomicSequenceGenerator sequenceGenerator = AtomicSequenceGenerator.getInstance();

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    private void insertPropertyIfNotExist(String name, String id, String dataType){
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName(name);
        productAttribute.setAttributeId(id);
        productAttribute.setDataType(dataType);
        try{
            productAttributeMapper.insert(productAttribute);
        }catch (Exception e){
            LOG.info("Not inserted attribute at startup: "+e.getMessage());
        }
    }

    private void insertProductTypeIfNotExist(String name, String id,  String description){
        ProductType productType = new ProductType();
        productType.setProductTypeName(name);
        productType.setProductTypeDescription(description);
        productType.setProductTypeId(id);

        try{
            productTypeMapper.insert(productType);
        }catch (Exception e){
            LOG.info("Not inserted product type at startup: "+e.getMessage());
        }
    }

    private void bindAttributeToProductType(String attrId, String productTypeId){
        ProductTypeAttributeMapping productTypeAttributeMapping = new ProductTypeAttributeMapping();

        productTypeAttributeMapping.setAttributeId(attrId);
        productTypeAttributeMapping.setProductTypeId(productTypeId);
        productTypeAttributeMapping.setSequence(String.valueOf(sequenceGenerator.getNext()));

        try{
            productTypeMapper.bindAttr(productTypeAttributeMapping);
        }catch (Exception e){
            LOG.info(String.format("Fail to bind attribute with producttype %s-$s, at startup: ",attrId, productTypeId)+e.getMessage());
        }
    }

    public void initAdminUser(){
        try{
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername("hichinaadmin");
            adminUser.setPassword("DtYV5/9L");
            adminUser.setCreatedTime(new Date());
            adminUserMapper.insert(adminUser);
        }catch (Exception e){
            LOG.info("Super admin user already exist:  "+e.getMessage());
        }
    }

    @Transactional
    public void initProperty(){
        LOG.info("executing insertInitProperty");

        insertPropertyIfNotExist("产品图", Constants.PRODUCTPROFILEIMAGEPROP, "image");
        insertPropertyIfNotExist("可用日期", Constants.AVAILABLEDATEGUIDPROP, "datestring");
        insertPropertyIfNotExist("天数", Constants.DAYSPROP, "integer");
        insertPropertyIfNotExist("供应商", Constants.SUPPLIERPROP, "string");
        insertPropertyIfNotExist("Option", Constants.TYPEOFPACKAGEPROP, "string");
        insertPropertyIfNotExist("成人单价", Constants.ADULTPRICEPROP, "integer");
        insertPropertyIfNotExist("儿童单价(>=2岁)", Constants.CHILDPRICEPROP, "integer");
        insertPropertyIfNotExist("婴儿单价(<2岁)", Constants.INFANTPRICEPROP, "integer");
        insertPropertyIfNotExist("单房差", Constants.SINGLEDIFFPRICEPROP , "integer");
        insertPropertyIfNotExist("价格", Constants.GENERALPRICE , "integer");
        insertPropertyIfNotExist("最大数量", Constants.MAXNUM , "integer");
    }

    @Transactional
    public void initProductType(){
        LOG.info("executed initProductType");
        insertProductTypeIfNotExist("Tour", Constants.TOURPRODUCTTYPE, "跟团游");
        insertProductTypeIfNotExist("Flight & Hotel", Constants.FLIGHTHOTELPRODUCTTYPE, "机酒自由行");
        insertProductTypeIfNotExist("Flight", Constants.FLIGHTPRODUCTTYPE, "机票package");
        insertProductTypeIfNotExist("Hotel", Constants.HOTELPRODUCTTYPE, "酒店package");
        insertProductTypeIfNotExist("Local Special", Constants.LOCALSPECIALTYPRODUCTTYPE, "文创土特产");
    }

    @Transactional
    public void initProductTypeBinding(){
        LOG.info("Executing initProductTypeBinding");

        // 跟团游
        bindAttributeToProductType(Constants.PRODUCTPROFILEIMAGEPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.AVAILABLEDATEGUIDPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.DAYSPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.SUPPLIERPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.TYPEOFPACKAGEPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.ADULTPRICEPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.CHILDPRICEPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.INFANTPRICEPROP,Constants.TOURPRODUCTTYPE);
        bindAttributeToProductType(Constants.SINGLEDIFFPRICEPROP,Constants.TOURPRODUCTTYPE);

        //机酒自由行
        bindAttributeToProductType(Constants.PRODUCTPROFILEIMAGEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.AVAILABLEDATEGUIDPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.DAYSPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.SUPPLIERPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.TYPEOFPACKAGEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.ADULTPRICEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.CHILDPRICEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.INFANTPRICEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.SINGLEDIFFPRICEPROP,Constants.FLIGHTHOTELPRODUCTTYPE);

        //机票package
        bindAttributeToProductType(Constants.PRODUCTPROFILEIMAGEPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.AVAILABLEDATEGUIDPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.DAYSPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.SUPPLIERPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.TYPEOFPACKAGEPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.ADULTPRICEPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.CHILDPRICEPROP,Constants.FLIGHTPRODUCTTYPE);
        bindAttributeToProductType(Constants.INFANTPRICEPROP,Constants.FLIGHTPRODUCTTYPE);

        //酒店package
        bindAttributeToProductType(Constants.PRODUCTPROFILEIMAGEPROP,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.AVAILABLEDATEGUIDPROP,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.DAYSPROP,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.SUPPLIERPROP,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.TYPEOFPACKAGEPROP,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.GENERALPRICE,Constants.HOTELPRODUCTTYPE);
        bindAttributeToProductType(Constants.MAXNUM,Constants.HOTELPRODUCTTYPE);

        //文创土特产
        bindAttributeToProductType(Constants.PRODUCTPROFILEIMAGEPROP,Constants.LOCALSPECIALTYPRODUCTTYPE);
        bindAttributeToProductType(Constants.SUPPLIERPROP,Constants.LOCALSPECIALTYPRODUCTTYPE);
        bindAttributeToProductType(Constants.TYPEOFPACKAGEPROP,Constants.LOCALSPECIALTYPRODUCTTYPE);
        bindAttributeToProductType(Constants.GENERALPRICE,Constants.LOCALSPECIALTYPRODUCTTYPE);
    }
}
