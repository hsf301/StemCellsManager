package com.huashengfu.StemCellsManager.entity.response.sms;

import java.io.Serializable;

public class OutlineResult implements Serializable {

    private int id;
    private String title;
    private String details;
    private String isYn;
    private int sid;
    private int oid;
    private long createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIsYn() {
        return isYn;
    }

    public void setIsYn(String isYn) {
        this.isYn = isYn;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
