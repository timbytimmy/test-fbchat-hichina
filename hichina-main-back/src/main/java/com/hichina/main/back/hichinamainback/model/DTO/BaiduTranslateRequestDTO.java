package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaiduTranslateRequestDTO {
    private String translationKey;
    private String from;
    private String to;
    private String query;
}
