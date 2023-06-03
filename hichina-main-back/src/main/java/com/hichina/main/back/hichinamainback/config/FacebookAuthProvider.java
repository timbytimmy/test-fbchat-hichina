package com.hichina.main.back.hichinamainback.config;

import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.FacebookAccessTokenValidator;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class FacebookAuthProvider implements AuthenticationProvider {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FacebookAccessTokenValidator facebookAccessTokenValidator;

    private User generateOrUpdateUser(String fbId, String name, String email, String profileImageUrl){
        User user = UserUtil.getUserByEmail(userMapper, email);
        if(user==null){
            //register new user
            user = new User();
            user.setCreatedTime(new Date());
            user.setSalt(CustomAuthenticationProvider.generateSalt());
            user.setUsername(name);
            user.setPassword("");
            user.setEmail(email);
            user.setFacebookId(fbId);
            user.setProfileImageUrl(profileImageUrl);
            user.setPwdCode(-1);
            user.setUserId(java.util.UUID.randomUUID().toString());
            userMapper.insert(user);
        }else{
            // update user with facebook info
            user.setFacebookId(fbId);
            user.setUsername(name);
            user.setProfileImageUrl(profileImageUrl);
            userMapper.update(user);
        }
        return user;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String idNameEmailCombo = authentication.getName();
        String accessToken = authentication.getCredentials().toString();

        boolean authed = facebookAccessTokenValidator.validateAccessToken(accessToken);
        if(!authed){
            return null;
        }
        String[] arrs = idNameEmailCombo.split(",");
        if(arrs.length!=4){
            return null;
        }
        String fbId = arrs[0];
        String name = arrs[1];
        String email = arrs[2];
        String profileImageUrl = arrs[3];
        User user = generateOrUpdateUser(fbId, name, email, profileImageUrl);
        return new UsernamePasswordAuthenticationToken(user.getUsername()+"["+user.getEmail()+"]", accessToken, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
