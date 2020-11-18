package com.huashengfu.StemCellsManager.entity.response;

import java.util.ArrayList;
import java.util.List;

public class PageResponse<T> {

    private int totalPage;
    private int total;
    private List<T> list = new ArrayList<>();

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
