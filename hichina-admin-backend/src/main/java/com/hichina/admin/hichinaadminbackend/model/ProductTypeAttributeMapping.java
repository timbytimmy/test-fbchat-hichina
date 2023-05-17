package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * this is a many to many relationship relation table
 */
@Data
@NoArgsConstructor
public class ProductTypeAttributeMapping {
    private String productTypeId;
    private String attributeId;

    /**
     * this is to maintain order of all attributes for a specific product type
     */
    private String sequence;
}
