package com.androidpotato.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.androidpotato.R;
import com.androidpotato.application.MyApp;

/**
 * Created by David on 2018/7/2.
 */

public class BaseActivity extends AppCompatActivity {
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
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
    public final void goToNewPage(Class<?> clzz) {
        Intent intent = new Intent(this, clzz);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }
}
