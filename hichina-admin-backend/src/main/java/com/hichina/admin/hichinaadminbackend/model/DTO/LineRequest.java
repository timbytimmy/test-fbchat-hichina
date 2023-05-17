package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineRequest {
    private String name;

    private String iconPath;

    private Integer rangeInDays;

    private Float basePrice;

    private String description;

}
