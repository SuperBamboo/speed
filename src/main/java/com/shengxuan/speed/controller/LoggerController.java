package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logger")
public class LoggerController {

    private final LoggerService loggerService;

    public LoggerController(LoggerService loggerService) {
        this.loggerService = loggerService;
    }


    @RequestMapping("/findAll")
    @ResponseBody
    public List<Logger> findAll() {
        List<Logger> all = loggerService.findAll();
        return all;
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Logger searchLogger, int pageNo, int pageSize){
        return loggerService.findPage(searchLogger,pageNo,pageSize);
    }
}
