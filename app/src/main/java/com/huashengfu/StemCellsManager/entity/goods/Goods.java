package com.huashengfu.StemCellsManager.entity.goods;

import com.huashengfu.StemCellsManager.entity.response.goods.Detail;
import com.huashengfu.StemCellsManager.entity.response.goods.Parameters;
import com.huashengfu.StemCellsManager.entity.response.goods.Sku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
{
			"brand": null,
			"goodsId": 300000001,
			"enterpriseId": null,
			"categoryId": null,
			"categoryName": null,
			"firstPicUrl": "https://www.huashengfu.cn/upload/images/goods/pic/O1CN017eRIZB2NPRQrB2jhj_!!2201453959955.jpg",
			"videoUrl": null,
			"name": "美白烟酰胺面膜正品保湿补水淡斑收缩毛孔紧致淡化祛痘印男女专用",
			"banners": null,
			"originalPrice": 150.0,
			"minPrice": 30.0,
			"sellSum": 10,
			"commentSum": null,
			"collectionSum": 10,
			"deliverAddress": null,
			"isUpperShelf": "Y",
			"upperShelfDate": 1588834755000,
			"picWidth": 314,
			"picHigh": 418,
			"content": null,
			"logistics": null,
			"serviceRemarks": null,
			"phone": null,
			"verifyStatus": null,
			"feightTemplateId": null,
			"labels": null,
			"params": null,
			"skus": null,
			"details": null
		}
 */
public class Goods implements Serializable {
    private String brand = "";
    private int goodsId;
    private String firstPicUrl = "";
    private String name = "";
    private List<String> banners = new ArrayList<>();
    private double originalPrice;
    private double minPrice;
    private int sellSum;
    private int collectionSum;
    private String isUpperShelf = "";
    private int categoryId;
    private String videoUrl = "";
    private ArrayList<String> labels = new ArrayList<>();
    private List<Parameters> params = new ArrayList<>();
    private List<Sku> skus = new ArrayList<>();
    private List<Detail> details = new ArrayList<>();
    private String deliverAddress = "";
    private double logistics;
    private String serviceRemarks = "";
    private String content = "";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServiceRemarks() {
        return serviceRemarks;
    }

    public void setServiceRemarks(String serviceRemarks) {
        this.serviceRemarks = serviceRemarks;
    }

    public double getLogistics() {
        return logistics;
    }

    public void setLogistics(double logistics) {
        this.logistics = logistics;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getFirstPicUrl() {
        return firstPicUrl;
    }

    public void setFirstPicUrl(String firstPicUrl) {
        this.firstPicUrl = firstPicUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
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

    public int getSellSum() {
        return sellSum;
    }

    public void setSellSum(int sellSum) {
        this.sellSum = sellSum;
    }

    public int getCollectionSum() {
        return collectionSum;
    }

    public void setCollectionSum(int collectionSum) {
        this.collectionSum = collectionSum;
    }

    public String getIsUpperShelf() {
        return isUpperShelf;
    }

    public void setIsUpperShelf(String isUpperShelf) {
        this.isUpperShelf = isUpperShelf;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
