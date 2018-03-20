package com.androidpotato.page.map;

import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.common.comminfos.EthernetInfo;
import com.davidzhou.library.communication.common.dto.BaseDataInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;
import com.davidzhou.library.communication.handler.CommMainMsgHandlerCallback;
import com.davidzhou.library.communication.net.socket.async.AsyncSocket;
import com.davidzhou.library.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by David on 2018/3/19 0019.
 */

public class LocationManager {
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
            LogUtil.d(TAG, "onConnect");
        }

        @Override
        public void onDisconnect() {
            LogUtil.d(TAG, "onDisconnect");

        }

        @Override
        public void onRecvData(BaseDataInfo dataInfo) {
            LogUtil.d(TAG, "onRecvData: " + dataInfo.toString());
            LogUtil.d(TAG, "onRecvDataStr: " + new String(dataInfo.getData()));
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
            LogUtil.d(TAG, "onError");
        }
    };
    private void parseResponseData(String responseStr) {
        LogUtil.d(TAG, "parseResponseData: " + responseStr);
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            if (!jsonObject.has(REQUEST_PARAMS_TAG)) {
                return;
            }
            JSONObject result = jsonObject.getJSONObject(REQUEST_PARAMS_TAG);
            String data = result.getString(GPIO_TAG);

            String[] dataSplit = data.split(",");
            LogUtil.i(TAG, "dataSplit: " + Arrays.toString(dataSplit));
            String longitudeStr // 经度
                    , latitudeStr;  // 纬度
            longitudeStr = dataSplit[3];
            latitudeStr = dataSplit[1];
            if (!EMPTY_STR.equals(latitudeStr) && !EMPTY_STR.equals(longitudeStr)) {
                double longitude = (Double.valueOf(longitudeStr)) / 100;
                double latitude = (Double.valueOf(latitudeStr)) / 100;
                LogUtil.i(TAG,"longitude: " + longitude + ",latitude: " + latitude);
                if (onLocationUpdateCallback != null) {
                    onLocationUpdateCallback.onResponse(longitude, latitude);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseReportData(String receiveStr) {
        LogUtil.d(TAG, "parseReportData: " + receiveStr);
        try {
            JSONObject jsonObject = new JSONObject(receiveStr);
            JSONArray serviceDataArrays = jsonObject.getJSONArray("serviceData");
            for (int i = 0; i < serviceDataArrays.length(); i++) {
                JSONObject tmpObj = serviceDataArrays.getJSONObject(i);
                if (CUSTOM_DATA_TAG.equals(tmpObj.getString(SERVICE_ID_TAG))) {
                    JSONObject dataObj = new JSONObject(tmpObj.getString(SERVICE_DATA_TAG));
                    String data = dataObj.getString(GPIO_TAG);
                    LogUtil.i(TAG, "data: " +data);
                    // $GPGLL,2232.4696,N,11356.7755,E,031005.000,A,A*52
//                    String regex = "$GPGLL,(.),(.),(.),(.),(.)";
//                    Pattern p = Pattern.compile(regex);
//                    Matcher matcher = p.matcher(data);
//                    if (matcher.find()) {
//                        LogUtil.i(TAG, "find: " +data);
//
//                    }
                    String[] dataSplit = data.split(",");
                    LogUtil.i(TAG, "dataSplit: " + Arrays.toString(dataSplit));
                    String longitudeStr // 经度
                            , latitudeStr;  // 纬度
                    longitudeStr = dataSplit[3];
                    latitudeStr = dataSplit[1];
                    if (!EMPTY_STR.equals(latitudeStr) && !EMPTY_STR.equals(longitudeStr)) {
                        double longitude = (Double.valueOf(longitudeStr)) / 100;
                        double latitude = (Double.valueOf(latitudeStr)) / 100;
                        LogUtil.i(TAG,"longitude: " + longitude + ",latitude: " + latitude);
                        if (onLocationUpdateCallback != null) {
                            onLocationUpdateCallback.onUpdate(longitude, latitude);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnLocationUpdateCallback {
        void onUpdate(double longitude, double latitude);
        void onRequest();
        void onResponse(double longitude, double latitude);
    }

    public void setOnLocationUpdateCallback(OnLocationUpdateCallback onLocationUpdateCallback) {
        this.onLocationUpdateCallback = onLocationUpdateCallback;
    }
}
