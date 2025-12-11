package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.RegionDeviceType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class DeviceSqlProvider {

    public String selectByDevice(@Param("device") Device device, List<RegionDeviceType> regionDeviceTypeList){
        StringBuilder afterWhere = new StringBuilder();
        if(regionDeviceTypeList!=null && regionDeviceTypeList.size()>0){
            afterWhere.append("(");
            for (int i = 0; i < regionDeviceTypeList.size(); i++) {
                if( i == 0 ){
                    afterWhere.append("(server_id = '"+regionDeviceTypeList.get(i).getServerId()+"' AND of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                } else {
                    afterWhere.append(" OR (server_id = '"+regionDeviceTypeList.get(i).getServerId()+"' AND of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                }
            }
            afterWhere.append(")");
        }else {
            afterWhere.append("1=2");
        }
        String role = afterWhere.toString();
        return new SQL(){{
            SELECT("*");
            FROM("device");
            if(role!=null && !"".equals(role)){
                WHERE(role);
            }
            if(device.getDeviceName() != null && !device.getDeviceName().isEmpty()){
                WHERE("device_name like concat('%', #{device.deviceName},'%')");
            }
            if(device.getServerId()!= -1){
                //用户选定的区域
                WHERE(" server_id = #{device.serverId}");
            }
            if(device.getOfRegionId()!= null && !device.getOfRegionId().isEmpty() && !"全部".equals(device.getOfRegionId())){
                //用户选定的区域
                WHERE(" of_region_id = #{device.ofRegionId}");
            }
            if(device.getOfSubRegionId()!= null && !device.getOfSubRegionId().isEmpty() && !"全部".equals(device.getOfSubRegionId())){
                WHERE(" of_subRegion_id = #{device.ofSubRegionId}");
            }
            if(device.getDeviceType()!= null && !device.getDeviceType().isEmpty() &&!"全部".equals(device.getDeviceType())){
                WHERE(" device_type = #{device.deviceType}");

            }
            ORDER_BY("CAST(device_id AS SIGNED) ASC");
        }}.toString();
    }

    public String selectDeviceIdsByDevice(List<RegionDeviceType> regionDeviceTypeList){
        StringBuilder afterWhere = new StringBuilder();
        if(regionDeviceTypeList!=null && regionDeviceTypeList.size()>0){
            afterWhere.append("(");
            for (int i = 0; i < regionDeviceTypeList.size(); i++) {
                if( i == 0 ){
                    afterWhere.append("(of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                } else {
                    afterWhere.append(" OR (of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                }
            }
            afterWhere.append(")");
        }else {
            afterWhere.append("1=2");
        }
        String role = afterWhere.toString();
        return new SQL(){{
            SELECT("device_id");
            FROM("device");
            if(role!=null && !"".equals(role)){
                WHERE(role);
            }
            ORDER_BY("CAST(device_id AS SIGNED) ASC");
        }}.toString();
    }

    public String selectServerDeviceIdsByDevice(List<RegionDeviceType> regionDeviceTypeList){
        StringBuilder afterWhere = new StringBuilder();
        if(regionDeviceTypeList!=null && regionDeviceTypeList.size()>0){
            afterWhere.append("(");
            for (int i = 0; i < regionDeviceTypeList.size(); i++) {
                if( i == 0 ){
                    afterWhere.append("(server_id = '"+regionDeviceTypeList.get(i).getServerId()+"'AND of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                } else {
                    afterWhere.append(" OR (server_id = '"+regionDeviceTypeList.get(i).getServerId()+"' AND of_region_id = '"+regionDeviceTypeList.get(i).getRegion()+"' AND of_subregion_id = '"+regionDeviceTypeList.get(i).getSubregion()+"' AND device_type = '"+regionDeviceTypeList.get(i).getDeviceType()+"')" );
                }
            }
            afterWhere.append(")");
        }else {
            afterWhere.append("1=2");
        }
        String role = afterWhere.toString();
        return new SQL(){{
            SELECT("server_id,device_id");
            FROM("device");
            if(role!=null && !"".equals(role)){
                WHERE(role);
            }
            ORDER_BY("CAST(server_id AS SIGNED) ASC");
        }}.toString();
    }
}
