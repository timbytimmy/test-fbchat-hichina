package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HomePostImageRequest {
    private String postLink;
    private String postImageUrl;
}
