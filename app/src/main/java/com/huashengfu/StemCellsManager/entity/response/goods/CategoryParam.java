package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
{
"id":1,
"typeId":3,
"name":"品牌"
}
 */
public class CategoryParam implements Serializable {

    private int id;
    private int typeId;
    private String name;

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
}
