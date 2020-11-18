package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;
import java.util.ArrayList;

/*
"id":3,
"pid":1,
"name":"面膜",
"children":[
],
"level":1,
"details":"干细胞面膜",
"picUrl":"",
"root":false,
"leaf":true
 */
public class Type implements Serializable {

    private int id;
    private int pid;
    private String name;
    private int level;
    private String details;
    private String picUrl;
    private boolean leaf;
    private boolean root;
    private ArrayList<Type> children = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public ArrayList<Type> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Type> children) {
        this.children = children;
    }
}
