package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
"id": 294,
                    "goodsId": 111881,
                    "skuName": "第一",
                    "skuSum": 132,
                    "skuSurplusSum": 132,
                    "skuPic": "http://test.huashengfu.cn:7777/upload/goods/pic/bd888dc25d9b457f90ebc15d5ba5a788.jpg",
                    "skuPrice": 1380.0
 */
public class Sku implements Serializable {

    private String skuName = "";
    private String skuPic = "";
    private int skuSum;
    private int skuSurplusSum;
    private double skuPrice;

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuPic() {
        return skuPic;
    }

    public void setSkuPic(String skuPic) {
        this.skuPic = skuPic;
    }

    public int getSkuSum() {
        return skuSum;
    }

    public void setSkuSum(int skuSum) {
        this.skuSum = skuSum;
    }

    public int getSkuSurplusSum() {
        return skuSurplusSum;
    }

    public void setSkuSurplusSum(int skuSurplusSum) {
        this.skuSurplusSum = skuSurplusSum;
    }

    public double getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(double skuPrice) {
        this.skuPrice = skuPrice;
    }
}
