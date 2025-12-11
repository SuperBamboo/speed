package com.shengxuan.speed.controller;

import com.shengxuan.speed.entity.pojo.Apply;
import com.shengxuan.speed.service.ApplyService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    private final ApplyService applyService;

    public ApplyController(ApplyService applyService) {
        this.applyService = applyService;
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<Apply> findAll(){
        List<Apply> applyList = applyService.findAll();
        return applyList;
    }

    @RequestMapping("/updateFlag")
    public void updateFlag(@RequestBody Apply apply){
        applyService.updateFlag(apply);
    }

    @RequestMapping("/findNewApply10Size")
    public List<Apply> findNewApply10Size(){
        //aaa
        return applyService.findNewApply10Size();
    }
}
