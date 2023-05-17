package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BlogListItemDTO {
    private String blogId;
    private Date createdTime;
    private String title;
    private String headImageUrl;
    private String content;
}
