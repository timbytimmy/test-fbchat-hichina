package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationCreateDTO {
    private String name;

    private String description;

    private Integer level;

    private String destinationProfileImage;

    private String parentId;
}
