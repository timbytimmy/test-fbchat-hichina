package com.hichina.main.back.hichinamainback.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;

public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * this is the tricky code with socks 5 proxy, can use as a template
     * @param url
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public static JsonObject sendToWithProxyV2(String url, String proxyHost, Integer proxyPort){
        InetSocketAddress proxyAddr = new InetSocketAddress(proxyHost, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            LOG.info("===ok client response code: "+response.code());
            Gson gson = new Gson();
            JsonObject entity = gson.fromJson(response.body().string(), JsonObject.class);
            return entity;
        } catch (IOException e) {
            LOG.error("===okclient exception: "+ e.getMessage());
            return null;
        }
    }

    private static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    public static JsonObject postUrlWithParams(String url, Map<String, String> params){
        System.setProperty("https.protocols", "TLSv1.2");

        FormBody.Builder builder = new FormBody.Builder();

        for(String key : params.keySet()){
            builder.add(key, params.get(key));
        }

        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url).post(formBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            LOG.info("===ok client response code with post: "+response.code());
            Gson gson = new Gson();
            JsonObject entity = gson.fromJson(response.body().string(), JsonObject.class);
            return entity;
        } catch (IOException e) {
            LOG.error("===okclient exception in postUrlWithParams: "+ e.getMessage());
            return null;
        }
    }

    public static JsonObject getUrlWithParams(String url, Map<String, String> params){
        System.setProperty("https.protocols", "TLSv1.2");

        url = getUrlWithQueryString(url, params);

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            LOG.info("===ok client response code: "+response.code());
            Gson gson = new Gson();
            JsonObject entity = gson.fromJson(response.body().string(), JsonObject.class);
            return entity;
        } catch (IOException e) {
            LOG.error("===okclient exception in getUrlWithParams: "+ e.getMessage());
            return null;
        }
    }

    public static String sendToWithProxy(String url, String proxyHost, Integer proxyPort) throws IOException {
        System.setProperty("https.protocols", "TLSv1.2");
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new MyConnectionSocketFactory(SSLContexts.createSystemDefault()))
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            InetSocketAddress socksaddr = new InetSocketAddress(proxyHost, proxyPort);
            HttpClientContext context = HttpClientContext.create();
            context.setAttribute("socks.address", socksaddr);
            HttpGet request = new HttpGet(url);
            System.setProperty("https.protocols", "TLSv1.2");
            CloseableHttpResponse response = httpclient.execute(request, context);
            try {
                return new String(EntityUtils.toByteArray(response.getEntity()), "UTF-8");
            } finally {
                response.close();
            }
        }catch (ClientProtocolException e) {
            LOG.error("===" + e.getMessage());
        } catch (IOException e) {
            LOG.error("===" + e.getMessage());
        } finally {
            httpclient.close();
        }
            return null;
    }

    static class MyConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MyConnectionSocketFactory(final SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            System.setProperty("https.protocols", "TLSv1.2");
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }
    }
}
