package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.User;
import com.shengxuan.speed.entity.pojo.Apply;

import java.util.List;

public interface UserService {

    /**
     * 查询所有的10分钟内申请包括手动和程控组装成apply
     * @return
     */
    List<User> findAll();

    void deleteById(Integer id);

    void add(User user);

    void update(User user);

    User findById(Integer id);

    User findByUsername(String username);

    /**
     * 根据用户id更改user_region列表
     * @param userId
     * @param regionDeviceTypeList
     */
    void updateUserRegion(Integer userId, List<RegionDeviceType> regionDeviceTypeList);

    /**
     * 判断此账号密码下用户是否存在
     * @param username
     * @param password
     * @return
     */
    User checkPassword(String username, String password);

    /**
     * 更具用户名更改用户密码
     * @param user
     */
    void updatePasswordByUsername(User user);
}
