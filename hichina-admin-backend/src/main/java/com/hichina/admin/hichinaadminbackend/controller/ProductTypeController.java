package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductTypeMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import com.hichina.admin.hichinaadminbackend.model.ProductType;
import com.hichina.admin.hichinaadminbackend.model.ProductTypeAttributeMapping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producttype")
public class ProductTypeController {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @GetMapping("/bindedattributesforproduct")
    public HichinaResponse getProductTypeBingingAttr(@RequestParam(value = "productTypeId", required = true) String productTypeId){
        HichinaResponse ret = new HichinaResponse();

        List<ProductAttribute>  attributes = productTypeMapper.findAttributesByProductType(productTypeId);

        ret.setMessage("成功获取产品类型:"+productTypeId+"所绑定的属性");
        ret.setOk(true);
        ret.setData(attributes);

        return ret;
    }

    @GetMapping
    public HichinaResponse getPagedProductType(@RequestParam(value = "page", required = true) Integer page,
                                                    @RequestParam(value = "pageSize", required = true) Integer size,
                                                    @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        if(size>0){
            PageHelper.startPage(page,size);
        }
        List<ProductType> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            rawData = productTypeMapper.findProductTypeByQuery(query);
            cnt = productTypeMapper.countByQuery(query);
        }else{
            rawData = productTypeMapper.findAllProductType();
            cnt = productTypeMapper.count();
        }
        ret.setMessage("成功获取所有产品类型");
        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);
        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }

    @PostMapping
    public HichinaResponse createProductType(@RequestBody ProductTypeCreateDTO request){
        HichinaResponse ret = new HichinaResponse();

        ProductType productType = new ProductType();
        productType.setProductTypeName(request.getName());
        productType.setProductTypeDescription(request.getDescription());
        productType.setProductTypeId(java.util.UUID.randomUUID().toString());

        try{
            productTypeMapper.insert(productType);
            ret.setData(productType.getProductTypeId());
            ret.setOk(true);
            ret.setMessage("成功创建产品类型");
        }catch (Exception e){
            ret.setData(null);
            ret.setOk(false);
            ret.setMessage("创建产品类型失败"+e.getMessage());
        }
        return ret;
    }

    @DeleteMapping("/batch")
    public HichinaResponse deleteProductTypes(@RequestBody ProductTypeBatchDeleteRequestDTO req){

        // TODO: 2023/3/11 don't allow delete if the product type has any reference by any product sku 
        productTypeMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除产品类别");

        return ret;
    }

    @PostMapping("/unbindattr")
    public HichinaResponse unbindAttributeFromProductType(@RequestBody BindAtrribute2ProductRequest req){
        // TODO: 2023/3/12 add constraint that if any attribute of this type is in use, don't allow unbind
        HichinaResponse ret = new HichinaResponse();
        try{
            productTypeMapper.unbindAttr(req.getProductTypeId(), req.getAttributeId());
            ret.setOk(true);
            ret.setMessage("成功解绑产品类型到属性的关联");
        }catch (Exception e){
            ret.setOk(false);
            ret.setMessage("解绑产品类型到属性的关联失败"+e.getMessage());
        }
        return ret;
    }

    @PostMapping("/bindattr")
    public HichinaResponse bindAtrributeWithProductType(@RequestBody BindAtrribute2ProductRequest req){
        HichinaResponse ret = new HichinaResponse();
        ProductTypeAttributeMapping productTypeAttributeMapping = new ProductTypeAttributeMapping();

        productTypeAttributeMapping.setAttributeId(req.getAttributeId());
        productTypeAttributeMapping.setProductTypeId(req.getProductTypeId());
        productTypeAttributeMapping.setSequence(String.valueOf(System.currentTimeMillis()));


        try{
            productTypeMapper.bindAttr(productTypeAttributeMapping);

            ret.setData(productTypeAttributeMapping.toString());
            ret.setOk(true);
            ret.setMessage("成功创建产品类型到属性的关联");
        }catch (Exception e){
            ret.setData(null);
            ret.setOk(false);
            ret.setMessage("创建产品类型到属性的关联失败"+e.getMessage());
        }
        return ret;
    }
}
