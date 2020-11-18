package com.huashengfu.StemCellsManager.entity.response.sms.init;

import java.io.Serializable;
import java.util.ArrayList;

/*
"id":9,
"name":"关节炎-干细胞疗法",
"content":"我曾经很想知道同样的话要说多少次才好那些再三强调的老套长大了才知道是不是需要很少主动拥抱就算为了自豪腼腆的笑要强而又低调穿的布料我赠送的外套过时也不丢掉还是一样太多理所应当让人觉得平常不算太小的房冬暖夏凉的那间放着我的床歌颂这种平凡 一两句唱不完恩重如山 听起来不自然回头去看这是说了谢谢",
"typeId":1,
"titles":[
"有效率100%",
"治愈率99%",
"靶向治疗"
]
 */
public class BaisicInfo implements Serializable {

    private int id;
    private String name = "";
    private String content = "";
    private String phone = "";
    private int typeId;
    private ArrayList<String> titles = new ArrayList<>();

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
