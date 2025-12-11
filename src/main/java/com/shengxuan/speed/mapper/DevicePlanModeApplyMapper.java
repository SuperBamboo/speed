package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.DevicePlanModeApply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DevicePlanModeApplyMapper {
    @Select("select * from device_plan_mode_apply where flag = 'false' ")
    @Results({
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "value",column = "value"),
            @Result(property = "planNo",column = "plan_no"),
            @Result(property = "holdTime",column = "hold_time"),
            @Result(property = "intervalTime",column = "interval_time"),
            @Result(property = "intervalTime",column = "interval_time"),
            @Result(property = "repetitions",column = "repetitions"),
            @Result(property = "sysTime",column = "sys_time"),
            @Result(property = "applyTime",column = "apply_time"),
            @Result(property = "flag",column = "flag")
    })
    List<DevicePlanModeApply> findAll();

    @Select("select * from device_plan_mode_apply where device_id = #{deviceId} and server_id = #{serverId}")
    List<DevicePlanModeApply> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Insert("insert into device_plan_mode_apply(device_id,server_id,value,plan_no,hold_time,interval_time,repetitions,sys_time,apply_time,flag) " +
            "values(#{deviceId},#{serverId},#{value},#{planNo},#{holdTime},#{intervalTime},#{repetitions},#{sysTime},#{applyTime},#{flag})")
    void add(DevicePlanModeApply devicePlanModeApply);

    @Delete("delete from device_plan_mode_apply where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Delete("delete from device_plan_mode_apply where apply_time < #{timeBefore10Minutes}")
    void deleteBefore10Minutes(@Param("timeBefore10Minutes")String timeBefore10Minutes);

    @Select("select * from device_plan_mode_apply where device_id = #{deviceId} and server_id = #{serverId} and flag = 'false' ")
    List<DevicePlanModeApply> findByDeviceIdAndChecked(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Update("update device_plan_mode_apply set flag = 'true' where device_id = #{deviceId} and server_id = #{serverId} and apply_time = #{applyTime}")
    void updateFlag(@Param("deviceId") String deviceId,@Param("serverId")int serverId,@Param("applyTime") String applyTime);

    @Delete("delete from device_plan_mode_apply where server_id = #{serverId}")
    void delByServerId(int serverId);
}
