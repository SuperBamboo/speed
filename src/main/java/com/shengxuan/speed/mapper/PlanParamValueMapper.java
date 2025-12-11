package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PlanParam;
import com.shengxuan.speed.entity.PlanParamValue;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanParamValueMapper {
    @Select("select * from plan_param_value where device_id = #{deviceId} and server_id = #{serverId}")
    List<PlanParamValue> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Select("select * from plan_param_value where device_id = #{deviceId} and plan_mode_no = #{planModeNo} and plan_param_no = #{planParamNo} and server_id = #{serverId}")
    @Results({
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "planModeNo",column = "plan_mode_no"),
            @Result(property = "planParamNo",column = "plan_param_no"),
            @Result(property = "planParamValueNo",column = "plan_param_value_no"),
            @Result(property = "planParamValueName",column = "plan_param_value_name")
    })
    List<PlanParamValue> findByDeviceIdAndPlanModeNoAndPlanParamNo(@Param("deviceId") String deviceId,@Param("planModeNo") String planModeNo,@Param("planParamNo") String planParamNo,@Param("serverId")int serverId);

    @Insert("insert into plan_param_value(device_id,server_id,plan_mode_no,plan_param_no,plan_param_value_no,plan_param_value_name) " +
            "values(#{deviceId},#{serverId},#{planModeNo},#{planParamNo},#{planParamValueNo},#{planParamValueName})")
    void add(PlanParamValue planParamValue);

    @Delete("delete from plan_param_value where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from plan_param_value where server_id = #{serverId}")
    void delByServerId(int serverId);
}
