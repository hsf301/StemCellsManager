package com.huashengfu.StemCellsManager.entity.response.sms;

import com.huashengfu.StemCellsManager.entity.response.sms.detail.Characteristic;
import com.huashengfu.StemCellsManager.entity.response.sms.detail.Label;
import com.huashengfu.StemCellsManager.entity.response.sms.detail.Process;
import com.huashengfu.StemCellsManager.entity.service.Introduction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
{
	"code": 200,
	"message": "操作成功",
	"data": {
		"id": 18,
		"name": "新测试，不要删除",
		"typeId": 28,
		"content": "测试服务，改了吧",
		"cover": "http://test.huashengfu.cn:7777/upload/services/pic/3d2a8b8ef0904699a56267f1d07907e0.jpg",
		"width": 1024,
		"height": 1820,
		"characteristic": {
			"titleList": ["好的", "鸟巢", "后面那个"],
			"details": "这是促销文案V改下"
		},
		"servicesOutLineList": [{
			"id": 40,
			"title": "交通",
			"details": "交通银行",
			"sid": 18,
			"oid": 1
		}, {
			"id": 41,
			"title": "餐食",
			"details": "鸭脖子",
			"sid": 18,
			"oid": 3
		}, {
			"id": 42,
			"title": "出行",
			"details": "别克凯越",
			"sid": 18,
			"oid": 4
		}, {
			"id": 43,
			"title": "住宿",
			"details": "万豪国际城",
			"sid": 18,
			"oid": 2
		}],
		"outLineList": [{
			"id": 1,
			"name": "往返交通",
			"content": "免费飞机往返，航班时间自选",
			"icon": "https://huashengfu.cn/upload/images/channel/fwxq_con_bg4_icon1@3x.png",
			"title": "交通"
		}, {
			"id": 2,
			"name": "五星酒店",
			"content": "五星酒店免费住宿",
			"icon": "https://huashengfu.cn/upload/images/channel/fwxq_con_bg4_icon2@3x.png",
			"title": "住宿"
		}, {
			"id": 3,
			"name": "定制饮食",
			"content": "根据用户情况免费提供定制健康餐",
			"icon": "https://huashengfu.cn/upload/images/channel/fwxq_con_bg4_icon3@3x.png",
			"title": "餐食"
		}, {
			"id": 4,
			"name": "专车接送",
			"content": "回输期间，全程专车接送",
			"icon": "https://huashengfu.cn/upload/images/channel/fwxq_con_bg4_icon4@3x.png",
			"title": "出行"
		}],
		"processList": [{
			"id": 24,
			"stepNo": 1,
			"title": "STEPS",
			"processList": [{
				"id": 47,
				"name": "第一天",
				"dayNo": 1,
				"content": "内容",
				"sid": 18,
				"sno": 24
			}, {
				"id": 48,
				"name": "第二天",
				"dayNo": 2,
				"content": "哈哈哈",
				"sid": 18,
				"sno": 24
			}],
			"sid": 18
		}, {
			"id": 25,
			"stepNo": 2,
			"title": "STEPS2",
			"processList": [{
				"id": 49,
				"name": "第一天",
				"dayNo": 1,
				"content": "在吃饭色太多",
				"sid": 18,
				"sno": 25
			}],
			"sid": 18
		}, {
			"id": 26,
			"stepNo": 3,
			"title": "steop3",
			"processList": [{
				"id": 50,
				"name": "第一天",
				"dayNo": 1,
				"content": "雪地靴",
				"sid": 18,
				"sno": 26
			}],
			"sid": 18
		}],
		"detailsList": [{
			"id": 41,
			"type": "TEXT",
			"details": "华盛顿代购活动现场",
			"height": 0.0,
			"width": 0.0,
			"sid": 18
		}, {
			"id": 42,
			"type": "TEXT",
			"details": "你不操心二十一世纪",
			"height": 0.0,
			"width": 0.0,
			"sid": 18
		}, {
			"id": 43,
			"type": "PIC",
			"details": "http://test.huashengfu.cn:7777/upload/services/pic/7d22fd6ce75d400592dd8710604a54a4.jpg",
			"height": 1820.0,
			"width": 1024.0,
			"sid": 18
		}],
		"commentList": [],
		"bannerList": ["http://test.huashengfu.cn:7777/upload/services/pic/3d2a8b8ef0904699a56267f1d07907e0.jpg"],
		"phone": "40012345678",
		"labelList": [{
			"name": "哈哈哈"
		}, {
			"name": "哦哦好的"
		}]
	}
}
 */
public class ServiceInfo implements Serializable {

    private int id;
    private String name = "";
    private int typeId;
    private String content = "";
    private String cover = "";
    private int width;
    private int height;
    private String phone = "";
    private Characteristic characteristic;
    private List<OutlineResult> servicesOutLineList = new ArrayList<>();
    private List<Outline> outLineList = new ArrayList<>();
    private List<Process> processList = new ArrayList<>();
    private List<Introduction> detailsList = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private List<Label> labelList = new ArrayList<>();

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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public List<OutlineResult> getServicesOutLineList() {
        return servicesOutLineList;
    }

    public void setServicesOutLineList(List<OutlineResult> servicesOutLineList) {
        this.servicesOutLineList = servicesOutLineList;
    }

    public List<Outline> getOutLineList() {
        return outLineList;
    }

    public void setOutLineList(List<Outline> outLineList) {
        this.outLineList = outLineList;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public List<Introduction> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Introduction> detailsList) {
        this.detailsList = detailsList;
    }

    public List<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<String> bannerList) {
        this.bannerList = bannerList;
    }

    public List<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<Label> labelList) {
        this.labelList = labelList;
    }
}
