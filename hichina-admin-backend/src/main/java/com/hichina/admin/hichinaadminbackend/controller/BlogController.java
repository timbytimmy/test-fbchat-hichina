package com.hichina.admin.hichinaadminbackend.controller;

import com.aliyuncs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.BlogMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {

    @Autowired
    private BlogMapper blogMapper;

    @PutMapping("/batchDraft")
    @Transactional
    public HichinaResponse draftBlogs(@RequestBody GeneralBatchDraftRequest req){
        
        blogMapper.batchDraft(req.getToDraft());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDraft());
        ret.setMessage("成功批量存草稿博客");

        return ret;
    }

    @DeleteMapping("/batch")
    @Transactional
    public HichinaResponse deleteBlogs(@RequestBody GeneralBatchDeleteRequest req){

        // delete base product record
        blogMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除博客");

        return ret;
    }

    @GetMapping("/list")
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
}
