package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HichinaProductWithPropertyBagDTO {
    private HichinaProductDTO hichinaProductDTO;

    private List<ProductPropertyBag> productPropertyBag;
}
