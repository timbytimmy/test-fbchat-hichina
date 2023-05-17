package com.hichina.main.back.hichinamainback.utils;

import com.alipay.api.internal.util.file.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;

@Component
public class WxPayUtil {

    @Value("${wechatpay.privateKeyPath}")
    private String privateKeyPath;

    @Value("${wechatpay.mchId}")
    private String mchId;

    @Value("${wechatpay.certSerialNo}")
    private String certSerialNo;

    @Value("${wechatpay.apiKey}")
    private String apiKey;

    /**
     * @Description: 获取httpClient
     * @Param: []
     * @return: org.apache.http.impl.client.CloseableHttpClient
     * @Author: XQD
     * @Date:2022/8/13 23:16
     */
//    public static CloseableHttpClient getHttpClient() {
//        CloseableHttpClient httpClient = null;
//        // 加载商户私钥（privateKey：私钥字符串）
//        try {
//            InputStream targetStream = FileUtils.openInputStream(new File(privateKeyPath));
//            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(targetStream);
//            //使用自动更新的签名验证器，不需要传入证书
//            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
//                    new WechatPay2Credentials(mchId, new PrivateKeySigner(certSerialNo, merchantPrivateKey)),
//                    apiKey.getBytes("utf-8"));
//            // 初始化httpClient
//            httpClient = WechatPayHttpClientBuilder.create()
//                    .withMerchant(mchId, certSerialNo, merchantPrivateKey)
//                    .withValidator(new WechatPay2Validator(verifier))
//                    .build();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return httpClient;
//    }


    /**
     * @Description: 生成签名
     * @Param: [message]
     * @return: java.lang.String
     * @Author: XQD
     * @Date:2022/6/7 16:00
     */
//    public static String sign(byte[] message) throws Exception {
//        Signature sign = Signature.getInstance("SHA256withRSA");
//        InputStream targetStream = FileUtils.openInputStream(new File(privateKeyPath));
//        PrivateKey privateKey = PemUtil.loadPrivateKey(targetStream);
//        sign.initSign(privateKey);
//        sign.update(message);
//        return Base64.getEncoder().encodeToString(sign.sign());
//    }


