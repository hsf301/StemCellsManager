package com.huashengfu.StemCellsManager.entity.response.sms;

import java.io.Serializable;

public class CharacteristicInfo implements Serializable {

    private int id;
    private String details;
    private String titleOne;
    private String titleTwo;
    private String titleThree;
    private String titleFour;
    private int sid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitleOne() {
        return titleOne;
    }

    public void setTitleOne(String titleOne) {
        this.titleOne = titleOne;
    }

    public String getTitleTwo() {
        return titleTwo;
    }

    public void setTitleTwo(String titleTwo) {
        this.titleTwo = titleTwo;
    }

    public String getTitleThree() {
        return titleThree;
    }

    public void setTitleThree(String titleThree) {
        this.titleThree = titleThree;
    }

    public String getTitleFour() {
        return titleFour;
    }

    public void setTitleFour(String titleFour) {
        this.titleFour = titleFour;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
