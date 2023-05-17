package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationUpdateDTO {
    private String name;

    private String description;

    private String destinationProfileImage;

    private Integer level;

    private String parentId;
}
