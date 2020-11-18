package com.huashengfu.StemCellsManager.entity.service;

import java.io.Serializable;

public class QA implements Serializable {

    private String question;
    private int answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
