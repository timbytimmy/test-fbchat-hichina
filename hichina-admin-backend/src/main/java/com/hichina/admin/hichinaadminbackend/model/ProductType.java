package com.hichina.admin.hichinaadminbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductType {
    @NotNull
    @JsonProperty("product_type_id")
    private String productTypeId;

    @NotNull
    @JsonProperty("product_type_name")
    private String productTypeName;

    @JsonProperty("product_type_description")
    private String productTypeDescription;
}
