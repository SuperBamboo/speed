package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.DeviceControlModeApply;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface DeviceControlModeApplyMapper {

    @Select("select * from device_control_mode_apply where flag = 'false' ")
    @Results({
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "value",column = "value"),
            @Result(property = "ctrlNo",column = "ctrl_no"),
            @Result(property = "relay1",column = "relay1"),
            @Result(property = "relay2",column = "relay2"),
            @Result(property = "displayNo",column = "display_no"),
            @Result(property = "warningTone",column = "warning_tone"),
            @Result(property = "volume",column = "volume"),
            @Result(property = "playInteval",column = "play_inteval"),
            @Result(property = "playNumbers",column = "play_numbers"),
            @Result(property = "holdTime",column = "hold_time"),
            @Result(property = "applyTime",column = "apply_time"),
            @Result(property = "flag",column = "flag")
    })
    List<DeviceControlModeApply> findAll();

    @Select("select * from device_control_mode_apply where device_id = #{deviceId} and server_id = #{serverId}")
    List<DeviceControlModeApply> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Insert("insert into device_control_mode_apply(device_id,server_id,value,ctrl_no,relay1,relay2,display_no,warning_tone,volume,play_inteval,play_numbers,hold_time,apply_time,flag) " +
            "values(#{deviceId},#{serverId},#{value},#{ctrlNo},#{relay1},#{relay2},#{displayNo},#{warningTone},#{volume},#{playInteval},#{playNumbers},#{holdTime},#{applyTime},#{flag})")
    void add(DeviceControlModeApply deviceControlModeApply);

    @Delete("delete from device_control_mode_apply where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from device_control_mode_apply where apply_time < #{timeBefore10Minutes}")
    void deleteBefore10Minutes(@Param("timeBefore10Minutes")String timeBefore10Minutes);

    @Select("select * from device_control_mode_apply where device_id = #{deviceId} and server_id = #{serverId} and flag = 'false' ")
    List<DeviceControlModeApply> findByDeviceIdAndChecked(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Update("update device_control_mode_apply set flag = 'true' where device_id = #{deviceId} and server_id = #{serverId} and apply_time = #{applyTime}")
    void updateFlag(@Param("deviceId") String deviceId,@Param("serverId")int serverId, @Param("applyTime") String applyTime);

    @Delete("delete from device_control_mode_apply where server_id = #{serverId}")
    void delByServerId(int serverId);
}
