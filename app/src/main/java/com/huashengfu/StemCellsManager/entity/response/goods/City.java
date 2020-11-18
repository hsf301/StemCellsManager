package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
"cityCode": "110000",
                        "cityName": "北京市",
                        "areaList": [
                            {
                                "areaName": "东城区",
                                "areaCode": "110101"
                            }
                        ]
 */
public class City implements Serializable {

    private String cityCode;
    private String cityName;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
