package com.hichina.admin.hichinaadminbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuGroup {
    private String skuGroupId;

    private String skuGroupName;

    private String productTypeId;

    private Date createdDate;

    private Boolean enabled;

    private String imageUrl;

    private Integer minPrice;
}
