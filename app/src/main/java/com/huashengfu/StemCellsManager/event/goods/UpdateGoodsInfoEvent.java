package com.huashengfu.StemCellsManager.event.goods;

import com.huashengfu.StemCellsManager.entity.goods.Goods;

public class UpdateGoodsInfoEvent {

    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public UpdateGoodsInfoEvent setGoods(Goods goods) {
        this.goods = goods;
        return this;
    }
}