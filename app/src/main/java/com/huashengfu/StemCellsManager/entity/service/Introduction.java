package com.huashengfu.StemCellsManager.entity.service;

import java.io.Serializable;

/*
{"id":8,"type":"TEXT","details":"还打电话","height":0.0,"width":0.0,"createDate":1602506691000,"sid":18}
 */
public class Introduction implements Serializable {

    private boolean photo = false;
    private boolean text = false;
    private String details;
    private int id;
    private String type;
    private int width;
    private int height;

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = !photo;
        this.photo = photo;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.photo = !text;
        this.text = text;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
