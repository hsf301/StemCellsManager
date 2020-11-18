package com.huashengfu.StemCellsManager.entity.response.sms;

import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;
import com.huashengfu.StemCellsManager.entity.service.ProcessStep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Process implements Serializable {
    private ProcessStep step;
    private List<ProcessSchedule> processList = new ArrayList<>();

    public ProcessStep getStep() {
        return step;
    }

    public void setStep(ProcessStep step) {
        this.step = step;
    }

    public List<ProcessSchedule> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessSchedule> processList) {
        this.processList = processList;
    }
}
