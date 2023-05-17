package com.hichina.admin.hichinaadminbackend.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductSkuUpdateDTO {
    private String productName;

    private String productContent;

    private List<ProductPropertyBag> propertyBags;
}
