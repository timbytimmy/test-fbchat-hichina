package com.hichina.main.back.hichinamainback.service;

import com.hichina.main.back.hichinamainback.config.Constants;
import com.hichina.main.back.hichinamainback.model.mongo.PageObjectView;
import com.hichina.main.back.hichinamainback.repository.PageObjectViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageStatsService {

    @Autowired
    private PageObjectViewRepository pageObjectViewRepository;


    public Long LogPageObjectView(String skuGroupId, String objType){
        List<PageObjectView> pageViews =  pageObjectViewRepository.queryByIdAndType(skuGroupId, objType);

        if(pageViews.isEmpty()){
            PageObjectView newPageView = new PageObjectView();
            newPageView.setViewCnt(1L);
            newPageView.setObjType(objType);
            newPageView.setObjId(skuGroupId);
            pageObjectViewRepository.save(newPageView);
            return 1L;
        }else{
            PageObjectView firstObj = pageViews.get(0);
            firstObj.setViewCnt(firstObj.getViewCnt()+1);
            pageObjectViewRepository.save(firstObj);
            return firstObj.getViewCnt();
        }

    }
}
