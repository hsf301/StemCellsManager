package com.huashengfu.StemCellsManager.entity.response.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UploadBanner implements Serializable {

    private List<String> data = new ArrayList<>();

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
