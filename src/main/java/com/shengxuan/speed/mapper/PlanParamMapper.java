package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PlanParam;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanParamMapper {
    @Select("select * from plan_param where device_id = #{deviceId} and server_id = #{serverId}")
    List<PlanParam> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Select("select * from plan_param where device_id = #{deviceId} and plan_mode_no = #{planModeNo} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="planModeNo",column = "plan_mode_no"),
            @Result(property ="planParamNo",column = "plan_param_no"),
            @Result(property ="planParamName",column = "plan_param_name"),
            @Result(property ="defaultValue",column = "default_value"),
            @Result(property ="isHavePlanParamValue",column = "is_have_plan_param_value")
    })
    List<PlanParam> findByDeviceIdAndPlanModeNo(@Param("deviceId") String deviceId,@Param("planModeNo") String planModeNo,@Param("serverId") int serverId);

    @Insert("insert into plan_param(device_id,server_id,plan_mode_no,plan_param_no,plan_param_name,default_value,is_have_plan_param_value) " +
            "values(#{deviceId},#{serverId},#{planModeNo},#{planParamNo},#{planParamName},#{defaultValue},#{isHavePlanParamValue})")
    void add(PlanParam planParam);

    @Delete("delete from plan_param where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Delete("delete from plan_param where server_id = #{serverId}")
    void delByServerId(int serverId);
}
