package com.hichina.admin.hichinaadminbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired
    private Environment env;

    @GetMapping("/servingurl")
    public String getPublicServiceUrl(){
        return env.getProperty("base.serving.url");
    }

    @GetMapping("/mainsitebaseurl")
    public String getMainsiteBaseUrl(){
        return env.getProperty("mainsite.base");
    }
}
