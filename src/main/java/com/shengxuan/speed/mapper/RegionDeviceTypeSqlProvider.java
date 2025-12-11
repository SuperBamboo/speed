package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.RegionDeviceType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class RegionDeviceTypeSqlProvider {

    public String selectByRegionDeviceType(@Param("regionDevicetype") RegionDeviceType regionDevicetype){
        return new SQL(){{
            SELECT("*");
            FROM("region_devicetype");
            if(regionDevicetype.getServerId()!= -1){
                WHERE(" server_id = #{serverId}");
            }
            if(regionDevicetype.getRegion()!= null && !regionDevicetype.getRegion().isEmpty() && !"全部".equals(regionDevicetype.getRegion())){
                WHERE(" region = #{region}");
            }
            if(regionDevicetype.getSubregion()!= null && !regionDevicetype.getSubregion().isEmpty() && !"全部".equals(regionDevicetype.getSubregion())){
                WHERE(" subregion= #{subregion}");
            }
            if(regionDevicetype.getDeviceType()!= null && !regionDevicetype.getDeviceType().isEmpty() && !"全部".equals(regionDevicetype.getDeviceType())){
                WHERE(" device_type = #{deviceType}");
            }
        }}.toString();
    }
}
