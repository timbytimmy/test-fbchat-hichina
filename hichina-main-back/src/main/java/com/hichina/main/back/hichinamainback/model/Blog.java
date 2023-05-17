package com.hichina.main.back.hichinamainback.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Blog {
    private String blogId;

    private String userId;

    private Date createdTime;

    private Date lastUpdateTime;

    private String title;

    private String headImageUrl;

    private String language;

    private String content;

    private Boolean draft;
}
