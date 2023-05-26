package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HomePostImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePostImage {
    private String postLink;
    private String postImageUrl;
}
