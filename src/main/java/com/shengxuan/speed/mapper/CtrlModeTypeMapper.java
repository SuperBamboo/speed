package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.CtrlModeType;
import com.shengxuan.speed.entity.Device;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface CtrlModeTypeMapper {
    @Insert("insert into ctrl_mode_type(server_id,device_id,ctrl_mode_type_no,ctrl_mode_type_name) " +
            "values(#{serverId},#{deviceId},#{ctrlModeTypeNo},#{ctrlModeTypeName})")
    void add(CtrlModeType ctrlModeType);

    @Delete("delete from ctrl_mode_type where device_id = #{deviceId} and server_id = #{serverId}")
    void del(@Param("deviceId") String deviceId, @Param("serverId") int serverId);

    @Select("select * from ctrl_mode_type where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "ctrlModeTypeNo",column = "ctrl_mode_type_no"),
            @Result(property = "ctrlModeTypeName",column = "ctrl_mode_type_name")
    })
    List<CtrlModeType> findByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);
}
