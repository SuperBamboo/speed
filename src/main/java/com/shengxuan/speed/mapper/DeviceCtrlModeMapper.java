package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.DeviceCtrlMode;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceCtrlModeMapper {
    @Select("select * from device_ctrl_mode where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceCtrlModeNo",column = "device_ctrl_mode_no"),
            @Result(property ="deviceCtrlModeName",column = "device_ctrl_mode_name"),
            @Result(property ="relayStau",column = "relay_stau"),
            @Result(property ="relayStauValidity",column = "relay_stau_validity"),
            @Result(property ="relayStau1",column = "relay_stau1"),
            @Result(property ="relayStau1Validity",column = "relay_stau1_validity"),
            @Result(property ="displayNo",column = "display_no"),
            @Result(property ="displayNoValidity",column = "display_no_validity"),
            @Result(property ="warningToneNo",column = "warning_tone_no"),
            @Result(property ="warningToneNoValidity",column = "warning_tone_no_validity"),
            @Result(property ="volume",column = "volume"),
            @Result(property ="volumeValidity",column = "volume_validity"),
            @Result(property ="playInteval",column = "play_inteval"),
            @Result(property ="playIntevalValidity",column = "play_inteval_validity"),
            @Result(property ="playNumbers",column = "play_numbers"),
            @Result(property ="playNumbersValidity",column = "play_numbers_validity"),
            @Result(property ="holdTime",column = "hold_time"),
            @Result(property ="holdTimeValidity",column = "hold_time_validity")
    })
    List<DeviceCtrlMode> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);
    @Select("select device_ctrl_mode_name from device_ctrl_mode where device_id = #{deviceId} and device_ctrl_mode_no = #{deviceCtrlModeNo} and server_id = #{serverId}")
    String findNameByDeviceIdAndCtrlNo(@Param("deviceId") String deviceId,@Param("deviceCtrlModeNo") String deviceCtrlModeNo,@Param("serverId")int serverId);

    @Insert("insert into device_ctrl_mode(device_id,server_id,device_ctrl_mode_no,device_ctrl_mode_name,relay_stau,relay_stau_validity,relay_stau1,relay_stau1_validity,display_no,display_no_validity,warning_tone_no,warning_tone_no_validity,volume,volume_validity,play_inteval,play_inteval_validity,play_numbers,play_numbers_validity,hold_time,hold_time_validity) " +
            "values(#{deviceId},#{serverId},#{deviceCtrlModeNo},#{deviceCtrlModeName},#{relayStau},#{relayStauValidity},#{relayStau1},#{relayStau1Validity},#{displayNo},#{displayNoValidity},#{warningToneNo},#{warningToneNoValidity},#{volume},#{volumeValidity},#{playInteval},#{playIntevalValidity},#{playNumbers},#{playNumbersValidity},#{holdTime},#{holdTimeValidity})")
    void add(DeviceCtrlMode deviceCtrlMode);

    @Delete("delete from device_ctrl_mode where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from device_ctrl_mode where server_id = #{serverId}")
    void delByServerId(Integer serverId);
}
