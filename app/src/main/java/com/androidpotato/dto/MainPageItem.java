package com.androidpotato.dto;

/**
 * Created by David on 2017/7/19 0019.
 */

public class MainPageItem {
    private String name;
    private Class<?> clz;

    public void setName(String itemName) {
        this.name = itemName;
    }

    public String getName() {
        return name;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public Class<?> getClz() {
        return clz;
    }
}
