package com.hichina.main.back.hichinamainback.service;

import com.hichina.main.back.hichinamainback.model.DTO.PayDTO;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


@Service
public class WechatPayService {
    private static final Logger LOG = LoggerFactory.getLogger(WechatPayService.class);

    @Value("${wechatpay.appId}")
    private String appId;

    @Value("${wechatpay.mchId}")
    private String mchId;

    @Value("${wechatpay.apiKey}")
    private String apiKey;

    @Value("${wechatpay.certPath}")
    private String certPath;

    @Value("${wechatpay.certSerialNo}")
    private String certSerialNo;

    @Value("${wechatpay.privateKeyPath}")
    private String privateKeyPath;

    @Autowired
    private Environment env;

    private RSAAutoCertificateConfig config;

    @Autowired
    public void setConfig(){
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        config = new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(certSerialNo)
                .apiV3Key(apiKey)
                .build();
    }

    public Transaction checkPaymentStatus(String outTradeNo){
        // QueryOrderByIdRequest queryRequest = new QueryOrderByIdRequest();
        QueryOrderByOutTradeNoRequest queryOrderByOutTradeNoRequest = new QueryOrderByOutTradeNoRequest();
        queryOrderByOutTradeNoRequest.setMchid(mchId);
        queryOrderByOutTradeNoRequest.setOutTradeNo(outTradeNo);
        NativePayService service = new NativePayService.Builder().config(config).build();

        try {
            Transaction result = service.queryOrderByOutTradeNo(queryOrderByOutTradeNoRequest);
            return result;
        } catch (ServiceException e) {
            // API返回失败, 例如ORDER_NOT_EXISTS
            System.out.printf("reponse body=[%s]\n", e.getResponseBody());
            LOG.error(String.format("code=[%s], message=[%s]", e.getErrorCode(), e.getErrorMessage()));
            LOG.error(String.format("reponse body=[%s]\n", e.getResponseBody()));
            return null;
        }
    }

    public PayDTO getPayForm(String outTradeNo, String orderId, String subject, Integer price) throws UnsupportedEncodingException {
        PayDTO ret = new PayDTO();

        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        //因为单位为分，所以转换成元对应的数字
        amount.setTotal(price*100);
        request.setAmount(amount);
        request.setAppid(appId);
        request.setMchid(mchId);
        request.setDescription(subject);
        request.setNotifyUrl(env.getProperty("wechatpay.notify.url"));
        request.setOutTradeNo(outTradeNo);
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付

        ret.setPrice(price);
        ret.setOrderId(orderId);
        String encodedCodeUrl = Base64.getEncoder().encodeToString(response.getCodeUrl().getBytes("UTF-8"));
        ret.setCodeUrl(encodedCodeUrl);
        return ret;
    }
}
