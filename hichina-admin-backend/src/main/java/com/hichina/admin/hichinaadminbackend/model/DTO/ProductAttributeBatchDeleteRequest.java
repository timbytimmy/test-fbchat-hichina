package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductAttributeBatchDeleteRequest {
    private List<String> toDelete;
}
