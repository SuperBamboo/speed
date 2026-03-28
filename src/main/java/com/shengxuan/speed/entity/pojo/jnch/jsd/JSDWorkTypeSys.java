package com.shengxuan.speed.entity.pojo.jnch.jsd;

import java.io.Serializable;
import java.util.List;

public class JSDWorkTypeSys implements Serializable {
    private int gzfs;
    private List<Boolean> sczt;
    private List<Boolean> sdzt;

    private String qsjd;
    private String jdsl;
    private String tbcs;
    private String tbzq;

    private int tsyj;
    private String tsjg;

    private String otherString1;
    private String otherString2;

    public String getOtherString2() {
        return otherString2;
    }

    public void setOtherString2(String otherString2) {
        this.otherString2 = otherString2;
    }

    public String getOtherString1() {
        return otherString1;
    }

    public void setOtherString1(String otherString1) {
        this.otherString1 = otherString1;
    }

    public int getGzfs() {
        return gzfs;
    }

    public void setGzfs(int gzfs) {
        this.gzfs = gzfs;
    }

    public List<Boolean> getSczt() {
        return sczt;
    }

    public void setSczt(List<Boolean> sczt) {
        this.sczt = sczt;
    }

    public List<Boolean> getSdzt() {
        return sdzt;
    }

    public void setSdzt(List<Boolean> sdzt) {
        this.sdzt = sdzt;
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

    public String getTbcs() {
        return tbcs;
    }

    public void setTbcs(String tbcs) {
        this.tbcs = tbcs;
    }

    public String getTbzq() {
        return tbzq;
    }

    public void setTbzq(String tbzq) {
        this.tbzq = tbzq;
    }

    public int getTsyj() {
        return tsyj;
    }

    public void setTsyj(int tsyj) {
        this.tsyj = tsyj;
    }

    public String getTsjg() {
        return tsjg;
    }

    public void setTsjg(String tsjg) {
        this.tsjg = tsjg;
    }
}
