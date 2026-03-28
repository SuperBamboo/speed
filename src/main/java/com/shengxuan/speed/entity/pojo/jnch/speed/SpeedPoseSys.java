package com.shengxuan.speed.entity.pojo.jnch.speed;

import java.io.Serializable;

public class SpeedPoseSys implements Serializable {
    private int czz;
    private int fx;
    private int lmd;
    private String qxjyz;

    private String str42;

    public String getStr42() {
        return str42;
    }

    public void setStr42(String str42) {
        this.str42 = str42;
    }

    public int getFx() {
        return fx;
    }

    public void setFx(int fx) {
        this.fx = fx;
    }

    public int getLmd() {
        return lmd;
    }

    public void setLmd(int lmd) {
        this.lmd = lmd;
    }

    public String getQxjyz() {
        return qxjyz;
    }

    public void setQxjyz(String qxjyz) {
        this.qxjyz = qxjyz;
    }

    public int getCzz() {
        return czz;
    }

    public void setCzz(int czz) {
        this.czz = czz;
    }
}
