package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.pojo.Apply;

import java.util.List;

public interface ApplyService {

    /**
     * 查询所有的10分钟内申请包括手动和程控组装成apply
     * @return
     */
    List<Apply> findAll();

    void updateFlag(Apply apply);

    List<Apply> findNewApply10Size();
}
