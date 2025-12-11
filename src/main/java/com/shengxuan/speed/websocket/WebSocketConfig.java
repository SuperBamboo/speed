package com.shengxuan.speed.websocket;

import com.shengxuan.speed.mapper.DeviceMapper;
import com.shengxuan.speed.mapper.RegionDeviceTypeMapper;
import com.shengxuan.speed.mapper.UserMapper;
import com.shengxuan.speed.mapper.UserRegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    private final UserMapper userMapper;

    private final UserRegionMapper userRegionMapper;

    private final DeviceMapper deviceMapper;

    private final RegionDeviceTypeMapper regionDeviceTypeMapper;

    public WebSocketConfig(WebSocketHandler webSocketHandler, UserMapper userMapper, UserRegionMapper userRegionMapper, DeviceMapper deviceMapper, RegionDeviceTypeMapper regionDeviceTypeMapper) {
        this.webSocketHandler = webSocketHandler;
        this.userMapper = userMapper;
        this.userRegionMapper = userRegionMapper;
        this.deviceMapper = deviceMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(userMapper,userRegionMapper,deviceMapper,regionDeviceTypeMapper), "/ws").setAllowedOrigins("*");
    }
}