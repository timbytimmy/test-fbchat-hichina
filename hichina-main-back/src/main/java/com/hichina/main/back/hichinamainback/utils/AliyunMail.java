package com.hichina.main.back.hichinamainback.utils;

import javax.mail.*;
import javax.mail.internet.*;
import javax.net.ssl.SSLContext;
import java.security.Security;
import java.util.Properties;

public class AliyunMail {
    public static final String ALIDM_SMTP_HOST = "smtp.qiye.aliyun.com";

    public static final String ALIDM_SMTP_PORT = "465";


    /**
     * @param sendAddress 发件人地址
     * @param sendPassword 发件人密码
     * @param host 协议
     * @param port 端口
     * @param subject 标题
     * @param content 内容
     * @throws Exception
     * @throws AddressException
     */
    public static void sendMail(String sendAddress,String sendPassword,String host,String port,String subject,String content,String internetAddress) throws AddressException, Exception {
        //设置SSL连接、邮件环境
       // Security.addProvider(new sun.security.ssl.SunJSSE());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.port", port);//设置端口

        props.put("mail.user", sendAddress);
        props.put("mail.password", sendPassword);

        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.debug", "true");//启用调试
        //建立邮件会话
        Session session = Session.getDefaultInstance(props, new Authenticator() {   //身份认证
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendAddress, sendPassword);//发件人账号、密码
            }
        });

        //建立邮件对象
        MimeMessage message = new MimeMessage(session);
        //设置邮件的发件人、收件人、主题
        //附带发件人名字
        //  message.setFrom(new InternetAddress("from_mail@qq.com", "optional-personal"));
        message.setFrom(new InternetAddress(sendAddress));//发件人账号
        message.setRecipients(Message.RecipientType.TO, internetAddress);//收件人账号

        //标题
        message.setSubject(subject);//邮件标题

        //内容
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/html;charset=utf-8");//邮件内容
        multipart.addBodyPart(contentPart);

        message.setContent(multipart);

        //发送邮件
        Transport.send(message);
    }
//    public static void main(String[] args) {
//        try {
//            sendMail("register@hichinatrip.com", "Qq123456", ALIDM_SMTP_HOST, ALIDM_SMTP_PORT, "subject test",
//                    "Hello Email", "fengchang_fight@163.com");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}