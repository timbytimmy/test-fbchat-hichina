package com.hichina.main.back.hichinamainback.utils;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FacebookAccessTokenValidator {
    private static final Logger LOG = LoggerFactory.getLogger(FacebookAccessTokenValidator.class);

    private static final String GRAPH_API_URL = "https://graph.facebook.com/v14.0/me?access_token=";

    static {
        System.setProperty("https.protocols", "TLSv1.2");
    }

    @Autowired
    private Environment env;

    public boolean validateAccessToken(String accessToken) {
        String proxyHost = "127.0.0.1";
        int proxyPort = Integer.parseInt(env.getProperty("gfw.proxy.port"));
        String targetUrl = GRAPH_API_URL+accessToken;
        JsonObject response = HttpUtils.sendToWithProxyV2(targetUrl, proxyHost, proxyPort);
        return response!=null?response.has("id"):false;
    }
}
