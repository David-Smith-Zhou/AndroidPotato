package com.androidpotato.page.map;

import android.os.AsyncTask;

import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.common.comminfos.EthernetInfo;
import com.davidzhou.library.communication.common.dto.BaseDataInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;
import com.davidzhou.library.communication.handler.CommMainMsgHandlerCallback;
import com.davidzhou.library.communication.net.socket.async.AsyncSocket;
import com.davidzhou.library.util.ULog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by David on 2018/3/19 0019.
 */

public class LocationManager {
    private static final String TRANSLATE_URL = "http://api.map.baidu.com/geoconv/v1/";
    private static final String address = "119.29.162.110";
    private static final int port = 1236;
    private static final String TAG = "LocationManager";
    private static final String SERVICE_DATA_TAG = "serviceData";
    private static final String SERVICE_ID_TAG = "serviceId";
    private static final String CUSTOM_DATA_TAG = "message1";
    private static final String REQUEST_PARAMS_TAG = "resultParams";
    private static final String GPIO_TAG = "GPS01";
    private static final String EMPTY_STR = "";
    private static final String TYPE_REPORT = "report-dev-callback";
    private AsyncSocket mSocket;
    private OnLocationUpdateCallback onLocationUpdateCallback;
    private boolean isCheck = false;


    public LocationManager() {
        EthernetInfo info = new EthernetInfo(CommInfo.Type.ETHERNET);
        info.setAddress(address);
        info.setPort(port);
        mSocket = new AsyncSocket(info);
        mSocket.setCommMainMsgHandlerCallback(commMainMsgHandlerCallback);
    }

