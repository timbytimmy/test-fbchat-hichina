package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayDTO {
    private Integer price;
    private String orderId;
    private String codeUrl;
}
