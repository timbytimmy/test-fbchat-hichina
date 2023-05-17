package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.Blog;
import com.hichina.main.back.hichinamainback.model.DTO.BlogDetailDTO;
import com.hichina.main.back.hichinamainback.model.DTO.BlogListItemDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "blogMapper")
public interface BlogMapper {
    @Select("select b.title,  u.username as authorName, b.created_time as createdTime, b.content  from blog b inner join user u on u.user_id = b.user_id where b.blog_id=#{blogId}")
    List<BlogDetailDTO> findBlogById(String blogId);

    @Select("select blog_id, created_time, title, head_image_url, content from blog where draft=0 order by created_time desc")
    List<BlogListItemDTO> findBlogSummaryList();

    @Select("select blog_id, created_time, title, head_image_url, content from blog where draft=0 and title like CONCAT('%',CONCAT(#{query},'%')) order by created_time desc")
    List<BlogListItemDTO> findBlogSummaryListByQuery(String query);

    @Select("select count(*) from blog where draft=0 and title like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countBlogSummaryListByQuery(String query);

    @Select("select count(*) from blog where draft=0")
    Integer countBlogSummaryList();

    @Select("select * from blog where blog_id=#{blogId}")
    List<Blog> findRawBlogById(String blogId);

    @Delete("delete from blog where blog_id=#{blogId}")
    void delete(String blogId);

    @Select("select * from blog where user_id=#{userId} order by created_time desc")
    List<Blog> findByUserId(String userId);

    @Select("select * from blog where user_id=#{userId} and title like CONCAT('%',CONCAT(#{query},'%')) order by created_time desc")
    List<Blog> findByUserIdAndQuery(String userId, String query);

    @Select("select count(*) from blog where user_id=#{userId} and title like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByUserIdAndQuery(String userId, String query);
    @Select("select count(*) from blog where user_id=#{userId}")
    Integer countByUserId(String userId);

    @Insert("INSERT INTO blog(blog_id, user_id, created_time, last_update_time, title, head_image_url, language, content, draft) VALUES(#{blogId},#{userId}, #{createdTime}, #{lastUpdateTime}, #{title}, #{headImageUrl}, #{language}, #{content}, #{draft})")
    Long insert(Blog blog);

    @Update("update `blog` SET user_id=#{userId}, created_time=#{createdTime}, last_update_time=#{lastUpdateTime}, title=#{title}, head_image_url=#{headImageUrl}, language=#{language}, content=#{content}, draft=#{draft} where blog_id=#{blogId}")
    void update(Blog blog);
}
