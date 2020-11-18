package com.huashengfu.StemCellsManager.entity.response.activity;

import com.huashengfu.StemCellsManager.entity.response.Description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 服务初始化
public class Init implements Serializable {

    private Description description;
    private String banner;
    private ParamInfo paramInfo;
    private List<Detail> details = new ArrayList<>();

    public ParamInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfo paramInfo) {
        this.paramInfo = paramInfo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
