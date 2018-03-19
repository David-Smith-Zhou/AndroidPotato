package com.androidpotato.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidpotato.R;

/**
 * Created by David on 2018/3/9 0009.
 */

public class SocketActivity extends AppCompatActivity {
    private static final String TAG = "SocketActivity";
    private ScrollView mTextSv, mFunSv;
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_socket);
        Toolbar toolbar = this.findViewById(R.id.page_socket_toolbar);
        toolbar.setTitle(R.string.mainPage_Socket);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }
    private void init() {
        mTextSv = this.findViewById(R.id.content_socket_text_scrollView);
        mFunSv = this.findViewById(R.id.content_socket_function_scrollView);
        mTextView = this.findViewById(R.id.content_socket_textView);

    }
    private void showText(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTextView.getHeight() > 512) {
                    mTextView.setText("");
                }
                mTextView.append(msg);
                mTextSv.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
