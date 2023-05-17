package com.hichina.admin.hichinaadminbackend.controller;

import com.hichina.admin.hichinaadminbackend.repository.HomeSlideImageRepository;
import com.hichina.admin.hichinaadminbackend.schedule.ScheduledJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
    @Autowired
    private HomeSlideImageRepository repository;

    @Autowired
    private ScheduledJobs scheduledJobs;

    @GetMapping
    public String mongotest(){
        scheduledJobs.UpdateBlogProfileImage();

        return "succeed";
    }
}
