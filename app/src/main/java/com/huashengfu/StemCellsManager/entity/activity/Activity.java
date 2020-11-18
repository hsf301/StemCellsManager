package com.huashengfu.StemCellsManager.entity.activity;

import com.huashengfu.StemCellsManager.entity.response.activity.Detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
{
			"id": 38,
			"startDate": null,
			"endDate": null,
			"createDate": null,
			"title": "大杀四方",
			"subTitle": "绝对成交看看你照片嘛呀呀",
			"banner": "http://test.huashengfu.cn:7777/upload/services/pic/ea6cd5df63cf433f972cd72a5d9d88ba.jpg",
			"views": 0,
			"detailsId": null,
			"isYn": "Y",
			"quota": 25,
			"surplusQuota": 25,
			"activityStatus": 1,
			"longitude": 123.49,
			"latitude": 41.7,
			"addr": "辽宁省沈阳市上深沟村861-20号楼E20楼宇三层",
			"eid": 11
		}
 */
public class Activity implements Serializable {

    private int id;
    private long startDate;
    private long endDate;
    private String title;
    private String subTitle;
    private String banner;
    private String addr;
    private int quota;
    private double longitude;
    private double latitude;
    private int activityStatus;
    private int surplusQuota;
    private int views;
    private List<Detail> details = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(int surplusQuota) {
        this.surplusQuota = surplusQuota;
    }
}
