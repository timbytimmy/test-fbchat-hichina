package com.hichina.main.back.hichinamainback.controller;


import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.mongo.BlogSlideImage;
import com.hichina.main.back.hichinamainback.model.mongo.GuidebookIntroSlideImage;
import com.hichina.main.back.hichinamainback.repository.BlogSlideImageRepository;
import com.hichina.main.back.hichinamainback.repository.GuidebookIntroSlideImageRepository;
import com.hichina.main.back.hichinamainback.model.mongo.HomeSlideImage;
import com.hichina.main.back.hichinamainback.repository.HomeSlideImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/pagecontent")
public class PageContentAdminController {
    @Autowired
    private HomeSlideImageRepository homeSlideImageRepository;

    @Autowired
    private GuidebookIntroSlideImageRepository guidebookIntroSlideImageRepository;

    @Autowired
    private BlogSlideImageRepository blogSlideImageRepository;

    @GetMapping("/homesliders")
    public HichinaResponse getAllHomeSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<HomeSlideImage> sliders = homeSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }

    @GetMapping("/guidebookintrosliders")
    public HichinaResponse getAllGuidebookIntroSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<GuidebookIntroSlideImage> sliders = guidebookIntroSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }

    @GetMapping("/bloghomesliders")
    public HichinaResponse getAllBloghomeSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<BlogSlideImage> sliders = blogSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }
}

