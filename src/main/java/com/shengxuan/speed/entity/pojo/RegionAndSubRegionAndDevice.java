package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;
import java.util.List;

public class RegionAndSubRegionAndDevice implements Serializable {
    private String region;

    private boolean expanded = false;

    private List<SubRegionAndDevice> subRegionAndDeviceList;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<SubRegionAndDevice> getSubRegionAndDeviceList() {
        return subRegionAndDeviceList;
    }

    public void setSubRegionAndDeviceList(List<SubRegionAndDevice> subRegionAndDeviceList) {
        this.subRegionAndDeviceList = subRegionAndDeviceList;
    }
}
