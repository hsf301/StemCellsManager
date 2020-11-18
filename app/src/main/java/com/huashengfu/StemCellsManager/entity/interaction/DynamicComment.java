package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;

/*

				"id": null,
				"subTitle": "2007年8月初，张柏芝为谢家诞下首名男孙Lucas，她与霆锋更决定花三万多元把BB的脐带血，储存于脐带血库十八年，为小孩的健康做好预防措施。",
				"picUrl": "https://www.huashengfu.cn/upload/images/cms/pic/de9c8068f0834ab08681847c9263e9d4.jpg",
				"commentSum": 5
 */
public class DynamicComment implements Serializable {

    private int id;
    private String subTitle = "";
    private String picUrl = "";
    private int commentSum;
    private String status = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }
}
