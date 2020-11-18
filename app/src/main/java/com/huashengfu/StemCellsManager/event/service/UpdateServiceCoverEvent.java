package com.huashengfu.StemCellsManager.event.service;

import com.huashengfu.StemCellsManager.entity.service.Service;

public class UpdateServiceCoverEvent {

    private Service service;

    public Service getService() {
        return service;
    }

    public UpdateServiceCoverEvent setService(Service service) {
        this.service = service;
        return this;
    }
}
