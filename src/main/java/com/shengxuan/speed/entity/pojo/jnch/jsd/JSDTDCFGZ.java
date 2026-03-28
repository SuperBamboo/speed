package com.shengxuan.speed.entity.pojo.jnch.jsd;

import java.io.Serializable;
import java.util.List;

public class JSDTDCFGZ implements Serializable {
    private List<Boolean> tjList;
    private List<Boolean> cfList;
    private List<Boolean> hfList;
    private int sczt;
    private String zdzxsj;
    private String hfycsj;

    public List<Boolean> getTjList() {
        return tjList;
    }

    public void setTjList(List<Boolean> tjList) {
        this.tjList = tjList;
    }

    public List<Boolean> getCfList() {
        return cfList;
    }

    public void setCfList(List<Boolean> cfList) {
        this.cfList = cfList;
    }

    public List<Boolean> getHfList() {
        return hfList;
    }

    public void setHfList(List<Boolean> hfList) {
        this.hfList = hfList;
    }

    public int getSczt() {
        return sczt;
    }

    public void setSczt(int sczt) {
        this.sczt = sczt;
    }

    public String getZdzxsj() {
        return zdzxsj;
    }

    public void setZdzxsj(String zdzxsj) {
        this.zdzxsj = zdzxsj;
    }

    public String getHfycsj() {
        return hfycsj;
    }

    public void setHfycsj(String hfycsj) {
        this.hfycsj = hfycsj;
    }
}
