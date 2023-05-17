package com.hichina.main.back.hichinamainback.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSkuIntAttributeMapping {
    private String skuId;

    private String dataType;

    private String attributeId;

    private Integer attributeValue;
}
