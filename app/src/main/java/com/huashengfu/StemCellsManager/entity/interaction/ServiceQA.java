package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;
/*
"id": 1,
				"name": "关节炎-干细胞疗法1",
				"cover": "http://test.huashengfu.cn:7777/upload/services/pic/7e37c69f1ada4f9f8dbf000b25e64e4b.jpg",
				"content": "此次非洲主题日论坛活动以“后疫情时代·非洲新机遇”为主题，在新冠肺炎疫情全球传播影响的前提下,国际交往与合作遇到空前挑战,各国经济下行压力增大,控制疫情与国际合作是当前的重要课题。在“后疫情时代”的全球经济重启中，在“一带一路”倡议和中非合作论坛框架下，中非在基础设施建设、资金支持和市场开放等领域的互利合作潜力巨大。",
				"topicSum": 16
				"status": "N",
 */
public class ServiceQA implements Serializable {

    private int id;
    private String name;
    private String cover;
    private String content;
    private int topicSum;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTopicSum() {
        return topicSum;
    }

    public void setTopicSum(int topicSum) {
        this.topicSum = topicSum;
    }
}
