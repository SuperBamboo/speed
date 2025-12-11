package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.RegionDeviceType;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface RegionDeviceTypeMapper {

    @Select("select * from region_devicetype")
    @Results({
            @Result(property ="rid",column = "rid"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="region",column = "region"),
            @Result(property ="subregion",column = "subregion"),
            @Result(property ="deviceType",column = "device_type")
    })
    List<RegionDeviceType> findAll();

    @Select("select * from region_devicetype where server_id = #{serverId}")
    @Results({
            @Result(property ="rid",column = "rid"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="region",column = "region"),
            @Result(property ="subregion",column = "subregion"),
            @Result(property ="deviceType",column = "device_type")
    })
    List<RegionDeviceType> findAllByServerId(int serverId);

    @Select("select * from region_devicetype where rid = #{rid}")
    @Results({
            @Result(property ="rid",column = "rid"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="region",column = "region"),
            @Result(property ="subregion",column = "subregion"),
            @Result(property ="deviceType",column = "device_type")
    })
    RegionDeviceType findByRid(int rid);

    @Insert("insert into region_devicetype(server_id,region,subregion,device_type) " +
            "values(#{regionDevicetype.serverId},#{regionDevicetype.region},#{regionDevicetype.subregion},#{regionDevicetype.deviceType})")
    void add(@Param("regionDevicetype") RegionDeviceType regionDevicetype);

    @SelectProvider(type = RegionDeviceTypeSqlProvider.class,method = "selectByRegionDeviceType")
    @Results({
            @Result(property ="rid",column = "rid"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="region",column = "region"),
            @Result(property ="subregion",column = "subregion"),
            @Result(property ="deviceType",column = "device_type")
    })
    List<RegionDeviceType> findByRegionDeviceType(RegionDeviceType regionDevicetype);

    @Delete("delete from region_devicetype where server_id = #{regionDevicetype.serverId} and region = #{regionDevicetype.region} and subregion = #{regionDevicetype.subregion} and device_type = #{regionDevicetype.deviceType}")
    void delByRegionDeviceType(@Param("regionDevicetype") RegionDeviceType regionDevicetype);
}
