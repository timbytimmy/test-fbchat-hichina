package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductAttributeCreateDTO {
    private String name;

    private String dataType;
}
