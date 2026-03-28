package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Phase;
import com.shengxuan.speed.entity.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {
    @Insert("insert into plan(server_id,device_id,plan_no,plan_name) " +
            "values(#{serverId},#{deviceId},#{planNo},#{planName})")
    void add(Plan plan);

    @Delete("delete from plan where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);

    @Select("select * from plan where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "planNo",column = "plan_no"),
            @Result(property = "planName",column = "plan_name")
    })
    List<Plan> findByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);
}
