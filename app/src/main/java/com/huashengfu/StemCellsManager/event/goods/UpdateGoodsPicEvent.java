package com.huashengfu.StemCellsManager.event.goods;

import com.huashengfu.StemCellsManager.entity.goods.Goods;

public class UpdateGoodsPicEvent {

    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public UpdateGoodsPicEvent setGoods(Goods goods) {
        this.goods = goods;
        return this;
    }
}
