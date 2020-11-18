package com.huashengfu.StemCellsManager.entity.response.sms.detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Characteristic implements Serializable {

    private List<String> titleList = new ArrayList<>();
    private String details = "";

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
