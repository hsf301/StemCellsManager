package com.huashengfu.StemCellsManager.entity.response.sms.detail;

import java.io.Serializable;

public class ProcessDay implements Serializable {

    private String name = "";
    private String content = "";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
