package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateGuidebookDTO {
    private String destinationId;

    private String coverImageUrl;

    private String shortDescription;

    private String downloadUrl;
}
