package com.huashengfu.StemCellsManager.entity.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
"id": 10113,
			"subTitle": "",
			"createTime": 1603179295000,
			"browseSum": 0,
			"thumbsUpSum": 0,
			"commentSum": 0,
			"forwardSum": 0,

			titleList
 */
public class Dynamic implements Serializable {

    private int id;
    private String subTitle;
    private long createTime;
    private int browseSum;
    private int commentSum;
    private int forwardSum;
    private List<Detail> detailsList = new ArrayList<>();

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBrowseSum() {
        return browseSum;
    }

    public void setBrowseSum(int browseSum) {
        this.browseSum = browseSum;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }

    public int getForwardSum() {
        return forwardSum;
    }

    public void setForwardSum(int forwardSum) {
        this.forwardSum = forwardSum;
    }

    public List<Detail> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Detail> detailsList) {
        this.detailsList = detailsList;
    }
}
