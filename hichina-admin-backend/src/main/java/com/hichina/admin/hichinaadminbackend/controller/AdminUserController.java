package com.hichina.admin.hichinaadminbackend.controller;

import com.hichina.admin.hichinaadminbackend.mapper.AdminUserMapper;
import com.hichina.admin.hichinaadminbackend.model.AdminUser;
import com.hichina.admin.hichinaadminbackend.model.DTO.AdminUserCreateDTO;
import com.hichina.admin.hichinaadminbackend.model.DTO.GeneralBatchDeleteRequest;
import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/adminuser")
public class AdminUserController {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @DeleteMapping("/batch")
    public HichinaResponse batchDeleteUser(@RequestBody  GeneralBatchDeleteRequest deleteRequest){
        if(deleteRequest.getToDelete().contains("hichinaadmin")){
            throw new RuntimeException("Cannot delete superadmin");
        }
        adminUserMapper.batchDelete(deleteRequest.getToDelete());
        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setMessage("Succeed deleting users: "+ deleteRequest.getToDelete());
        return ret;
    }

    @PostMapping
    public HichinaResponse createAdminUser(@RequestBody AdminUserCreateDTO request){
        HichinaResponse ret = new HichinaResponse();
        AdminUser user2Create = new AdminUser();
        String username = request.getUsername();
        if("1".equals(request.getRole())){
            // EMPLOYEE
            username = "EMP_"+username;
        }else if("2".equals(request.getRole())){
            // SUPPLIER
            username = "SP_"+username;
        }
        user2Create.setUsername(username);
        user2Create.setPassword(request.getPassword());
        user2Create.setCreatedTime(new Date());

        adminUserMapper.insert(user2Create);

        ret.setMessage("Succeed createing user: "+ username);
        ret.setOk(true);

        return ret;
    }

    @GetMapping("/listalladminuser")
    public HichinaResponse getAllAdminUsers(){
        HichinaResponse ret = new HichinaResponse();
        List<AdminUser> adminUsers = adminUserMapper.getAllAdminUser();

        ret.setOk(true);
        ret.setData(adminUsers);
        ret.setMessage("Succeed getting all admin users");

        return ret;
    }
}
