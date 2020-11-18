package com.huashengfu.StemCellsManager.entity.response.admin;

import java.io.Serializable;
/*
{
            "newCommentNum": 2,
            "newConsultNum": 1,
            "newSignUpNum": 3,
            "newOrderNum": 0
        }
 */
public class NewNumber implements Serializable {

    // 动态评论
    private int newCommentNum;
    // 咨询总数
    private int newConsultNum;
    // 新的报名
    private int newSignUpNum;
    // 待处理订单
    private int newOrderNum;

    public int getNewCommentNum() {
        return newCommentNum;
    }

    public void setNewCommentNum(int newCommentNum) {
        this.newCommentNum = newCommentNum;
    }

    public int getNewConsultNum() {
        return newConsultNum;
    }

    public void setNewConsultNum(int newConsultNum) {
        this.newConsultNum = newConsultNum;
    }

    public int getNewSignUpNum() {
        return newSignUpNum;
    }

    public void setNewSignUpNum(int newSignUpNum) {
        this.newSignUpNum = newSignUpNum;
    }

    public int getNewOrderNum() {
        return newOrderNum;
    }

    public void setNewOrderNum(int newOrderNum) {
        this.newOrderNum = newOrderNum;
    }
}
