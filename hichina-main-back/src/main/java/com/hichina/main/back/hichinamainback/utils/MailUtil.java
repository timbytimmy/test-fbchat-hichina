package com.hichina.main.back.hichinamainback.utils;


import com.hichina.main.back.hichinamainback.controller.PublicRegisterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private Environment env;

    public void sendMailV2(String to, String title, String content){
        try {
            LOG.info(String.format("===sending reg validate email to %s with title %s ",to, title));
            AliyunMail.sendMail(env.getProperty("hichina.email.sender"), env.getProperty("hichina.email.authcode"), AliyunMail.ALIDM_SMTP_HOST, AliyunMail.ALIDM_SMTP_PORT, title,
                    content, to);
        } catch (Exception e) {
            LOG.error("===Error sending aliyun mail during registration: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMail(String to, String title, String content){
        // TODO Auto-generated method stub
        Properties prop = new Properties();
        prop.put("mail.host", env.getProperty("hichina.mail.host"));
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        try {
            // 使用java发送邮件5步骤
            // 1.创建sesssion
            Session session = Session.getInstance(prop);
            // 开启session的调试模式，可以查看当前邮件发送状态
            session.setDebug(true);

            Transport ts;
            // 2.通过session获取Transport对象（发送邮件的核心API）
            ts = session.getTransport();
            // 3.通过邮件用户名密码链接
            String sender = env.getProperty("hichina.email.sender");
            String emailAuth = env.getProperty("hichina.email.authcode");
            ts.connect(sender, emailAuth);
            // 4.创建邮件
            Message msg = createSimpleMail(session, sender, to, title, content);
            // 5.发送电子邮件
            ts.sendMessage(msg, msg.getAllRecipients());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public MimeMessage createSimpleMail(Session session, String sender, String to, String title, String content)
            throws AddressException, MessagingException {
        // 创建邮件对象
        MimeMessage mm = new MimeMessage(session);
        // 设置发件人
        mm.setFrom(new InternetAddress(sender));
        // 设置收件人
        mm.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // 设置抄送人
        //mm.setRecipient(Message.RecipientType.CC, new InternetAddress(
        //      "用户名@163.com"));

        mm.setSubject(title);
        mm.setContent(content, "text/html;charset=utf-8");

        return mm;
    }
}
