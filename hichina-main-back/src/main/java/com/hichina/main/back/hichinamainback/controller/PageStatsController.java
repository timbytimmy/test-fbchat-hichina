package com.hichina.main.back.hichinamainback.controller;

import com.hichina.main.back.hichinamainback.config.Constants;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.service.PageStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/pagestats")
public class PageStatsController {
    @Autowired
    private PageStatsService pageStatsService;

    @PostMapping("/view-product/{skuGroupId}")
    @EnableHichinaAutoLog(description = "logProductView")
    public HichinaResponse logProductView(@PathVariable("skuGroupId") String skuGroupId){
        HichinaResponse ret = new HichinaResponse();

        Long viewCnt = pageStatsService.LogPageObjectView(skuGroupId, Constants.PRODUCTDETAILPAGE);

        ret.setMessage("Update succeed incrementing view count for product page");
        ret.setOk(true);
        ret.setData(viewCnt);

        return ret;
    }

    @PostMapping("/view-destination/{destinationId}")
    @EnableHichinaAutoLog(description = "logDestinationDetailView")
    public HichinaResponse logDestinationView(@PathVariable("destinationId") String destination){
        HichinaResponse ret = new HichinaResponse();

        Long viewCnt = pageStatsService.LogPageObjectView(destination, Constants.DESTINATIONDETAILPAGE);

        ret.setMessage("Update succeed incrementing view count for destination detail page");
        ret.setOk(true);
        ret.setData(viewCnt);

        return ret;
    }

    @PostMapping("/view-blog/{blogId}")
    @EnableHichinaAutoLog(description = "logProductView")
    public HichinaResponse logBlogView(@PathVariable("blogId") String blogId){
        HichinaResponse ret = new HichinaResponse();

        Long viewCnt = pageStatsService.LogPageObjectView(blogId, Constants.BLOGDETAILPAGE);

        ret.setMessage("Update succeed incrementing view count for product page");
        ret.setOk(true);
        ret.setData(viewCnt);

        return ret;
    }
}
