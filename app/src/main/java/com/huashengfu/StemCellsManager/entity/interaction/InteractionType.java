package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;

public class InteractionType implements Serializable {

    private String name;
    private int resId;
    private int resSelectId;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResSelectId() {
        return resSelectId;
    }

    public void setResSelectId(int resSelectId) {
        this.resSelectId = resSelectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
