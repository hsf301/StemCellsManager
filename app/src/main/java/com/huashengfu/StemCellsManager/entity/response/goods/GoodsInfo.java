package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
"brand": "正式测试品牌",
            "goodsId": 111881,
            "enterpriseId": 11,
            "categoryId": 188,
            "categoryName": null,
            "firstPicUrl": "http://test.huashengfu.cn:7777/upload/goods/pic/35a515fc28154a66b16022bab2e0438e.jpg",
            "videoUrl": "http://test.huashengfu.cn:7777/upload/goods/video/d883fdfbcd864d4d8ecdc09380608654.mp4",
            "name": "名称",
            "banners": null,
            "originalPrice": 1358.0,
            "minPrice": 1380.0,
            "sellSum": 0,
            "commentSum": 0,
            "collectionSum": 0,
            "deliverAddress": "",
            "isUpperShelf": "N",
            "upperShelfDate": null,
            "picWidth": 1024,
            "picHigh": 1820,
            "content": null,
            "labelId": "20,21",
            "activityId": null,
            "detailsId": null,
            "logistics": 0.0,
            "serviceRemarks": "支持七天无理由退换货",
            "phone": "13512345682",
            "verifyStatus": "N",
            "feightTemplateId": null,
            "isRebate": null,
            "rebateMode": null,
            "rebateValue": null,
            "labels": [
                "方法",
                "刚刚"
            ],
            "params": [
                {
                    "id": 754,
                    "goodsId": 111881,
                    "name": "品牌",
                    "details": "品牌二"
                },
                {
                    "id": 755,
                    "goodsId": 111881,
                    "name": "材质",
                    "details": "测阿迪"
                },
                {
                    "id": 756,
                    "goodsId": 111881,
                    "name": "功效",
                    "details": "明明白白"
                },
                {
                    "id": 757,
                    "goodsId": 111881,
                    "name": "产地",
                    "details": "长城"
                }
            ],
            "skus": [
                {
                    "id": 294,
                    "goodsId": 111881,
                    "skuName": "第一",
                    "skuSum": 132,
                    "skuSurplusSum": 132,
                    "skuPic": "http://test.huashengfu.cn:7777/upload/goods/pic/bd888dc25d9b457f90ebc15d5ba5a788.jpg",
                    "skuPrice": 1380.0
                },
                {
                    "id": 295,
                    "goodsId": 111881,
                    "skuName": "第二",
                    "skuSum": 3580,
                    "skuSurplusSum": 3580,
                    "skuPic": "http://test.huashengfu.cn:7777/upload/goods/pic/3cf05275c9d44497a0907989f4b61ab6.jpg",
                    "skuPrice": 2536.0
                }
            ]
 */
public class GoodsInfo implements Serializable {

    private int goodsId;
    private int categoryId;
    private String name = "";
    private String brand = "";
    private double originalPrice;
    private double minPrice;
    private String isUpperShelf = "";
    private List<String> labels = new ArrayList<>();
    private double logistics;
    private String serviceRemarks = "";
    private String phone = "";
    private List<Parameters> params = new ArrayList<>();
    private List<Sku> skus = new ArrayList<>();
    private String deliverAddress = "";
    private String content = "";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public String getIsUpperShelf() {
        return isUpperShelf;
    }

    public void setIsUpperShelf(String isUpperShelf) {
        this.isUpperShelf = isUpperShelf;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public double getLogistics() {
        return logistics;
    }

    public void setLogistics(double logistics) {
        this.logistics = logistics;
    }

    public String getServiceRemarks() {
        return serviceRemarks;
    }

    public void setServiceRemarks(String serviceRemarks) {
        this.serviceRemarks = serviceRemarks;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Parameters> getParams() {
        return params;
    }

    public void setParams(List<Parameters> params) {
        this.params = params;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
