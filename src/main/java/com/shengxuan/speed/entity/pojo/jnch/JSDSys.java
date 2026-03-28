package com.shengxuan.speed.entity.pojo.jnch;

import com.shengxuan.speed.entity.pojo.jnch.jsd.*;

import java.io.Serializable;
import java.util.List;

public class JSDSys implements Serializable {

    private JSDWorkTypeSys jsdWorkTypeSys;

    private List<JSDZDYSDSys> jsdzdysdSysList;

    private List<JSDSDFASys> jsdsdfaSysList;
    private List<JSDSDFASys> jsdsdfaSysList1;

    private List<JSDTSR> jsdtsrList;

    private List<JSDTDCFGZ> jsdtdcfgzList;

    private List<JSDVoiceSys> jsdVoiceSysList;

    private List<JSDZDYSDCFGZ> jsdzdysdcfgzList;

    private JSDWebNetSys jsdWebNetSys;

    private JSDPowerSys jsdPowerSys;

    private JSDPoseSys jsdPoseSys;

    private JSDOtherSys jsdOtherSys;

    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public JSDOtherSys getJsdOtherSys() {
        return jsdOtherSys;
    }

    public void setJsdOtherSys(JSDOtherSys jsdOtherSys) {
        this.jsdOtherSys = jsdOtherSys;
    }

    public JSDPoseSys getJsdPoseSys() {
        return jsdPoseSys;
    }

    public void setJsdPoseSys(JSDPoseSys jsdPoseSys) {
        this.jsdPoseSys = jsdPoseSys;
    }

    public JSDPowerSys getJsdPowerSys() {
        return jsdPowerSys;
    }

    public void setJsdPowerSys(JSDPowerSys jsdPowerSys) {
        this.jsdPowerSys = jsdPowerSys;
    }

    public JSDWebNetSys getJsdWebNetSys() {
        return jsdWebNetSys;
    }

    public void setJsdWebNetSys(JSDWebNetSys jsdWebNetSys) {
        this.jsdWebNetSys = jsdWebNetSys;
    }

    public List<JSDZDYSDCFGZ> getJsdzdysdcfgzList() {
        return jsdzdysdcfgzList;
    }

    public void setJsdzdysdcfgzList(List<JSDZDYSDCFGZ> jsdzdysdcfgzList) {
        this.jsdzdysdcfgzList = jsdzdysdcfgzList;
    }

    public List<JSDVoiceSys> getJsdVoiceSysList() {
        return jsdVoiceSysList;
    }

    public void setJsdVoiceSysList(List<JSDVoiceSys> jsdVoiceSysList) {
        this.jsdVoiceSysList = jsdVoiceSysList;
    }

    public List<JSDTDCFGZ> getJsdtdcfgzList() {
        return jsdtdcfgzList;
    }

    public void setJsdtdcfgzList(List<JSDTDCFGZ> jsdtdcfgzList) {
        this.jsdtdcfgzList = jsdtdcfgzList;
    }

    public List<JSDTSR> getJsdtsrList() {
        return jsdtsrList;
    }

    public void setJsdtsrList(List<JSDTSR> jsdtsrList) {
        this.jsdtsrList = jsdtsrList;
    }

    public List<JSDSDFASys> getJsdsdfaSysList() {
        return jsdsdfaSysList;
    }

    public void setJsdsdfaSysList(List<JSDSDFASys> jsdsdfaSysList) {
        this.jsdsdfaSysList = jsdsdfaSysList;
    }

    public List<JSDSDFASys> getJsdsdfaSysList1() {
        return jsdsdfaSysList1;
    }

    public void setJsdsdfaSysList1(List<JSDSDFASys> jsdsdfaSysList1) {
        this.jsdsdfaSysList1 = jsdsdfaSysList1;
    }

    public List<JSDZDYSDSys> getJsdzdysdSysList() {
        return jsdzdysdSysList;
    }

    public void setJsdzdysdSysList(List<JSDZDYSDSys> jsdzdysdSysList) {
        this.jsdzdysdSysList = jsdzdysdSysList;
    }

    public JSDWorkTypeSys getJsdWorkTypeSys() {
        return jsdWorkTypeSys;
    }

    public void setJsdWorkTypeSys(JSDWorkTypeSys jsdWorkTypeSys) {
        this.jsdWorkTypeSys = jsdWorkTypeSys;
    }
}
