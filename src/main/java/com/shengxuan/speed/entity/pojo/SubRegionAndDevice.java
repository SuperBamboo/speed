package com.shengxuan.speed.entity.pojo;

import com.shengxuan.speed.entity.Device;

import java.io.Serializable;
import java.util.List;

public class SubRegionAndDevice implements Serializable {
    private String subRegion;

    private boolean expanded = false;

    private List<Device> deviceList;

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