    public void send() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "commd1");
            JSONObject params = new JSONObject();
            params.put("GPS01", "123");
            jsonObject.put("params", params);
            mSocket.write(jsonObject.toString().getBytes());
            isCheck = true;
            if (onLocationUpdateCallback != null) {
                onLocationUpdateCallback.onRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mSocket.write();
    }

    public void connect() {
        mSocket.connect();
    }

    public void disconnect() {
        mSocket.disconnect();
    }

    private CommMainMsgHandlerCallback commMainMsgHandlerCallback = new CommMainMsgHandlerCallback() {
        @Override
        public void onConnect() {
            ULog.d(TAG, "onConnect");
        }

        @Override
        public void onDisconnect() {
            ULog.d(TAG, "onDisconnect");

        }

        @Override
        public void onRecvData(BaseDataInfo dataInfo) {
            ULog.d(TAG, "onRecvData: " + dataInfo.toString());
            ULog.d(TAG, "onRecvDataStr: " + new String(dataInfo.getData()));
            try {
                JSONObject jsonObject = new JSONObject(new String(dataInfo.getData()));
                String type = jsonObject.getString("type");
                if (TYPE_REPORT.equals(type)) {
                    parseReportData(jsonObject.getString("data"));
                } else {
                    parseResponseData(jsonObject.getString("data"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            parseReportData(new String(dataInfo.getData()));
        }

        @Override
        public void onError(ErrorMsg msg) {
            ULog.d(TAG, "onError");
        }
    };

    private void parseResponseData(String responseStr) {
        ULog.d(TAG, "parseResponseData: " + responseStr);
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            if (!jsonObject.has(REQUEST_PARAMS_TAG)) {
                return;
            }
            JSONObject result = jsonObject.getJSONObject(REQUEST_PARAMS_TAG);
            String data = result.getString(GPIO_TAG);

            String[] dataSplit = data.split(",");
            ULog.i(TAG, "dataSplit: " + Arrays.toString(dataSplit));
            String longitudeStr // 经度
                    , latitudeStr;  // 纬度
            longitudeStr = dataSplit[3];
            latitudeStr = dataSplit[1];
            if (!EMPTY_STR.equals(latitudeStr) && !EMPTY_STR.equals(longitudeStr)) {
                double longitude = getBaiduMapLocation(Double.valueOf(longitudeStr));
                double latitude = getBaiduMapLocation(Double.valueOf(latitudeStr));

//                double longitude = Double.valueOf(longitudeStr);
//                double latitude = Double.valueOf(latitudeStr);
//                translate(longitude, latitude);
                MyAsyncTask task = new MyAsyncTask(this);
                task.execute(longitude, latitude);

//                ULog.i(TAG,"longitude: " + longitude + ",latitude: " + latitude);
//                if (onLocationUpdateCallback != null) {
//                    onLocationUpdateCallback.onResponse(longitude, latitude);
//                }
            } else {
                callOnErrorCallback("地址信息为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseReportData(String receiveStr) {
        ULog.d(TAG, "parseReportData: " + receiveStr);
        try {
            JSONObject jsonObject = new JSONObject(receiveStr);
            JSONArray serviceDataArrays = jsonObject.getJSONArray("serviceData");
            for (int i = 0; i < serviceDataArrays.length(); i++) {
                JSONObject tmpObj = serviceDataArrays.getJSONObject(i);
                if (CUSTOM_DATA_TAG.equals(tmpObj.getString(SERVICE_ID_TAG))) {
                    JSONObject dataObj = new JSONObject(tmpObj.getString(SERVICE_DATA_TAG));
                    String data = dataObj.getString(GPIO_TAG);
                    ULog.i(TAG, "data: " + data);
                    String[] dataSplit = data.split(",");
                    ULog.i(TAG, "dataSplit: " + Arrays.toString(dataSplit));
                    String longitudeStr // 经度
                            , latitudeStr;  // 纬度
                    longitudeStr = dataSplit[3];
                    latitudeStr = dataSplit[1];
                    if (!EMPTY_STR.equals(latitudeStr) && !EMPTY_STR.equals(longitudeStr)) {
                        double longitude = getBaiduMapLocation(Double.valueOf(longitudeStr));
                        double latitude = getBaiduMapLocation(Double.valueOf(latitudeStr));


//                        double longitude = Double.valueOf(longitudeStr);
//                        double latitude = Double.valueOf(latitudeStr);
//                        translate(longitude, latitude);
                        MyAsyncTask task = new MyAsyncTask(this);
                        task.execute(longitude, latitude);

                    } else {
                        callOnErrorCallback("地址信息为空");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOnErrorCallback(String msg) {
        if (onLocationUpdateCallback != null) {
            onLocationUpdateCallback.onError(msg);
        }
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
                String resultStr = response.body().string();
                ULog.i(TAG, "useOKHttp: result: " + resultStr);
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    int status = jsonObject.getInt("status");
                    if (0 == status) {
                        JSONArray array = jsonObject.getJSONArray("result");
                        JSONObject result = array.getJSONObject(0);
                        double longitudeRes = result.getDouble("x");
                        double latitudeRes = result.getDouble("y");
                        ULog.i(TAG, "longitude: " + longitudeRes + ",latitude: " + latitudeRes);
                        if (onLocationUpdateCallback != null) {
                            if (isCheck) {
                                isCheck = false;
                                onLocationUpdateCallback.onResponse(longitudeRes, latitudeRes);
                            } else {
                                onLocationUpdateCallback.onUpdate(longitudeRes, latitudeRes);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getBaiduMapLocation(final double value) {
        ULog.i(TAG, "value: " + value);
        double tail = value % 100;
        ULog.i(TAG, "tail: " + tail);
        double head = value - tail;
        ULog.i(TAG, "head: " + head);
        return head / 100 + tail / 60;
    }

    public interface OnLocationUpdateCallback {
        void onUpdate(double longitude, double latitude);

        void onRequest();

        void onResponse(double longitude, double latitude);

        void onError(String msg);
    }

    public void setOnLocationUpdateCallback(OnLocationUpdateCallback onLocationUpdateCallback) {
        this.onLocationUpdateCallback = onLocationUpdateCallback;
    }

    static class MyAsyncTask extends AsyncTask {
        private WeakReference<LocationManager> weakReference;

        public MyAsyncTask(LocationManager locationManager) {
            weakReference = new WeakReference<LocationManager>(locationManager);
        }

        @Override
        protected void onPreExecute() {
            ULog.i(TAG, "onPreExecute: ");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ULog.i(TAG, "doInBackground: " + objects);
            LocationManager locationManager = weakReference.get();
            if (locationManager == null) {
                return null;
            }
            locationManager.useOKHttp((double) objects[0], (double) objects[1]);

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
