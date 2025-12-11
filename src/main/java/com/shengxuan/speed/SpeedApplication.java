package com.shengxuan.speed;

import com.shengxuan.speed.config.SpeedProperties;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.socket.Client;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@MapperScan("com.shengxuan.speed.mapper")
@EnableConfigurationProperties(SpeedProperties.class)
@EnableScheduling
public class SpeedApplication {



    public static void main(String[] args) {
        SpringApplication.run(SpeedApplication.class, args);
    }

    @Component
    public static class MyApplicationRunner implements ApplicationRunner{

        private final WebSocketHandler webSocketHandler;

        private final ServerMapper serverMapper;
        private final DeviceMapper deviceMapper;

        private final WarningToneMapper warningToneMapper;
        private final DisplayMapper displayMapper;
        private final AlarmMapper alarmMapper;

        private final DeviceCtrlModeMapper deviceCtrlModeMapper;
        private final PlanParamValueMapper planParamValueMapper;
        private final PlanParamMapper planParamMapper;
        private final PlanModeMapper planModeMapper;
        private final ParameterMapper parameterMapper;
        private final PortMapper portMapper;

        private final PushAlarmMapper pushAlarmMapper;

        private final RegionMapper regionMapper;

        private final DeviceControlModeApplyMapper deviceControlModeApplyMapper;
        private final DevicePlanModeApplyMapper devicePlanModeApplyMapper;
        private final PlanParamApplyMapper planParamApplyMapper;
        private final RegionDeviceTypeMapper regionDeviceTypeMapper;

        private UserRegionMapper userRegionMapper;
        public static List<Client>  clientList= new ArrayList();

        private final SpeedProperties speedProperties;

        public MyApplicationRunner(UserRegionMapper userRegionMapper,ServerMapper serverMapper, DeviceMapper deviceMapper, WarningToneMapper warningToneMapper, DisplayMapper displayMapper, AlarmMapper alarmMapper, DeviceCtrlModeMapper deviceCtrlModeMapper, PlanParamValueMapper planParamValueMapper, PlanParamMapper planParamMapper, PlanModeMapper planModeMapper, ParameterMapper parameterMapper, PortMapper portMapper, PushAlarmMapper pushAlarmMapper, RegionMapper regionMapper, DeviceControlModeApplyMapper deviceControlModeApplyMapper, DevicePlanModeApplyMapper devicePlanModeApplyMapper, PlanParamApplyMapper planParamApplyMapper, WebSocketHandler webSocketHandler, RegionDeviceTypeMapper regionDeviceTypeMapper, SpeedProperties speedProperties) {
            this.userRegionMapper = userRegionMapper;
            this.serverMapper = serverMapper;
            this.deviceMapper = deviceMapper;
            this.warningToneMapper = warningToneMapper;
            this.displayMapper = displayMapper;
            this.alarmMapper = alarmMapper;
            this.deviceCtrlModeMapper = deviceCtrlModeMapper;
            this.planParamValueMapper = planParamValueMapper;
            this.planParamMapper = planParamMapper;
            this.planModeMapper = planModeMapper;
            this.parameterMapper = parameterMapper;
            this.portMapper = portMapper;
            this.pushAlarmMapper = pushAlarmMapper;
            this.regionMapper = regionMapper;
            this.deviceControlModeApplyMapper = deviceControlModeApplyMapper;
            this.devicePlanModeApplyMapper = devicePlanModeApplyMapper;
            this.planParamApplyMapper = planParamApplyMapper;
            this.webSocketHandler = webSocketHandler;
            this.regionDeviceTypeMapper = regionDeviceTypeMapper;
            this.speedProperties = speedProperties;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            //启动删除两个月前推送的报警信息
            LocalDate date2MonthAgo = LocalDate.now().minusMonths(2);
            String format = date2MonthAgo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            pushAlarmMapper.deleteDateAgo(format);

            //0.程序一启动就先启动主服务器
            Client client = new Client(0,speedProperties.getIp(), speedProperties.getPort(), speedProperties.getUsername(), speedProperties.getPassword(),
                    deviceMapper, warningToneMapper,displayMapper,alarmMapper,deviceCtrlModeMapper,planParamValueMapper,planParamMapper,planModeMapper,parameterMapper,portMapper,pushAlarmMapper,regionMapper,deviceControlModeApplyMapper,devicePlanModeApplyMapper,planParamApplyMapper,regionDeviceTypeMapper,userRegionMapper,webSocketHandler);
            client.start();
            clientList.add(client);
            //2.查询数据库查询server
            List<Server> serverList = serverMapper.findAll();
            for (Server server : serverList) {
                if(server.getIp()!=null && server.getPort()!=0 && !Objects.equals(server.getUsername(), "") && !Objects.equals(server.getPassword(), "")){
                    Client client1 = new Client(server.getId(),server.getIp(),server.getPort(),server.getUsername(),server.getPassword(),
                            deviceMapper, warningToneMapper,displayMapper,alarmMapper,deviceCtrlModeMapper,planParamValueMapper,planParamMapper,planModeMapper,parameterMapper,portMapper,pushAlarmMapper,regionMapper,deviceControlModeApplyMapper,devicePlanModeApplyMapper,planParamApplyMapper,regionDeviceTypeMapper,userRegionMapper,webSocketHandler);
                    client1.start();
                    clientList.add(client1);
                    //修改该服务器启动状态为运行
                    if(server.isFlag() == false){
                        serverMapper.updateFlagById(server.getId(),true);
                    }
                }
            }
            //删除所有启动服务器之外的数据
        }
    }

}
