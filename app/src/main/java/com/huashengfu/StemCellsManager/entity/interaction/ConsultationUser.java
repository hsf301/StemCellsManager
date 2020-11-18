package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;
/*
"id": 1,
					"applicant": "瓦萨米",
					"headPic": "https://www.huashengfu.cn/upload/images/member/pic/7ca7060f24df46c3ad88922c08eb6a79.jpg",
					"phone": "13189097741",
					"status": 2,
					"createTime": 1600135605000,
					"sex": 1,
					"age": 21,
					"medicalHistory": "啥病没有",
					"uid": 1000018
 */
public class ConsultationUser implements Serializable {

    private int id;
    private String headPic;
    private String phone;
    private int status;
    private long createTime;
    private String uname;
    private int uid;
    private String applicant;
    private int sex;
    private int age;
    private String medicalHistory;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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
