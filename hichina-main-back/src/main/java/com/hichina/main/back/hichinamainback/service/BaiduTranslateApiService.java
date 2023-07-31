package com.hichina.main.back.hichinamainback.service;

import com.google.gson.JsonObject;
import com.hichina.main.back.hichinamainback.model.mongo.BaiduTranslateCache;
import com.hichina.main.back.hichinamainback.repository.BaiduTranslateCacheRepository;
import com.hichina.main.back.hichinamainback.utils.HttpUtils;
import com.hichina.main.back.hichinamainback.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BaiduTranslateApiService {
    private static final Logger LOG = LoggerFactory.getLogger(BaiduTranslateApiService.class);

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    @Autowired
    private BaiduTranslateCacheRepository baiduTranslateCacheRepository;

    @Value("${baidu.appId}")
    private String appId;

    @Value("${baidu.securityKey}")
    private String securityKey;

    public JsonObject getTransResult(String query, String from, String to) {
        LOG.info("==========querying======"+query);
        Map<String, String> params = buildParams(query, from, to);
        JsonObject ret = HttpUtils.postUrlWithParams(TRANS_API_HOST, params);
        int retryCount = 0;
        int maxRetryCount = 3;
        int minMilli = 300;
        int maxMilli = 1000;
        while(retryCount++<maxRetryCount && ret.get("error_code")!=null && "54003".equals(ret.get("error_code").getAsString())){
            try {
                int randomNum = minMilli + (int)(Math.random() * ((maxMilli - minMilli) + 1));
                Thread.sleep(randomNum);
                LOG.info("====retrying after sleep: "+ randomNum + " millisec");
                LOG.info("====retrying sending to baidu: "+ retryCount);

                ret = HttpUtils.postUrlWithParams(TRANS_API_HOST, params);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    public String fetchTranslationValueByKey(String translationKey){
        if(baiduTranslateCacheRepository.queryByTranslationKey(translationKey).size()>0){
            return baiduTranslateCacheRepository.queryByTranslationKey(translationKey).get(0).getText();
        }
        return null;
    }

    public void cacheTranslation(String translationKey, String text){
        BaiduTranslateCache obj = new BaiduTranslateCache();
        obj.setText(text);
        obj.setTranslationKey(translationKey);
        baiduTranslateCacheRepository.save(obj);
    }


    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appId);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appId + query + salt + securityKey; // 加密前的原文
        params.put("sign", Md5Util.md5v2(src));

        return params;
    }
}
