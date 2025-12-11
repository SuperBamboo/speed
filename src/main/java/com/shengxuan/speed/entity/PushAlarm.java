package com.shengxuan.speed.entity;

import java.io.Serializable;

/**
 * @author Yunzhu_Chen
 * 主动推送的报警信息实体类
 */
public class PushAlarm implements Serializable {
    private int id;
    private String alarmNo; //报警编号
    private String deviceId;    //设备编号

    private int serverId;
    private String deviceName;  //设备名称
    private String ofRegionName;    //所属区域
    private String ofSubregionName; //所属子区
    private String deviceModel; //规格型号
    private String supplier;    //供应商
    private String longitude;   //经度
    private String dimension;   //维度
    private String occurDate;   //发生日期
    private String occurTime;   //发生时间
    private String alarmType;   //报警类型
    private String alarmDesc;   //报警描述
    private String isCheck = "false";     //用户是否查看过该报警信息

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public PushAlarm() {
    }

    public PushAlarm(String alarmNo, String deviceId, String deviceName, String ofRegionName, String ofSubregionName, String deviceModel, String supplier, String longitude, String dimension, String occurDate, String occurTime, String alarmType, String alarmDesc, String isCheck) {
        this.alarmNo = alarmNo;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.ofRegionName = ofRegionName;
        this.ofSubregionName = ofSubregionName;
        this.deviceModel = deviceModel;
        this.supplier = supplier;
        this.longitude = longitude;
        this.dimension = dimension;
        this.occurDate = occurDate;
        this.occurTime = occurTime;
        this.alarmType = alarmType;
        this.alarmDesc = alarmDesc;
        this.isCheck = isCheck;
    }

    public String getAlarmNo() {
        return alarmNo;
    }

    public void setAlarmNo(String alarmNo) {
        this.alarmNo = alarmNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOfRegionName() {
        return ofRegionName;
    }

    public void setOfRegionName(String ofRegionName) {
        this.ofRegionName = ofRegionName;
    }

    public String getOfSubregionName() {
        return ofSubregionName;
    }

    public void setOfSubregionName(String ofSubregionName) {
        this.ofSubregionName = ofSubregionName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmDesc() {
        return alarmDesc;
    }

    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }


}
