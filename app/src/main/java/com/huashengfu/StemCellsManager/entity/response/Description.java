package com.huashengfu.StemCellsManager.entity.response;

import java.io.Serializable;

/*
"id":1,
            "pageNo":1,
            "title":"通过文字描述，让用户对于此项服务有一个概要性的了解和认知",
            "icon":"https:\/\/huashengfu.cn\/upload\/images\/banner\/wzms_con_icon1@3x.png",
            "pic":"https:\/\/huashengfu.cn\/upload\/images\/banner\/wzms_shouji1@3x.png",
            "type":1,
            "content1":"标题字数要求不多于20字",
            "content2":"文字描述要求不多余300字",
            "content3":"每个关键字不多余10个字，最多可设置5个"
 */
public class Description implements Serializable {

    private int id;
    private int pageNo;
    private String title = "";
    private String icon = "";
    private String pic = "";
    private int type;
    private String content1 = "";
    private String content2 = "";
    private String content3 = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }
}
