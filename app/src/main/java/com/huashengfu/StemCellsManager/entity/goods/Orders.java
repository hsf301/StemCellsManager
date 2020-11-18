package com.huashengfu.StemCellsManager.entity.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
"orderId": "1120102817341469420",
                "storeId": null,
                "memberId": 1000012,
                "createTime": 1603877654000,
                "completionTime": null,
                "totalAmount": 189.7,
                "payAmount": 0.01,
                "freightAmount": 0.0,
                "payType": 0,
                "status": 1,
                "billType": null,
                "billHeader": null,
                "billContent": null,
                "billReceiverPhone": null,
                "billReceiverEmail": null,
                "receiverAddressId": null,
                "note": "",
                "courierName": null,
                "courierNo": null,
                "receiverName": null,
                "receiverPhone": null,
                "receiverRegion": "辽宁省 沈阳市 于洪区",
				"receiverDetailAddress": "我的人生态度就是",
                "detailsList": [
                    {
                        "id": 29,
                        "orderId": "1120102817341469420",
                        "goodsId": 700000001,
                        "goodsName": "联合利华清扬男士净澈劲爽沐浴露平衡控油600g单品多口味选择",
                        "goodsQuantity": 1,
                        "goodsSkuId": 10,
                        "goodsSkuName": "600g（活力运动）",
                        "goodsSkuPic": "https://img.alicdn.com/imgextra/i1/700459267/O1CN01q6eTaz2IKKuHcFHpp_!!0-item_pic.jpg_430x430q90.jpg",
                        "goodsSkuPrice": 59.9
                    }
                ],
                "returnOrder": null
            }
 */
public class Orders implements Serializable {

    private String orderId;
    private int memberId;
    private long createTime;
    private double totalAmount;
    private double payAmount;
    private double freightAmount;
    private int status;
    private List<OrderDetail> detailsList = new ArrayList<>();
    private String receiverName;
    private String receiverPhone;
    private String receiverAddressId;
    private String courierName;
    private String courierNo;
    private String receiverRegion;
    private String receiverDetailAddress;
    private ReturnOrder returnOrder;

    public ReturnOrder getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(ReturnOrder returnOrder) {
        this.returnOrder = returnOrder;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetail> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<OrderDetail> detailsList) {
        this.detailsList = detailsList;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddressId() {
        return receiverAddressId;
    }

    public void setReceiverAddressId(String receiverAddressId) {
        this.receiverAddressId = receiverAddressId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailAddress() {
        return receiverDetailAddress;
    }

    public void setReceiverDetailAddress(String receiverDetailAddress) {
        this.receiverDetailAddress = receiverDetailAddress;
    }
}
