package com.shengxuan.speed.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.mapper.LoggerMapper;
import com.shengxuan.speed.service.LoggerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggerServiceImpl implements LoggerService {

    private final LoggerMapper loggerMapper;

    public LoggerServiceImpl(LoggerMapper loggerMapper) {
        this.loggerMapper = loggerMapper;
    }

    @Override
    public List<Logger> findAll() {
        return loggerMapper.findAll();
    }

    @Override
    public PageResult findPage(Logger logger, int pageNo, int pageSize) {
        if(logger.getDate()!=null && logger.getDate()!="" && logger.getDate()!="null"){
            //传过来的时yyyy-MM-dd 需要转换成yyyyMMdd
            logger.setDate(logger.getDate().replace("-",""));
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Logger> list = loggerMapper.findByConditionAndPage(logger);
        //按照时间倒叙排列
        list.sort((a,b) ->{
            int dateComparison = b.getDate().compareTo(a.getDate());
            if(dateComparison!=0){
                return dateComparison;
            }else {
                return b.getDate().compareTo(a.getDate());
            }
        });
        PageInfo<Logger> pageInfo =  new PageInfo<>(list);
        long total = pageInfo.getTotal();
        return new PageResult(total,pageSize,pageNo,list);
    }

}
