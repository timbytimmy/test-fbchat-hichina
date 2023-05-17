package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("BlogSlideImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSlideImage {
    private String title;
    private String subTitle;
    private String imageUrl;
    private String linkToBlog;
}
