package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HichinaProductListDTO {
    private String skuGroupId;
    private String skuGroupName;
    private String productTypeId;
    private String productTypeName;
    private Integer minPrice;
    private String imageUrl;
}
