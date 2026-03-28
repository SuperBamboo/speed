package com.shengxuan.speed.entity.pojo.jnch;

import com.shengxuan.speed.entity.pojo.jnch.speed.*;

import java.io.Serializable;
import java.util.List;

public class SpeedSys implements Serializable {

    private String parameter;
    private SpeedWebNetSys speedWebNetSys;

    private SpeedAndTJSys speedAndTJSys;

    private SpeedPowerSys speedPowerSys;

    private SpeedVoiceSys speedVoiceSys;

    private SpeedPoseSys speedPoseSys;


   private List<SpeedDisPlayFA> disPlayFAList;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public List<SpeedDisPlayFA> getDisPlayFAList() {
        return disPlayFAList;
    }

    public void setDisPlayFAList(List<SpeedDisPlayFA> disPlayFAList) {
        this.disPlayFAList = disPlayFAList;
    }

    public SpeedPoseSys getSpeedPoseSys() {
        return speedPoseSys;
    }

    public void setSpeedPoseSys(SpeedPoseSys speedPoseSys) {
        this.speedPoseSys = speedPoseSys;
    }

    public SpeedVoiceSys getSpeedVoiceSys() {
        return speedVoiceSys;
    }

    public void setSpeedVoiceSys(SpeedVoiceSys speedVoiceSys) {
        this.speedVoiceSys = speedVoiceSys;
    }

    public SpeedPowerSys getSpeedPowerSys() {
        return speedPowerSys;
    }

    public void setSpeedPowerSys(SpeedPowerSys speedPowerSys) {
        this.speedPowerSys = speedPowerSys;
    }

    public SpeedAndTJSys getSpeedAndTJSys() {
        return speedAndTJSys;
    }

    public void setSpeedAndTJSys(SpeedAndTJSys speedAndTJSys) {
        this.speedAndTJSys = speedAndTJSys;
    }

    public SpeedWebNetSys getSpeedWebNetSys() {
        return speedWebNetSys;
    }

    public void setSpeedWebNetSys(SpeedWebNetSys speedWebNetSys) {
        this.speedWebNetSys = speedWebNetSys;
    }
}
