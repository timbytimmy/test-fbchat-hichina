package com.hichina.main.back.hichinamainback.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hichina.main.back.hichinamainback.model.DTO.TranslateObj;
import com.hichina.main.back.hichinamainback.model.mongo.BaiduTranslateCache;
import com.hichina.main.back.hichinamainback.repository.BaiduTranslateCacheRepository;
import com.hichina.main.back.hichinamainback.utils.HttpUtils;
import com.hichina.main.back.hichinamainback.utils.Md5Util;
import com.hichina.main.back.hichinamainback.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaiduTranslateApiService {
    private static final Logger LOG = LoggerFactory.getLogger(BaiduTranslateApiService.class);

    private static Gson gson = new Gson();

    @Autowired
    private RedisUtil redisUtil;

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
        int minMilli = 500;
        int maxMilli = 1100;
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

    @Async
    public void triggerTranslationJobAsync(String translationKey, String query, String from, String to) {
        String translatedText = "";
        Document doc = Jsoup.parse(query);
        htmlTextTagsTranslate(doc.childNodes(), from, to);
        translatedText = doc.html();

        cacheTranslation(translationKey, translatedText);

        redisUtil.delete(translationKey);
    }

    private void htmlTextTagsTranslate(List<Node> nodes, String from, String to) {
        for(Node node:nodes) {
            htmlTextTagsTranslate(node.childNodes(), from, to);
            if(node instanceof TextNode){
                String originalText = ((TextNode) node).text();

                if(StringUtils.isEmpty(originalText.trim())){
                    LOG.info("==============empty text node skip========");
                    continue;
                }
                JsonObject result = getTransResult(originalText, from, to);
                String transString = "";
                if(result.get("trans_result") != null){
                    LOG.info("=====has trans result");

                    JsonArray transResults = (JsonArray)result.get("trans_result");
                    if(transResults.size()>0){
                        LOG.info("=====has trans result 2");

                        TranslateObj translateObj = gson.fromJson(transResults.get(0).getAsJsonObject(), TranslateObj.class);
                        transString = translateObj.getDst();
                        LOG.info("===transString==="+transString);
                    }
                }else{
                    LOG.info("=====empty trans result");
                    LOG.info(result.toString());
                }
                ((TextNode) node).text(transString);
            }
        }
    }
}
