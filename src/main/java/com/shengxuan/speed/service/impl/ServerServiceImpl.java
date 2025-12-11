package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.entity.pojo.ResultQuery;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.service.ServerService;
import com.shengxuan.speed.util.CheckUtils;
import com.shengxuan.speed.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {
    private final ServerMapper serverMapper;

    private final AlarmMapper alarmMapper;

    private final DeviceControlModeApplyMapper deviceControlModeApplyMapper;

    private final DeviceCtrlModeMapper deviceCtrlModeMapper;

    private final DevicePlanModeApplyMapper devicePlanModeApplyMapper;

    private final DisplayMapper displayMapper;

    private final ParameterMapper parameterMapper;

    private final PlanModeMapper planModeMapper;

    private final PlanParamMapper planParamMapper;

    private final PlanParamApplyMapper planParamApplyMapper;

    private final PlanParamValueMapper planParamValueMapper;

    private final PortMapper portMapper;

    private final PushAlarmMapper pushAlarmMapper;

    private final WarningToneMapper warningToneMapper;

    private final DeviceMapper deviceMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    private final UserRegionMapper userRegionMapper;

    private final LoggerMapper loggerMapper;

    public ServerServiceImpl(ServerMapper serverMapper, AlarmMapper alarmMapper, DeviceControlModeApplyMapper deviceControlModeApplyMapper, DeviceCtrlModeMapper deviceCtrlModeMapper, DevicePlanModeApplyMapper devicePlanModeApplyMapper, DisplayMapper displayMapper, ParameterMapper parameterMapper, PlanModeMapper planModeMapper, PlanParamMapper planParamMapper, PlanParamApplyMapper planParamApplyMapper, PlanParamValueMapper planParamValueMapper, PortMapper portMapper, PushAlarmMapper pushAlarmMapper, WarningToneMapper warningToneMapper, DeviceMapper deviceMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, UserRegionMapper userRegionMapper, LoggerMapper loggerMapper) {
        this.serverMapper = serverMapper;
        this.alarmMapper = alarmMapper;
        this.deviceControlModeApplyMapper = deviceControlModeApplyMapper;
        this.deviceCtrlModeMapper = deviceCtrlModeMapper;
        this.devicePlanModeApplyMapper = devicePlanModeApplyMapper;
        this.displayMapper = displayMapper;
        this.parameterMapper = parameterMapper;
        this.planModeMapper = planModeMapper;
        this.planParamMapper = planParamMapper;
        this.planParamApplyMapper = planParamApplyMapper;
        this.planParamValueMapper = planParamValueMapper;
        this.portMapper = portMapper;
        this.pushAlarmMapper = pushAlarmMapper;
        this.warningToneMapper = warningToneMapper;
        this.deviceMapper = deviceMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.userRegionMapper = userRegionMapper;
        this.loggerMapper = loggerMapper;
    }


    @Override
    public List<Server> findAll() {
        List<Server> serverList = serverMapper.findAll();
        return serverList;
    }

    @Override
    public void deleteById(Integer id) {
        if (id == 0){
            return;
        }
        //先删除关联数据
        alarmMapper.delByServerId(id);
        deviceControlModeApplyMapper.delByServerId(id);
        deviceCtrlModeMapper.delByServerId(id);
        devicePlanModeApplyMapper.delByServerId(id);
        displayMapper.delByServerId(id);
        parameterMapper.delByServerId(id);
        planModeMapper.delByServerId(id);
        planParamMapper.delByServerId(id);
        planParamApplyMapper.delByServerId(id);
        planParamValueMapper.delByServerId(id);
        portMapper.delByServerId(id);
        pushAlarmMapper.delByServerId(id);
        warningToneMapper.delByServerId(id);
        deviceMapper.delByServerId(id);
        List<RegionDeviceType> regionDeviceTypeListByServerId = regionDeviceTypeMapper.findAllByServerId(id);
        for (RegionDeviceType regionDeviceType : regionDeviceTypeListByServerId) {
            Integer rid = regionDeviceType.getRid();
            //先删除 userRegion
            userRegionMapper.delByRid(rid);
            regionDeviceTypeMapper.delByRegionDeviceType(regionDeviceType);
        }
        //先获取该服务器名称再删除该服务器
        String serverName = serverMapper.findById(id).getServerName();
        serverMapper.delById(id);
        //此处插入日志------------------------------start-------------------
        Logger logger = new Logger();
        //1.设置用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.setUsername(userDetails.getUsername());
        }else {
            logger.setUsername("未知用户");
        }
        //设置日期
        String dateNotHaveTime = DateFormat.getDateNotHaveTime();
        logger.setDate(dateNotHaveTime);
        //获取详细时间
        String date = DateFormat.dateNowFormat();
        logger.setTime(date);
        //构建描述文字
        StringBuffer sb = new StringBuffer();
        sb.append("删除了 ");
        //0.获取服务器名称
        //Server server = serverMapper.findById(id);    //此时已经没有该服务器消息
        sb.append(serverName);
        sb.append(" 及其下所有关联数据!");
        logger.setDesc1(sb.toString());
        loggerMapper.add(logger);
        //-----------------插入日志结束------------------------------
    }

    @Override
    public ResultQuery add(Server server) {
        ResultQuery rs = new ResultQuery();
        String serverName = server.getServerName();
        Server server1 = findByServerName(serverName);
        if(server1 != null && server1.getServerName().equals(serverName)){
            rs.setType(-1);
            rs.setMessage("服务器名称已经存在!");
            return rs;
        }
        String ip = server.getIp();
        if(!CheckUtils.isValidIP(ip)){
            rs.setType(-1);
            rs.setMessage("IP错误!");
            return rs;
        }
        Integer port = server.getPort();
        if(!CheckUtils.isValidPort(port)){
            rs.setType(-1);
            rs.setMessage("端口号错误!");
            return rs;
        }
        String username = server.getUsername();
        if(!CheckUtils.isValidStr(username)){
            rs.setType(-1);
            rs.setMessage("用户名错误!");
            return rs;
        }
        String password = server.getPassword();
        if(!CheckUtils.isValidStr(password)){
            rs.setType(-1);
            rs.setMessage("密码错误!");
            return rs;
        }

        serverMapper.add(server);
        rs.setType(0);
        rs.setMessage("添加服务器成功!");
        return rs;
    }


    @Override
    public Server findByServerName(String serverName) {
        Server server = serverMapper.findByServerName(serverName);
        return server;
    }

}
