package com.shengxuan.speed.entity.pojo;

import java.util.List;

public class ServerAndRegionAndSubRegionAndDevice {
    private String server;
    private boolean expanded = false;
    private List<RegionAndSubRegionAndDevice> RegionAndSubRegionAndDeviceList;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<RegionAndSubRegionAndDevice> getRegionAndSubRegionAndDeviceList() {
        return RegionAndSubRegionAndDeviceList;
    }

    public void setRegionAndSubRegionAndDeviceList(List<RegionAndSubRegionAndDevice> regionAndSubRegionAndDeviceList) {
        RegionAndSubRegionAndDeviceList = regionAndSubRegionAndDeviceList;
    }
}
