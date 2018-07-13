package com.androidpotato.page;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.androidpotato.R;
import com.androidpotato.adapter.HomeAdapter;
import com.androidpotato.dto.MainPageItem;
import com.androidpotato.page.map.MapActivity;
import com.androidpotato.page.test.TestActivity;
import com.androidpotato.page.wifi.WifiActivity;
import com.davidzhou.library.util.ULog;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity{

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
        recyclerView = (RecyclerView) this.findViewById(R.id.mainPage_RecyclerView);
        items = new ArrayList<>();
        homeAdapter = new HomeAdapter(MainActivity.this, items);
        homeAdapter.setOnHomeAdapterListener(onHomeAdapterListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeAdapter);
        initData();
    }

    private void initData() {
        addItemWithMainPageItem(FileTestActivity.class, "文件测试");
        addItemWithMainPageItem(CameraActivity.class, this.getString(R.string.mainPage_Camera));
        addItemWithMainPageItem(TouchActivity.class, this.getString(R.string.mainPage_TouchView));
        addItemWithMainPageItem(HttpActivity.class, this.getString(R.string.mainPage_Http));
//        addItemWithMainPageItem(AIDLActivity.class, this.getString(R.string.mainPage_Aidl));
        addItemWithMainPageItem(WifiActivity.class, this.getString(R.string.mainPage_Wifi));
        addItemWithMainPageItem(MapActivity.class, getString(R.string.mainPage_Map));
        addItemWithMainPageItem(TestActivity.class, getString(R.string.mainPage_Test));
        homeAdapter.notifyDataSetChanged();
    }
    private void addItemWithMainPageItem(Class<?> cls, String name) {
        MainPageItem aidlItem = new MainPageItem();
        aidlItem.setName(name);
        aidlItem.setClz(cls);
        items.add(aidlItem);
    }

    private HomeAdapter.OnHomeAdapterListener onHomeAdapterListener = new HomeAdapter.OnHomeAdapterListener() {
        @Override
        public void onItemClick(View view, int position) {
            ULog.i(TAG, "onClick: item : " + position);
            goToNewPage(items.get(position).getClz());
        }

        @Override
        public void onItemLongClick(View view, int position) {
            ULog.i(TAG, "onLongClick: item : " + position);
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
