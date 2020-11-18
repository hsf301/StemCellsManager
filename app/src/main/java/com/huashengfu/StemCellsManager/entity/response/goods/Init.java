package com.huashengfu.StemCellsManager.entity.response.goods;

import com.huashengfu.StemCellsManager.entity.response.Description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 服务初始化
public class Init implements Serializable {

    private Description description;
    private List<String> banner = new ArrayList<>();
    private String video;

    // 第二步初始化返回的数据
    private List<Province> address = new ArrayList<>();
    private ArrayList<Type> typeList = new ArrayList<>();
    private Map<String, List<CategoryParam>> categoryParamMap = new ConcurrentHashMap<>();
    private GoodsInfo goodsInfo;
    //第三步初始化返回的数据
    private List<Detail> details = new ArrayList<>();

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<Province> getAddress() {
        return address;
    }

    public void setAddress(List<Province> address) {
        this.address = address;
    }

    public ArrayList<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<Type> typeList) {
        this.typeList = typeList;
    }

    public Map<String, List<CategoryParam>> getCategoryParamMap() {
        return categoryParamMap;
    }

    public void setCategoryParamMap(Map<String, List<CategoryParam>> categoryParamMap) {
        this.categoryParamMap = categoryParamMap;
    }

    public List<String> getBanner() {
        return banner;
    }

    public void setBanner(List<String> banner) {
        this.banner = banner;
    }

    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
