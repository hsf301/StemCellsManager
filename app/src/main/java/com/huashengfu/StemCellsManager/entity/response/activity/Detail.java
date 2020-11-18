package com.huashengfu.StemCellsManager.entity.response.activity;
/*
{"id":28,"details":"http://test.huashengfu.cn:7777/upload/services/pic/e71d47520f854197a07beeaeed975d21.jpg","type":"PIC","height":1820.0,"width":1024.0,"aid":41}
 */
public class Detail {

    private int id;
    private String details;
    private String type;
    private int height;
    private int width;
    private int aid;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
