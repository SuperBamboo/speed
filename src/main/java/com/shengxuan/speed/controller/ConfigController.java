package com.shengxuan.speed.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Value("${websocket.url}")
    private String websocketUrl;

    @RequestMapping("/getWebSocketUrl")
    public String getWebSocketUrl(){
        return websocketUrl;
    }
}
