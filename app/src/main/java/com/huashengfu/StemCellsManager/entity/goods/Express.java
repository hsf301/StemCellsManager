package com.huashengfu.StemCellsManager.entity.goods;

import java.io.Serializable;

/*

    "id":1,
    "courierName":"顺丰"
 */
public class Express implements Serializable {
    private String courierName;
    private int id;

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
