package com.huashengfu.StemCellsManager.entity.response.sms.init;

import java.io.Serializable;
import java.util.ArrayList;

/*
"id":1,
"pid":0,
"name":"基因检测",
"children":[],
"level":0,
"icon":"https:\/\/huashengfu.cn\/upload\/images\/channel\/sy_con_bg3_icon1.png",
"leaf":false,
"root":true
 */
public class Type implements Serializable {

    private int id;
    private int pid;
    private String name;
    private int level;
    private String icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
