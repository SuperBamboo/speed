package com.shengxuan.speed.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengxuan.speed.entity.*;
import com.shengxuan.speed.entity.pojo.*;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.service.DeviceService;
import com.shengxuan.speed.service.RegionDeviceTypeService;
import com.shengxuan.speed.util.CoordinateTransform;
import com.shengxuan.speed.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.*;

import static com.shengxuan.speed.socket.Client.regionList;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    private final PushAlarmMapper pushAlarmMapper;

    private final DeviceControlModeApplyMapper ctrlApplyMapper;

    private final DevicePlanModeApplyMapper planApplyMapper;

    private final UserMapper userMapper;
    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    private final UserRegionMapper userRegionMapper;

    private final ServerMapper serverMapper;


    public DeviceServiceImpl(DeviceMapper deviceMapper, DevicePlanModeApplyMapper planApplyMapper, DeviceControlModeApplyMapper ctrlApplyMapper, PushAlarmMapper pushAlarmMapper, UserMapper userMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, UserRegionMapper userRegionMapper, ServerMapper serverMapper) {
        this.deviceMapper = deviceMapper;
        this.planApplyMapper = planApplyMapper;
        this.ctrlApplyMapper = ctrlApplyMapper;
        this.pushAlarmMapper = pushAlarmMapper;
        this.userMapper = userMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.userRegionMapper = userRegionMapper;
        this.serverMapper = serverMapper;
    }

    @Override
    public List<Device> findAll() {
        return deviceMapper.findAll();
    }


    @Override
    public PageResult findPage(Device device, int pageNo, int pageSize) {
        //再次转换deviceType
        if(device!=null){
            String deviceType = device.getDeviceType();
            if("测速".equals(deviceType)){
                device.setDeviceType("0");
            } else if ("显示屏".equals(deviceType)) {
                device.setDeviceType("1");
            } else if ("警闪灯".equals(deviceType)) {
                device.setDeviceType("2");
            } else if ("移动灯".equals(deviceType)) {
                device.setDeviceType("3");
            } else if ("信号灯".equals(deviceType)) {
                device.setDeviceType("4");
            }
        }
        com.shengxuan.speed.entity.User user = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(user.getId());
        List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
        for (UserRegion userRegion : userRegionList) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegion.getRid());
            if(byRid!=null){
                regionDeviceTypeList.add(byRid);
            }
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Device> list = deviceMapper.findByConditionAndPage(device,regionDeviceTypeList);
        for (Device device1 : list) {
            int serverId = device1.getServerId();
            if(serverId == 0){
                Server server = new Server();
                server.setId(0);
                server.setServerName("主服务器");
                device1.setServer(server);
            }else {
                Server byId = serverMapper.findById(serverId);
                device1.setServer(byId);
            }
        }
        PageInfo<Device> pageInfo =  new PageInfo<>(list);
        long total = pageInfo.getTotal();
        return new PageResult(total,pageSize,pageNo,list);
    }

    @Override
    public Device findById(String deviceId,int serverId) {
        Server server;
        if(serverId == 0){
            server = new Server();
            server.setId(0);
            server.setServerName("0--主服务器");
        }else {
            server = serverMapper.findById(serverId);
            server.setServerName(server.getId()+"--"+server.getServerName());
        }
        Device device = deviceMapper.findByDeviceId(deviceId, serverId);
        device.setServer(server);
        return device;
    }

    @Override
    public List<RegionAndSubRegion> findAllRegion() {
        List<RegionAndSubRegion> list = new ArrayList<>();
        //1.先查询出所有的region list
        List<String> regionList = new ArrayList<>();
        regionList.add("全部");
        //查询权限内的区域
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        Set<String> allRegion = new LinkedHashSet<String>();
        for (int i = 0; i < userRegionList.size(); i++) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegionList.get(i).getRid());
            allRegion.add(String.valueOf(byRid.getRegion()));
        }
        regionList.addAll(allRegion);
        //2.循环列表找到对应的subRegion
        for (String regionName : regionList) {
            RegionAndSubRegion regionAndSubRegion = new RegionAndSubRegion();
            List<String> subRegionList = new ArrayList<>();
            subRegionList.add("全部");
            Set<String> allSubRegionByRegion = new LinkedHashSet<String>();
            for (int i = 0; i < userRegionList.size(); i++) {
                RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegionList.get(i).getRid());
                allSubRegionByRegion.add(String.valueOf(byRid.getSubregion()));
            }
            subRegionList.addAll(allSubRegionByRegion);
            regionAndSubRegion.setRegionName(regionName);
            regionAndSubRegion.setSubRegionNameList(subRegionList);
            list.add(regionAndSubRegion);
        }
        return list;
    }

    @Override
    public List<String> findAllDeviceType() {
        List<String> allDeviceType = new ArrayList<>();
        allDeviceType.add("全部");
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        Set<String> allDeviceType1 = new LinkedHashSet<String>();
        for (int i = 0; i < userRegionList.size(); i++) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegionList.get(i).getRid());
            String deviceType = byRid.getDeviceType();
            if("0".equals(deviceType)){
                allDeviceType1.add("测速");
            }else if ("1".equals(deviceType)) {
                allDeviceType1.add("显示屏");
            } else if ("2".equals(deviceType)) {
                allDeviceType1.add("警闪灯");
            } else if ("3".equals(deviceType)) {
                allDeviceType1.add("移动灯");
            } else if("4".equals(deviceType)){
                allDeviceType1.add("信号灯");
            }else {

            }
        }
        allDeviceType.addAll(allDeviceType1);
        return allDeviceType;
    }

    @Override
    public List<Device> findAllAndFlag() {
        //查询权限下所有的设备
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        List<RegionDeviceType> list = new ArrayList<>();
        for (int i = 0; i < userRegionList.size(); i++) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegionList.get(i).getRid());
            list.add(byRid);
        }
        Device device1 = new Device();
        device1.setOfRegionId("全部");
        device1.setOfSubRegionId("全部");
        device1.setDeviceType("全部");
        device1.setServerId(-1);    //表示查询所有的服务器下的
        List<Device> deviceList = deviceMapper.findByConditionAndPage(device1, list);
        for (Device device : deviceList) {
            int serverId = device.getServerId();
            Server server;
            if(serverId == 0){
                server = new Server();
                server.setId(0);
                server.setServerName("主服务器");
            }else {
                server = serverMapper.findById(device.getServerId());
            }
            device.setServer(server);
        }
        //List<Device> deviceList = deviceMapper.findAll();
        //删除10分钟之前的申请
        String timeBefore10Minutes = DateFormat.getTimeBefore10Minutes();
        ctrlApplyMapper.deleteBefore10Minutes(timeBefore10Minutes);
        planApplyMapper.deleteBefore10Minutes(timeBefore10Minutes);
        if(deviceList!=null && deviceList.size()>0){
            for (Device device : deviceList) {
                String deviceId = device.getDeviceId();
                int serverId = device.getServerId();
                //1.先查询报警表判断有没有(未读的)
                List<PushAlarm> pushAlarmList = pushAlarmMapper.findByDeviceIdAndChecked(deviceId,serverId);
                if(pushAlarmList!=null && pushAlarmList.size()>0){
                    device.setFlag("false");
                    continue;
                }
                List<DeviceControlModeApply> list1 = ctrlApplyMapper.findByDeviceIdAndChecked(deviceId,serverId);
                if(list1!=null && list1.size()>0){
                    device.setFlag("false");
                    continue;
                }
                List<DevicePlanModeApply> list2 = planApplyMapper.findByDeviceIdAndChecked(deviceId,serverId);
                if(list2!=null && list2.size()>0){
                    device.setFlag("false");
                }
            }
        }
        //再此直接转换经纬度传过去
        for (Device device : deviceList) {
            Map<String, Double> stringDoubleMap = CoordinateTransform.wgs84ToGcj02(Double.parseDouble(device.getLongitude()), Double.parseDouble(device.getDimension()));
            Double lng = stringDoubleMap.get("lng");
            Double lat = stringDoubleMap.get("lat");
            device.setLongitude(String.valueOf(lng));
            device.setDimension(String.valueOf(lat));
        }

        //循环列表判断该设备是否有报警信息或者申请信息
        return deviceList;
    }


    @Override
    public List<ServerAndRegionAndSubRegionAndDevice> findAllServerAndRegionAndSubRegionAndDevice() {
        List<ServerAndRegionAndSubRegionAndDevice> list = new ArrayList<>();
        //0.查询出所有的server list
        List<String> serverList = new ArrayList<>();
        com.shengxuan.speed.entity.User currentUser = findCurrentUser();
        List<UserRegion> userRegionList = userRegionMapper.findByUserId(currentUser.getId());
        Set<String> allServer = new LinkedHashSet<String>();
        allServer.add("0###===###主服务器");
        //获取所有权限内的regionDeviceTypeList;
        List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
        for (int i = 0; i < userRegionList.size(); i++) {
            RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegionList.get(i).getRid());
            regionDeviceTypeList.add(byRid);
            //allRegion.add(String.valueOf(byRid.getRegion()));
            Server server = serverMapper.findById(byRid.getServerId());
            if(server!=null){
                allServer.add(byRid.getServerId()+"###===###"+String.valueOf(server.getServerName()));
            }
        }

        serverList.addAll(allServer);

        for (String serverName : serverList) {
            //server
            ServerAndRegionAndSubRegionAndDevice srsd = new ServerAndRegionAndSubRegionAndDevice(); //构造对象
            srsd.setServer(serverName.split("###===###")[1]);
            Set<String> allRegion = new LinkedHashSet<String>();    //对应服务器所属region
            //循环regionDeviceTypeList找到对应server下的region
            for (RegionDeviceType regionDeviceType : regionDeviceTypeList) {
                if(Objects.equals(regionDeviceType.getServerId()+"", serverName.split("###===###")[0])){
                    allRegion.add(regionDeviceType.getRegion());
                }
            }
            List<String> allRegionByServer = new ArrayList<>();
            allRegionByServer.addAll(allRegion);
            List<RegionAndSubRegionAndDevice> list2 = new ArrayList<>();
            //遍历server所属region 构造 subregion
            for (String region : allRegionByServer) {
                //region
                RegionAndSubRegionAndDevice rsd = new RegionAndSubRegionAndDevice();
                rsd.setRegion(region);
                Set<String> allSubregion = new LinkedHashSet<String>();
                //循环regionDeviceTypeList找到对应region下的subregion
                for (RegionDeviceType regionDeviceType : regionDeviceTypeList) {
                    if(regionDeviceType.getRegion().equals(region)){
                        allSubregion.add(regionDeviceType.getSubregion());
                    }
                }
                List<String> allSubRegionByRegion = new ArrayList<>();
                allSubRegionByRegion.addAll(allSubregion);
                List<SubRegionAndDevice> list3 = new ArrayList<>();
                for (String subRegion : allSubRegionByRegion) {
                    //subregion
                    SubRegionAndDevice subRegionAndDevice = new SubRegionAndDevice();
                    subRegionAndDevice.setSubRegion(subRegion);
                    Set<String> allDeviceType = new LinkedHashSet<String>();
                    for (RegionDeviceType regionDeviceType : regionDeviceTypeList) {
                        if(region.equals(regionDeviceType.getRegion()) && subRegion.equals(regionDeviceType.getSubregion())){
                            allDeviceType.add(regionDeviceType.getDeviceType());
                        }
                    }
                    //List<Device> deviceList = deviceMapper.findByRegionAndSubRegion(regionName,subRegion);
                    List<String> deviceTypeList =new ArrayList<>();
                    deviceTypeList.addAll(allDeviceType);
                    List<Device> deviceList = deviceMapper.findByServerAndRegionAndSubRegionAndDeviceTypeList(Integer.parseInt(serverName.split("###===###")[0]),region,subRegion,deviceTypeList);
                    for (Device device : deviceList) {
                        //坐标转换
                        Map<String, Double> stringDoubleMap = CoordinateTransform.wgs84ToGcj02(Double.parseDouble(device.getLongitude()), Double.parseDouble(device.getDimension()));
                        Double lng = stringDoubleMap.get("lng");
                        Double lat = stringDoubleMap.get("lat");
                        device.setLongitude(String.valueOf(lng));
                        device.setDimension(String.valueOf(lat));
                        int serverId = device.getServerId();
                        Server server;
                        if(serverId == 0){
                            server = new Server();
                            server.setId(0);
                            server.setServerName("主服务器");
                        }else {
                            server = serverMapper.findById(serverId);
                        }
                        device.setServer(server);
                    }
                    subRegionAndDevice.setDeviceList(deviceList);
                    list3.add(subRegionAndDevice);
                }
                rsd.setSubRegionAndDeviceList(list3);
                list2.add(rsd);
            }
            srsd.setRegionAndSubRegionAndDeviceList(list2);
            list.add(srsd);
        }
        return list;
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
