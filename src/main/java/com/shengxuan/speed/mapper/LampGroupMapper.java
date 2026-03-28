package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.LampGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LampGroupMapper {
    @Insert("insert into lamp_group(server_id,device_id,lamp_group_no,lamp_group_name) " +
            "values(#{serverId},#{deviceId},#{lampGroupNo},#{lampGroupName})")
    void add(LampGroup lampGroup);

    @Delete("delete from lamp_group where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);

    @Select("select * from lamp_group where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "lampGroupNo",column = "lamp_group_no"),
            @Result(property = "lampGroupName",column = "lamp_group_name")
    })
    List<LampGroup> findByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);
}
