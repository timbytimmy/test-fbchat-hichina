package com.hichina.main.back.hichinamainback.controller;

import com.aliyuncs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.mapper.BlogMapper;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.DTO.BlogDetailDTO;
import com.hichina.main.back.hichinamainback.model.DTO.BlogListItemDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.PaginationWrapper;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/blog")
public class PublicBlogController {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/{blogId}")
    @EnableHichinaAutoLog(description = "Get blog detail by id")
    public HichinaResponse getBlogDetailById(@PathVariable("blogId") String blogId){
        List<BlogDetailDTO> blogs = blogMapper.findBlogById(blogId);
        HichinaResponse ret = new HichinaResponse();

        if(blogs.isEmpty()){
            ret.setOk(false);
            ret.setMessage("失败获取id为%s的blog详情");
            return ret;
        }
        ret.setData(blogs.get(0));
        ret.setOk(true);
        ret.setMessage(String.format("成功获取id为%s的blog详情", blogId));
        return ret;
    }

    @GetMapping("/list")
    @EnableHichinaAutoLog(description = "Get blog list by page")
    public HichinaResponse getBloglist(@RequestParam(value = "page", required = true) Integer page,
                                       @RequestParam(value = "pageSize", required = true) Integer size,
                                       @RequestParam(value = "query") String query){

        HichinaResponse ret = new HichinaResponse();

        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<BlogListItemDTO> blogListItemDTOS;
        Integer cnt = 0;
        if(StringUtils.isEmpty(query)){
            blogListItemDTOS = blogMapper.findBlogSummaryList();
            cnt = blogMapper.countBlogSummaryList();
        }else{
            blogListItemDTOS = blogMapper.findBlogSummaryListByQuery(query);
            cnt = blogMapper.countBlogSummaryListByQuery(query);
        }

        blogListItemDTOS = shortenContent(blogListItemDTOS);

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(blogListItemDTOS);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);
        ret.setMessage("Succeed getting blog list");

        return ret;
    }

    private List<BlogListItemDTO> shortenContent(List<BlogListItemDTO> blogListItemDTOS){
        blogListItemDTOS = blogListItemDTOS.stream().map(r->{return removeHtmlTagAndSubstring(r);}).collect(Collectors.toList());
        return blogListItemDTOS;
    }

    private BlogListItemDTO removeHtmlTagAndSubstring(BlogListItemDTO item){
         String raw = Jsoup.parse( item.getContent()).text();
         item.setContent(raw.substring(0, Math.min(100, raw.length())));
         return item;
    }
}
