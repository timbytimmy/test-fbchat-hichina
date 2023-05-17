package com.hichina.main.back.hichinamainback.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuidebookListDTO {
    private String guideId;

    private String downloadUrl;

    private String coverImageUrl;

    private String shortDescription;

    private String destinationId;

    private String destinationName;
}
