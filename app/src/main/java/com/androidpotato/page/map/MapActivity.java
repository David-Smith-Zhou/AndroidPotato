package com.androidpotato.page.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.androidpotato.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.davidzhou.library.util.LogUtil;
import com.davidzhou.library.util.ToastUtil;

/**
 * Created by David on 2018/3/13 0013.
 */

public class MapActivity extends AppCompatActivity {
    private static final String TAG = "MapActivity";
    private MapView mapView;
    private LocationManager locationManager;
    private double longitude = 113.960471, latitude = 22.546178;
    private Button checkBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.page_map);
        Toolbar toolbar = findViewById(R.id.map_toolbar);
        toolbar.setTitle("地图显示");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        mapView = findViewById(R.id.mapView);
        showInMap(latitude, longitude);
        locationManager = new LocationManager();
        locationManager.setOnLocationUpdateCallback(new LocationManager.OnLocationUpdateCallback() {
            @Override
            public void onUpdate(final double longitude, final double latitude) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (needUpdate(longitude, latitude)) {
                            LogUtil.i(TAG, "need update");
                            showInMap(latitude, longitude);
                        }
                    }
                });
            }

            @Override
            public void onRequest() {
                checkBtn.setEnabled(false);

            }

            @Override
            public void onResponse(final double longitude, final double latitude) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkBtn.setEnabled(true);
                        showInMap(latitude, longitude);
                    }
                });
            }

            @Override
            public void onError(String msg) {
                ToastUtil.ToastShort(MapActivity.this, msg);
            }
        });
        checkBtn = findViewById(R.id.map_check_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.send();
            }
        });
        locationManager.connect();
    }

    private boolean needUpdate(double longitude, double latitude) {
        if (!(this.longitude == longitude && this.latitude == latitude)) {
            this.latitude = latitude;
            this.longitude = longitude;
            ToastUtil.ToastShort(MapActivity.this, "定位更新中。。。");
            return true;
        } else {
            ToastUtil.ToastShort(MapActivity.this, "定位已接收到，无需更新");
            return false;
        }
    }

    private void showInMap(double latitude, double longitude) {
        BaiduMap map = mapView.getMap();
        map.clear();
        LatLng location = new LatLng(latitude, longitude);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(location)
                .zoom(15)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        map.setMapStatus(mapStatusUpdate);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location);
        OverlayOptions overlayOptions = new MarkerOptions()
                .position(location)
                .icon(bitmap);
        map.addOverlay(overlayOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
