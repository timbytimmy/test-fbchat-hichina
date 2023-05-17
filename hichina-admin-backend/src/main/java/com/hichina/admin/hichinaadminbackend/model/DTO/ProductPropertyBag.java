package com.hichina.admin.hichinaadminbackend.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPropertyBag {
    private Long sequence;

    private String attributeId;

    private String attributeName;

    private String attributeValue;

    @NotNull
    private String dataType;
}
