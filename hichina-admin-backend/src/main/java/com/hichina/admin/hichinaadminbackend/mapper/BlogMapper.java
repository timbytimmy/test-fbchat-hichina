package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.BlogListItemDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component(value = "blogMapper")
public interface BlogMapper {
    @Delete("<script>" +
            "Delete FROM blog WHERE blog_id in \n" +
            "    <foreach item='item' collection='blogIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> blogIds);

    @Update("<script>" +
            "update blog set draft=1 WHERE blog_id in \n" +
            "    <foreach item='item' collection='blogIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDraft(List<String> blogIds);


    @Update("UPDATE blog SET head_image_url = REGEXP_SUBSTR(content, '(http|https)://[^ \\n]+(jpg|jpeg|png|gif)') WHERE last_update_time>#{startDate} and head_image_url is null or LENGTH(head_image_url)<1 and content REGEXP '(http|https)://[^ \\n]+(jpg|jpeg|png|gif)';")
    void updateCoverWithFirstImage(Date startDate);

    @Select("select blog_id, title, created_time, draft from blog order by created_time desc")
    List<BlogListItemDTO> findBlogSummaryList();

    @Select("select count(*) from blog where title like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countBlogSummaryListByQuery(String query);

    @Select("select count(*) from blog")
    Integer countBlogSummaryList();

    @Select("select blog_id, title, created_time, draft from blog where title like CONCAT('%',CONCAT(#{query},'%')) or blog_id like CONCAT('%',CONCAT(#{query},'%')) order by created_time desc")
    List<BlogListItemDTO> findBlogSummaryListByQuery(String query);

}
