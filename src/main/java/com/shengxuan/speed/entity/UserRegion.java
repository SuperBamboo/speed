package com.shengxuan.speed.entity;

import java.io.Serializable;

public class UserRegion implements Serializable {
    private Integer uid;
    private Integer rid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}
