package com.hichina.main.back.hichinamainback.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSkuVarcharAttributeMapping {
    private String skuId;

    private String dataType;

    private String attributeId;

    private String attributeValue;
}
