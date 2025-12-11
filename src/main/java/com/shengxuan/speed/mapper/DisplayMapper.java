package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Display;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DisplayMapper {
    @Select("select * from display where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "serverId",column = "server_id"),
            @Result(property = "displayNo",column = "display_no"),
            @Result(property = "displayName",column = "display_name"),
            @Result(property = "displayColor",column = "display_color")
    })
    List<Display> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Insert("insert into display(device_id,server_id,display_no,display_name,display_color) values(#{deviceId},#{serverId},#{displayNo},#{displayName},#{displayColor})")
    void add(Display display);

    @Delete("delete from display where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Delete("delete from display where server_id = #{serverId}")
    void delByServerId(int serverId);
}
