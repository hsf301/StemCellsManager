package com.huashengfu.StemCellsManager.entity.interaction;

import java.io.Serializable;
/*

"id": 23,
		"createTime": "1602754146000",
		"updateTime": null,
		"commentText": "你妈妈",
		"status": "N",
		"nickName": "15804926009",
		"headPicUrl": "https://www.huashengfu.cn/upload/images/member/pic/e68934bb98274ce484b2adc5e7698321.jpg"

 */
public class Comment implements Serializable {


    private int id;
    private long createTime;
    private String commentText;
    private String status;
    private String nickName;
    private String headPicUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }
}
