package com.androidpotato.page;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by David on 2018/3/20 0020.
 */

public class AsyncTaskTestActivity extends AppCompatActivity {
    private static final String TAG = "AsyncTaskTestActivity";
    private ScrollView scrollView;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_async_task_test);
        init();
    }
    private void init() {
        scrollView = findViewById(R.id.async_task_test_scrollView);
        textView = findViewById(R.id.async_task_test_textView);
        Toolbar toolbar = findViewById(R.id.async_task_test_toolbar);
        toolbar.setTitle(getString(R.string.mainPage_AsyncTaskTest));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        for (int i = 0; i < 3; i++) {
            MyAsyncTask task = new MyAsyncTask(AsyncTaskTestActivity.this);
            task.execute();
        }
    }
    static class MyAsyncTask extends AsyncTask {
        private WeakReference<AsyncTaskTestActivity> weakReference;

        public MyAsyncTask(AsyncTaskTestActivity activity) {
            this.weakReference = new WeakReference<AsyncTaskTestActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LogUtil.i(TAG, "name: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId() + ", onPreExecute");
        }

        @Override
        protected Integer doInBackground(Object[] objects) {
            for (int i = 0; i < 10; i++) {
                try {
                    LogUtil.i(TAG, "name: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId() + ", doInBackground");
                    publishProgress();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 1000;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            LogUtil.i(TAG, "name: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId() + ", onProgressUpdate");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            LogUtil.i(TAG, "name: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId() + ", onPostExecute");
            AsyncTaskTestActivity activity = weakReference.get();
            if (activity == null
                    || activity.isFinishing()
                    || activity.isDestroyed()) {
                return;
            }
            super.onPostExecute(o);
        }
    }
    private void showInTextView(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(msg);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
