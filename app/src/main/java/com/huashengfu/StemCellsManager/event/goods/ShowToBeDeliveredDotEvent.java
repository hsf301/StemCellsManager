package com.huashengfu.StemCellsManager.event.goods;

public class ShowToBeDeliveredDotEvent {
    private boolean show = false;

    public boolean isShow() {
        return show;
    }

    public ShowToBeDeliveredDotEvent setShow(boolean show) {
        this.show = show;
        return this;
    }
}
