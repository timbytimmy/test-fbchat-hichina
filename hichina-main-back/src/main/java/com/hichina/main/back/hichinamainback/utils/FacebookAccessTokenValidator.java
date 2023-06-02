package com.hichina.main.back.hichinamainback.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Component
public class FacebookAccessTokenValidator {
    private static final Logger LOG = LoggerFactory.getLogger(FacebookAccessTokenValidator.class);

    private static final String GRAPH_API_URL = "https://graph.facebook.com/v14.0/me?access_token=";

    @Autowired
    private Environment env;

    public boolean validateAccessToken(String accessToken) {
        // Define the proxy server details
        String proxyHost = "127.0.0.1";
        int proxyPort = Integer.parseInt(env.getProperty("gfw.proxy.port"));
        // Define the target URL
        String targetUrl = "https://graph.facebook.com/v14.0/me?access_token="+accessToken;
        // Create a Proxy object with the proxy server details
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));
        // Create a URL object with the target URL
        // Open a connection to the URL using the proxy
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetUrl);
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setRequestMethod("GET");
            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            String response = responseBuilder.toString();
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonResponse = jsonParser.parse(response).getAsJsonObject();
            return jsonResponse.has("id");
        } catch (IOException e) {
            return false;
        } finally {
            connection.disconnect();
        }
    }
}
