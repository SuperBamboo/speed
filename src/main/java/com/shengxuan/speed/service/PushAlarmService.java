package com.shengxuan.speed.service;

import com.shengxuan.speed.entity.Alarm;
import com.shengxuan.speed.entity.PushAlarm;
import com.shengxuan.speed.entity.pojo.PageResult;

import java.util.List;

public interface PushAlarmService {
    List<PushAlarm> findAll();

    /**
     * 分页条件查询
     * @param pushAlarm
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(PushAlarm pushAlarm, int pageNo, int pageSize);

    /**
     * 根据id查询所有的报警信息部分也
     * @param deviceId
     * @return
     */
    List<PushAlarm> findAllByDeviceId(String deviceId,int serverId);

    void updateCheck(Integer id);

    void updateCheckAll();

    /**
     * 查询数据库最新的10条报警记录
     * @return
     */
    List<PushAlarm> findNewAlarm10Size();
}
