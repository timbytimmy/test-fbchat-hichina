package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "adminUserMapper")
public interface AdminUserMapper {

    @Select("select * from admin_user where username=#{username}")
    List<AdminUser> getUserByName(String username);

    @Delete("<script>" +
            "Delete FROM admin_user WHERE username in \n" +
            "    <foreach item='item' collection='usernames' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> usernames);

    @Select("select * from admin_user order by created_time desc")
    List<AdminUser> getAllAdminUser();

    @Insert("INSERT INTO admin_user(username, password, created_time) VALUES(#{username},#{password}, #{createdTime})")
    void insert(AdminUser adminUser);
}
