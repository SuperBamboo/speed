package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.User;
import com.shengxuan.speed.entity.pojo.ResultQuery;
import com.shengxuan.speed.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/getCurrentUser")
    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    @RequestMapping("/checkPassword")
    public ResultQuery checkPassword(String username, String password){
        ResultQuery resultQuery = new ResultQuery();
        if(password ==null || "".equals(password)){
            resultQuery.setType(-1);
            resultQuery.setMessage("用户原密码不能为空");
        }else {
            User user = userService.checkPassword(username,password);
            if(user == null){
                resultQuery.setType(-1);
                resultQuery.setMessage("原密码错误");
            }else {
                resultQuery.setType(0);
                resultQuery.setMessage("密码正确");
            }
        }
        return resultQuery;
    }

    @RequestMapping("/updateUserPassword")
    public void updateUserPassword(@RequestBody User user){
        userService.updatePasswordByUsername(user);
    }
}
