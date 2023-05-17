package com.hichina.main.back.hichinamainback.controller;


import com.hichina.main.back.hichinamainback.config.CustomAuthenticationProvider;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaResponse;
import com.hichina.main.back.hichinamainback.model.DTO.RegisterDTO;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import com.hichina.main.back.hichinamainback.utils.RedisUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/public/register")
public class PublicRegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(PublicRegisterController.class);

    @Autowired
    private Environment env;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MailUtil mailUtil;

    @PostMapping
    public HichinaResponse register(@RequestBody RegisterDTO request){
        HichinaResponse ret = new HichinaResponse();

        List<User> users = userMapper.findByUsernameOrEmail(request.getEmail());

        if(!users.isEmpty()){
            ret.setOk(false);
            ret.setMessage(String.format("User %s already exist",request.getEmail()));
            return ret;
        }

        String redisKey = java.util.UUID.randomUUID().toString();
        String redisValue = request.getEmail()+","+request.getPassword();

        redisUtil.add(redisKey, 5*60L, redisValue);

        if(isEmail(request.getEmail())){
            sendValidationEmail(redisKey, request.getEmail());
        }else{
            // handle phone registration
            // LOG.info("Doing phone reg");
        }

        ret.setOk(true);
        ret.setMessage("Succeed insert into redis");

        return ret;
    }

    @PostMapping("/pushintodb/{regKey}")
    public HichinaResponse pushRedisCacheIntoDb(@PathVariable("regKey") String regKey){
        HichinaResponse ret = new HichinaResponse();
        Object toPushObj = redisUtil.get(regKey);
        if(toPushObj==null){
            ret.setOk(false);
            ret.setMessage("Validation expire, please re-register");
            return ret;
        }
        String emailAndPassword = toPushObj.toString();
        int firstCommaPosition = emailAndPassword.indexOf(",");
        if(firstCommaPosition<0){
            throw new RuntimeException("Invalid register info: "+ emailAndPassword);
        }
        String identity = emailAndPassword.substring(0, firstCommaPosition);
        String password = emailAndPassword.substring(firstCommaPosition+1);

        String salt = CustomAuthenticationProvider.generateSalt();
        User user = new User();
        user.setSalt(salt);
        user.setPwdCode(-1);
        if(isEmail(identity)){
            user.setEmail(identity);
            user.setUsername(identity.substring(0,identity.indexOf("@")));
        }else{
            user.setPhone(identity);
        }
        user.setCreatedTime(new Date());
        user.setUserId(java.util.UUID.randomUUID().toString());
        try {
            user.setPassword(CustomAuthenticationProvider.generateStrongPasswordHash(password, salt));
        } catch (NoSuchAlgorithmException e) {
            LOG.error("===registration set password error1: "+e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            LOG.error("===registration set password error2: "+e.getMessage());
            throw new RuntimeException(e);
        }
        userMapper.insert(user);

        // invalidate cache
        redisUtil.delete(regKey);

        ret.setOk(true);
        ret.setMessage("Succeed finish registering");

        return ret;
    }

    private Boolean isEmail(String identity){
        return true;
    }

    private void sendValidationEmail(String regKey, String email){
        String link = env.getProperty("frontend.url") + "/regvalidation/"+regKey;
        String html = "<table>\n" +
                "              <tr height=\"40\">\n" +
                "                <td style=\"padding-left:25px;padding-right:25px;font-size:18px;font-family:'微软雅黑','黑体',arial;\">\n" +
                "                  <A href=\"mailto:"+email+"\">Dear "+email+"</A>，how are you doing?\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"15\">\n" +
                "                <td></td>\n" +
                "              </tr>\n" +
                "              <tr height=\"30\">\n" +
                "                <td style=\"padding-left:55px;padding-right:55px;font-family:'微软雅黑','黑体',arial;font-size:14px;\">\n" +
                "                  Thank you for using HiChinaTravel service.\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"30\">\n" +
                "                <td style=\"padding-left:55px;padding-right:55px;font-family:'微软雅黑','黑体',arial;font-size:14px;\">\n" +
                "                  Please click link below(available for 5 minutes)，so that you can start using HiChinaTravel service：\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"60\">\n" +
                "                <td style=\"padding-left:55px;padding-right:55px;font-family:'微软雅黑','黑体',arial;font-size:14px;\">\n" +
                "                  <a href=\""+link+"\" target=\"_blank\" style=\"display: inline-block;color:#fff;line-height: 40px;background-color: #1989fa;border-radius: 5px;text-align: center;text-decoration: none;font-size: 14px;padding: 1px 30px;\">\n" +
                "                    Validate email right now\n" +
                "                  </a>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"10\">\n" +
                "                <td></td>\n" +
                "              </tr>\n" +
                "              <tr height=\"20\">\n" +
                "                <td style=\"padding-left:55px;padding-right:55px;font-family:'微软雅黑','黑体',arial;font-size:12px;\">\n" +
                "                  If you cannot open the link directly, please copy the link and open it in a browser:\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"30\">\n" +
                "                <td style=\"padding-left:55px;padding-right:65px;font-family:'微软雅黑','黑体',arial;line-height:18px;\">\n" +
                "                  <a style=\"color:#0c94de;font-size:12px;\" href=\""+link+"\">\n" +
                "                    "+link+"\n" +
                "                  </a>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"20\">\n" +
                "                <td style=\"padding-left:55px;padding-right:55px;font-family:'微软雅黑','黑体',arial;font-size:12px;\">\n" +
                "                  If you have not apply for HiChinaTravel service ，it might be a mistake by some other users, please ignore this email or<a href=\"mailto:customerservice@hichinatrip.com\">Contact Us</a>。\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr height=\"20\">\n" +
                "                <td></td>\n" +
                "              </tr>\n" +
                "            </table>";
        mailUtil.sendMailV2(email, "HiChinaTravel Register Validation", html);
    }
}
