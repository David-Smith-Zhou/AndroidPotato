package com.androidpotato.page.map.dto;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * Created by David on 2018/3/19 0019.
 */

public class RecvData {
    private String lastMessageTime;
    private String devSerial;
    private List<ServiceData> serviceDatas;
    private String createTime;

    public void setServiceDatas(List<ServiceData> serviceDatas) {
        this.serviceDatas = serviceDatas;
    }

    public void setDevSerial(String devSerial) {
        this.devSerial = devSerial;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDevSerial() {
        return devSerial;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public List<ServiceData> getServiceDatas() {
        return serviceDatas;
    }

    public String getCreateTime() {
        return createTime;
    }
}
