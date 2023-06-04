package com.hichina.admin.hichinaadminbackend.controller;

import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaResponse;
import com.hichina.admin.hichinaadminbackend.model.DTO.HomePostImageRequest;
import com.hichina.admin.hichinaadminbackend.model.mongo.BlogSlideImage;
import com.hichina.admin.hichinaadminbackend.model.mongo.GuidebookIntroSlideImage;
import com.hichina.admin.hichinaadminbackend.model.mongo.HomePostImage;
import com.hichina.admin.hichinaadminbackend.model.mongo.HomeSlideImage;
import com.hichina.admin.hichinaadminbackend.repository.BlogSlideImageRepository;
import com.hichina.admin.hichinaadminbackend.repository.GuidebookIntroSlideImageRepository;
import com.hichina.admin.hichinaadminbackend.repository.HomePostImageRepository;
import com.hichina.admin.hichinaadminbackend.repository.HomeSlideImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagecontent")
public class PageContentAdminController {
    @Autowired
    private GuidebookIntroSlideImageRepository guidebookIntroSlideImageRepository;

    @Autowired
    private HomeSlideImageRepository homeSlideImageRepository;

    @Autowired
    private BlogSlideImageRepository blogSlideImageRepository;

    @Autowired
    private HomePostImageRepository homePostImageRepository;

    @GetMapping("/homepostimage")
    public HichinaResponse getHomePostImage(){
        HichinaResponse ret = new HichinaResponse();
        List<HomePostImage> sliders = homePostImageRepository.findAll();
        if(sliders.isEmpty()){
            ret.setData(null);
        }else{
            ret.setData(sliders.get(0));
        }
        ret.setOk(true);
        ret.setMessage("成功获取blog首页所有Home post");
        return ret;
    }

    @PostMapping("/homepostimage")
    public HichinaResponse setHomePostImage(@RequestBody HomePostImageRequest request){
        HichinaResponse ret = new HichinaResponse();
        homePostImageRepository.deleteAll();
        HomePostImage homePostImage = new HomePostImage();
        homePostImage.setPostImageUrl(request.getPostImageUrl());
        homePostImage.setPostLink(request.getPostLink());
        homePostImageRepository.insert(homePostImage);
        ret.setOk(true);
        ret.setMessage("Succeed update home post Image");

        return ret;
    }

    @PostMapping("/guidebookintrosliders")
    public HichinaResponse setGuidebookIntroSliders(@RequestBody List<GuidebookIntroSlideImage> sliders){
        guidebookIntroSlideImageRepository.deleteAll();
        guidebookIntroSlideImageRepository.insert(sliders);

        HichinaResponse ret = new HichinaResponse();
        ret.setMessage("成功设置攻略页轮播图");
        ret.setOk(true);
        ret.setData(sliders);

        return ret;
    }

    @PostMapping("/homesliders")
    public HichinaResponse setHomeSliders(@RequestBody List<HomeSlideImage> sliders){
        homeSlideImageRepository.deleteAll();
        homeSlideImageRepository.insert(sliders);

        HichinaResponse ret = new HichinaResponse();
        ret.setMessage("成功设置首页轮播图");
        ret.setOk(true);
        ret.setData(sliders);

        return ret;
    }

    @PostMapping("/bloghomeliders")
    public HichinaResponse setBlogSliders(@RequestBody List<BlogSlideImage> sliders){
        blogSlideImageRepository.deleteAll();
        blogSlideImageRepository.insert(sliders);

        HichinaResponse ret = new HichinaResponse();
        ret.setMessage("成功设置博客页面轮播图");
        ret.setOk(true);
        ret.setData(sliders);
        return ret;
    }

    @GetMapping("/bloghomeliders")
    public HichinaResponse getAllBlogSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<BlogSlideImage> sliders = blogSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取blog首页所有轮播图");
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

    @GetMapping("/homesliders")
    public HichinaResponse getAllHomeSliders(){
        HichinaResponse ret = new HichinaResponse();
        List<HomeSlideImage> sliders = homeSlideImageRepository.findAll();
        ret.setData(sliders);
        ret.setOk(true);
        ret.setMessage("成功获取所有轮播图");
        return ret;
    }
}
