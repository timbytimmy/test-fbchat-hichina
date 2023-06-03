package com.hichina.main.back.hichinamainback.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.IOException;

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
        // Define the proxy server details
        String proxyHost = "127.0.0.1";

        int proxyPort = Integer.parseInt(env.getProperty("gfw.proxy.port"));
        // Define the target URL
        String targetUrl = GRAPH_API_URL+accessToken;
        // Create a Proxy object with the proxy server details

        try {
            String response = HttpUtils.sendToWithProxy(targetUrl, proxyHost, proxyPort);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonResponse = jsonParser.parse(response).getAsJsonObject();
            return jsonResponse.has("id");
        } catch (IOException e) {
            LOG.error("===didn't validate succeed"+e.getMessage());
            return false;
        }
    }
}
