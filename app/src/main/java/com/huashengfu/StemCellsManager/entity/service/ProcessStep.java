package com.huashengfu.StemCellsManager.entity.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProcessStep implements Serializable {

    private List<ProcessSchedule> processSchedules = new ArrayList<>();
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProcessSchedule> getProcessSchedules() {
        return processSchedules;
    }

    public void setProcessSchedules(List<ProcessSchedule> processSchedules) {
        this.processSchedules = processSchedules;
    }
}
