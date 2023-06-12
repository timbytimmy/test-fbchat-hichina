package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.JsonObject;
import com.hichina.main.back.hichinamainback.utils.HttpUtils;
import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import com.hichina.main.back.hichinamainback.utils.WxPayUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/showme")
    public String trySomething() throws IOException {
        String clientIp = getClientIp();
        return clientIp;
    }

    public String getClientIp() {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @GetMapping("/setCookie")
    public String setCookie() throws IOException {
        // Define the proxy server details
        String proxyHost = "127.0.0.1";
        int proxyPort = 1083;

        // Define the target URL
        String targetUrl = "https://graph.facebook.com/v14.0/me?access_token=EAAOIMZBkz7HoBAE2w4bLC6XxH2SM3tUNfHRRQSgxykJ2upVuCuZAJPEuZCAenKnACS8uDHGfbmnVNCEIuxqevR0f3LYI1p8x2MO3tYh5eypNdIXuGOjPdkuLfVP1JH8ZCTBDuoj7vadhZAjPNtEl8IjCz1dzZAakfsPpiZCFsfYy4yYQZAWmOfbIWjcPkhbXVmvQfoVwL5f9KUDbUdwKxaHN";

        JsonObject response = HttpUtils.sendToWithProxyV2(targetUrl, proxyHost, proxyPort);

        return response.toString();
    }

}
