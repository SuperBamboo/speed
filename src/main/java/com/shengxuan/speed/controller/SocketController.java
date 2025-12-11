package com.shengxuan.speed.controller;

import com.shengxuan.speed.SpeedApplication;
import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.entity.pojo.DeviceControlModeSet;
import com.shengxuan.speed.entity.pojo.DevicePlanModeSet;
import com.shengxuan.speed.entity.pojo.ResultQuery;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.socket.Client;
import com.shengxuan.speed.util.DateFormat;
import com.shengxuan.speed.util.callback.CallbackListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/socket")
public class SocketController {

    private final LoggerMapper loggerMapper;

    private final DeviceMapper deviceMapper;

    private final DeviceCtrlModeMapper deviceCtrlModeMapper;

    private final PlanModeMapper planModeMapper;

    private final ServerMapper serverMapper;

    private final WarningToneMapper warningToneMapper;

    private final DisplayMapper displayMapper;

    private final AlarmMapper alarmMapper;

    private final PlanParamValueMapper planParamValueMapper;

    private final PlanParamMapper planParamMapper;

    private final ParameterMapper parameterMapper;

    private final PortMapper portMapper;

    private final PushAlarmMapper pushAlarmMapper;

    private final RegionMapper regionMapper;

    private final DeviceControlModeApplyMapper deviceControlModeApplyMapper;

    private final DevicePlanModeApplyMapper devicePlanModeApplyMapper;

    private final PlanParamApplyMapper planParamApplyMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    private final UserRegionMapper userRegionMapper;

    private final WebSocketHandler webSocketHandler;


    public SocketController(LoggerMapper loggerMapper, DeviceMapper deviceMapper, DeviceCtrlModeMapper deviceCtrlModeMapper, PlanModeMapper planModeMapper, ServerMapper serverMapper, WarningToneMapper warningToneMapper, DisplayMapper displayMapper, AlarmMapper alarmMapper, PlanParamValueMapper planParamValueMapper, PlanParamMapper planParamMapper, ParameterMapper parameterMapper, PushAlarmMapper pushAlarmMapper, PortMapper portMapper, RegionMapper regionMapper, DeviceControlModeApplyMapper deviceControlModeApplyMapper, DevicePlanModeApplyMapper devicePlanModeApplyMapper, PlanParamApplyMapper planParamApplyMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, UserRegionMapper userRegionMapper, WebSocketHandler webSocketHandler) {
        this.loggerMapper = loggerMapper;
        this.deviceMapper = deviceMapper;
        this.deviceCtrlModeMapper = deviceCtrlModeMapper;
        this.planModeMapper = planModeMapper;
        this.serverMapper = serverMapper;
        this.warningToneMapper = warningToneMapper;
        this.displayMapper = displayMapper;
        this.alarmMapper = alarmMapper;
        this.planParamValueMapper = planParamValueMapper;
        this.planParamMapper = planParamMapper;
        this.parameterMapper = parameterMapper;
        this.pushAlarmMapper = pushAlarmMapper;
        this.portMapper = portMapper;
        this.regionMapper = regionMapper;
        this.deviceControlModeApplyMapper = deviceControlModeApplyMapper;
        this.devicePlanModeApplyMapper = devicePlanModeApplyMapper;
        this.planParamApplyMapper = planParamApplyMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.userRegionMapper = userRegionMapper;
        this.webSocketHandler = webSocketHandler;
    }


