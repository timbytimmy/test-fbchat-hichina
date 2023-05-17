package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HomeSlideImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeSlideImage {
    private String title;
    private String subTitle;
    private String imageUrl;
    private String linkToBlog;
}
