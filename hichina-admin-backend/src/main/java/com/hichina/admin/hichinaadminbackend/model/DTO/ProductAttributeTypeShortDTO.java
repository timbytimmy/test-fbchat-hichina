package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductAttributeTypeShortDTO {
    private String attributeId;

    private String attributeName;

    private String dataType;
}
