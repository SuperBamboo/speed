package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.*;
import com.shengxuan.speed.entity.pojo.Apply;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.service.ApplyService;
import com.shengxuan.speed.util.BytesUtil;
import com.shengxuan.speed.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ApplyServiceImpl implements ApplyService {

    private final DeviceMapper deviceMapper;

    private final DeviceCtrlModeMapper deviceCtrlModeMapper;

    private final PlanModeMapper planModeMapper;
    private final DeviceControlModeApplyMapper deviceControlModeApplyMapper;

    private final DevicePlanModeApplyMapper devicePlanModeApplyMapper;
    private final UserRegionMapper userRegionMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;
    private final UserMapper userMapper;

    public ApplyServiceImpl(DeviceControlModeApplyMapper deviceControlModeApplyMapper, DevicePlanModeApplyMapper devicePlanModeApplyMapper, DeviceMapper deviceMapper, DeviceCtrlModeMapper deviceCtrlModeMapper, PlanModeMapper planModeMapper, UserRegionMapper userRegionMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, UserMapper userMapper) {
        this.deviceControlModeApplyMapper = deviceControlModeApplyMapper;
        this.devicePlanModeApplyMapper = devicePlanModeApplyMapper;
        this.deviceMapper = deviceMapper;
        this.deviceCtrlModeMapper = deviceCtrlModeMapper;
        this.planModeMapper = planModeMapper;
        this.userRegionMapper = userRegionMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.userMapper = userMapper;
    }

    /**
     * 查询10分钟内两种申请并组装
     * @return
     */
    @Override
    public List<Apply> findAll() {
        //1.删除10分钟之前的两种数据
        String timeBefore10Minutes = DateFormat.getTimeBefore10Minutes();
        deviceControlModeApplyMapper.deleteBefore10Minutes(timeBefore10Minutes);
        devicePlanModeApplyMapper.deleteBefore10Minutes(timeBefore10Minutes);
        //2.查询ctrlApply
        List<DevicePlanModeApply> planApplyList = devicePlanModeApplyMapper.findAll();
        //3.查询planApply
        List<DeviceControlModeApply> ctrlApplyList = deviceControlModeApplyMapper.findAll();
        //4.数据组装
        List<Apply> applyList = new ArrayList<>();
        if(ctrlApplyList!=null && ctrlApplyList.size()>0){
            for (DeviceControlModeApply deviceControlModeApply : ctrlApplyList) {
                Apply apply = new Apply();
                apply.setServerId(deviceControlModeApply.getServerId());
                apply.setFlag(deviceControlModeApply.getFlag());
                apply.setDeviceId(deviceControlModeApply.getDeviceId());
                //Device device = deviceMapper.findByDeviceId(deviceControlModeApply.getDeviceId());
                Device device = deviceMapper.findByDeviceIdAndServerId(deviceControlModeApply.getDeviceId(),deviceControlModeApply.getServerId());
                apply.setDeviceName(device.getDeviceName());
                String deviceCtrlModeName = deviceCtrlModeMapper.findNameByDeviceIdAndCtrlNo(deviceControlModeApply.getDeviceId(), deviceControlModeApply.getCtrlNo(),deviceControlModeApply.getServerId());
                apply.setName(deviceCtrlModeName);
                if("1".equals(deviceControlModeApply.getValue())){
                    //下发手动管控申请
                    apply.setApplyType(1);
                    apply.setApplyTypeStr("手动管控");
                }else {
                    apply.setApplyType(3);
                    apply.setApplyTypeStr("取消手动管控");
                }
                apply.setApplyTime(deviceControlModeApply.getApplyTime());

                //转换成前端好解析的格式
                String relayStauDec = deviceControlModeApply.getRelay1();
                String relayStauBinary = BytesUtil.hexToBinary(BytesUtil.decToHex(relayStauDec));
                deviceControlModeApply.setRelay1(relayStauBinary);

                String relayStau1Dec = deviceControlModeApply.getRelay2();
                String relayStau1Binary = BytesUtil.hexToBinary(BytesUtil.decToHex(relayStau1Dec));
                deviceControlModeApply.setRelay2(relayStau1Binary);

                apply.setDeviceControlModeApply(deviceControlModeApply);
                applyList.add(apply);

            }
        }

        if(planApplyList!=null && planApplyList.size()>0){
            for (DevicePlanModeApply devicePlanModeApply : planApplyList) {
                Apply apply = new Apply();
                apply.setServerId(devicePlanModeApply.getServerId());
                apply.setFlag(devicePlanModeApply.getFlag());
                apply.setDeviceId(devicePlanModeApply.getDeviceId());
                //Device dev = deviceMapper.findByDeviceId(devicePlanModeApply.getDeviceId());
                Device device = deviceMapper.findByDeviceIdAndServerId(devicePlanModeApply.getDeviceId(),devicePlanModeApply.getServerId());
                apply.setDeviceName(device.getDeviceName());
                String planModeName = planModeMapper.findNameByDeviceIdAndPlanNo(devicePlanModeApply.getDeviceId(), devicePlanModeApply.getPlanNo(),devicePlanModeApply.getServerId());
                apply.setName(planModeName);
                if("1".equals(devicePlanModeApply.getValue())){
                    //下发手动程控申请
                    apply.setApplyType(2);
                    apply.setApplyTypeStr("手动程控");
                }else {
                    apply.setApplyType(4);
                    apply.setApplyTypeStr("取消手动程控");
                }
                apply.setApplyTime(devicePlanModeApply.getApplyTime());
                apply.setDevicePlanModeApply(devicePlanModeApply);
                applyList.add(apply);
            }
        }
        //5.根据时间倒序排列
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Collections.sort(applyList, new Comparator<Apply>() {
            @Override
            public int compare(Apply o1, Apply o2) {
                LocalDateTime time1 = LocalDateTime.parse(o1.getApplyTime(), formatter);
                LocalDateTime time2 = LocalDateTime.parse(o2.getApplyTime(), formatter);
                return time2.compareTo(time1);
            }
        });
        //权限筛选
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
        List<Apply> applyList10Size = new ArrayList<>();
        if(applyList.size()>0){
            for (Apply apply : applyList) {
                String applyDeviceId = apply.getDeviceId();
                int applyServerId = apply.getServerId();
                for (ServerDevice serverDeviceId : serverDeviceIds) {
                    if(serverDeviceId.getDeviceId().equals(applyDeviceId) && serverDeviceId.getServerId() == applyServerId){
                        applyList10Size.add(apply);
                    }
                }
            }
        }
        return applyList10Size;
    }

    @Override
    public void updateFlag(Apply apply) {
        int applyType = apply.getApplyType();
        if(applyType == 1 || applyType == 3){
            //手动管控
            deviceControlModeApplyMapper.updateFlag(apply.getDeviceId(),apply.getServerId(),apply.getApplyTime());
        }else {
            devicePlanModeApplyMapper.updateFlag(apply.getDeviceId(),apply.getServerId(),apply.getApplyTime());
        }
    }

    @Override
    public List<Apply> findNewApply10Size() {
        List<Apply> applyList10Size = new ArrayList<>();
        //1.只要前10个未处理的数据
        List<Apply> applyList = findAll();
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
        int count = 0;
        if(applyList!=null && applyList.size()>0){
            for (Apply apply : applyList) {
                String applyDeviceId = apply.getDeviceId();
                int applyServerId = apply.getServerId();
                for (ServerDevice serverDeviceId : serverDeviceIds) {
                    if(serverDeviceId.getDeviceId().equals(applyDeviceId) && serverDeviceId.getServerId() == applyServerId){
                        if(count<10){
                            applyList10Size.add(apply);
                            count++;
                        }
                    }
                }
            }
        }
        return applyList10Size;
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
