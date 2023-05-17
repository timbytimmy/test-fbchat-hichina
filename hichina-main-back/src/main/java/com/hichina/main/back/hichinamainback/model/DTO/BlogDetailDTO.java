package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogDetailDTO {
    private String title;

    private String content;

    private String createdTime;

    private String authorName;
}
