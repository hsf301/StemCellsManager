package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Upload implements Serializable {

    private ArrayList<String> data = new ArrayList<>();

    private UploadVideo video;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public UploadVideo getVideo() {
        return video;
    }

    public void setVideo(UploadVideo video) {
        this.video = video;
    }
}
