package com.androidpotato.page.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.LogUtil;


/**
 * Created by David on 2018/3/27 0027.
 */

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    public static final String LOCAL_ACTION = "local_action_test";
    private static final int MSG_WORK = 0x01;
    private static final int MSG_MAIN = 0x02;
    private Handler handler;
    private ScrollView scrollView;
    private TextView textView;
    private LocalBroadcastManager localBroadcastManager;

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
        scrollView = this.findViewById(R.id.test_scrollView);
        textView = this.findViewById(R.id.test_textView);
        registerBroadcast();

        TestHandlerThread handlerThread = new TestHandlerThread("TestHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_WORK:
                        LogUtil.i(TAG, "MSG_WORK");
                        break;
                    case MSG_MAIN:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtil.i(TAG, "MSG_MAIN");
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
        testBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg("WTF");
                Intent intent = new Intent(TestActivity.this, MyIntentService.class);
                startService(intent);
            }
        });
    }
    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter(LOCAL_ACTION);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }
    private void unRegisterBroadcast() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null
                    && intent.getAction() != null
                    && intent.getAction().equals(LOCAL_ACTION)) {
                showMsg(intent.getExtras().getString("msg"));
            }
        }
    };

    private void showMsg(String msg) {
        if (textView.getHeight() > 1024) {
            textView.setText("");
        }
        textView.append(msg + "\n");
        scrollView.fullScroll(View.FOCUS_DOWN);
        LogUtil.i(TAG, msg);
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
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
