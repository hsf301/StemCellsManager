package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;
/*

"id": 8,
					"name": "曹雪芹",
					"sex": 0,
					"status": "N",
					"applySum": 1,
					"phone": "18907652639",
					"createTime": 1603703342000,
					"uid": 1000018

 */
public class ActivityRegistrationUser implements Serializable {

    private int id;
    private String name;
    private String status;
    private long createTime;
    private int uid;
    private String phone;
    private int sex;
    private int applySum;

    public int getApplySum() {
        return applySum;
    }

    public void setApplySum(int applySum) {
        this.applySum = applySum;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
