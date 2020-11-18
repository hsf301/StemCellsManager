package com.huashengfu.StemCellsManager.entity.response;

import java.io.Serializable;
/*
"id":1,
"name":"干细胞中心",
"icon":"https://huashengfu.cn/upload/images/channel/sy_con_bg2_icon1.png",
"sort":1
 */
public class EnterpriseType implements Serializable {

    private int id;
    private String name;
    private String icon;
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
