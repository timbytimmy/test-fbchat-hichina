package com.hichina.main.back.hichinamainback.controller;

import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import com.hichina.main.back.hichinamainback.utils.WxPayUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequestMapping("/api/public/demo")
public class DemoController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxPayUtil wxPayUtil;



    @GetMapping("/showme")
    public String trySomething() throws IOException {
//        String ve = wxPayUtil.test();

//        return ve;
        return "f*ck";
    }
    @GetMapping("/setCookie")
    public String setCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        Cookie cookie = new Cookie("myCookie", "cookieValue");
        cookie.setMaxAge(3600); // Set the cookie expiration time in seconds
        response.addCookie(cookie);
        return "Cookie set successfully";
    }

}
