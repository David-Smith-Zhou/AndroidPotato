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
import com.androidpotato.page.map.MapActivity;
import com.androidpotato.page.wifi.WifiActivity;
import com.davidzhou.library.util.LogUtil;

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
//        addItemWithMainPageItem(TimeCounterActivity.class, this.getString(R.string.mainPage_Counter));
//        addItemWithMainPageItem(WebViewActivity.class, this.getString(R.string.mainPage_WebBrowser));
        addItemWithMainPageItem(CameraActivity.class, this.getString(R.string.mainPage_Camera));
        addItemWithMainPageItem(TouchActivity.class, this.getString(R.string.mainPage_TouchView));
//        addItemWithMainPageItem(HttpActivity.class, this.getString(R.string.mainPage_Http));
        addItemWithMainPageItem(BluetoothSSPActivity.class, this.getString(R.string.mainPage_Bluetooth));
//        addItemWithMainPageItem(AIDLActivity.class, this.getString(R.string.mainPage_Aidl));
        addItemWithMainPageItem(WifiActivity.class, this.getString(R.string.mainPage_Wifi));
//        addItemWithMainPageItem(SocketActivity.class, this.getString(R.string.mainPage_Socket));
        addItemWithMainPageItem(MapActivity.class, getString(R.string.mainPage_Map));
        addItemWithMainPageItem(AsyncTaskTestActivity.class, getString(R.string.mainPage_AsyncTaskTest));
        homeAdapter.notifyDataSetChanged();
    }
    private void addItemWithMainPageItem(Class<?> cls, String name) {
        MainPageItem aidlItem = new MainPageItem();
        Intent intent = new Intent().setClass(this, cls);
        aidlItem.setIntent(intent);
        aidlItem.setName(name);
        items.add(aidlItem);
    }

    private HomeAdapter.OnHomeAdapterListener onHomeAdapterListener = new HomeAdapter.OnHomeAdapterListener() {
        @Override
        public void onItemClick(View view, int position) {
            LogUtil.i(TAG, "onClick: item : " + position);
            startActivity(items.get(position).getIntent());
        }

        @Override
        public void onItemLongClick(View view, int position) {
            LogUtil.i(TAG, "onLongClick: item : " + position);
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
