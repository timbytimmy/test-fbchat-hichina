package com.hichina.main.back.hichinamainback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/service")
public class PublicServiceController {
    @Autowired
    private Environment env;
    @GetMapping
    public String getFrontendUrl(){
        return env.getProperty("frontend.url");
    }

    @GetMapping("/backend-service")
    public String getBackendUrl(){
        return env.getProperty("backend.servicebase.url");
    }
}
