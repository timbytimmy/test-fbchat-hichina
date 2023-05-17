package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreationDTO {
    private String meta;
    private String skuId;
    private String status;
    private Integer price;
    private String payMethod;
}
