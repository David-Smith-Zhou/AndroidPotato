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
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.features.JniFeature;
import com.davidzhou.library.util.ULog;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Created by David on 2018/3/27 0027.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestActivity";
    public static final String LOCAL_ACTION = "local_action_test";
    private static final int MSG_WORK = 0x01;
    private static final int MSG_MAIN = 0x02;
    private Handler handler;
    private ScrollView scrollView;
    private TextView textView;
    private LocalBroadcastManager localBroadcastManager;
    private ExecutorService executor;
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
                        ULog.i(TAG, "MSG_WORK");
                        break;
                    case MSG_MAIN:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ULog.i(TAG, "MSG_MAIN");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };

        executor = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_testBtn_1: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(MSG_WORK);
                    }
                }).start();
            }
            break;
            case R.id.test_testBtn_2: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(MSG_MAIN);
                    }
                }).start();
            }
            break;
            case R.id.test_testBtn_3: {
                showMsg("WTF");
                Intent intent = new Intent(TestActivity.this, MyIntentService.class);
                startService(intent);
            }
            break;
            case R.id.test_testBtn_4: {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ULog.i(TAG, "before sleep");
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ULog.i(TAG, "after sleep");
                    }
                });
            }
            break;
            case R.id.test_testBtn_5: {
                ScheduledFuture<?> scheduledFuture = ((ScheduledExecutorService) executor).scheduleAtFixedRate(runnable,
                        0,
                        2,
                        TimeUnit.SECONDS);
            }
            break;
            case R.id.test_testBtn_6: {
                executor.shutdown();
            }
            break;
            case R.id.test_testBtn_7: {
                ULog.i(TAG, "I get: " + JniFeature.getRandom());
                ULog.i(TAG, JniFeature.getFormatString("123"));
            }
            break;
            default:
                break;
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ULog.i(TAG, "before sleep");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ULog.i(TAG, "after sleep");
        }
    };
    private Callable<Integer> callable = new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
            ULog.i(TAG, "before sleep");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ULog.i(TAG, "after sleep");
            return 1;
        }
    };

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
        ULog.i(TAG, msg);
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
