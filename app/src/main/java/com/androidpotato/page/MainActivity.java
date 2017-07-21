package com.androidpotato.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.androidpotato.R;
import com.androidpotato.adapter.HomeAdapter;
import com.androidpotato.dto.MainPageItem;
import com.androidpotato.mylibrary.util.UtilLog;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<MainPageItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }
    private void init() {
        initView();
        items = new ArrayList<>();
        homeAdapter = new HomeAdapter(MainActivity.this, items);
        homeAdapter.setOnHomeAdapterListener(onHomeAdapterListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeAdapter);
        initData();
    }
    private void initView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.mainPage_RecyclerView);

    }

    private void initData() {
        MainPageItem timeCount = new MainPageItem();
        Intent intent = new Intent().setClass(this, TimeCounterActivity.class);
        timeCount.setIntent(intent);
        timeCount.setName(this.getString(R.string.mainPage_Counter));
        items.add(timeCount);

        MainPageItem webViewItem = new MainPageItem();
        intent = new Intent().setClass(this, WebViewActivity.class);
        webViewItem.setIntent(intent);
        webViewItem.setName(this.getString(R.string.mainPage_WebBrowser));
        items.add(webViewItem);

        MainPageItem cameraItem = new MainPageItem();
        intent = new Intent().setClass(this, CameraActivity.class);
        cameraItem.setIntent(intent);
        cameraItem.setName(this.getString(R.string.mainPage_Camera));
        items.add(cameraItem);

        MainPageItem touchItem = new MainPageItem();
        intent = new Intent().setClass(this, TouchActivity.class);
        touchItem.setIntent(intent);
        touchItem.setName(this.getString(R.string.mainPage_TouchView));
        items.add(touchItem);

        MainPageItem httpItem = new MainPageItem();
        intent = new Intent().setClass(this, HttpActivity.class);
        httpItem.setIntent(intent);
        httpItem.setName(this.getString(R.string.mainPage_Http));
        items.add(httpItem);

        MainPageItem bluetoothItem = new MainPageItem();
        intent = new Intent().setClass(this, BluetoothSSPActivity.class);
        bluetoothItem.setIntent(intent);
        bluetoothItem.setName(this.getString(R.string.mainPage_Bluetooth));
        items.add(bluetoothItem);

        MainPageItem aidlItem = new MainPageItem();
        intent = new Intent().setClass(this, AIDLActivity.class);
        aidlItem.setIntent(intent);
        aidlItem.setName(this.getString(R.string.mainPage_Aidl));
        items.add(aidlItem);
        homeAdapter.notifyDataSetChanged();
    }

    private HomeAdapter.OnHomeAdapterListener onHomeAdapterListener = new HomeAdapter.OnHomeAdapterListener() {
        @Override
        public void onItemClick(View view, int position) {
            UtilLog.i(TAG, "onClick: item : " + position);
            startActivity(items.get(position).getIntent());
        }

        @Override
        public void onItemLongClick(View view, int position) {
            UtilLog.i(TAG, "onLongClick: item : " + position);
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
