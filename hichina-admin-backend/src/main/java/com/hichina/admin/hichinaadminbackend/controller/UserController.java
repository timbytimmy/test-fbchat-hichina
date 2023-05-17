package com.hichina.admin.hichinaadminbackend.controller;

import com.hichina.admin.hichinaadminbackend.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/info")
public class UserController {

    @Autowired
    private UserUtil userUtil;

    @GetMapping()
    public String whoami(){
        return currentUserName();
    }

    public String currentUserName(){
        return userUtil.currentUserName();
    }
}
