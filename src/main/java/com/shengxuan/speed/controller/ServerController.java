package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.Server;
import com.shengxuan.speed.entity.User;
import com.shengxuan.speed.entity.pojo.ResultQuery;
import com.shengxuan.speed.service.ServerService;
import com.shengxuan.speed.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @RequestMapping("/findAll")
    public List<Server> findAll(){
        return serverService.findAll();
    }

    @RequestMapping("/findByServerName")
    public Server findByServerName(String serverName){
        return serverService.findByServerName(serverName);
    }

    @RequestMapping("/add")
    public ResultQuery add(@RequestBody Server server){
        return serverService.add(server);
    }

    @RequestMapping("/deleteById")
    public void deleteById(Integer id){
        //先删除关联权限user_region
        serverService.deleteById(id);
    }
}
