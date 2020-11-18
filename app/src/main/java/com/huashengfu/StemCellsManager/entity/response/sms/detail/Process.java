package com.huashengfu.StemCellsManager.entity.response.sms.detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Process implements Serializable {

    private String title = "";
    private List<ProcessDay> processList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProcessDay> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessDay> processList) {
        this.processList = processList;
    }
}
