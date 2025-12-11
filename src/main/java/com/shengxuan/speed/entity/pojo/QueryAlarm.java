package com.shengxuan.speed.entity.pojo;


import java.io.Serializable;

/**
 * 主动查询的报警信息实体类
 */
public class QueryAlarm implements Serializable {
    private String alarmno; //报警编号
    private String deviceid;    //设备编号
    private String devicename;  //设备名称
    private String ofregionname;    //所属区域
    private String ofsubregionname; //所属子区
    private String devicemodel; //规格型号
    private String supplier;    //供应商
    private String longitude;   //经度
    private String dimension;   //维度
    private String occurdate;   //发生日期
    private String occurtime;   //发生时间
    private String alarmtype;   //报警类型
    private String alarmdesc;   //报警描述

    public QueryAlarm() {
    }

    public String getAlarmno() {
        return alarmno;
    }

    public void setAlarmno(String alarmno) {
        this.alarmno = alarmno;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getOfregionname() {
        return ofregionname;
    }

    public void setOfregionname(String ofregionname) {
        this.ofregionname = ofregionname;
    }

    public String getOfsubregionname() {
        return ofsubregionname;
    }

    public void setOfsubregionname(String ofsubregionname) {
        this.ofsubregionname = ofsubregionname;
    }

    public String getDevicemodel() {
        return devicemodel;
    }

    public void setDevicemodel(String devicemodel) {
        this.devicemodel = devicemodel;
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

    public String getOccurdate() {
        return occurdate;
    }

    public void setOccurdate(String occurdate) {
        this.occurdate = occurdate;
    }

    public String getOccurtime() {
        return occurtime;
    }

    public void setOccurtime(String occurtime) {
        this.occurtime = occurtime;
    }

    public String getAlarmtype() {
        return alarmtype;
    }

    public void setAlarmtype(String alarmtype) {
        this.alarmtype = alarmtype;
    }

    public String getAlarmdesc() {
        return alarmdesc;
    }

    public void setAlarmdesc(String alarmdesc) {
        this.alarmdesc = alarmdesc;
    }
}
