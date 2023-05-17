package com.hichina.admin.hichinaadminbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HichinaLine {
    private Long id;

    @JsonProperty("public_id")

    private String publicId;

    @NotNull
    private String name;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("icon_path")
    private String iconPath;

    @JsonProperty("range_in_days")
    private Integer rangeInDays;

    @JsonProperty("base_price")
    private float basePrice;

    private String description;
}
