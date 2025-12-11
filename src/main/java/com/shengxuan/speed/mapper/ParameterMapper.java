package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Parameter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ParameterMapper {
    @Select("select * from parameter where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="parameterNo",column = "parameter_no"),
            @Result(property ="parameterName",column = "parameter_name"),
            @Result(property ="parameterType",column = "parameter_type"),
            @Result(property ="parameterValue",column = "parameter_value")
    })
    List<Parameter> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Insert("insert into parameter(device_id,server_id,parameter_no,parameter_name,parameter_type,parameter_value) " +
            "values(#{deviceId},#{serverId},#{parameterNo},#{parameterName},#{parameterType},#{parameterValue})")
    void add(Parameter parameter);

    @Delete("delete from parameter where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Delete("delete from parameter where server_id = #{serverId}")
    void delByServerId(int serverId);
}
