package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationProfileDTO {
    private String destinationId;

    private String destinationName;

    private String destinationProfileImage;

    private String parentDestinationName;
}
