package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PlanMode;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanModeMapper {
    @Select("select * from plan_mode where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="planModeNo",column = "plan_mode_no"),
            @Result(property ="planModeName",column = "plan_mode_name"),
            @Result(property ="holdTime",column = "hold_time"),
            @Result(property ="repetitions",column = "repetitions"),
            @Result(property ="intervalTime",column = "interval_time"),
            @Result(property ="sysTime",column = "sys_time")
    })
    List<PlanMode> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Select("select plan_mode_name from plan_mode where device_id = #{deviceId} and plan_mode_no = #{planModeNo} and server_id = #{serverId}")
    String findNameByDeviceIdAndPlanNo(@Param("deviceId") String deviceId,@Param("planModeNo") String planModeNo,@Param("serverId")int serverId);

    @Insert("insert into plan_mode(device_id,server_id,plan_mode_no,plan_mode_name,hold_time,repetitions,interval_time,sys_time) " +
            "values(#{deviceId},#{serverId},#{planModeNo},#{planModeName},#{holdTime},#{repetitions},#{intervalTime},#{sysTime})")
    void add(PlanMode planMode);

    @Delete("delete from plan_mode where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from plan_mode where server_id = #{serverId}")
    void delByServerId(int serverId);
}
