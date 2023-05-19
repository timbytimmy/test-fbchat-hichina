package com.hichina.main.back.hichinamainback.controller;

import com.aliyuncs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.mapper.DestinationMapper;
import com.hichina.main.back.hichinamainback.model.DTO.DestinationProfileDTO;
import com.hichina.main.back.hichinamainback.model.DTO.DestinationWithGuidebook;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.PaginationWrapper;
import com.hichina.main.back.hichinamainback.model.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/public/destination")
public class PublicDestinationController {

    @Autowired
    private DestinationMapper destinationMapper;

    @GetMapping("/{destinationId}")
    @EnableHichinaAutoLog(description = "Get destination with guidebook by id")
    public HichinaResponse getByDestinationWithGuidebookById(@PathVariable("destinationId") String destinationId){
        HichinaResponse ret = new HichinaResponse();

        List<DestinationWithGuidebook> destinations = destinationMapper.findDestinationWithGuidebookById(destinationId);

        if(destinations.isEmpty()){
            ret.setData(null);
            ret.setOk(false);
            ret.setMessage(String.format("失败获取id为%s的destination with guidebook", destinationId));
            return ret;
        }

        // do not support multiple guidebook for a destination, if there are multiple, pick the first
        ret.setData(destinations.get(0));
        ret.setOk(true);
        ret.setMessage(String.format("成功获取id为%s的destination with guidebook", destinationId));

        return ret;
    }

    @GetMapping("/children/{destinationId}")
    @EnableHichinaAutoLog(description = "Get child destinations")
    public HichinaResponse getChildDestinations(@PathVariable("destinationId") String destinationId){
        HichinaResponse ret = new HichinaResponse();
        List<Destination> destinations = destinationMapper.findDirectChildById(destinationId);
        ret.setData(destinations);
        ret.setOk(true);
        ret.setMessage(String.format("成功获取的destination %s的子景点",destinationId));
        return ret;
    }

    @GetMapping("/list")
    @EnableHichinaAutoLog(description = "Get destination list api")
    public HichinaResponse getDestinationList(@RequestParam(value = "page", required = true) Integer page,
                                              @RequestParam(value = "pageSize", required = true) Integer size,
                                              @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();

        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<DestinationProfileDTO> destinationProfileDTOS;
        Integer cnt = 0;
        if(StringUtils.isEmpty(query)){
            destinationProfileDTOS = destinationMapper.findAllDestinations();
            cnt = destinationMapper.countAllDestinations();
        }else{
            destinationProfileDTOS = destinationMapper.findAllDestinationsByQuery(query);
            cnt = destinationMapper.countAllDestinationsByQuery(query);
        }

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(destinationProfileDTOS);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setOk(true);
        ret.setData(paginationWrapper);
        ret.setMessage("Successfully got list of destinations");

        return ret;
    }

}
