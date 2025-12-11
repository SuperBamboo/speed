package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Alarm;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlarmMapper {
    @Select("select * from warning_tone where device_id = #{deviceId} and server_id = #{serverId}")
    List<Alarm> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Insert("insert into alarm(device_id,server_id,alarm_no,alarm_name) values(#{deviceId},#{serverId},#{alarmNo},#{alarmName})")
    void add(Alarm alarm);

    @Delete("delete from alarm where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from alarm where server_id = #{serverId}")
    void delByServerId(int serverId);
}
