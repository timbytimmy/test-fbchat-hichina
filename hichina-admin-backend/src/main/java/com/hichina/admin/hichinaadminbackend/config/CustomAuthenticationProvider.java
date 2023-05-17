package com.hichina.admin.hichinaadminbackend.config;

import com.hichina.admin.hichinaadminbackend.mapper.AdminUserMapper;
import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<AdminUser> users = adminUserMapper.getUserByName(name);
        if(users.isEmpty()){
            return null;
        }
        AdminUser user = users.get(0);
        if(password.equals(user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user.getUsername(), password, new ArrayList<>());
        }else{
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
