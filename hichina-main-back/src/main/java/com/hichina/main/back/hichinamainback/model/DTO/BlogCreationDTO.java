package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogCreationDTO {
    private String title;

    private String content;

    private String headImageUrl;

    private Boolean draft;
}
