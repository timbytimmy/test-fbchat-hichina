package com.hichina.main.back.hichinamainback.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Optional;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.mapper.BlogMapper;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.Blog;
import com.hichina.main.back.hichinamainback.model.DTO.BlogCreationDTO;
import com.hichina.main.back.hichinamainback.model.DTO.BlogEditDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.PaginationWrapper;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    private static LanguageDetector languageDetector;

    static {
        //load all languages:
        List<LanguageProfile> languageProfiles = null;
        try {
            languageProfiles = new LanguageProfileReader().readAllBuiltIn();
            //build language detector:
            languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{blogId}")
    @EnableHichinaAutoLog(description = "Get blog detail by id")
    public HichinaResponse getBlogDetailById(@PathVariable("blogId")String blogId){
        HichinaResponse ret = new HichinaResponse();
        List<Blog> blogs = blogMapper.findRawBlogById(blogId);
        if(blogs.isEmpty()){
            throw new RuntimeException("Blog not found: "+ blogId);
        }
        Blog blog = blogs.get(0);
        ret.setData(blog);
        ret.setOk(true);
        ret.setMessage("Successfully load blog detail: "+blogId);
        return ret;

    }

    @GetMapping("/mybloglist")
    @EnableHichinaAutoLog(description = "Get my blog list")
    public HichinaResponse getMyBlogs(@RequestParam(value = "page", required = true) Integer page,
                                      @RequestParam(value = "pageSize", required = true) Integer size,
                                      @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        if(page>0){
            PageHelper.startPage(page,size);
        }

        Integer cnt = 0;
        List<Blog> blogs;
        if(StringUtils.isEmpty(query)){
            blogs = blogMapper.findByUserId(user.getUserId());
            cnt = blogMapper.countByUserId(user.getUserId());
        }else{
            blogs = blogMapper.findByUserIdAndQuery(user.getUserId(), query);
            cnt = blogMapper.countByUserIdAndQuery(user.getUserId(), query);
        }

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(blogs);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);
        ret.setMessage("Successfully get my blogs");

        return ret;
    }

    @PostMapping
    @EnableHichinaAutoLog(description = "create blog")
    public HichinaResponse createBlog(@RequestBody BlogCreationDTO request){
        HichinaResponse ret = new HichinaResponse();

        Blog blog = new Blog();

        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        Date currentTimestamp = new Date();
        blog.setBlogId(java.util.UUID.randomUUID().toString());
        blog.setCreatedTime(currentTimestamp);
        blog.setLastUpdateTime(currentTimestamp);
        blog.setContent(request.getContent());
        blog.setTitle(request.getTitle());
        blog.setHeadImageUrl(request.getHeadImageUrl());
        blog.setDraft(request.getDraft());
        // sample user principal format: BloodySail2283[Bloodysail2283J@gmail.com]
        blog.setUserId(user.getUserId());
        blog.setLanguage(detectLanguage(request.getContent()));

        blogMapper.insert(blog);

        ret.setData(blog);
        ret.setOk(true);
        ret.setMessage("成功创建博客");
        return ret;
    }

    @DeleteMapping("/{blogId}")
    @EnableHichinaAutoLog(description = "delete blog")
    public HichinaResponse deleteBlog(@PathVariable("blogId")String blogId){
        HichinaResponse ret = new HichinaResponse();
        blogMapper.delete(blogId);

        ret.setMessage("Succeed deleteing blog: "+blogId);
        ret.setOk(true);
        return ret;
    }

    @PutMapping("/edit-basic/{blogId}")
    @EnableHichinaAutoLog(description = "edit blog basic")
    public HichinaResponse editBasic(@PathVariable("blogId")String blogId, @RequestBody BlogEditDTO request){
        HichinaResponse ret = new HichinaResponse();
        List<Blog> blogs = blogMapper.findRawBlogById(blogId);
        if(blogs.isEmpty()){
            throw new RuntimeException("Blog not found to update:" + blogId);
        }
        Blog blog = blogs.get(0);

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setHeadImageUrl(request.getHeadImageUrl());
        blog.setLastUpdateTime(new Date());
        blog.setLanguage(detectLanguage(request.getContent()));

        blogMapper.update(blog);

        ret.setOk(true);
        ret.setMessage("Succeed update blog: "+ blogId);

        return ret;
    }

    @PutMapping("/publish/{blogId}")
    @EnableHichinaAutoLog(description = "publish blog")
    public HichinaResponse publishBlog(@PathVariable("blogId")String blogId){
        HichinaResponse ret = new HichinaResponse();
        List<Blog> blogs = blogMapper.findRawBlogById(blogId);
        if(blogs.isEmpty()){
            throw new RuntimeException("Cannot find blogs by id: "+ blogId);
        }
        Blog blog = blogs.get(0);

        blog.setDraft(false);

        blogMapper.update(blog);
        ret.setOk(true);
        ret.setMessage("Succeed updating blog: "+ blogId);
        return ret;
    }

    private String detectLanguage(String input){
        //create a text object factory
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        //query:
        TextObject textObject = textObjectFactory.forText(input);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if(lang.isPresent()){
            return lang.get().getLanguage();
        }else{
            return "en";
        }
    }
}
