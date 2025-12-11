package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.entity.pojo.ResultQuery;

import java.util.List;

public interface ServerService {

    /**
     * 查询所有的10分钟内申请包括手动和程控组装成apply
     * @return
     */
    List<Server> findAll();

    void deleteById(Integer id);

    ResultQuery add(Server server);


    Server findByServerName(String serverName);

    /**
     * 判断此账号密码下用户是否存在
     * @param username
     * @param password
     * @return
     */
    //User checkPassword(String username, String password);

    /**
     * 更具用户名更改用户密码
     * @param user
     */
    //void updatePasswordByUsername(User user);
}
