package com.hichina.admin.hichinaadminbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

@Mapper
@Component(value = "blogMapper")
public interface BlogMapper {
    @Update("UPDATE blog SET head_image_url = REGEXP_SUBSTR(content, '(http|https)://[^ \\n]+(jpg|jpeg|png|gif)') WHERE last_update_time>#{startDate} and head_image_url is null or LENGTH(head_image_url)<1 and content REGEXP '(http|https)://[^ \\n]+(jpg|jpeg|png|gif)';")
    void updateCoverWithFirstImage(Date startDate);
}
