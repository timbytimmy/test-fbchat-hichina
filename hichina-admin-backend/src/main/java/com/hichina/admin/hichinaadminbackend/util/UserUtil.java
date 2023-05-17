package com.hichina.admin.hichinaadminbackend.util;

import com.hichina.admin.hichinaadminbackend.mapper.AdminUserMapper;
import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUtil {
    @Autowired
    private AdminUserMapper adminUserMapper;

    public String currentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }else{
            return null;
        }
    }

    public AdminUser currentUser(String username){
        List<AdminUser> adminUsers = adminUserMapper.getUserByName(username);
        if(adminUsers.isEmpty()){
            return null;
        }else{
            return adminUsers.get(0);
        }
    }
}
