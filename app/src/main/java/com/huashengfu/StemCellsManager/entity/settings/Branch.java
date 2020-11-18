package com.huashengfu.StemCellsManager.entity.settings;

import java.io.Serializable;
/*
 {
			"name": "分支机构1",
			"pic": "https://www.huashengfu.cn/upload/images/goods/pic/O1CN017eRIZB2NPRQrB2jhj_!!2201453959955.jpg",
			"longitude": 123.45,
			"latitude": 41.8,
			"businessHours": "10:00-21:00",
			"addr": "沈阳市沈河区",
			"phone": "13120200000",
			"eid": 1
		}
 */
public class Branch implements Serializable {

    private String name = "";
    private String pic = "";
    private String businessHours = "";
    private String addr = "";
    private String phone = "";
    private double latitude;
    private double longitude;
    private int eid;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
