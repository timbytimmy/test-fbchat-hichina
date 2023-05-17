package com.hichina.admin.hichinaadminbackend.model.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkuGroupStatsDTO {
    private String skuGroupId;

    private String skuGroupName;

    private String productTypeId;

    private String productTypeName;

    private Integer productCount;

    private Boolean enabled;
}
