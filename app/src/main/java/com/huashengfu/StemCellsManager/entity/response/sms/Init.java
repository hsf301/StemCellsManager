package com.huashengfu.StemCellsManager.entity.response.sms;

import com.huashengfu.StemCellsManager.entity.response.sms.init.BaisicInfo;
import com.huashengfu.StemCellsManager.entity.response.Description;
import com.huashengfu.StemCellsManager.entity.response.sms.init.Type;
import com.huashengfu.StemCellsManager.entity.service.Introduction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
// 服务初始化
public class Init implements Serializable {

    private Description description;
    // 第一步初始化返回的数据
    private BaisicInfo baisicInfo;
    // 第一步初始化返回的数据
    private ArrayList<Type> typeList = new ArrayList<>();
    // 第二步轮播图初始化返回的数据
    private List<String> banners = new ArrayList<>();
    // 第二步轮播图初始化返回的数据，这个不再使用
    private UploadCover cover;
    // 第三步初始化返回的数据
    private CharacteristicInfo characteristicInfo;
    // 第四步初始化返回的数据
    private List<Outline> outlineList = new ArrayList<>();
    private List<OutlineResult> resultList = new ArrayList<>();
    // 第五步初始化返回的数据
    private List<Process> processList = new ArrayList<>();
    // 第六步初始化返回的数据
    private List<Introduction> detailsList = new ArrayList<>();

    public List<Introduction> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Introduction> detailsList) {
        this.detailsList = detailsList;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public List<OutlineResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<OutlineResult> resultList) {
        this.resultList = resultList;
    }

    public List<Outline> getOutlineList() {
        return outlineList;
    }

    public void setOutlineList(List<Outline> outlineList) {
        this.outlineList = outlineList;
    }

    public CharacteristicInfo getCharacteristicInfo() {
        return characteristicInfo;
    }

    public void setCharacteristicInfo(CharacteristicInfo characteristicInfo) {
        this.characteristicInfo = characteristicInfo;
    }

    public BaisicInfo getBaisicInfo() {
        return baisicInfo;
    }

    public void setBaisicInfo(BaisicInfo baisicInfo) {
        this.baisicInfo = baisicInfo;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public ArrayList<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<Type> typeList) {
        this.typeList = typeList;
    }

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
    }

    public UploadCover getCover() {
        return cover;
    }

    public void setCover(UploadCover cover) {
        this.cover = cover;
    }
}
