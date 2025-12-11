package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Lamp;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface WarningToneMapper {
    @Select("select * from warning_tone where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="warningToneNo",column = "warning_tone_no"),
            @Result(property ="warningToneName",column = "warning_tone_name"),
            @Result(property ="warningToneLen",column = "warning_tone_len")
    })
    List<WarningTone> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Insert("insert into warning_tone(device_id,server_id,warning_tone_no,warning_tone_name,warning_tone_len) values(#{deviceId},#{serverId},#{warningToneNo},#{warningToneName},#{warningToneLen})")
    void add(WarningTone warningTone);

    @Delete("delete from warning_tone where device_id = #{deviceId} and server_id = #{serverId}")
    void delByDeviceId(@Param("deviceId") String deviceId, @Param("serverId") int serverId);

    @Delete("delete from warning_tone where server_id = #{serverId}")
    void delByServerId(int serverId);
}
