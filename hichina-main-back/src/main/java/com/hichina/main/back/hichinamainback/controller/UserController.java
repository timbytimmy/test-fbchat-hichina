package com.hichina.main.back.hichinamainback.controller;

import com.hichina.main.back.hichinamainback.config.CustomAuthenticationProvider;
import com.hichina.main.back.hichinamainback.config.EnableHichinaAutoLog;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.DTO.*;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @PutMapping("/basicInfo")
    @EnableHichinaAutoLog(description = "update my profile")
    public HichinaResponse updateBasicInfo(@RequestBody UserUpdateRequestDTO request){
        HichinaResponse ret = new HichinaResponse();

        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNationality(request.getNationality());
        user.setPhone(request.getPhone());

        userMapper.update(user);

        ret.setOk(true);
        ret.setMessage("Succeed updating user basic info");

        return ret;
    }
    @PutMapping("/updatePass")
    @EnableHichinaAutoLog(description = "update user pass")
    public HichinaResponse updatePass(@RequestBody UpdatePasswordDTO request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        String encrypted = CustomAuthenticationProvider.generateStrongPasswordHash(request.getOldPass(), user.getSalt());

        if(!encrypted.equals(user.getPassword())){
            ret.setOk(false);
            ret.setMessage("Old password not correct");
            return ret;
        }
        String newEncrypted = CustomAuthenticationProvider.generateStrongPasswordHash(request.getNewPass(), user.getSalt());
        user.setPassword(newEncrypted);

        userMapper.update(user);

        ret.setOk(true);
        ret.setMessage("Succeed updated password");
        return ret;
    }

    @GetMapping(value="/whoami")
    @EnableHichinaAutoLog(description = "find who am i")
    public String whoami(){
        return currentUser();
    }

    private SimplifiedUserDTO simplifyUser(User user){
        SimplifiedUserDTO ret = new SimplifiedUserDTO();
        ret.setUsername(currentUser());
        ret.setProfileImageUrl(user.getProfileImageUrl());
        return ret;
    }

    @GetMapping(value="/whoamiv2")
    @EnableHichinaAutoLog(description = "find who am i v2 with profile image")
    public HichinaResponse whoamiV2(){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());
        ret.setOk(true);
        SimplifiedUserDTO simplifiedUserDTO = simplifyUser(user);
        ret.setData(simplifiedUserDTO);
        ret.setMessage("Successfully got my user simplified info");
        return ret;
    }

    public static String currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }else{
            return null;
        }
    }

    @PostMapping("/update-profile-image")
    public HichinaResponse updateMyProfileImageUrl(@RequestBody GeneralSingleStringDTO request){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        user.setProfileImageUrl(request.getParam());

        userMapper.update(user);

        ret.setOk(true);
        ret.setMessage("Successfully update user profile image");

        return ret;
    }

    @GetMapping("/mysecurityinfo")
    public HichinaResponse getMyUserDetail(){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        ret.setOk(true);
        ret.setData(hideCredential(user));
        ret.setMessage("Successfully got message");

        return ret;
    }

    private User hideCredential(User user){
        user.setPassword("*");
        user.setSalt("*");
        return user;
    }
}
