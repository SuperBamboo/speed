package com.shengxuan.speed.entity.pojo;


import com.shengxuan.speed.entity.PlanParamValue;

import java.io.Serializable;
import java.util.List;

/**
 * 带planParamValue的planParam
 */
public class PlanParam1 implements Serializable {
    private String deviceId;
    private String planModeNo;
    private String planParamNo;
    private String planParamName;
    private String defaultValue;
    private boolean isHavePlanParamValue;
    private List<PlanParamValue> planParamValueList;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlanModeNo() {
        return planModeNo;
    }

    public void setPlanModeNo(String planModeNo) {
        this.planModeNo = planModeNo;
    }

    public String getPlanParamNo() {
        return planParamNo;
    }

    public void setPlanParamNo(String planParamNo) {
        this.planParamNo = planParamNo;
    }

    public String getPlanParamName() {
        return planParamName;
    }

    public void setPlanParamName(String planParamName) {
        this.planParamName = planParamName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isHavePlanParamValue() {
        return isHavePlanParamValue;
    }

    public void setHavePlanParamValue(boolean havePlanParamValue) {
        isHavePlanParamValue = havePlanParamValue;
    }

    public List<PlanParamValue> getPlanParamValueList() {
        return planParamValueList;
    }

    public void setPlanParamValueList(List<PlanParamValue> planParamValueList) {
        this.planParamValueList = planParamValueList;
    }
}
