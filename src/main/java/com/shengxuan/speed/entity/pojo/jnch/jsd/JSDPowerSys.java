package com.shengxuan.speed.entity.pojo.jnch.jsd;

import java.io.Serializable;
import java.util.List;

public class JSDPowerSys implements Serializable {
    private String xdcdymdz;
    private String tyndymdz;
    private int cdkz;
    private int dylx;

    public int getDylx() {
        return dylx;
    }

    public void setDylx(int dylx) {
        this.dylx = dylx;
    }

    private List<JSDPowerDY> jsdPowerDYList;

    public List<JSDPowerDY> getJsdPowerDYList() {
        return jsdPowerDYList;
    }

    public void setJsdPowerDYList(List<JSDPowerDY> jsdPowerDYList) {
        this.jsdPowerDYList = jsdPowerDYList;
    }

    public String getXdcdymdz() {
        return xdcdymdz;
    }

    public void setXdcdymdz(String xdcdymdz) {
        this.xdcdymdz = xdcdymdz;
    }

    public String getTyndymdz() {
        return tyndymdz;
    }

    public void setTyndymdz(String tyndymdz) {
        this.tyndymdz = tyndymdz;
    }

    public int getCdkz() {
        return cdkz;
    }

    public void setCdkz(int cdkz) {
        this.cdkz = cdkz;
    }


}
