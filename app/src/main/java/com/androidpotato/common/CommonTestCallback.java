package com.androidpotato.common;

import com.androidpotato.page.test.TestTemplateActivity;

/**
 * Created by David on 2018/7/2.
 */

public interface CommonTestCallback {
    void start();
    void resume();
    void pause();
    void restart();
    void stop();
    void destroy();
    void onBtnClicked(TestTemplateActivity.BtnIndex position);
}
