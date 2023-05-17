package com.hichina.main.back.hichinamainback.controller;

import com.aliyuncs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.hichina.main.back.hichinamainback.mapper.GuidebookMapper;
import com.hichina.main.back.hichinamainback.model.DTO.GuidebookListDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.PaginationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/guidebook")
public class PublicGuidebookController {
    @Autowired
    private GuidebookMapper guidebookMapper;

    @GetMapping
    public HichinaResponse getGuidebooks(@RequestParam(value = "page", required = true) Integer page,
                                         @RequestParam(value = "pageSize", required = true) Integer size,
                                         @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();

        if(page>0){
            PageHelper.startPage(page,size);
        }

        int cnt = 0;
        List<GuidebookListDTO> data;
        if(StringUtils.isEmpty(query)){
            data =  guidebookMapper.findAllGuidebook();
            cnt= guidebookMapper.count();
        }else{
            data = guidebookMapper.findGuidebookByQuery(query);
            cnt = guidebookMapper.countByQuery(query);
        }

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(data);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);
        ret.setMessage("Succeed getting all guidebooks...");

        return ret;
    }
}
