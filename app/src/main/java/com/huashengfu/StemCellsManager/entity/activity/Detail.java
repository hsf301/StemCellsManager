package com.huashengfu.StemCellsManager.entity.activity;

import java.io.Serializable;

public class Detail implements Serializable {

    private boolean photo = false;
    private boolean text = false;
    private String content;
    private int height;
    private int width;

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
