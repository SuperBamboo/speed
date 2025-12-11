package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Port;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PortMapper {
    @Select("select * from port where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="portNo",column = "port_no"),
            @Result(property ="portName",column = "port_name")
    })
    List<Port> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Insert("insert into port(device_id,server_id,port_no,port_name) values(#{deviceId},#{serverId},#{portNo},#{portName})")
    void add(Port port);

    @Delete("delete from port where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Delete("delete from port where server_id = #{serverId}")
    void delByServerId(int serverId);
}
