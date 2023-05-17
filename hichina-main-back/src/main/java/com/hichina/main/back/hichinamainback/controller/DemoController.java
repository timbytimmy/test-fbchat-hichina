package com.hichina.main.back.hichinamainback.controller;

import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import com.hichina.main.back.hichinamainback.utils.WxPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/public/demo")
public class DemoController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxPayUtil wxPayUtil;

    @GetMapping
    public String trySomething() throws IOException {
//        String ve = wxPayUtil.test();

//        return ve;
        return "";
    }

}
