package com.shengxuan.speed.entity;

import java.io.Serializable;

/**
 * 设备实体类
 */
public class Device implements Serializable {

    private String deviceId;    //设备编号
    private String deviceName;  //设备名称
    private String ofRegionId;  //所属区域
    private String ofSubRegionId;   //所属子区
    private String deviceType;  //设备类型
    private String deviceModel; //规格型号
    private String deviceAttribute; //设备属性  0.退出监视 1.设备 2.信号灯
    private String supplier;    //供应商
    private String serviceDate; //投用日期  YYYYMMMDD年年年年月月日日
    private String longitude;   //经度
    private String dimension;   //纬度
    private String flag = "false";    //数据是否被查询标示

    private int serverId;

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

    public String getOfRegionId() {
        return ofRegionId;
    }

    public void setOfRegionId(String ofRegionId) {
        this.ofRegionId = ofRegionId;
    }

    public String getOfSubRegionId() {
        return ofSubRegionId;
    }

    public void setOfSubRegionId(String ofSubRegionId) {
        this.ofSubRegionId = ofSubRegionId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceAttribute() {
        return deviceAttribute;
    }

    public void setDeviceAttribute(String deviceAttribute) {
        this.deviceAttribute = deviceAttribute;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", ofRegionId='" + ofRegionId + '\'' +
                ", ofSubRegionId='" + ofSubRegionId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceAttribute='" + deviceAttribute + '\'' +
                ", supplier='" + supplier + '\'' +
                ", serviceDate='" + serviceDate + '\'' +
                ", longitude='" + longitude + '\'' +
                ", dimension='" + dimension + '\'' +
                ", flag='" + flag + '\'' +
                ", serverId=" + serverId +
                ", server=" + server +
                '}';
    }
}
