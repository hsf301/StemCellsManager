package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
[{"id":471,"goodsId":111881,"detailsText":"http:\/\/test.huashengfu.cn:7777\/upload\/goods\/pic\/e93fedc3aa3f400a9b9ff62141865b1c.jpg","t
    ype":"PIC","assetsHeight":0,"assetsWidth":0},{"id":472,"goodsId":111881,"detailsText":"就得瑟一个他的是谁呀","type":"TEXT","assetsHeight":0,"assetsWidth":0}]
 */
public class Detail implements Serializable {

    private int id;
    private int goodsId;
    private String detailsText = "";
    private String type = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getDetailsText() {
        return detailsText;
    }

    public void setDetailsText(String detailsText) {
        this.detailsText = detailsText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
