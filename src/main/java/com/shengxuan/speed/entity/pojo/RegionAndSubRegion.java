package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;
import java.util.List;

public class RegionAndSubRegion implements Serializable {
    private String regionName;
    private List<String> subRegionNameList;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<String> getSubRegionNameList() {
        return subRegionNameList;
    }

    public void setSubRegionNameList(List<String> subRegionNameList) {
        this.subRegionNameList = subRegionNameList;
    }
}
