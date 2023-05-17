package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HichinaProductWithPropertyBagDTO {
    private HichinaProductBasicDTO hichinaProductBasicDTO;

    private List<ProductPropertyBag> productPropertyBag;
}
