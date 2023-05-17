package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationWithGuidebook {
    private String destinationId;
    private String destinationName;

    private String description;

    private String downloadUrl;

    private String destinationProfileImage;

}
