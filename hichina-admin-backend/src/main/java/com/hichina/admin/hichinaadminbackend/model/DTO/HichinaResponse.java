package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HichinaResponse {
    private Boolean ok;
    private String message;
    private Object data;
}
