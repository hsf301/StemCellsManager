package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
{"id":1394,"typeId":408,"name":"品牌"}
 */
public class Parameters implements Serializable {

    private int id;
    private int typeId;
    private String name;
    private String details;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
