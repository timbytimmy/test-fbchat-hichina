package com.hichina.main.back.hichinamainback.utils;

import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {
    private static String extractEmailFromPrincipal(String input){
        Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(input);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static User getUserByCurrentPrincipal(UserMapper userMapper, String principalCombo){
        String email = extractEmailFromPrincipal(principalCombo);
        List<User> users = userMapper.findByUsernameOrEmail(email);
        if(users.isEmpty()){
            throw new RuntimeException("Did not get user from current principal");
        }else{
            return users.get(0);
        }
    }
}
