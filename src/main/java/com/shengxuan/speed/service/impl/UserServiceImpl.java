package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.User;
import com.shengxuan.speed.entity.UserRegion;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRegionMapper userRegionMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;


    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRegionMapper userRegionMapper, RegionDeviceTypeMapper regionDeviceTypeMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRegionMapper = userRegionMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
    }

    /**
     * 查询10分钟内两种申请并组装
     * @return
     */
    @Override
    public List<User> findAll() {
        List<User> userList = userMapper.findAll();
        return userList;
    }

    @Override
    public void deleteById(Integer id) {
        //先删除关联数据 user_region
        userRegionMapper.delByUserId(id);
        //2.再删除用户
        userMapper.delById(id);
    }

    @Override
    public void add(User user) {
        //加密后存储
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.add(user);
        //如果是超级权限用户，默认拥有所有权限
        if("SUPPER".equals(user.getRole())){
            Integer userId = userMapper.findByUsername(user.getUsername()).getId();
            List<RegionDeviceType> regionDeviceTypeList = regionDeviceTypeMapper.findAll();
            for (RegionDeviceType regionDeviceType : regionDeviceTypeList) {
                userRegionMapper.add(userId,regionDeviceType.getRid());
            }
        }
    }

    @Override
    public void update(User user) {
        //加密后修改
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.update(user);
    }

    @Override
    public User findById(Integer id) {
        User user = userMapper.findById(id);
        user.setPassword("");
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if(user!=null){
            //单独查询的时候找到某个用户拥有的权限
            List<UserRegion> list = userRegionMapper.findByUserId(user.getId());
            user.setUserRegionList(list);
        }else {
            user = new User();
            user.setUsername("该用户不存在");
        }
        return user;
    }

    @Override
    public void updateUserRegion(Integer userId, List<RegionDeviceType> regionDeviceTypeList) {
        //1.先删除关联用户的user_region
        userRegionMapper.delByUserId(userId);
        //2.插入regionDeviceTypeList中选中的rid
        for (int i = 0; i < regionDeviceTypeList.size(); i++) {
            if(regionDeviceTypeList.get(i).isCheck()){
                userRegionMapper.add(userId,regionDeviceTypeList.get(i).getRid());
            }
        }

    }

    @Override
    public User checkPassword(String username, String password) {
        User user = userMapper.findByUsername(username);
        if(user!=null){
            // 创建 PasswordEncoder 实例（通常使用 BCryptPasswordEncoder）

            // 判断用户输入的密码是否与数据库中的密码匹配
            boolean isMatch = passwordEncoder.matches(password,user.getPassword());

            if (isMatch) {
                System.out.println("密码匹配！");
                return user;
            } else {
                System.out.println("密码不匹配！");
                return null;
            }
        }
        return null;
    }

    @Override
    public void updatePasswordByUsername(User user) {
        if(user!=null){
            userMapper.updatePasswordByUsername(user.getUsername(),passwordEncoder.encode(user.getPassword()));
        }
    }
}
