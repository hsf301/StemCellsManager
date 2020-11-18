package com.huashengfu.StemCellsManager.entity.response.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
{
	"id": 35,
	"startDate": 1601481600000,
	"endDate": 1604073600000,
	"title": "活动嗯",
	"subTitle": "云集店主你好我就好咯咯",
	"labels": ["哈哈"],
	"quota": 36,
	"longitude": 123.33,
	"latitude": 41.64,
	"addr": "辽宁省沈阳市碧桂园南门",
	"eid": 11
}
 */
public class ParamInfo implements Serializable {

    private int id;
    private long startDate;
    private long endDate;
    private String title = "";
    private String subTitle = "";
    private List<String> labels = new ArrayList<>();
    private int quota;
    private double longitude;
    private double latitude;
    private String addr = "";
    private int eid;
    private String phone = "";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }
}
