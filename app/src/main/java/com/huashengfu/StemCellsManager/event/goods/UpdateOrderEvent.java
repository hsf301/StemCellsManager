package com.huashengfu.StemCellsManager.event.goods;

import com.huashengfu.StemCellsManager.entity.goods.Orders;

public class UpdateOrderEvent {
    private Orders orders;

    public Orders getOrders() {
        return orders;
    }

    public UpdateOrderEvent setOrders(Orders orders) {
        this.orders = orders;
        return this;
    }
}
