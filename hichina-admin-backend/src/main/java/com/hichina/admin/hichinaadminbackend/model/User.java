package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String salt;
    private String password;
    private Date createdTime;
    private String email;
    private String facebookId;
    private String googleId;
    private String wxId;
    private String phone;
    private String username;
    private Integer gender;
    private Date birthDate;
    private String nationality;
    private String licenseType;
    private String licenseNumber;
    private Date licenseSignDate;
    private Date licenseExpireDate;
    private String signature;
    private String profileImageUrl;
    private String passportImageUrl;
    private Integer pwdCode;
    private String loginType;
}
