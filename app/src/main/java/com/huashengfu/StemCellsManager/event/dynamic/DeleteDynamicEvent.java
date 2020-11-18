package com.huashengfu.StemCellsManager.event.dynamic;

import com.huashengfu.StemCellsManager.entity.dynamic.Dynamic;

public class DeleteDynamicEvent {
    private Dynamic dynamic;

    public Dynamic getDynamic() {
        return dynamic;
    }

    public DeleteDynamicEvent setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
        return this;
    }
}
