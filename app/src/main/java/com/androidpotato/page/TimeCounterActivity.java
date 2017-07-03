package com.androidpotato.page;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidpotato.R;


/**
 * Created by DavidSmith on 2016/6/11 0011.
 */
public class TimeCounterActivity extends AppCompatActivity {
    private static final String TAG = "TimeCounterActivity";
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_time_counter);
        init();
    }
    private void init() {
        Button backBtn = (Button) findViewById(R.id.page_timeCounter_backBtn_Btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeCounterActivity.this.finish();
            }
        });
        textView = (TextView) findViewById(R.id.page_timeCounter_timeText_Tv);
        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(20000, 1000);
        myCountDownTimer.start();
    }
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            Log.i(TAG, "Count down timer onFinish");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("" + 0);
                }
            });
        }


        @Override
        public void onTick(final long millisUntilFinished) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("0:" + (millisUntilFinished/1000));
                }
            });
        }
    }
}

