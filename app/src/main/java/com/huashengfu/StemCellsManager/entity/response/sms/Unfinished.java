package com.huashengfu.StemCellsManager.entity.response.sms;

import java.io.Serializable;

public class Unfinished implements Serializable {

    private int id;
    private int isFinish;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }
}
