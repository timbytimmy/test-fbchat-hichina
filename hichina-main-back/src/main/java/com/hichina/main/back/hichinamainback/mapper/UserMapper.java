package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "userMapper")
public interface UserMapper {

    @Select("select * from user where email=#{account}")
    List<User> findByEmail(String account);

    @Update("update user SET salt=#{salt},password=#{password},created_time=#{createdTime},email=#{email},facebook_id=#{facebookId},google_id=#{googleId},wx_id=#{wxId},phone=#{phone},username=#{username},gender=#{gender},birth_date=#{birthDate},nationality=#{nationality},license_type=#{licenseType},license_number=#{licenseNumber},license_sign_date=#{licenseSignDate},license_expire_date=#{licenseExpireDate},signature=#{signature},profile_image_url=#{profileImageUrl},passport_image_url=#{passportImageUrl},pwd_code=#{pwdCode},login_type=#{loginType} where user_id=#{userId}")
    void update(User user);

    @Insert("insert into user(user_id, salt, password, created_time, email, facebook_id, google_id, wx_id, phone, username, gender, birth_date, nationality, license_type, license_number, license_sign_date, license_expire_date, signature, profile_image_url, passport_image_url, pwd_code, login_type) VALUES(#{userId}, #{salt}, #{password}, #{createdTime}, #{email}, #{facebookId}, #{googleId}, #{wxId}, #{phone}, #{username}, #{gender}, #{birthDate}, #{nationality}, #{licenseType}, #{licenseNumber}, #{licenseSignDate}, #{licenseExpireDate}, #{signature}, #{profileImageUrl}, #{passportImageUrl}, #{pwdCode}, #{loginType})")
    void insert(User user);
}
