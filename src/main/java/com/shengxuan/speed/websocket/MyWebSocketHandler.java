package com.shengxuan.speed.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shengxuan.speed.entity.*;
import com.shengxuan.speed.entity.pojo.MyWebSocketSessionAndUser;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import com.shengxuan.speed.mapper.DeviceMapper;
import com.shengxuan.speed.mapper.RegionDeviceTypeMapper;
import com.shengxuan.speed.mapper.UserMapper;
import com.shengxuan.speed.mapper.UserRegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    public static List<WebSocketSession> sessions = new ArrayList<>();

    public static List<MyWebSocketSessionAndUser> sessionAndUsers = new ArrayList<>();

    private final UserMapper userMapper;

    private final UserRegionMapper userRegionMapper;

    private final DeviceMapper deviceMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    public MyWebSocketHandler(UserMapper userMapper, UserRegionMapper userRegionMapper, DeviceMapper deviceMapper, RegionDeviceTypeMapper regionDeviceTypeMapper) {
        this.userMapper = userMapper;
        this.userRegionMapper = userRegionMapper;
        this.deviceMapper = deviceMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        session.getAttributes().put("sessionId",sessionId);
        String name = session.getPrincipal().getName();
        User user = userMapper.findByUsername(name);
        TextMessage message = new TextMessage("SessionId:" + sessionId);
        // 添加新连接
        sessions.add(session);
        MyWebSocketSessionAndUser a = new MyWebSocketSessionAndUser();
        a.setUser(user);
        a.setSession(session);
        //添加用户信息和session绑定
        sessionAndUsers.add(a);
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        for (MyWebSocketSessionAndUser sessionAndUser : sessionAndUsers) {
            if(sessionAndUser.getSession() == session){
                sessionAndUsers.remove(sessionAndUser);
                return;
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 收到消息时的处理逻辑
        String payload = message.getPayload();
        System.out.println("Received message from socket: " + payload);

        // 向所有连接的前端发送消息
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage("Message from socket: " + payload));
        }
    }

    /**
     * 后台产生的消息推送给链接的每个用户（报警，申请）
     * @param message
     */
    public void broadcastMessage(int serverId,String message){
        for (MyWebSocketSessionAndUser sessionAndUser : sessionAndUsers) {
            User user = sessionAndUser.getUser();
            if (user != null){
                List<UserRegion> userRegionList = userRegionMapper.findByUserId(user.getId());
                List<RegionDeviceType> regionDeviceTypeList = new ArrayList<>();
                for (UserRegion userRegion : userRegionList) {
                    RegionDeviceType byRid = regionDeviceTypeMapper.findByRid(userRegion.getRid());
                    regionDeviceTypeList.add(byRid);
                }
                //List<String> deviceIds = deviceMapper.findByRegionDeviceType(regionDeviceTypeList);
                List<ServerDevice> serverDeviceList = deviceMapper.findByServerRegionDeviceType(regionDeviceTypeList);

                String deviceId = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try{
                    if(message!=null && !"".equals(message)){
                        if(message.contains("3===")){
                            //手控申请
                            String[] split = message.split("===");
                            DeviceControlModeApply deviceControlModeApply = objectMapper.readValue(split[1], DeviceControlModeApply.class);
                            deviceId = deviceControlModeApply.getDeviceId();
                            //serverId = deviceControlModeApply.getServerId();
                        } else if (message.contains("4===")) {
                            //管控申请
                            String[] split = message.split("===");
                            DevicePlanModeApply devicePlanModeApply = objectMapper.readValue(split[1], DevicePlanModeApply.class);
                            deviceId = devicePlanModeApply.getDeviceId();
                            //serverId = devicePlanModeApply.getServerId();
                        }else {
                            //报警消息
                            String[] split = message.split(" ");
                            deviceId = split[0];
                        }
                    }

                    if(serverDeviceList!=null && deviceId!=null && serverId!=-1){
                        for (ServerDevice serverDevice : serverDeviceList) {
                            if(serverDevice.getDeviceId().equals(deviceId) && serverDevice.getServerId() == serverId){
                                sessionAndUser.getSession().sendMessage(new TextMessage(message));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 后台延迟产生的消息传回给发送给后台任务对应的前台
     * @param n 消息类型 1：状态返回 2.设置成功/失败
     * @param sessionId 前后台链接通道id
     * @param message   消息体
     */
    public void broadcastMessageBySessionId(int n,String sessionId,String message){
        for (WebSocketSession session : sessions) {

            try{
                if(session.getId().equals(sessionId)){
                    session.sendMessage(new TextMessage(String.valueOf(n)+"==="+message));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}