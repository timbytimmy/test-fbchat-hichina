package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HichinaResponse {
    private Boolean ok;
    private String message;
    private Object data;
}
