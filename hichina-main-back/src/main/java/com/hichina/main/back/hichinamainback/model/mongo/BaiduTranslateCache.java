package com.hichina.main.back.hichinamainback.model.mongo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("BaiduTranslateCache")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiduTranslateCache {
    private String translationKey;
    private String text;
}
