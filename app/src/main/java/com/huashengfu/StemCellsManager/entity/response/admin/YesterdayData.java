package com.huashengfu.StemCellsManager.entity.response.admin;

import java.io.Serializable;

/*
"eid": 1,
			"sbrowseSum": 0,
			"scollectionSum": 0,
			"sconsultSum": 0,
			"dbrowseSum": 0,
			"dcommentSum": 0,
			"dthumbsUpSum": 0,
			"gbrowseSum": 0,
			"gcollectionSum": 0,
			"gsellSum": 0
 */
public class YesterdayData implements Serializable {
    private String eid = "";
    private int sbrowseSum = 0;
    private int scollectionSum = 0;
    private int sconsultSum = 0;
    private int dbrowseSum = 0;
    private int dcommentSum = 0;
    private int dthumbsUpSum = 0;
    private int gbrowseSum = 0;
    private int gcollectionSum = 0;
    private int gsellSum = 0;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public int getSbrowseSum() {
        return sbrowseSum;
    }

    public void setSbrowseSum(int sbrowseSum) {
        this.sbrowseSum = sbrowseSum;
    }

    public int getScollectionSum() {
        return scollectionSum;
    }

    public void setScollectionSum(int scollectionSum) {
        this.scollectionSum = scollectionSum;
    }

    public int getSconsultSum() {
        return sconsultSum;
    }

    public void setSconsultSum(int sconsultSum) {
        this.sconsultSum = sconsultSum;
    }

    public int getDbrowseSum() {
        return dbrowseSum;
    }

    public void setDbrowseSum(int dbrowseSum) {
        this.dbrowseSum = dbrowseSum;
    }

    public int getDcommentSum() {
        return dcommentSum;
    }

    public void setDcommentSum(int dcommentSum) {
        this.dcommentSum = dcommentSum;
    }

    public int getDthumbsUpSum() {
        return dthumbsUpSum;
    }

    public void setDthumbsUpSum(int dthumbsUpSum) {
        this.dthumbsUpSum = dthumbsUpSum;
    }

    public int getGbrowseSum() {
        return gbrowseSum;
    }

    public void setGbrowseSum(int gbrowseSum) {
        this.gbrowseSum = gbrowseSum;
    }

    public int getGcollectionSum() {
        return gcollectionSum;
    }

    public void setGcollectionSum(int gcollectionSum) {
        this.gcollectionSum = gcollectionSum;
    }

    public int getGsellSum() {
        return gsellSum;
    }

    public void setGsellSum(int gsellSum) {
        this.gsellSum = gsellSum;
    }
}
