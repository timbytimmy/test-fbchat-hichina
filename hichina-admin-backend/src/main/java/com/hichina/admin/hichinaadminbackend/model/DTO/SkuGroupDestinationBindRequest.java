package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuGroupDestinationBindRequest {
    private String skuGroupId;
    private String destinationId;
}
