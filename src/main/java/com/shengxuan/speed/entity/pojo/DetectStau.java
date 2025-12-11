package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

public class DetectStau implements Serializable {
    private String detectId;
    private String detectName;
    private String detectValue;
    private String abnormalStau;

    public String getDetectId() {
        return detectId;
    }

    public void setDetectId(String detectId) {
        this.detectId = detectId;
    }

    public String getDetectName() {
        return detectName;
    }

    public void setDetectName(String detectName) {
        this.detectName = detectName;
    }

    public String getDetectValue() {
        return detectValue;
    }

    public void setDetectValue(String detectValue) {
        this.detectValue = detectValue;
    }

    public String getAbnormalStau() {
        return abnormalStau;
    }

    public void setAbnormalStau(String abnormalStau) {
        this.abnormalStau = abnormalStau;
    }

    @Override
    public String toString() {
        return "DetectStau{" +
                "detectId='" + detectId + '\'' +
                ", detectName='" + detectName + '\'' +
                ", detectValue='" + detectValue + '\'' +
                ", abnormalStau='" + abnormalStau + '\'' +
                '}';
    }
}
