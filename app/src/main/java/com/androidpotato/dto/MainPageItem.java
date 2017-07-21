package com.androidpotato.dto;

import android.content.Intent;

/**
 * Created by David on 2017/7/19 0019.
 */

public class MainPageItem {
    private String name;
    private Intent intent;

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getName() {
        return name;
    }
}
