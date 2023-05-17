package com.hichina.main.back.hichinamainback.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductSkuTimestampAttributeMapping {
    private String skuId;

    private String dataType;

    private String attributeId;

    private Date attributeValue;
}
