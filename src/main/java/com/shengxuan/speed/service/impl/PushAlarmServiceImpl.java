package com.shengxuan.speed.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengxuan.speed.entity.Alarm;
import com.shengxuan.speed.entity.PushAlarm;
import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.UserRegion;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.service.AlarmService;
import com.shengxuan.speed.service.PushAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushAlarmServiceImpl implements PushAlarmService {

    private final PushAlarmMapper pushAlarmMapper;

    private final UserMapper userMapper;

    private final UserRegionMapper userRegionMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    private final DeviceMapper deviceMapper;

    public PushAlarmServiceImpl(PushAlarmMapper pushAlarmMapper, UserMapper userMapper, UserRegionMapper userRegionMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, DeviceMapper deviceMapper) {
        this.pushAlarmMapper = pushAlarmMapper;
        this.userMapper = userMapper;
        this.userRegionMapper = userRegionMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public List<PushAlarm> findAll() {
        return pushAlarmMapper.findAll();
    }


    @Override
    public PageResult findPage(PushAlarm pushAlarm, int pageNo, int pageSize) {
        if(pushAlarm.getOccurDate()!=null && pushAlarm.getOccurDate()!="" && pushAlarm.getOccurDate()!="null"){
            //传过来的时yyyy-MM-dd 需要转换成yyyyMMdd
            pushAlarm.setOccurDate(pushAlarm.getOccurDate().replace("-",""));
        }
        if(pushAlarm.getAlarmDesc()!=null && pushAlarm.getAlarmDesc()!="" && pushAlarm.getAlarmDesc()!="null"){
            //传过来的时yyyy-MM-dd 需要转换成yyyyMMdd
            pushAlarm.setAlarmDesc(pushAlarm.getAlarmDesc().replace("-",""));
        }
        //获取用户权限下的设备id集合
        //筛选出权限下的设备申请
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
        for (UserRegion userRegion : userRegionList) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegion.getRid());
            regionDeviceTypeList.add(byRid);
        }
        //List<String> deviceIds = deviceMapper.findByRegionDeviceType(regionDeviceTypeList);
        List<ServerDevice> serverDeviceIds = deviceMapper.findByServerRegionDeviceType(regionDeviceTypeList);
        PageHelper.startPage(pageNo,pageSize);
        List<PushAlarm> list = pushAlarmMapper.findByConditionAndPage1(pushAlarm,serverDeviceIds);
        //按照时间倒叙排列
        list.sort((a,b) ->{
            int dateComparison = b.getOccurDate().compareTo(a.getOccurDate());
            if(dateComparison!=0){
                return dateComparison;
            }else {
                return b.getOccurTime().compareTo(a.getOccurTime());
            }
        });
        PageInfo<PushAlarm> pageInfo =  new PageInfo<>(list);
        long total = pageInfo.getTotal();
        return new PageResult(total,pageSize,pageNo,list);
    }

    @Override
    public List<PushAlarm> findAllByDeviceId(String deviceId,int serverId) {
        return pushAlarmMapper.findByDeviceId(deviceId,serverId);
    }

    @Override
    public void updateCheck(Integer id) {
        pushAlarmMapper.updateCheck(id);
    }

    @Override
    public void updateCheckAll() {
        pushAlarmMapper.updateCheckAll();
    }

    @Override
    public List<PushAlarm> findNewAlarm10Size() {
        //此处更改成权限下的前10条最新报警信息
        //获取当前用户
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        //获取用户关联的权限集合
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
        for (UserRegion userRegion : userRegionList) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegion.getRid());
            regionDeviceTypeList.add(byRid);
        }
        //获取用户能看的所有设备id集合
        //List<String> deviceIds = deviceMapper.findByRegionDeviceType(regionDeviceTypeList);
        List<ServerDevice> serverDeviceList = deviceMapper.findByServerRegionDeviceType(regionDeviceTypeList);
        if(serverDeviceList!=null && serverDeviceList.size()>0){
            //查询能看到的设备前10条报警信息
            return pushAlarmMapper.findNewAlarm10SizeByRegion(serverDeviceList);
        }
        return null;
        //return pushAlarmMapper.findNewAlarm10Size();
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
