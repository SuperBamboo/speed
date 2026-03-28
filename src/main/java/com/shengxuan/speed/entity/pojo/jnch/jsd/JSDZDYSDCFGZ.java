package com.shengxuan.speed.entity.pojo.jnch.jsd;

import java.io.Serializable;
import java.util.List;

public class JSDZDYSDCFGZ implements Serializable {
    private List<Boolean> tjList;
    private List<Boolean> cfList;
    private List<Boolean> hfList;

    private String qsjd;
    private String jdsl;
    private String tbzq;
    private String tbxwc;
    private int bfyj;
    private String bbjg;
    private String bbcs;

    public List<Boolean> getTjList() {
        return tjList;
    }

    public void setTjList(List<Boolean> tjList) {
        this.tjList = tjList;
    }

    public List<Boolean> getCfList() {
        return cfList;
    }

    public void setCfList(List<Boolean> cfList) {
        this.cfList = cfList;
    }

    public List<Boolean> getHfList() {
        return hfList;
    }

    public void setHfList(List<Boolean> hfList) {
        this.hfList = hfList;
    }

    public String getQsjd() {
        return qsjd;
    }

    public void setQsjd(String qsjd) {
        this.qsjd = qsjd;
    }

    public String getJdsl() {
        return jdsl;
    }

    public void setJdsl(String jdsl) {
        this.jdsl = jdsl;
    }

    public String getTbzq() {
        return tbzq;
    }

    public void setTbzq(String tbzq) {
        this.tbzq = tbzq;
    }

    public String getTbxwc() {
        return tbxwc;
    }

    public void setTbxwc(String tbxwc) {
        this.tbxwc = tbxwc;
    }

    public int getBfyj() {
        return bfyj;
    }

    public void setBfyj(int bfyj) {
        this.bfyj = bfyj;
    }

    public String getBbjg() {
        return bbjg;
    }

    public void setBbjg(String bbjg) {
        this.bbjg = bbjg;
    }

    public String getBbcs() {
        return bbcs;
    }

    public void setBbcs(String bbcs) {
        this.bbcs = bbcs;
    }
}
