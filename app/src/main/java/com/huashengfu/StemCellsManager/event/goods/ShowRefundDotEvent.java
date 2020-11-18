package com.huashengfu.StemCellsManager.event.goods;

public class ShowRefundDotEvent {
    private boolean show = false;

    public boolean isShow() {
        return show;
    }

    public ShowRefundDotEvent setShow(boolean show) {
        this.show = show;
        return this;
    }
}
