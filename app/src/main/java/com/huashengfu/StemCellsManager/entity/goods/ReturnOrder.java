package com.huashengfu.StemCellsManager.entity.goods;

import java.io.Serializable;
/*
"orderId": "1120102815483249426",
				"status": 1,
				"type": 7,
				"refundAmount": 0.01,
				"createDate": 1603873928000,
				"updateDate": 1603873928000,
				"opNo": null,
				"courierName": null,
				"courierNo": null,
				"remarks": "接口看看看看"
 */
public class ReturnOrder implements Serializable {

    private String orderId;
    private String remarks;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
