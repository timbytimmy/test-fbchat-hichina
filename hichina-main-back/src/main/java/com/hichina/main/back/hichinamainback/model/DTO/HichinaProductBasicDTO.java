package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HichinaProductBasicDTO {
    private String skuId;
    private String productTypeName;
    private String productName;
    private String productTypeId;
    private String productContent;
    private String createdTime;
}
