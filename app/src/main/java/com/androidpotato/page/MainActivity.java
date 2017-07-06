package com.androidpotato.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.androidpotato.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainPage_timeCounter_Btn:
                Intent intent = new Intent().setClass(this, TimeCounterActivity.class);
                startActivity(intent);
                break;
            case R.id.mainPage_WebView_Btn:
                intent = new Intent().setClass(this, WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.mainPage_Camera_Btn:
                intent = new Intent().setClass(this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.mainPage_Touch_Btn:
                intent = new Intent().setClass(this, TouchActivity.class);
                startActivity(intent);
                break;
            case R.id.mainPage_Http_Btn:
                intent = new Intent().setClass(this, HttpActivity.class);
                startActivity(intent);
                break;
            default:
                Log.e(TAG, "In onClick: default block: id = " + v.getId());
                break;
        }
    }

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
