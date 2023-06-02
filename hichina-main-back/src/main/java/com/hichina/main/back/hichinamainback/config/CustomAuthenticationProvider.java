package com.hichina.main.back.hichinamainback.config;

import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(name.contains(",")){
            return null;
        }

        try {
            List<User> users = userMapper.findByUsernameOrEmail(name);
            if(users.isEmpty()){
                return null;
            }
            User user = users.get(0);
            if(correctUserNameAndPassword(user, password)){
                return new UsernamePasswordAuthenticationToken(user.getUsername()+"["+user.getEmail()+"]", password, new ArrayList<>());
            }else{
                return null;
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }
    }

    private Boolean correctUserNameAndPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String storedPassord = user.getPassword();
        Integer pwdCode = user.getPwdCode();
        if(pwdCode!=null && pwdCode==-1){
            return newLoginLogicCheck(user, password);
        }else{
            // old logic, allow old logic only once here
            if(validatePasswordTheOldWay(password,storedPassord, pwdCode )){
                // update pwdcode -1,  generate random salt, use the salt and new logic to generate encrypted password
                String salt = generateSalt();
                user.setSalt(salt);
                user.setPwdCode(-1);
                user.setPassword(generateStrongPasswordHash(password, salt));
                userMapper.update(user);

                return true;
            }else{
                return false;
            }
        }
    }

    private Boolean newLoginLogicCheck(User user, String plainPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encrypted = generateStrongPasswordHash(plainPassword, user.getSalt());

        return user.getPassword().equals(encrypted);
    }

    public static String generateStrongPasswordHash(String password,String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] saltByte = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, saltByte, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(saltByte) + ":" + toHex(hash);
    }
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static String generateSalt(){
        return RandomStringUtils.randomAlphanumeric(16);
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


    private Boolean validatePasswordTheOldWay(String plainText, String passwordInDB, Integer pwdCode){
        String encrypted = md5(plainText+(pwdCode==null?"":pwdCode.toString()));

        return passwordInDB.equals(encrypted);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
