package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateLoginTypeRequestDTO {
    private String email;
    private String loginType;
}
