package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AdminUser {
    private String username;
    private String password;
    private Date createdTime;
}
