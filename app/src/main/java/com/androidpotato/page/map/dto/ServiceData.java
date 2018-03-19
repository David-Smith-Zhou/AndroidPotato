package com.androidpotato.page.map.dto;

/**
 * Created by David on 2018/3/19 0019.
 */

public class ServiceData {
    private String serviceId;
    private Object serviceData;

    public void setServiceData(Object serviceData) {
        this.serviceData = serviceData;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getServiceData() {
        return serviceData;
    }

    public String getServiceId() {
        return serviceId;
    }
}
