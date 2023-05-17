package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HichinaProduct {
    private String skuId;

    private String skuGroupId;

    private String productTypeId;

    private String productName;

    private String productContent;

    private Date createdTime;

    private String createdBy;

}
