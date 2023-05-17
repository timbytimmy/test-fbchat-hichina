package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BindAtrribute2ProductRequest {
    private String productTypeId;
    private String attributeId;
}
