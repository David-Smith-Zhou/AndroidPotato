package com.androidpotato.page;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.androidpotato.R;
import com.davidzhou.library.util.LogUtil;

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

public class HttpActivity extends Activity {
    private static final String TAG = "HttpActivity";
    private static final int BRANCH_CODE = 1;
    private static final String TRANSLATE_URL = "http://api.map.baidu.com/geoconv/v1/";
    private static final String DEVICE_SN = "testvirtualdevice";
    private static final double LONGITUDE = 113.567794;
    private static final double LATITUDE = 22.324640;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_http);
        init();
    }
    private void init() {
        Button debugBtn = findViewById(R.id.http_debug_btn);
        debugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execHttpRequest();
            }
        });
    }
    private void execHttpRequest() {
        MyAsyncTask task = new MyAsyncTask(HttpActivity.this);
        task.execute();
    }

    private void connect() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("pay.smit.com.cn")
                .appendPath("smitup")
                .appendPath("do")
                .appendPath("update")
                .appendPath("check")
                .appendPath("apk")
                .appendQueryParameter("device_sn", DEVICE_SN)
                .appendQueryParameter("version_code", "" + 1)
                .appendQueryParameter("version_name", "V1.0")
                .appendQueryParameter("package_name", "com.smit.terminalapp")
                .appendQueryParameter("branch_code", "" + BRANCH_CODE);
        String url = builder.build().toString();
        LogUtil.i(TAG, "url: " + url);
    }
    private void useOKHttp(double longitude, double latitude) {
        try {
            String address = "http://api.map.baidu.com/geoconv/v1/?" +
                    "coords=" + longitude + "," + latitude + "&from=1&to=5" +
                    "&mcode=3B:9D:6E:18:00:F2:89:95:F1:BC:D4:CF:86:C2:F8:3A:21:AE:64:AC;com.androidpotato" +
                    "&ak=3H7yalkLbGG13xMiLRwfCClV";
            LogUtil.i(TAG, "useOKHttp: address: " + address);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                LogUtil.i(TAG, "useOKHttp: result: " + response.body().string());
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
            LogUtil.i(TAG, "useHttpURLConnection: address: " + address);
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                String result = streamToString(inputStream);
                LogUtil.i(TAG, "useHttpURLConnection: result: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String streamToString(InputStream inputStream) throws IOException{
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
            LogUtil.i(TAG, "onPreExecute: " );
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            LogUtil.i(TAG, "doInBackground: " + objects);
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
            LogUtil.i(TAG, "onProgressUpdate: " + values);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            LogUtil.i(TAG, "onPostExecute: " + o);
            super.onPostExecute(o);
        }

        @Override
        protected void onCancelled() {
            LogUtil.i(TAG, "onCancelled: ");
            super.onCancelled();
        }
    }
}
