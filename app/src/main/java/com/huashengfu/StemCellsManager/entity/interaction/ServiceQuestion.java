package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;

/*
"id": 30,
			"status": "N",
			"topic": "辣鸡\n",
			"createTime": 1603411080000,
			"sid": 1,
			"uname": "15804926009",
			"uheadPicUrl": "https://www.huashengfu.cn/upload/images/member/pic/e68934bb98274ce484b2adc5e7698321.jpg",
			"uid": 1000012
 */
public class ServiceQuestion implements Serializable {

    private int id;
    private String topic;
    private int sid;
    private String uname;
    private int uid;
    private String status;
    private String uheadPicUrl;
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUheadPicUrl() {
        return uheadPicUrl;
    }

    public void setUheadPicUrl(String uheadPicUrl) {
        this.uheadPicUrl = uheadPicUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
