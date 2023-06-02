package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        String targetUrl = "https://graph.facebook.com/v14.0/me?access_token=EAAOIMZBkz7HoBABqSIYZClDV3rAVoK13PWFLhV3cxI3ZAamWQpSjdmv75QWWtLKv3g257ZAoMUetnIxZCNM6A6sP7qWP2khPv1SQOQqo6TjZBTIHZCkg7kkIeDMlcOKqaPSb35oF1S8prev7uRzLsCg8CYY8shdpMZBSFZAu0OekgC9rZCpQtW59YOB7LAjJkEcNsPMPyAuDF6uw6jrVmCNBeW";

        // Create a Proxy object with the proxy server details
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));

        // Create a URL object with the target URL
        URL url = new URL(targetUrl);

        // Open a connection to the URL using the proxy
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        connection.setRequestMethod("GET");
        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        String response = responseBuilder.toString();
        // Set the request method (GET, POST, etc.)
//        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read the response content
        // ...

        // Close the connection
        connection.disconnect();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = jsonParser.parse(response).getAsJsonObject();

        System.out.println(jsonResponse.has("id"));
        return response;
    }

}
