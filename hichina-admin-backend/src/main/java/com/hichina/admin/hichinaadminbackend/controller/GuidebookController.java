package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.GuidebookMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import com.hichina.admin.hichinaadminbackend.model.Guidebook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/guidebook")
public class GuidebookController {
    @Autowired
    private GuidebookMapper guidebookMapper;

    @PutMapping("/{guideId}")
    public HichinaResponse updateGuideBook(@PathVariable("guideId") String guideId, @RequestBody CreateGuidebookDTO request){
        HichinaResponse ret = new HichinaResponse();
        List<Guidebook> guidebooks = guidebookMapper.findRawByGuideId(guideId);
        if(guidebooks.isEmpty()){
            throw new RuntimeException("没找到任何可更新的攻略书"+guideId);
        }
        Guidebook toUpdate = guidebooks.get(0);

        toUpdate.setDownloadUrl(request.getDownloadUrl());
        toUpdate.setCoverImageUrl(request.getCoverImageUrl());
        toUpdate.setDestinationId(request.getDestinationId());
        toUpdate.setShortDescription(request.getShortDescription());

        guidebookMapper.update(toUpdate);
        ret.setMessage(String.format("成功更新id为%s的攻略书",guideId));
        ret.setOk(true);
        ret.setData(toUpdate);

        return ret;
    }

    @DeleteMapping("/batch")
    @Transactional
    public HichinaResponse deleteDestinations(@RequestBody GuidebookDeleteRequest req){

        // delete base product record
        guidebookMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除攻略书");

        return ret;
    }

    @PostMapping
    public HichinaResponse createGuideBook(@RequestBody CreateGuidebookDTO request){
        HichinaResponse ret = new HichinaResponse();

        Guidebook guidebook = new Guidebook();
        guidebook.setGuideId(java.util.UUID.randomUUID().toString());
        guidebook.setDestinationId(request.getDestinationId());
        guidebook.setCoverImageUrl(request.getCoverImageUrl());
        guidebook.setShortDescription(request.getShortDescription());
        guidebook.setDownloadUrl(request.getDownloadUrl());
        guidebook.setCreatedDate(new Date());

        guidebookMapper.insert(guidebook);

        ret.setData(guidebook);
        ret.setOk(true);
        ret.setMessage("成功创建攻略书");
        return ret;
    }

    @GetMapping("/{guideId}")
    public HichinaResponse getGuideBookDTOById(@PathVariable("guideId") String guideId){
        HichinaResponse ret = new HichinaResponse();
        List<GuidebookListDTO> guidebooks = guidebookMapper.findGuidebookByGuideId(guideId);
        if(guidebooks.isEmpty()){
            ret.setOk(false);
            ret.setMessage(String.format("没有找到任何id为:%s的攻略书",guideId));
        }
        GuidebookListDTO guidebook = guidebooks.get(0);

        ret.setData(guidebook);
        ret.setOk(true);
        ret.setMessage(String.format("成功找到id为:%s的攻略书", guideId));

        return ret;
    }

    @GetMapping
    public HichinaResponse getDestinations(@RequestParam(value = "page", required = true) Integer page,
                                           @RequestParam(value = "pageSize", required = true) Integer size,
                                           @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<GuidebookListDTO> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            rawData = guidebookMapper.findGuidebookByQuery(query);
            cnt = guidebookMapper.countByQuery(query);
        }else{
            rawData = guidebookMapper.findAllGuidebook();
            cnt = guidebookMapper.count();
        }

        ret.setMessage("Successfully got all guidebooks");

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }
}
