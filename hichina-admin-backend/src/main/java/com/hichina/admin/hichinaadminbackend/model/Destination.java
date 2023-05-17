package com.hichina.admin.hichinaadminbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Destination {
    private String destinationId;

    private String destinationName;

    // 5, 6, 7 分别代表省,市,景点三级
    private Integer level;

    private String parentId;

    private String description;

    private String destinationProfileImage;

    @JsonProperty("created_date")
    private Date createdDate;
}
