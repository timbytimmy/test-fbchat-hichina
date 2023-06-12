package com.hichina.main.back.hichinamainback.controller;


import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.mongo.BlogSlideImage;
import com.hichina.main.back.hichinamainback.model.mongo.GuidebookIntroSlideImage;
import com.hichina.main.back.hichinamainback.model.mongo.HomePostImage;
import com.hichina.main.back.hichinamainback.repository.BlogSlideImageRepository;
import com.hichina.main.back.hichinamainback.repository.GuidebookIntroSlideImageRepository;
import com.hichina.main.back.hichinamainback.model.mongo.HomeSlideImage;
import com.hichina.main.back.hichinamainback.repository.HomePostImageRepository;
import com.hichina.main.back.hichinamainback.repository.HomeSlideImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/pagecontent")
public class PublicPageContentAdminController {
    @Autowired
    private HomeSlideImageRepository homeSlideImageRepository;

    @Autowired
    private GuidebookIntroSlideImageRepository guidebookIntroSlideImageRepository;

    @Autowired
    private BlogSlideImageRepository blogSlideImageRepository;

    @Autowired
    private HomePostImageRepository homePostImageRepository;

    @GetMapping("/homepost")
    @EnableHichinaAutoLog(description = "Get home post")
    public HichinaResponse getHomePost(){
        HichinaResponse ret = new HichinaResponse();
        List<HomePostImage> postImages = homePostImageRepository.findAll();
        if(postImages.isEmpty()){
            ret.setOk(false);
            ret.setMessage("Cannot get home post");
            return ret;
        }else{
            HomePostImage data = postImages.get(0);
            ret.setData(data);
            ret.setOk(true);
            ret.setMessage("Succeed getting home post");
            return ret;
        }
    }

    @GetMapping("/homesliders")
    @EnableHichinaAutoLog(description = "Get home slider")
    public HichinaResponse getAllHomeSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<HomeSlideImage> sliders = homeSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }

    @GetMapping("/guidebookintrosliders")
    @EnableHichinaAutoLog(description = "Get guidebook intro sliders")
    public HichinaResponse getAllGuidebookIntroSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<GuidebookIntroSlideImage> sliders = guidebookIntroSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }

    @GetMapping("/bloghomesliders")
    @EnableHichinaAutoLog(description = "blog home sliders")
    public HichinaResponse getAllBloghomeSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<BlogSlideImage> sliders = blogSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }
}

