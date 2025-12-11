package com.shengxuan.speed.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengxuan.speed.entity.Alarm;
import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.pojo.PageResult;
import com.shengxuan.speed.mapper.AlarmMapper;
import com.shengxuan.speed.mapper.DeviceMapper;
import com.shengxuan.speed.service.AlarmService;
import com.shengxuan.speed.service.DeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {

    private final AlarmMapper alarmMapper;

    public AlarmServiceImpl(AlarmMapper alarmMapper) {
        this.alarmMapper = alarmMapper;
    }

    /*@Override
    public List<Alarm> findAll() {
        return alarmMapper.findAll();
    }*/


    /*@Override
    public PageResult findPage(Alarm alarm, int pageNo, int pageSize,String deviceId) {
        PageHelper.startPage(pageNo,pageSize);
        List<Alarm> list = alarmMapper.findByDeviceId(deviceId);
        PageInfo<Alarm> pageInfo =  new PageInfo<>(list);
        long total = pageInfo.getTotal();
        return new PageResult(total,pageSize,pageNo,list);
    }

    @Override
    public Alarm findById(String deviceId) {
        return alarmMapper.findByDeviceId(deviceId);
    }*/

}
