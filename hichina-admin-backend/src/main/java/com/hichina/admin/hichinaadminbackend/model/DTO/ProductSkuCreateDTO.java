package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ProductSkuCreateDTO {
    private String productName;

    private String productDescription;

    private String productTypeId;

    private Map<String, String> customPropertyBag;
}
