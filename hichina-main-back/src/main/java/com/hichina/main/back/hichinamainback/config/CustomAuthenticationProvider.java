package com.hichina.main.back.hichinamainback.config;

import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.FacebookAccessTokenValidator;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FacebookAccessTokenValidator facebookAccessTokenValidator;


    /**
     * the assumption for this method is, register already happens for this user, otherwise login fails
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        // this name here is actually the login identity, which is email by default
        String name = authentication.getName();
        // this password is password for normal login, and accesstoken string for facebook login
        String password = authentication.getCredentials().toString();

        try {
            User user = UserUtil.getUserByEmail(userMapper, name);

            // user not exist for this email, login fail
            if(user==null){
                return null;
            }

            if(correctUserNameAndPassword(user, password, user.getLoginType())){
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

    private Boolean correctUserNameAndPassword(User user, String password, String loginType) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(StringUtils.isEmpty(loginType) || Constants.REGULAR_LOGIN.equals(loginType)){
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
        }else if(Constants.FACEBOOK_LOGIN.equals(loginType)){
            return facebookAccessTokenValidator.validateAccessToken(password);
        }else{
            return false;
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
