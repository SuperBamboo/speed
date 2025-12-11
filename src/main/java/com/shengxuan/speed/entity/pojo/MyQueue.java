package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

public class MyQueue implements Serializable {
    private String sessionId;
    private String seq;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public MyQueue() {
    }

    public MyQueue(String sessionId, String seq) {
        this.sessionId = sessionId;
        this.seq = seq;
    }
}
