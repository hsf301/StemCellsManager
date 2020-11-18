package com.huashengfu.StemCellsManager.entity.service;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceTypeGroup implements Serializable {

    private String name;
    private ArrayList<ServiceType> serviceTypes = new ArrayList<>();
    private int select = -1;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public ServiceType getServiceType(){
        if(select < 0)
            return null;
        else
            return serviceTypes.get(select);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(ArrayList<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }
}
