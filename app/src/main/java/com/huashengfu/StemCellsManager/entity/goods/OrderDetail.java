package com.huashengfu.StemCellsManager.entity.goods;

import java.io.Serializable;
/*

"id": 29,
                        "orderId": "1120102817341469420",
                        "goodsId": 700000001,
                        "goodsName": "联合利华清扬男士净澈劲爽沐浴露平衡控油600g单品多口味选择",
                        "goodsQuantity": 1,
                        "goodsSkuId": 10,
                        "goodsSkuName": "600g（活力运动）",
                        "goodsSkuPic": "https://img.alicdn.com/imgextra/i1/700459267/O1CN01q6eTaz2IKKuHcFHpp_!!0-item_pic.jpg_430x430q90.jpg",
                        "goodsSkuPrice": 59.9

 */
public class OrderDetail implements Serializable {

    private int id;
    private String orderId;
    private int goodsId;
    private String goodsName;
    private int goodsQuantity;
    private int goodsSkuId;
    private String goodsSkuName;
    private String goodsSkuPic;
    private double goodsSkuPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(int goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public int getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(int goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getGoodsSkuPic() {
        return goodsSkuPic;
    }

    public void setGoodsSkuPic(String goodsSkuPic) {
        this.goodsSkuPic = goodsSkuPic;
    }

    public double getGoodsSkuPrice() {
        return goodsSkuPrice;
    }

    public void setGoodsSkuPrice(double goodsSkuPrice) {
        this.goodsSkuPrice = goodsSkuPrice;
    }
}
