package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("GuidebookIntroSlideImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuidebookIntroSlideImage {
    private String imageUrl;
    private String linkTo;
}
