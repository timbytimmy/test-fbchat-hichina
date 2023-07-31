package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.Gson;
import com.hichina.main.back.hichinamainback.model.DTO.BaiduTranslateRequestDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.service.BaiduTranslateApiService;
import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/public/baidutranslate")
public class BaiduTranslateController {
    private static final Logger LOG = LoggerFactory.getLogger(BaiduTranslateController.class);

    @Autowired
    private BaiduTranslateApiService baiduTranslateApiService;


    @Autowired
    private RedisUtil redisUtil;

    private static Gson gson = new Gson();

    @PostMapping
    public HichinaResponse baiduTranslate(@RequestBody BaiduTranslateRequestDTO request){
        HichinaResponse ret = new HichinaResponse();

        // check if translation job is ongoing, if it is return and notify user, else continue with reading cache or trigger translation job

        if(redisUtil.get(request.getTranslationKey())!=null){
            ret.setMessage("A translation job is ongoing...");
            ret.setOk(false);
            return ret;
        }

        String translatedText = "";

        String cachedText = baiduTranslateApiService.fetchTranslationValueByKey(request.getTranslationKey());
        if(cachedText!=null){
            translatedText = cachedText;
            ret.setOk(true);
            ret.setMessage("Succeed getting baidu translation");
            ret.setData(translatedText);
            return ret;
        }else{
            // 1. add redis cache indicating a translation job is submitted
            redisUtil.add(request.getTranslationKey(), 5*60L, "");
            // 2. start an async job doing the translation and return with notify message imediately
            baiduTranslateApiService.triggerTranslationJobAsync(request.getTranslationKey(), request.getQuery(), request.getFrom(), request.getTo());
            ret.setOk(true);
            ret.setMessage("Triggered a job translating this blog");
            return ret;
        }
    }


}
