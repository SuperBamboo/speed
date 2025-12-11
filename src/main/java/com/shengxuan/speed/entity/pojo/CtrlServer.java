package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

/**
 * 可远程控制设备信息
 */
public class CtrlServer implements Serializable {

    /**
     * 服务器ip
     */
    private String ServerIP;

    /**
     * String格式服务器端口号
     */
    private int ServerPort1;


    /**
     * Byte格式服务器端口号
     */
    private int ServerPort2;


    /**
     * 控制类别
     * 1.移动信号灯
     * 2.可变车道
     * 3.显示屏
     * 4.警示柱
     * 5.HD204信号机
     * 6.HD207信号机
     * 7.HD2016信号机
     */
    private int CtrlType;


    /**
     * 设备在服务器的ID
     */
    private int CtrlID;

    public String getServerIP() {
        return ServerIP;
    }

    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }

    public int getServerPort1() {
        return ServerPort1;
    }

    public void setServerPort1(int serverPort1) {
        ServerPort1 = serverPort1;
    }

    public int getServerPort2() {
        return ServerPort2;
    }

    public void setServerPort2(int serverPort2) {
        ServerPort2 = serverPort2;
    }

    public int getCtrlType() {
        return CtrlType;
    }

    public void setCtrlType(int ctrlType) {
        CtrlType = ctrlType;
    }

    public int getCtrlID() {
        return CtrlID;
    }

    public void setCtrlID(int ctrlID) {
        CtrlID = ctrlID;
    }

    @Override
    public String toString() {
        return "CtrlServer{" +
                "ServerIP='" + ServerIP + '\'' +
                ", ServerPort1=" + ServerPort1 +
                ", ServerPort2=" + ServerPort2 +
                ", CtrlType=" + CtrlType +
                ", CtrlID=" + CtrlID +
                '}';
    }
}
