package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.User;
import com.shengxuan.speed.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/findAll")
    public List<User> findAll(){
        return userService.findAll();
    }

    @RequestMapping("/findByUsername")
    public User findByUserName(String username){
        return userService.findByUsername(username);
    }

    @RequestMapping("/update")
    public void update(@RequestBody User user){
        userService.update(user);
    }
    @RequestMapping("/updateUserRegion")
    public void updateUserRegion(Integer userId,@RequestBody List<RegionDeviceType> regionDeviceTypeList){
        userService.updateUserRegion(userId,regionDeviceTypeList);
    }
    @RequestMapping("/add")
    public void add(@RequestBody User user){
        userService.add(user);
    }

    @RequestMapping("/deleteById")
    public void deleteById(Integer id){
        //先删除关联权限user_region
        userService.deleteById(id);

    }

    @RequestMapping("/findById")
    public User findById(Integer id){
        return userService.findById(id);
    }
}
