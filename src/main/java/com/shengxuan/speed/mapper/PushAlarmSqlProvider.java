package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PushAlarm;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class PushAlarmSqlProvider {

    public String selectByPushAlarm(@Param("pushAlarm") PushAlarm pushAlarm, @Param("deviceIds")List<String> deviceIds){
        StringBuilder afterWhere = new StringBuilder();
        if(deviceIds!=null && deviceIds.size()>0){
            afterWhere.append("device_id IN(");
            for (int i = 0; i < deviceIds.size(); i++) {
                if( i == 0 ){
                    afterWhere.append(deviceIds.get(i));
                } else {
                    afterWhere.append(","+deviceIds.get(i));
                }
            }
            afterWhere.append(")");
        }else {
            afterWhere.append("1=2");
        }
        String role = afterWhere.toString();
        String string = new SQL() {{
            SELECT("*");
            FROM("pushalarm");
            if (role != null && !"".equals(role)) {
                WHERE(role);
            }
            if (pushAlarm.getDeviceId() != null && !pushAlarm.getDeviceId().isEmpty()) {
                WHERE("device_id = #{pushAlarm.deviceId}");
            }
            if (pushAlarm.getServerId() != -1) {
                WHERE("server_id = #{pushAlarm.serverId}");
            }
            if (pushAlarm.getDeviceName() != null && !pushAlarm.getDeviceName().isEmpty()) {
                WHERE("device_name like concat('%', #{pushAlarm.deviceName},'%')");
            }
            if (pushAlarm.getOfRegionName() != null && !pushAlarm.getOfRegionName().isEmpty() && !"全部".equals(pushAlarm.getOfRegionName())) {
                WHERE(" of_region_name = #{pushAlarm.ofRegionName}");
            }
            if (pushAlarm.getOfSubregionName() != null && !pushAlarm.getOfSubregionName().isEmpty() && !"全部".equals(pushAlarm.getOfSubregionName())) {
                WHERE(" of_subregion_name = #{pushAlarm.ofSubregionName}");
            }
            if (pushAlarm.getOccurDate() != null && !pushAlarm.getOccurDate().isEmpty()) {
                WHERE(" occur_date >= #{pushAlarm.occurDate}");
            }
            ORDER_BY("id desc");
            /*if(device.getDeviceModel()!= null && !device.getDeviceModel().isEmpty()){
                WHERE(" device_model = #{device.deviceModel}");
            }*/
        }}.toString();
        return string;
    }

    public String selectByPushAlarm1(@Param("pushAlarm") PushAlarm pushAlarm, @Param("serverDeviceList")List<ServerDevice> serverDeviceList){
        StringBuilder afterWhere = new StringBuilder();
        if(serverDeviceList!=null && serverDeviceList.size()>0){
            afterWhere.append("(");
            for (int i = 0; i < serverDeviceList.size(); i++) {
                ServerDevice serverDevice = serverDeviceList.get(i);
                if( i == 0 ){
                    afterWhere.append("(device_id = ").append(serverDevice.getDeviceId())
                            .append(" AND server_id = ").append(serverDevice.getServerId()).append(")");
                } else {
                    afterWhere.append(" OR (device_id = ").append(serverDevice.getDeviceId())
                            .append(" AND server_id = ").append(serverDevice.getServerId()).append(")");
                }
            }
            afterWhere.append(")");
        }else {
            afterWhere.append("1=2");
        }
        String role = afterWhere.toString();
        String string = new SQL() {{
            SELECT("*");
            FROM("pushalarm");
            if (role != null && !"".equals(role)) {
                WHERE(role);
            }
            if (pushAlarm.getDeviceId() != null && !pushAlarm.getDeviceId().isEmpty()) {
                WHERE("device_id = #{pushAlarm.deviceId}");
            }
            if (pushAlarm.getServerId() != -1) {
                WHERE("server_id = #{pushAlarm.serverId}");
            }
            if (pushAlarm.getDeviceName() != null && !pushAlarm.getDeviceName().isEmpty()) {
                WHERE("device_name like concat('%', #{pushAlarm.deviceName},'%')");
            }
            if (pushAlarm.getOfRegionName() != null && !pushAlarm.getOfRegionName().isEmpty() && !"全部".equals(pushAlarm.getOfRegionName())) {
                WHERE(" of_region_name = #{pushAlarm.ofRegionName}");
            }
            if (pushAlarm.getOfSubregionName() != null && !pushAlarm.getOfSubregionName().isEmpty() && !"全部".equals(pushAlarm.getOfSubregionName())) {
                WHERE(" of_subregion_name = #{pushAlarm.ofSubregionName}");
            }
            if (pushAlarm.getOccurDate() != null && !pushAlarm.getOccurDate().isEmpty()) {
                WHERE(" occur_date >= #{pushAlarm.occurDate}");
            }
            if (pushAlarm.getAlarmDesc() != null && !pushAlarm.getAlarmDesc().isEmpty()) {
                WHERE(" occur_date <= #{pushAlarm.alarmDesc}");
            }
            ORDER_BY("id desc");
        }}.toString();
        return string;
    }
}
