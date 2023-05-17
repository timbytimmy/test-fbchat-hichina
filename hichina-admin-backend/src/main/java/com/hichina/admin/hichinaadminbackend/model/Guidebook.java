package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Guidebook {
    private String guideId;

    private String downloadUrl;

    private String coverImageUrl;

    private String shortDescription;

    private String destinationId;

    private Date createdDate;
}
