package com.hichina.main.back.hichinamainback.model.DTO;

import com.hichina.main.back.hichinamainback.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreregisterFacebookReqDTO {
    private String facebookId;
    private String name;
    private String email;
    private String profileImageUrl;
    private String accessToken;
}
