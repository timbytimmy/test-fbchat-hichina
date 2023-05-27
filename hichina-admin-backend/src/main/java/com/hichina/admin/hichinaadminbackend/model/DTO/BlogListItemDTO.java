package com.hichina.admin.hichinaadminbackend.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BlogListItemDTO {
    private String blogId;
    private String title;
    private Date createdTime;
    private Boolean draft;
}