    /**
     * @Description: 验证签名
     * @Param: [serial, message, signature]请求头中带的序列号, 验签名串, 请求头中的应答签名
     * @return: boolean
     * @Author: XQD
     * @Date:2021/9/14 10:36
     */
    public boolean signVerify(String serial, String message, String signature) throws IOException {
        AutoUpdateCertificatesVerifier verifier = null;
        try {
            // 加载商户私钥（privateKey：私钥字符串）
            InputStream targetStream = FileUtils.openInputStream(new File(privateKeyPath));

            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(targetStream);

            //使用自动更新的签名验证器，不需要传入证书
            verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(mchId, new PrivateKeySigner(certSerialNo, merchantPrivateKey)),
                    apiKey.getBytes("utf-8"));

            return verifier.verify(serial, message.getBytes("utf-8"), signature);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 解密订单信息
     * @Param: [body] 应答报文主体
     * @return: java.lang.String
     * @Author: XQD
     * @Date:2021/9/14 11:48
     */
    public String decryptOrder(String body) {
        try {
            AesUtil aesUtil = new AesUtil(apiKey.getBytes("utf-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(body);
            JsonNode resource = node.get("resource");
            String ciphertext = resource.get("ciphertext").textValue();
            String associatedData = resource.get("associated_data").textValue();
            String nonce = resource.get("nonce").textValue();
            return aesUtil.decryptToString(associatedData.getBytes("utf-8"), nonce.getBytes("utf-8"), ciphertext);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

//    public String test() throws IOException {
////        String message = "";
////        message += "1683011935\n";
////        message += "HkniF8Hs47Kpcl5Dm6vRD9QvuFe2l4WE\n";
////        message += "{\"summary\":\"支付成功\",\"event_type\":\"TRANSACTION.SUCCESS\",\"create_time\":\"2023-05-02T15:18:54+08:00\",\"resource\":{\"original_type\":\"transaction\",\"algorithm\":\"AEAD_AES_256_GCM\",\"ciphertext\":\"9evFZJuBKPdXDBzVNrsuX57KvzhCPRI3PRb+WXEMwfq3o8Q65nwpGs5nF9QYzcRLGvEOSyczUscFl6LEb9+a/j8HZEVuReeSBo7s1BkBredVns/JPjEv2y8Xb5L6LUESVofDxqoczoaS/v09H7KDlCpzLt2zMn/LBKjXJkUWQHTQY6xOWWbz9ge88j8TqgKFbgHlrSk4hSPWNEdix26/9/RgfCTLGZH5cnf7hcPu/iNGhzlSbJGS1sL/WA4Siq9uIed7AaoOjylxLY3WrOJvc294UffuXbriqM3iqWMPZnpViceobvqwzfb+Txe3vkdtKaCaDOZ0uj0JLeWNdS3Pbdz5Aw4c6wZMVn4xNY3rHJuA7wgs4fNVpQstQr98Oabe4uTbZwbiO9/fHss84pjbbsZK7u1AMkcNWU6deGbsXjI/nFWezrqCxKBQNb9gZWvBnEjZWtuZy1gBp5Oluf0Aj3Iwvyw63gLmI6XHZ65NeiMz8iZBLlt9GWHIf7crBcu3gpgoKNBzaHnfE1LfOH0JRFPdlvSYQsbATx9ZhT0m71mcz/GbJLT2m/PDFibpVWJ2EO6RBuMNP0gzVgNcU7K8O7FrzSjDsj8=\",\"associated_data\":\"transaction\",\"nonce\":\"53WqxlFBBom5\"},\"resource_type\":\"encrypt-resource\",\"id\":\"f94adeac-7ac4-5bca-a2d3-87584f774da1\"}\n";
////        boolean verified = signVerify("5B746C3C05BBE3DADC6FB02512310779FF50AA1C", message,"Ac1BDR918H08cJpFGwp1EUlqcX5fU/1EpXLFyxbZNkOl5WbbL4Kh+BlqQm7vq/I2rSZ4oSGxZPr/EIYU07x0PA7NGh8UgY9qyxi49H+iRXSpROdZnTT6QLZ3sfAkH3R9vA/L4WlJcNAPE2x52hsNIgTscMpNicezK9ZdDTV4HYi2gUc6LvFRZIqZnrIO+Ki4+og8du9718KDSna+e+u/Pq5uwpUbj3B/IbAfWuXc4wVCzPWG/UQafKOLrJ4LUdrjm661FqsWf40tAFb9IQdWDadbvpLgxN93aFdBqoiwcJ0lAwp0pUHbUaAUWCGxITaZoyM+Pk5wfymqKkKGUeA94w==");
//
//        StringBuilder signStr = new StringBuilder();
//        // 应答时间戳\n
//        signStr.append("1683011935").append("\n");
//        // 应答随机串\n
//        signStr.append("HkniF8Hs47Kpcl5Dm6vRD9QvuFe2l4WE").append("\n");
//        String body = "{\"summary\":\"支付成功\",\"event_type\":\"TRANSACTION.SUCCESS\",\"create_time\":\"2023-05-02T15:18:54+08:00\",\"resource\":{\"original_type\":\"transaction\",\"algorithm\":\"AEAD_AES_256_GCM\",\"ciphertext\":\"9evFZJuBKPdXDBzVNrsuX57KvzhCPRI3PRb+WXEMwfq3o8Q65nwpGs5nF9QYzcRLGvEOSyczUscFl6LEb9+a/j8HZEVuReeSBo7s1BkBredVns/JPjEv2y8Xb5L6LUESVofDxqoczoaS/v09H7KDlCpzLt2zMn/LBKjXJkUWQHTQY6xOWWbz9ge88j8TqgKFbgHlrSk4hSPWNEdix26/9/RgfCTLGZH5cnf7hcPu/iNGhzlSbJGS1sL/WA4Siq9uIed7AaoOjylxLY3WrOJvc294UffuXbriqM3iqWMPZnpViceobvqwzfb+Txe3vkdtKaCaDOZ0uj0JLeWNdS3Pbdz5Aw4c6wZMVn4xNY3rHJuA7wgs4fNVpQstQr98Oabe4uTbZwbiO9/fHss84pjbbsZK7u1AMkcNWU6deGbsXjI/nFWezrqCxKBQNb9gZWvBnEjZWtuZy1gBp5Oluf0Aj3Iwvyw63gLmI6XHZ65NeiMz8iZBLlt9GWHIf7crBcu3gpgoKNBzaHnfE1LfOH0JRFPdlvSYQsbATx9ZhT0m71mcz/GbJLT2m/PDFibpVWJ2EO6RBuMNP0gzVgNcU7K8O7FrzSjDsj8=\",\"associated_data\":\"transaction\",\"nonce\":\"53WqxlFBBom5\"},\"resource_type\":\"encrypt-resource\",\"id\":\"f94adeac-7ac4-5bca-a2d3-87584f774da1\"}";
//
//        signStr.append(body).append("\n");
//
//        if(signVerify("5B746C3C05BBE3DADC6FB02512310779FF50AA1C", signStr.toString(), "Ac1BDR918H08cJpFGwp1EUlqcX5fU/1EpXLFyxbZNkOl5WbbL4Kh+BlqQm7vq/I2rSZ4oSGxZPr/EIYU07x0PA7NGh8UgY9qyxi49H+iRXSpROdZnTT6QLZ3sfAkH3R9vA/L4WlJcNAPE2x52hsNIgTscMpNicezK9ZdDTV4HYi2gUc6LvFRZIqZnrIO+Ki4+og8du9718KDSna+e+u/Pq5uwpUbj3B/IbAfWuXc4wVCzPWG/UQafKOLrJ4LUdrjm661FqsWf40tAFb9IQdWDadbvpLgxN93aFdBqoiwcJ0lAwp0pUHbUaAUWCGxITaZoyM+Pk5wfymqKkKGUeA94w==")){
//            return "signed passed";
//        }else{
//            return "not passed";
//        }
//    }

}