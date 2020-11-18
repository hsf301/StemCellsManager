package com.huashengfu.StemCellsManager.entity.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/*
{
        "id": 17,
            "typeId": 28,
            "name": "哈哈哈哈",
            "content": "高低床",
            "title": null,
            "keyword": null,
            "cover": "http://test.huashengfu.cn:7777/upload/services/pic/7c7a99acc8bc48658a5efb21f6a2b46a.jpg",
            "width": null,
            "height": null,
            "status": "N",
            "collectionSum": 0,
            "browseSum": 0,
            "labelId": "61",
            "labelList": [{
        "name": "韩国"
    }],
        "waitReply": null,
            "commentSum": 0,
            "topicList": [],
        "eid": 11
    }
 */
public class Service implements Serializable {

    private String name;
    private String content;
    private int id;
    private int typeId;
    private String title;
    private String cover;
    private String status;
    private int browseSum;
    private int collectionSum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBrowseSum() {
        return browseSum;
    }

    public void setBrowseSum(int browseSum) {
        this.browseSum = browseSum;
    }

    public int getCollectionSum() {
        return collectionSum;
    }

    public void setCollectionSum(int collectionSum) {
        this.collectionSum = collectionSum;
    }
}
