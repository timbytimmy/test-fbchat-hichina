package com.hichina.main.back.hichinamainback.controller;

import com.aliyun.oss.OSSClient;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private Environment env;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @EnableHichinaAutoLog(description = "upload image")
    public HichinaResponse uploadProfile(@RequestParam("imageFile") MultipartFile uploadFile, @RequestParam("expectedType") String expectedType) {
        HichinaResponse ret = new HichinaResponse();
        try {
            byte[] profileBytes = uploadFile.getBytes();
            String BLOG_BUCKET_NAME = env.getProperty("aliyun.blog.bucket");
            String endpoint = env.getProperty("aliyun.oss.endpoint");
            String ossDomain = env.getProperty("aliyun.oss.endpoint.domain");
            String accessKeyId = env.getProperty("aliyun.oss.root.access.key.id");
            String accessKeySecret = env.getProperty("aliyun.oss.root.access.key.secret");
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);

            String key = java.util.UUID.randomUUID().toString()+".jpg";
            ossClient.putObject(BLOG_BUCKET_NAME, key, new ByteArrayInputStream(profileBytes));
            ossClient.shutdown();
            ret.setOk(true);
            String style = expectedType.equals("blogImage")?"blogcontent":"thumbnail";
            String url = "https://"+BLOG_BUCKET_NAME+"."+ossDomain+"/"+key+"?x-oss-process=style/"+style;
            ret.setData(url);
            return ret;
        } catch (IOException e) {
            LOG.error("=============="+e.getMessage()+"========");
            return null;
        }
    }
}
