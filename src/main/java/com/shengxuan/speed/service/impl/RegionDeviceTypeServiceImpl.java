package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.entity.UserRegion;
import com.shengxuan.speed.mapper.RegionDeviceTypeMapper;
import com.shengxuan.speed.mapper.ServerMapper;
import com.shengxuan.speed.mapper.UserMapper;
import com.shengxuan.speed.mapper.UserRegionMapper;
import com.shengxuan.speed.service.RegionDeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RegionDeviceTypeServiceImpl implements RegionDeviceTypeService {

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    private final UserRegionMapper userRegionMapper;

    private final UserMapper userMapper;

    private final ServerMapper serverMapper;

    public RegionDeviceTypeServiceImpl(RegionDeviceTypeMapper regionDeviceTypeMapper, UserRegionMapper userRegionMapper, UserMapper userMapper, ServerMapper serverMapper) {
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.userRegionMapper = userRegionMapper;
        this.userMapper = userMapper;
        this.serverMapper = serverMapper;
    }

    @Override
    public List<RegionDeviceType> findByUserId(Integer uid) {
        List<RegionDeviceType> all = regionDeviceTypeMapper.findAll();
        for (RegionDeviceType regionDeviceType : all) {
            if(regionDeviceType.getServerId() == 0){
                Server server = new Server();
                server.setId(0);
                server.setServerName("主服务器");
                regionDeviceType.setServer(server);
            }else {
                Server ser = serverMapper.findById(regionDeviceType.getServerId());
                regionDeviceType.setServer(ser);
            }
        }
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(uid);
        for (int i = 0; i < all.size(); i++) {
            for (int i1 = 0; i1 < userRegionList.size(); i1++) {
                if((int)userRegionList.get(i1).getRid() == (int)all.get(i).getRid()){
                    all.get(i).setCheck(true);
                }
            }
        }
        return all;
    }

    /**
     * 当前登录用户的所有regionDeviceType
     * @return
     */
    @Override
    public List<RegionDeviceType> findByCurrentUser() {
        List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
        //查询权限内的区域
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        for (UserRegion userRegion : userRegionList) {
            Integer rid = userRegion.getRid();
            RegionDeviceType regionDeviceType = regionDeviceTypeMapper.findByRid(rid);
            int serverId = regionDeviceType.getServerId();
            Server server;
            if(serverId == 0){
                server = new Server();
                server.setId(0);
                server.setServerName("0--主服务器");
            }else {
                server = serverMapper.findById(serverId);
                server.setServerName(serverId+"--"+server.getServerName());
            }
            regionDeviceType.setServer(server);
            regionDeviceTypeList.add(regionDeviceType);
        }
        return regionDeviceTypeList;
    }

    /**
     * 获取当前用户登录的用户
     * @return
     */
    public com.shengxuan.speed.entity.User findCurrentUser(){
        com.shengxuan.speed.entity.User user = null;
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails!=null){
            String username = userDetails.getUsername();
            if(username.equalsIgnoreCase("supper")){
                user = new com.shengxuan.speed.entity.User();
                user.setUsername("supper");
                user.setId(0);
            }else {
                user = userMapper.findByUsername(username);
            }

        }
        return user;
    }
}
