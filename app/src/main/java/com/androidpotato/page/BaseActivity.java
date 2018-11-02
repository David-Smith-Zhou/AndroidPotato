package com.androidpotato.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.R;
import com.androidpotato.application.MyApp;
import com.davidzhou.library.util.ULog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by David on 2018/7/2.
 */

public class BaseActivity extends RxAppCompatActivity {
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp myApplication = (MyApp) getApplication();
        myApplication.addToList(this);
    }

    @Override
    public void finish() {
        super.finish();
        MyApp myApplication = (MyApp) getApplication();
        myApplication.removeFromList(this);
        ULog.i(TAG, "mCompositeDisposable.dispose();");
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public final void goToNewPage(Class<?> clzz) {
        Intent intent = new Intent(this, clzz);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }
}
