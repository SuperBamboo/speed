package com.shengxuan.speed.entity.pojo.jnch.jsd;

import java.io.Serializable;
import java.util.List;

public class JSDVoiceSys implements Serializable {
    private List<Boolean> tjList;
    private List<Boolean> cfjList;
    private List<Boolean> hfList;

    private int bfyj;
    private String bbjg;
    private String bbcs;

    public List<Boolean> getTjList() {
        return tjList;
    }

    public void setTjList(List<Boolean> tjList) {
        this.tjList = tjList;
    }

    public List<Boolean> getCfjList() {
        return cfjList;
    }

    public void setCfjList(List<Boolean> cfjList) {
        this.cfjList = cfjList;
    }

    public List<Boolean> getHfList() {
        return hfList;
    }

    public void setHfList(List<Boolean> hfList) {
        this.hfList = hfList;
    }

    public int getBfyj() {
        return bfyj;
    }

    public void setBfyj(int bfyj) {
        this.bfyj = bfyj;
    }

    public String getBbjg() {
        return bbjg;
    }

    public void setBbjg(String bbjg) {
        this.bbjg = bbjg;
    }

    public String getBbcs() {
        return bbcs;
    }

    public void setBbcs(String bbcs) {
        this.bbcs = bbcs;
    }
}
