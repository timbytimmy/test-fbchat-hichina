package com.hichina.main.back.hichinamainback.controller;

import com.google.gson.JsonObject;
import com.hichina.main.back.hichinamainback.config.Constants;
import com.hichina.main.back.hichinamainback.config.CustomAuthenticationProvider;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.PreregisterFacebookReqDTO;
import com.hichina.main.back.hichinamainback.model.DTO.UpdateLoginTypeRequestDTO;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.FacebookAccessTokenValidator;
import com.hichina.main.back.hichinamainback.utils.HttpUtils;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/public/login")
public class PublicLoginController {

    private static final String GRAPH_API_URL = "https://graph.facebook.com/v14.0/me?access_token=";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FacebookAccessTokenValidator facebookAccessTokenValidator;


    @Autowired
    private Environment env;

    @PostMapping("/prereg-facebook")
    public HichinaResponse preRegisterFacebook(@RequestBody PreregisterFacebookReqDTO request){
        HichinaResponse ret = new HichinaResponse();
        if(facebookAccessTokenValidator.validateAccessToken(request.getAccessToken())){
            // if already exist, just update some property, otherwise, register one and set corresponding login type to facebook
            generateOrUpdateUser(request.getFacebookId(), request.getName(), request.getEmail(), request.getProfileImageUrl());
            ret.setOk(true);
            ret.setMessage("Succeed pre register or update facebook user");
        }else{
            ret.setOk(false);
            ret.setMessage("Access token not valid for facebook login");
        }
        return ret;
    }

    @PutMapping("/type")
    public HichinaResponse updateLoginType(@RequestBody UpdateLoginTypeRequestDTO request){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByEmail(userMapper, request.getEmail());
        if(user==null){
            ret.setOk(false);
            ret.setMessage(String.format("User does not exist for email: %s", request.getEmail()));
        }else{
            user.setLoginType(request.getLoginType());
            userMapper.update(user);
            ret.setOk(true);
            ret.setMessage(String.format("Succeed updating login type for user : %s", request.getEmail()));
        }
        return ret;
    }

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
            user.setLoginType(Constants.FACEBOOK_LOGIN);
            userMapper.insert(user);
        }else{
            // update user with facebook info
            user.setFacebookId(fbId);
            user.setUsername(name);
            user.setProfileImageUrl(profileImageUrl);
            user.setLoginType(Constants.FACEBOOK_LOGIN);
            userMapper.update(user);
        }
        return user;
    }
}
