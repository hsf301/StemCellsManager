package com.huashengfu.StemCellsManager.entity.response.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable {
    private YesterdayData yesterdayData = new YesterdayData();
    private ArrayList<ShowNum> showNum = new ArrayList<>();
    private StoreInfo storeInfo = new StoreInfo();
    private NewNumber newNumber = new NewNumber();

    public NewNumber getNewNumber() {
        return newNumber;
    }

    public void setNewNumber(NewNumber newNumber) {
        this.newNumber = newNumber;
    }

    public YesterdayData getYesterdayData() {
        return yesterdayData;
    }

    public void setYesterdayData(YesterdayData yesterdayData) {
        this.yesterdayData = yesterdayData;
    }

    public ArrayList<ShowNum> getShowNum() {
        return showNum;
    }

    public void setShowNum(ArrayList<ShowNum> showNum) {
        this.showNum = showNum;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }
}
