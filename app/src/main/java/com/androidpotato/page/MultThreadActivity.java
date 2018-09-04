package com.androidpotato.page;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;

import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.page.test.TestTemplateActivity;
import com.davidzhou.library.util.ULog;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultThreadActivity extends TestTemplateActivity {
    private static final String TAG = "MultThreadActivity";
    private Handler mHandler;
    private MyHandlerThread mMyHandlerThread;
    private ScheduledExecutorService service;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTestBtn1().setText("AsyncTask");
        getTestBtn2().setText("HandlerThread");
        getTestBtn3().setText("ThreadPool");
        getTestBtn4().setText("IntentService");
        setCommonTestCallback(mCommonTestCallback);
        mMyHandlerThread = new MyHandlerThread();
        mMyHandlerThread.start();
        mHandler = new Handler(mMyHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ULog.i(TAG, "handleMessage");
            }
        };
        service = Executors.newScheduledThreadPool(1);
    }

    private CommonTestCallback mCommonTestCallback = new CommonTestCallback() {
        @Override
        public void onBtnClicked(BtnIndex position) {
            switch (position) {
                case BTN_INDEX_1:
                    MultThreadTestAsyncTask task = new MultThreadTestAsyncTask(MultThreadActivity.this);
                    task.execute(5);
                    break;
                case BTN_INDEX_2:
                    mHandler.sendEmptyMessage(0);
                    break;
                case BTN_INDEX_3:
                    service.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            ULog.i(TAG, "scheduleAtFixedRate");
                        }
                    }, 0, 1, TimeUnit.SECONDS);
                    break;
                case BTN_INDEX_4:

                    break;
                case BTN_INDEX_5:
                    break;
                case BTN_INDEX_6:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyHandlerThread.quit();
        service.shutdown();
    }

    private class MyHandlerThread extends HandlerThread {
        public MyHandlerThread() {
            super("MyHandlerThread");
        }
    }

    private static class MultThreadTestAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<MultThreadActivity> weakReference;

        public MultThreadTestAsyncTask(MultThreadActivity activity) {
            weakReference = new WeakReference<>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MultThreadActivity activity = weakReference.get();
            if (activity != null
                    && !activity.isFinishing()
                    && !activity.isDestroyed()) {
                activity.showText("MultThreadTestAsyncTask onPreExecute");
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                ULog.i("doInBackground", "integer: " + integers[0]);
                int i = 0;
                while(i < integers[0]) {
                    publishProgress(i);
                    Thread.sleep(1 * 1000);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "doInBackground done";
        }

        @Override
        protected void onCancelled(String s) {
            ULog.i("onCancelled", "String: " + s);
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            ULog.i("onCancelled", "no params");
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ULog.i("onProgressUpdate", "Integer: " + values[0]);
            super.onProgressUpdate(values);
            MultThreadActivity activity = weakReference.get();
            if (activity != null
                    && !activity.isFinishing()
                    && !activity.isDestroyed()) {
                activity.showText("onProgressUpdate: " + values[0]);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            ULog.i("onPostExecute", "String: " + s);
            super.onPostExecute(s);
            MultThreadActivity activity = weakReference.get();
            if (activity != null
                    && !activity.isFinishing()
                    && !activity.isDestroyed()) {
                activity.showText(s);
            }
        }
    }
}
