package com.androidpotato.page;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.androidpotato.R;
import com.davidzhou.library.util.LogUtil;

import java.util.Locale;

/**
 * Created by David on 2018/3/27 0027.
 */

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private static final int MSG_WORK = 0x01;
    private static final int MSG_MAIN = 0x02;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.test_toolbar);
        toolbar.setTitle(getString(R.string.mainPage_Test));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TestHandlerThread handlerThread = new TestHandlerThread("TestHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_WORK:
                        showMsg("MSG_WORK");
                        break;
                    case MSG_MAIN:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMsg("MSG_MAIN");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };
        Button testBtn1 = findViewById(R.id.test_testBtn_1);
        Button testBtn2 = findViewById(R.id.test_testBtn_2);
        Button testBtn3 = findViewById(R.id.test_testBtn_3);
        testBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(MSG_WORK);
                    }
                }).start();
            }
        });
        testBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(MSG_MAIN);
                    }
                }).start();
            }
        });
    }

    private void showMsg(String msg) {
        String fmt = "[Thread id- %1$d, Thread name- %2$s]: %3$s";
        String data = String.format(Locale.getDefault(), fmt,
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                msg);
        LogUtil.i(TAG, data);
    }

    public class TestHandlerThread extends HandlerThread {
        public TestHandlerThread(String name) {
            super(name);
        }

        public TestHandlerThread(String name, int priority) {
            super(name, priority);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
