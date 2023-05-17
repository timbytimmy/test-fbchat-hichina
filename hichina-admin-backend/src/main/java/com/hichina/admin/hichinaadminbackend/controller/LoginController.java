package com.hichina.admin.hichinaadminbackend.controller;

import com.hichina.admin.hichinaadminbackend.mapper.UserMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.LoginTestDTO;
import com.hichina.admin.hichinaadminbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/testlogin")


public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public Boolean tryLogin(@RequestBody LoginTestDTO request){
        List<User> users =  userMapper.findByEmail(request.getEmail());
        if(users.isEmpty()){
            return false;
        }
        User user = users.get(0);

        Integer pwdCode = user.getPwdCode();
        String passwordInDB = user.getPassword();
        String plainText = request.getPassword();

        return validatePassword(plainText, passwordInDB, pwdCode);
    }

    private String md5(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            //calculating message digest of an input that return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            //converting byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            //converting message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    private Boolean validatePassword(String plainText, String passwordInDB, Integer pwdCode){
        String encrypted = md5(plainText+(pwdCode==null?"":pwdCode.toString()));

        return passwordInDB.equals(encrypted);
    }
}
