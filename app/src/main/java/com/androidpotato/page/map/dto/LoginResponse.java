package com.androidpotato.page.map.dto;

/**
 * Created by David on 2018/3/14 0014.
 */

public class LoginResponse extends BaseResponse {
    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
