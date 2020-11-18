package com.huashengfu.StemCellsManager.entity.response.admin;

import java.io.Serializable;

/*
* "id": 1,
			"name": "华盛福官方旗舰店",
			"icon": "https:\/\/huashengfu.cn\/upload\/images\/banner\/jgsy_bg_touxiang@3x.png",
			"details": "中国医科大学附属盛京医院是一所现代化大型综合性、数字化大学附属医院。医院南湖院区位于沈阳市和平区三好街，滑翔院区位于沈阳市铁西区滑翔路，总占地面积13万平方米，总建筑面积54万平方米。",
			"officialWeb": "www.baidu.com",
			"phone": "13120000000",
			"status": 2,
			"customerServiceNo": null,
			"type": 1,
			"fansSum": 102,
			"serviceSum": 0,
			"dynamicSum": 0,
			"goodsSum": 0,
			"activitySum": 0,
			"commentSum": 100,
			"ceo": "李长贵",
			"createDate": "2020-09-08 13:37:19",
			"enterpriseType": "个人独资",
			"enterpriseName": "中国神雁健康产业集团有限公司",
			"registerAddress": "无锡市滨湖区马山梅梁西路88号",
			"sketch": "中国干细胞企业领先企业，的“产、学、研”三位一体科研平台",
			"sellActivity": "线下预订，即送13999优惠，下单后赠送Iphone12，ipadPro 苹果电视，手表",
			"vip": "N"
* */
public class StoreInfo implements Serializable {

    private int id;
    private String name = "";
    private String icon = "";
    private int status;
    private String phone = "";
    private String vip = "";
    private String details = "";
    private String officialWeb = "";
    private String ceo = "";
    private String enterpriseType = "";
    private String enterpriseName = "";
    private String registerAddress = "";
    private String sketch = "";
    private String sellActivity = "";
    private String customerServiceNo = "";
    private int type;
    private int fansSum;
    private int serviceSum;
    private int dynamicSum;
    private int goodsSum;
    private int activitySum;
    private int commentSum;
    private long createDate;
    private String weChat = "";
    private double longitude;
    private double latitude;
    private int city;
    private String banners = "";

    public String getBanners() {
        return banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOfficialWeb() {
        return officialWeb;
    }

    public void setOfficialWeb(String officialWeb) {
        this.officialWeb = officialWeb;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getSellActivity() {
        return sellActivity;
    }

    public void setSellActivity(String sellActivity) {
        this.sellActivity = sellActivity;
    }

    public String getCustomerServiceNo() {
        return customerServiceNo;
    }

    public void setCustomerServiceNo(String customerServiceNo) {
        this.customerServiceNo = customerServiceNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFansSum() {
        return fansSum;
    }

    public void setFansSum(int fansSum) {
        this.fansSum = fansSum;
    }

    public int getServiceSum() {
        return serviceSum;
    }

    public void setServiceSum(int serviceSum) {
        this.serviceSum = serviceSum;
    }

    public int getDynamicSum() {
        return dynamicSum;
    }

    public void setDynamicSum(int dynamicSum) {
        this.dynamicSum = dynamicSum;
    }

    public int getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(int goodsSum) {
        this.goodsSum = goodsSum;
    }

    public int getActivitySum() {
        return activitySum;
    }

    public void setActivitySum(int activitySum) {
        this.activitySum = activitySum;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }
}
