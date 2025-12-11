package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.pojo.PageResult;

import java.util.List;

public interface LoggerService {
    List<Logger> findAll();

    /**
     * 分页条件查询
     * @param logger
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(Logger logger, int pageNo, int pageSize);

}
