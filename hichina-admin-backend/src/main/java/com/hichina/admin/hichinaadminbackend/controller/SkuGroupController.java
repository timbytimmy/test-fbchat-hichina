package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductSkuGroupMapper;
import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaResponse;
import com.hichina.admin.hichinaadminbackend.model.DTO.PaginationWrapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.SkuGroupStatsDTO;
import com.hichina.admin.hichinaadminbackend.model.ProductSkuGroup;
import com.hichina.admin.hichinaadminbackend.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skugroup")
public class SkuGroupController {
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ProductSkuGroupMapper productSkuGroupMapper;

    private boolean checkUseMineOnly(String currentUserName){
        AdminUser adminUser = userUtil.currentUser(currentUserName);
        return adminUser.getUsername().startsWith("SP_");
    }

    @GetMapping("/groupstats")
    public HichinaResponse getProductGroupStatsInfo(@RequestParam(value = "page", required = true) Integer page,
                                                    @RequestParam(value = "pageSize", required = true) Integer size,
                                                    @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        if(page>0){
            PageHelper.startPage(page,size);
        }

        String currentUser = userUtil.currentUserName();
        boolean mineOnly = checkUseMineOnly(currentUser);

        List<SkuGroupStatsDTO> data;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            data = productSkuGroupMapper.findSkuGroupStatsByQuery(query, mineOnly, currentUser);
            cnt = productSkuGroupMapper.countSkuGroupStatsByQuery(query, mineOnly, currentUser);
        }else{
            data = productSkuGroupMapper.findAllSkuGroupStats(mineOnly, currentUser);
            cnt = productSkuGroupMapper.countAllSkuGroupStats(mineOnly, currentUser);
        }

        ret.setOk(true);
        ret.setMessage("成功获取sku group的统计数据");

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(data);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        return ret;
    }

    private void enableDisableGroup(String skuGroupId, Boolean toEnable){
        List<ProductSkuGroup> groups = productSkuGroupMapper.findBySkuGroupId(skuGroupId);

        if(groups.isEmpty()){
            throw new RuntimeException("No group to update");
        }

        ProductSkuGroup toUpdate = groups.get(0);

        toUpdate.setEnabled(toEnable);

        productSkuGroupMapper.update(toUpdate);
    }

    @PutMapping("/enable/{skuGroupId}")
    public HichinaResponse enableGroup(@PathVariable("skuGroupId") String skuGroupId){
        HichinaResponse ret = new HichinaResponse();

        enableDisableGroup(skuGroupId, true);

        ret.setData(skuGroupId);
        ret.setOk(true);
        ret.setMessage("成功启用sku group: "+ skuGroupId);

        return ret;
    }

    @PutMapping("/disable/{skuGroupId}")
    public HichinaResponse disableGroup(@PathVariable("skuGroupId") String skuGroupId){
        HichinaResponse ret = new HichinaResponse();

        enableDisableGroup(skuGroupId, false);

        ret.setData(skuGroupId);
        ret.setOk(true);
        ret.setMessage("成功停用sku group: "+ skuGroupId);

        return ret;
    }
}
