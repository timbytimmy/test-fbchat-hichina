package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hichina.main.back.hichinamainback.utils.HttpUtils;
import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import com.hichina.main.back.hichinamainback.utils.WxPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

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
    public String setCookie() throws IOException {
        // Define the proxy server details
        String proxyHost = "127.0.0.1";
        int proxyPort = 1083;

        // Define the target URL
        String targetUrl = "https://graph.facebook.com/v14.0/me?access_token=EAAOIMZBkz7HoBAGkAuxiCExK50z7KBaIePyqmoHtzy49WWkO5y1GvkcvSNZBv7nEZAjNFv9DwZBecOEImptxcGmThXkShS8zL7j3tK0YkcHaTgTQeZBvXuRVTL35Juq5ZCD96pMOTIZBdADUtoPgMN14WgiipQnWk3saICPQKNmeUe9Xgj3VwB4YDwTarD6LWS81oX4n6ztbYqvLwmfSuqA";


        String response = HttpUtils.sendToWithProxy(targetUrl, proxyHost, proxyPort);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = jsonParser.parse(response).getAsJsonObject();

        System.out.println(jsonResponse.has("id"));
        return response;
    }

}
