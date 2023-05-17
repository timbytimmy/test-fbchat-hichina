package com.hichina.admin.hichinaadminbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductAttribute {
    @NotNull
    private String attributeId;

    @NotNull
    private String attributeName;

    @NotNull
    private String dataType;
}
