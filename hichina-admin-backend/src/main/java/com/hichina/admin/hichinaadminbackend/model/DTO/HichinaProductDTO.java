package com.hichina.admin.hichinaadminbackend.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class HichinaProductDTO {
    @JsonProperty("sku_id")
    private String skuId;
    @JsonProperty("product_type_name")
    private String productTypeName;
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_type_id")
    private String productTypeId;

    @JsonProperty("product_content")
    private String productContent;

    @JsonProperty("created_time")
    private String createdTime;
}
