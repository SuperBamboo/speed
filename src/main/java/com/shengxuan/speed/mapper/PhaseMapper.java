package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.LampGroup;
import com.shengxuan.speed.entity.Phase;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhaseMapper {
    @Insert("insert into phase(server_id,device_id,phase_no,phase_name) " +
            "values(#{serverId},#{deviceId},#{phaseNo},#{phaseName})")
    void add(Phase phase);

    @Delete("delete from phase where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);

    @Select("select * from phase where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "phaseNo",column = "phase_no"),
            @Result(property = "phaseName",column = "phase_name")
    })
    List<Phase> findByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);
}