    @RequestMapping("/sendDeviceStau")
    @ResponseBody
    public ResultQuery sendDeviceStau(String sessionId, String deviceId,int serverId) {
        ResultQuery resultQuery = new ResultQuery();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
                for (Client client : clientList) {
                    if(client.getServerId() == serverId){
                        client.sendQueryStatus(deviceId,sessionId);
                    }
                }
            }
        }).start();

        resultQuery.setType(1);
        resultQuery.setMessage("已向socket连接发送了查询 "+deviceId+"指令");
        return resultQuery;
    }

    @RequestMapping("/sendDeviceRefresh")
    @ResponseBody
    public ResultQuery sendDeviceRefresh() {
        ResultQuery resultQuery = new ResultQuery();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
                for (Client client : clientList) {
                    client.sendSysInfo();
                }
            }
        }).start();

        resultQuery.setType(1);
        resultQuery.setMessage("已向socket连接发送了刷新设备列表指令");
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
        logger.setDesc1("对服务器发送了刷新设备列表指令");
        loggerMapper.add(logger);
        return resultQuery;
    }

    @RequestMapping("/sendSDGK")
    @ResponseBody
    public ResultQuery sendDeviceCtrlModeSet(@RequestBody DeviceControlModeSet deviceCtrlModeSet,String sessionId){
        int serverId = deviceCtrlModeSet.getServerId();
        ResultQuery resultQuery = new ResultQuery();
        List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
        for (Client client : clientList) {
            if(client.getServerId() == serverId){
                client.sendCancelCtrl(deviceCtrlModeSet,sessionId);
            }
        }
        //SpeedApplication.MyApplicationRunner.clientList.get(0).sendCancelCtrl(deviceCtrlModeSet,sessionId);
        resultQuery.setType(2);
        resultQuery.setMessage("已向socket连接发送了下发手控指令" +"设备id "+deviceCtrlModeSet.getDeviceId());
        //此处插入日志
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
        sb.append("对 ");
        //0.获取服务器名称
        String serverName = serverMapper.findById(deviceCtrlModeSet.getServerId()).getServerName();
        sb.append(serverName);
        sb.append(" 下 ");
        //1.获取设备名称
        //Device device = deviceMapper.findByDeviceId(deviceCtrlModeSet.getDeviceId());
        Device device = deviceMapper.findByDeviceIdAndServerId(deviceCtrlModeSet.getDeviceId(),deviceCtrlModeSet.getServerId());
        String deviceName = device.getDeviceName();
        sb.append(deviceName);
        sb.append(" 发出了 ");
        if("0".equals(deviceCtrlModeSet.getValue())){
            sb.append("取消手动管控指令");
        }else {
            String ctrlName = deviceCtrlModeMapper.findNameByDeviceIdAndCtrlNo(deviceCtrlModeSet.getDeviceId(), deviceCtrlModeSet.getCtrlNo(),deviceCtrlModeSet.getServerId());
            sb.append(ctrlName);
            sb.append(" 手动管控指令");
        }
        logger.setDesc1(sb.toString());
        loggerMapper.add(logger);
        return resultQuery;
    }

    @RequestMapping("/sendCSGK")
    @ResponseBody
    public ResultQuery sendDevicePlanModeSet(@RequestBody DevicePlanModeSet devicePlanModeSet, String sessionId){
        ResultQuery resultQuery = new ResultQuery();
        int serverId = devicePlanModeSet.getServerId();
        List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
        for (Client client : clientList) {
            if(client.getServerId() == serverId){
                client.sendCancelPlan(devicePlanModeSet,sessionId);
            }
        }
        //SpeedApplication.MyApplicationRunner.clientList.get(0).sendCancelPlan(devicePlanModeSet,sessionId);
        resultQuery.setType(3);
        resultQuery.setMessage("已向socket连接发送了下发程控指令" +"设备id "+devicePlanModeSet.getDeviceId());
        //此处插入日志
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
        sb.append("对 ");
        //1.获取设备名称
        //Device device = deviceMapper.findByDeviceId(devicePlanModeSet.getDeviceId());
        Device device = deviceMapper.findByDeviceIdAndServerId(devicePlanModeSet.getDeviceId(),devicePlanModeSet.getServerId());
        String deviceName = device.getDeviceName();
        sb.append(deviceName);
        sb.append(" 发出了 ");
        if("0".equals(devicePlanModeSet.getValue())){
            sb.append("取消手动程控指令");
        }else {
            String planName = planModeMapper.findNameByDeviceIdAndPlanNo(devicePlanModeSet.getDeviceId(), devicePlanModeSet.getPlanNo(),devicePlanModeSet.getServerId());
            sb.append(planName);
            sb.append(" 手动管控指令");
        }
        logger.setDesc1(sb.toString());
        loggerMapper.add(logger);
        return resultQuery;
    }

    @RequestMapping("/sendDeviceByDeviceIdAndServerId")
    @ResponseBody
    public void sendDeviceByDeviceIdAndServerId(String deviceId,int serverId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
                for (Client client : clientList) {
                    if(client.getServerId() == serverId){
                        client.sendDevice(deviceId);
                    }
                }
            }
        }).start();
    }

    @RequestMapping("/openSocket")
    @ResponseBody
    public ResultQuery openSocket(@RequestBody Server server){
        ResultQuery resultQuery = new ResultQuery();
        String ip = server.getIp();
        Integer port = server.getPort();
        String username = server.getUsername();
        String password = server.getPassword();
        Integer serverId = server.getId();
        if(Objects.equals(ip, "") || port == 0 || Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(server.getServerName(), "")){
            resultQuery.setType(-1);
            resultQuery.setMessage("启动失败!,服务器数据有误!");
            return resultQuery;
        }
        //判断服务是否启动
        List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
        for (Client client : clientList) {
            if (client.getServerId() == serverId && server.isFlag() == true){
                //需要启动的服务器已经在运行了
                resultQuery.setType(-1);
                resultQuery.setMessage("启动失败,该服务器已经在运行!");
                return resultQuery;
            }
        }
        Client client = new Client(serverId,ip,port,username,password,deviceMapper,warningToneMapper,displayMapper,alarmMapper,deviceCtrlModeMapper,
                planParamValueMapper,planParamMapper,planModeMapper,parameterMapper,portMapper,pushAlarmMapper,regionMapper,deviceControlModeApplyMapper,
                devicePlanModeApplyMapper,planParamApplyMapper,regionDeviceTypeMapper,userRegionMapper,webSocketHandler);
        client.start();
        SpeedApplication.MyApplicationRunner.clientList.add(client);
        resultQuery.setType(0);
        resultQuery.setMessage(server.getServerName()+" 启动成功!");
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
        sb.append("开启了 ");
        //0.获取服务器名称
        sb.append(server.getServerName());
        logger.setDesc1(sb.toString());
        loggerMapper.add(logger);
        //-----------------插入日志结束------------------------------
        //修改该服务器启动状态为运行
        serverMapper.updateFlagById(serverId,true);
        return resultQuery;
    }

    @RequestMapping("/closeSocket")
    @ResponseBody
    public ResultQuery closeSocket(@RequestBody Server server){
        ResultQuery resultQuery = new ResultQuery();
        String ip = server.getIp();
        Integer port = server.getPort();
        String username = server.getUsername();
        String password = server.getPassword();
        Integer serverId = server.getId();
        if(Objects.equals(ip, "") || port == 0 || Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(server.getServerName(), "")){
            resultQuery.setType(-1);
            resultQuery.setMessage("关闭失败!,服务器数据有误!");
            return resultQuery;
        }
        //判断服务是否已经关闭
        List<Client> clientList = SpeedApplication.MyApplicationRunner.clientList;
        boolean flag = false;   //标志服务器是否在运行列表中
        for (Client client : clientList) {
            if (client.getServerId() == serverId && server.isFlag() == true){
                //需要启动的服务器已经在运行了
                flag = true;
                //发送退出报文，关闭该服务器
                client.sendExit(new CallbackListener() {
                    @Override
                    public void onFinish(int code, String response) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                client.setStopTread(true);
                //修改该服务器启动状态为关闭
                serverMapper.updateFlagById(serverId,false);
                resultQuery.setType(0);
                resultQuery.setMessage("关闭成功!");
                clientList.remove(client);
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
                sb.append("关闭了 ");
                //0.获取服务器名称
                sb.append(server.getServerName());
                logger.setDesc1(sb.toString());
                loggerMapper.add(logger);
                //-----------------插入日志结束------------------------------
                return resultQuery;
            }
        }
        if (flag == false){
            //服务器已经关闭
            resultQuery.setType(-1);
            resultQuery.setMessage("关闭失败!,该服务器已经关闭!");
            return resultQuery;
        }
        return resultQuery;
    }
}
