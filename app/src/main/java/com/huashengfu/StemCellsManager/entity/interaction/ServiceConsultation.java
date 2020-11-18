package com.huashengfu.StemCellsManager.entity.interaction;

import com.huashengfu.StemCellsManager.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
"sname": "骨癌-关爱老人人人有责",
				"scover": "https://huashengfu.cn/upload/images/goods/pic/20200711111250.jpg",
				"sid": 3
				scount:1
				"status": "N",
 */
public class ServiceConsultation implements Serializable {

    private String sname;
    private String scontent;
    private String scover;
    private int sid;
    private int scount;
    private String status;
    private List<ConsultationUser> consultList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScontent() {
        return scontent;
    }

    public void setScontent(String scontent) {
        this.scontent = scontent;
    }

    public List<ConsultationUser> getConsultList() {
        return consultList;
    }

    public void setConsultList(List<ConsultationUser> consultList) {
        this.consultList = consultList;
    }

    public int getScount() {
        return scount;
    }

    public void setScount(int scount) {
        this.scount = scount;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getScover() {
        return scover;
    }

    public void setScover(String scover) {
        this.scover = scover;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public boolean hasUnRead(){
        return status.equals(Constants.Status.Service.no);
    }
}
