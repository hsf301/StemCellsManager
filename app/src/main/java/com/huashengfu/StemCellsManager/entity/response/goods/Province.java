package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
"cityList": [
                    {
                        "cityCode": "110000",
                        "cityName": "北京市",
                        "areaList": [
                            {
                                "areaName": "东城区",
                                "areaCode": "110101"
                            },
                            {
                                "areaName": "西城区",
                                "areaCode": "110102"
                            },
                            {
                                "areaName": "朝阳区",
                                "areaCode": "110105"
                            },
                            {
                                "areaName": "丰台区",
                                "areaCode": "110106"
                            },
                            {
                                "areaName": "石景山区",
                                "areaCode": "110107"
                            },
                            {
                                "areaName": "海淀区",
                                "areaCode": "110108"
                            },
                            {
                                "areaName": "门头沟区",
                                "areaCode": "110109"
                            },
                            {
                                "areaName": "房山区",
                                "areaCode": "110111"
                            },
                            {
                                "areaName": "通州区",
                                "areaCode": "110112"
                            },
                            {
                                "areaName": "顺义区",
                                "areaCode": "110113"
                            },
                            {
                                "areaName": "昌平区",
                                "areaCode": "110114"
                            },
                            {
                                "areaName": "大兴区",
                                "areaCode": "110115"
                            },
                            {
                                "areaName": "怀柔区",
                                "areaCode": "110116"
                            },
                            {
                                "areaName": "平谷区",
                                "areaCode": "110117"
                            },
                            {
                                "areaName": "密云区",
                                "areaCode": "110118"
                            },
                            {
                                "areaName": "延庆区",
                                "areaCode": "110119"
                            }
                        ]
                    }
                ],
                "provinceCode": "110000",
                "provinceName": "北京市"
 */
public class Province implements Serializable {

    private String provinceName;
    private String provinceCode;
    private List<City> cityList = new ArrayList<>();

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
