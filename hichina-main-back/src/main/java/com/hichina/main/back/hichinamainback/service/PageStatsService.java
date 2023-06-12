package com.hichina.main.back.hichinamainback.service;

import com.hichina.main.back.hichinamainback.config.Constants;
import com.hichina.main.back.hichinamainback.model.mongo.HichinaPv;
import com.hichina.main.back.hichinamainback.model.mongo.PageObjectView;
import com.hichina.main.back.hichinamainback.repository.PageObjectViewRepository;
import com.hichina.main.back.hichinamainback.repository.PvRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PageStatsService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PageObjectViewRepository pageObjectViewRepository;

    @Autowired
    private PvRepository pvRepository;

    public String logPv(String pageName){
        String clientIp = getClientIp();
        Date now = new Date();

        HichinaPv hichinaPv = new HichinaPv();
        hichinaPv.setClientIp(clientIp);
        hichinaPv.setTimestamp(now);
        hichinaPv.setPageName(pageName);

        pvRepository.save(hichinaPv);

        return clientIp;
    }

    public String getClientIp() {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

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
