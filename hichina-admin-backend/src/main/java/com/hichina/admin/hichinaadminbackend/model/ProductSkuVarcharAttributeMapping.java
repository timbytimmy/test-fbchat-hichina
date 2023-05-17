package com.hichina.admin.hichinaadminbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSkuVarcharAttributeMapping {
    @JsonProperty("sku_id")
    private String skuId;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("attribute_id")
    private String attributeId;

    @JsonProperty("attribute_value")
    private String attributeValue;
}
