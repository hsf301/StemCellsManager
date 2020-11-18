package com.huashengfu.StemCellsManager.entity.interaction;

import com.huashengfu.StemCellsManager.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*

"aname": "测试线下活动",
				"acover": "https://huashengfu.cn/upload/images/goods/pic/20200711095905.jpg",
				"asubtitle": "福克斯报道提到，根据美国经济政策研究所2015年的一份报告，2000年1月至2014年12月间，美国制造业损失了500万个就业岗位，原因是“经济大衰退（2007-2009）之前制造业产品的贸易逆差不断扩大，而经济大衰退期间又出现大规模的产出崩溃”。",
				"aendDate": "1602378974000",
				"aid": 1,
				"acount":16

 */
public class ActivityRegistration implements Serializable {

    private String aname;
    private String acover;
    private String asubtitle;
    private long aendDate;
    private int aid;
    private int acount;
    private String status;

    private List<ActivityRegistrationUser> signUpList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getAcover() {
        return acover;
    }

    public void setAcover(String acover) {
        this.acover = acover;
    }

    public String getAsubtitle() {
        return asubtitle;
    }

    public void setAsubtitle(String asubtitle) {
        this.asubtitle = asubtitle;
    }

    public long getAendDate() {
        return aendDate;
    }

    public void setAendDate(long aendDate) {
        this.aendDate = aendDate;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAcount() {
        return acount;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public List<ActivityRegistrationUser> getSignUpList() {
        return signUpList;
    }

    public void setSignUpList(List<ActivityRegistrationUser> signUpList) {
        this.signUpList = signUpList;
    }

    public boolean hasUnRead(){
        return status.equals(Constants.Status.Activity.no);
    }
}
