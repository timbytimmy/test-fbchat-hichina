package com.hichina.main.back.hichinamainback.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.mapper.OrderMapper;
import com.hichina.main.back.hichinamainback.model.Order;
import com.hichina.main.back.hichinamainback.service.AliPayService;
import com.hichina.main.back.hichinamainback.utils.WxPayUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/pay")
public class PublicPayController {

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WxPayUtil wxPayUtil;

    private static final Logger LOG = LoggerFactory.getLogger(PublicPayController.class);
    @PostMapping("/wechatpaycallback")
    @EnableHichinaAutoLog(description = "wechat pay callback")
    public String postProcessWechatpay(@RequestHeader Map<String, String> headers, @RequestBody JSONObject params) throws IOException {
//        LOG.info("=====wechat callback notify");
//        for(String k: headers.keySet()){
//            LOG.info("key: "+k);
//            LOG.info("value: "+headers.get(k));
//        }
//
//        LOG.info("===body that passed in: ");
//        LOG.info(params.toString());
//
//        String timestamp = headers.get("wechatpay-timestamp");
//        String nonce = headers.get("wechatpay-nonce");
//        String wechatPaySerial = headers.get("wechatpay-serial");
//        String body = params.toString();
//        String signature = headers.get("wechatpay-signature");
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(timestamp).append("\n");
//        sb.append(nonce).append("\n");
//        sb.append(body).append("\n");
//
//        boolean verified = wxPayUtil.signVerify(wechatPaySerial, sb.toString(), signature);
//        LOG.info("===is this verified request? "+ verified);

        String decrypted = wxPayUtil.decryptOrder(params.toString());
        if(!StringUtil.isNullOrEmpty(decrypted)){
            JSONObject jsonObject = JSON.parseObject(decrypted);
            if("SUCCESS".equals(jsonObject.get("trade_state"))){
                // update order to PAID
                String outTradeNo = jsonObject.get("out_trade_no").toString();
                String orderIdPostfix = outTradeNo.substring(0, 16);
                String userIdPostfix = outTradeNo.substring(16);
                List<Order> orders = orderMapper.findByOrderIdPostFixAndUserIdPostfix(orderIdPostfix, userIdPostfix);
                if(orders.isEmpty()){
                    throw new RuntimeException("cannot find the right order by composed out_trade_no");
                }
                Order order = orders.get(0);

                order.setPayingInfo(jsonObject.toJSONString());
                order.setStatus(Order.OrderStatus.PAID);

                orderMapper.update(order);
            }
        }
        return "success";
    }

    @PostMapping("/alipaycallback")
    @EnableHichinaAutoLog(description = "alipay callback")
    public String postProcessAlipay(@RequestParam Map<String, String> params){
        String verifyResult = aliPayService.verifySign(params);
        if("success".equals(verifyResult)){
            // do order update work;
            String outTradeNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            String tradeStatus = params.get("trade_status");

            List<Order> orders = orderMapper.findByOrderId(outTradeNo);
            if(orders.isEmpty()){
                LOG.info("trade status: "+ tradeStatus);
                LOG.warn(String.format("%s and did not find order to update status: %s",tradeStatus,outTradeNo));
            }else{
                Order order = orders.get(0);
                order.setStatus(Order.OrderStatus.PAID);
                order.setPayingInfo(params.toString());
                orderMapper.update(order);
            }
        }
        return verifyResult;
    }
}
