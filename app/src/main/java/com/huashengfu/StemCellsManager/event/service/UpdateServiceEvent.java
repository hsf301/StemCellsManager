package com.huashengfu.StemCellsManager.event.service;

import com.huashengfu.StemCellsManager.entity.service.Service;

public class UpdateServiceEvent {

    private Service service;

    public Service getService() {
        return service;
    }

    public UpdateServiceEvent setService(Service service) {
        this.service = service;
        return this;
    }
}
