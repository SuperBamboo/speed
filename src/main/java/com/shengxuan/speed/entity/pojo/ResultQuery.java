package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

public class ResultQuery implements Serializable {
    private int type;//-1就是有错误
    private String message;

    public ResultQuery() {
    }

    public ResultQuery(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
