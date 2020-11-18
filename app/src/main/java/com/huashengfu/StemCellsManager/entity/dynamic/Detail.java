package com.huashengfu.StemCellsManager.entity.dynamic;

import java.io.Serializable;
/*
"id": 692,
				"dynamicId": 10113,
				"type": "VIDEO",
				"assetsHeight": 1280.0,
				"assetsWidth": 720.0,
				"thumbnail": "http://test.huashengfu.cn:7777/upload/cms/video/99ec2f287f2d47dbaa58c3b66714ca63.mp4"
 */
public class Detail implements Serializable {
    private int id;
    private int dynamicId;
    private String type;
    private String thumbnail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
