package com.hichina.main.back.hichinamainback.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
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
import java.net.*;

public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

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

//            HttpHost target = new HttpHost(proxyHost, proxyPort, "http");
            HttpGet request = new HttpGet(url);

//            LOG.info("Executing request " + request + " to " + target + " via SOCKS proxy " + socksaddr);
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
