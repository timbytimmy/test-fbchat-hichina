package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductAttributeMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productsharedattributes")
public class ProductSharedAttributesController {
    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @GetMapping
    public HichinaResponse getPagedSharedAttributes(@RequestParam(value = "page", required = true) Integer page,
                                                    @RequestParam(value = "pageSize", required = true) Integer size,
                                                    @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        if(page>0){
            PageHelper.startPage(page,size);
        }
        List<ProductAttribute> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            rawData = productAttributeMapper.findAttributesByQuery(query);
            cnt = productAttributeMapper.countByQuery(query);
        }else{
            rawData = productAttributeMapper.findAllAttributes();
            cnt = productAttributeMapper.count();
        }
        ret.setMessage("成功获取所有属性");
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
    public HichinaResponse createSharedAttributes(@RequestBody ProductAttributeCreateDTO request){
        HichinaResponse ret = new HichinaResponse();

        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName(request.getName());
        productAttribute.setAttributeId(java.util.UUID.randomUUID().toString());
        productAttribute.setDataType(request.getDataType());

        try{
            productAttributeMapper.insert(productAttribute);
            ret.setData(productAttribute.getAttributeId());
            ret.setOk(true);
            ret.setMessage("成功创建属性");
        }catch (Exception e){
            ret.setData(null);
            ret.setOk(false);
            ret.setMessage("创建属性失败"+e.getMessage());
        }
        return ret;
    }

    @DeleteMapping("/batch")
    public HichinaResponse deleteProductAttributes(@RequestBody ProductAttributeBatchDeleteRequest req){

        // TODO: check if the attributes has any reference in the attribute value table, if yes, block
        productAttributeMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除属性");

        return ret;
    }
}
