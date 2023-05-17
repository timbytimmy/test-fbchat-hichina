package com.hichina.main.back.hichinamainback.controller;

import com.hichina.main.back.hichinamainback.mapper.DestinationMapper;
import com.hichina.main.back.hichinamainback.model.DTO.DestinationProfileDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/destination")
public class DestinationController {
    @Autowired
    private DestinationMapper destinationMapper;

    @GetMapping("/rand6")
    public HichinaResponse getRand6Destinations(){
        HichinaResponse ret = new HichinaResponse();
        List<DestinationProfileDTO> rand6Destinations =  destinationMapper.findRandomDestinationsByCount(6);

        ret.setMessage("成功获取6个随机目的地");
        ret.setOk(true);
        ret.setData(rand6Destinations);

        return ret;
    }
}
