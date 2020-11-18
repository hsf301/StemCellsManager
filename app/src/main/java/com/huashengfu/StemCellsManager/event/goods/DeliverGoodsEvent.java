package com.huashengfu.StemCellsManager.event.goods;

import com.huashengfu.StemCellsManager.entity.goods.Orders;

public class DeliverGoodsEvent {
    private Orders orders;

    public Orders getOrders() {
        return orders;
    }

    public DeliverGoodsEvent setOrders(Orders orders) {
        this.orders = orders;
        return this;
    }
}
