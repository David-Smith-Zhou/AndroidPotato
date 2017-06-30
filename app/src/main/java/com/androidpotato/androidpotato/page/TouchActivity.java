package com.androidpotato.androidpotato.page;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.TextView;

import com.androidpotato.androidpotato.R;
import com.androidpotato.androidpotato.widget.TouchView;


/**
 * Created by David on 2017/6/20 0020.
 */

public class TouchActivity extends Activity {
    private static final String TAG = "TouchActivity";
    private TextView tv;
    private TouchView touchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_touch);
        initView();
    }

    private void initView() {
        tv = (TextView) this.findViewById(R.id.touch_tv);
        touchView = (TouchView) this.findViewById(R.id.touch_touchView);
    }

    private void setText(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        tv.setText(getString(R.string.touchText, x, y));
    }
}
