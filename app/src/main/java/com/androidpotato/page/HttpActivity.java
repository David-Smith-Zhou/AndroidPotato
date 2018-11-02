package com.androidpotato.page;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.page.test.TestTemplateActivity;
import com.davidzhou.library.util.ULog;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by David on 2017/6/26 0026.
 */

public class HttpActivity extends TestTemplateActivity implements CommonTestCallback {
    private static final String TAG = "HttpActivity";
    private static final int BRANCH_CODE = 1;
    private static final String TRANSLATE_URL = "http://api.map.baidu.com/geoconv/v1/";
    private static final String DEVICE_SN = "testvirtualdevice";
    private static final double LONGITUDE = 113.567794;
    private static final double LATITUDE = 22.324640;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCommonTestCallback(this);
    }

    @Override
    public void onBtnClicked(BtnIndex position) {
        switch (position) {
            case BTN_INDEX_1:
                execHttpRequest();
                break;
        }
    }

    private void execHttpRequest() {
        MyAsyncTask task = new MyAsyncTask(HttpActivity.this);
        task.execute();
    }

    private void useOKHttp(double longitude, double latitude) {
        try {
            String address = "http://api.map.baidu.com/geoconv/v1/?" +
                    "coords=" + longitude + "," + latitude + "&from=1&to=5" +
                    "&mcode=3B:9D:6E:18:00:F2:89:95:F1:BC:D4:CF:86:C2:F8:3A:21:AE:64:AC;com.androidpotato" +
                    "&ak=3H7yalkLbGG13xMiLRwfCClV";
            ULog.i(TAG, "useOKHttp: address: " + address);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ULog.i(TAG, "useOKHttp: result: " + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void useHttpURLConnection(double longitude, double latitude) {
        try {
            String address = "http://api.map.baidu.com/geoconv/v1/?" +
                    "coords=" + longitude + "," + latitude + "&from=1&to=5" +
                    "&mcode=3B:9D:6E:18:00:F2:89:95:F1:BC:D4:CF:86:C2:F8:3A:21:AE:64:AC;com.androidpotato" +
                    "&ak=3H7yalkLbGG13xMiLRwfCClV";
            ULog.i(TAG, "useHttpURLConnection: address: " + address);
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                String result = streamToString(inputStream);
                ULog.i(TAG, "useHttpURLConnection: result: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String streamToString(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int count = inputStream.read(buffer);
        byte[] data = new byte[count];
        System.arraycopy(buffer, 0, data, 0, count);
        return new String(data);
    }

    static class MyAsyncTask extends AsyncTask {
        private WeakReference<HttpActivity> weakReference;

        public MyAsyncTask(HttpActivity httpActivity) {
            weakReference = new WeakReference<HttpActivity>(httpActivity);
        }

        @Override
        protected void onPreExecute() {
            ULog.i(TAG, "onPreExecute: ");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ULog.i(TAG, "doInBackground: " + objects);
            HttpActivity httpActivity = weakReference.get();
            if (httpActivity == null
                    || httpActivity.isFinishing()
                    || httpActivity.isDestroyed()) {
                return null;
            }
            httpActivity.useHttpURLConnection(LONGITUDE, LATITUDE);
            httpActivity.useOKHttp(LONGITUDE, LATITUDE);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            ULog.i(TAG, "onProgressUpdate: " + values);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            ULog.i(TAG, "onPostExecute: " + o);
            super.onPostExecute(o);
        }

        @Override
        protected void onCancelled() {
            ULog.i(TAG, "onCancelled: ");
            super.onCancelled();
        }
    }
}
