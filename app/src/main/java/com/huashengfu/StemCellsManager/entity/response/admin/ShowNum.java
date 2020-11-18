package com.huashengfu.StemCellsManager.entity.response.admin;

import java.io.Serializable;

/*
"title": "商品数量",
			"name": "pms",
			"showNum": 41,
			"noShowNum": 1
 */
public class ShowNum implements Serializable {

    private String title = "";
    private String name = "";
    private int showNum = 0;
    private int noShowNum = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getNoShowNum() {
        return noShowNum;
    }

    public void setNoShowNum(int noShowNum) {
        this.noShowNum = noShowNum;
    }
}
