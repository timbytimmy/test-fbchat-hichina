package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hichina.main.back.hichinamainback.model.DTO.BaiduTranslateRequestDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.TranslateObj;
import com.hichina.main.back.hichinamainback.repository.BaiduTranslateCacheRepository;
import com.hichina.main.back.hichinamainback.service.BaiduTranslateApiService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/baidutranslate")
public class BaiduTranslateController {
    private static final Logger LOG = LoggerFactory.getLogger(BaiduTranslateController.class);

    @Autowired
    private BaiduTranslateApiService baiduTranslateApiService;



    private static Gson gson = new Gson();

    @PostMapping
    public HichinaResponse baiduTranslate(@RequestBody BaiduTranslateRequestDTO request){
        HichinaResponse ret = new HichinaResponse();

        String translatedText = "";

        String cachedText = baiduTranslateApiService.fetchTranslationValueByKey(request.getTranslationKey());
        if(cachedText!=null){
            translatedText = cachedText;
        }else{
            Document doc = Jsoup.parse(request.getQuery());
            htmlTextTagsTranslate(doc.childNodes(), request.getFrom(), request.getTo());
            translatedText = doc.html();
            baiduTranslateApiService.cacheTranslation(request.getTranslationKey(), translatedText);
        }
        ret.setOk(true);
        ret.setMessage("Succeed getting baidu translation");
        ret.setData(translatedText);
        return ret;
    }

    private void htmlTextTagsTranslate(List<Node> nodes, String from, String to) {
        for(Node node:nodes) {
            htmlTextTagsTranslate(node.childNodes(), from, to);
            if(node instanceof TextNode){
                String originalText = ((TextNode) node).text();

                JsonObject result = baiduTranslateApiService.getTransResult(originalText, from, to);
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
