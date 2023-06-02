package com.hichina.main.back.hichinamainback.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;

@Component
public class FacebookAccessTokenValidator {
    private static final String GRAPH_API_URL = "https://graph.facebook.com/v14.0/me?access_token=";

    @Autowired
    private Environment env;

    public boolean validateAccessToken(String accessToken) {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new MyConnectionSocketFactory())
                .register("https",new MySSLConnectionSocketFactory()).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(reg);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", Integer.parseInt(env.getProperty("gfw.proxy.port")));
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute("socks.address", socketAddress);
        HttpGet httpGet = new HttpGet(GRAPH_API_URL + accessToken);
        try {
            JsonObject response = httpClient.execute(httpGet, httpResponse -> {
                JsonParser jsonParser = new JsonParser();
                InputStreamReader reader = new InputStreamReader(httpResponse.getEntity().getContent());
                return jsonParser.parse(reader).getAsJsonObject();
            }, context);
            return response.has("id");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            // socket代理
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }
    }
    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {
        public MySSLConnectionSocketFactory() {
            super(SSLContexts.createDefault(), getDefaultHostnameVerifier());
        }
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }
    }
}
