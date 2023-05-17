package com.hichina.main.back.hichinamainback.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.hichina.main.back.hichinamainback.model.DTO.PayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class AliPayService {
    private static final Logger LOG = LoggerFactory.getLogger(AliPayService.class);

    public static String SIGN_TYPE="RSA2";
    @Autowired
    private Environment env;

    public String verifySign(Map<String, String> params){
        boolean signVerified;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, env.getProperty("alipay.public.key"), "utf-8", SIGN_TYPE);
        } catch (AlipayApiException e) {
            LOG.error("验签失败", e);
            return "failure";
        }
        if (!signVerified) {
            LOG.warn("验签失败，通知内容：{}", params);
            return "failure";
        }
        return "success";
    }

    public PayDTO getPayForm(String orderId, String subject, Integer price) throws UnsupportedEncodingException {
        AlipayClient alipayClient = new DefaultAlipayClient(env.getProperty("alipay.api.url"), env.getProperty("alipay.appid"), env.getProperty("alipay.private.key"), "json", "utf-8", env.getProperty("alipay.public.key"), SIGN_TYPE);
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderId);
        params.put("subject", subject);
        params.put("total_amount", price.toString());
        params.put("product_code", "FAST_INSTANT_TRADE_PAY");

        String body;

        try {
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setBizContent(JSONObject.toJSONString(params));
            request.setReturnUrl(env.getProperty("alipay.return.url"));
            request.setNotifyUrl(env.getProperty("alipay.notify.url"));
            body = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            LOG.error("生成支付宝支付表单失败", e);
            throw new RuntimeException("生成支付宝支付表单失败");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("支付表单内容: {}", body);
        }

        PayDTO payDTO = new PayDTO();
        payDTO.setPrice(price);
        payDTO.setOrderId(orderId);
        String encodedBody = Base64.getEncoder().encodeToString(body.getBytes("UTF-8"));
        payDTO.setCodeUrl(encodedBody);
        return payDTO;
    }
}
