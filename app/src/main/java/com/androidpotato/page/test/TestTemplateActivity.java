package com.androidpotato.page.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidpotato.R;
import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.page.BaseActivity;
import com.davidzhou.library.util.ULog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_1;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_2;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_3;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_4;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_5;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_INDEX_6;
import static com.androidpotato.page.test.TestTemplateActivity.BtnIndex.BTN_UNKNOWN;

/**
 * Created by David on 2018/6/29.
 */

public class TestTemplateActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "TestTemplateActivity";

    public enum BtnIndex {
        BTN_UNKNOWN,
        BTN_INDEX_1,
        BTN_INDEX_2,
        BTN_INDEX_3,
        BTN_INDEX_4,
        BTN_INDEX_5,
        BTN_INDEX_6,
    }

    @BindView(R.id.common_test_text_scrollView)
    ScrollView mTextScrollView;
    @BindView(R.id.common_test_textView)
    TextView mTextView;

    private CommonTestCallback mCommonTestCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ULog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_common_test);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public final void showText(String text) {
        mTextView.append(text);
        mTextScrollView.fullScroll(View.FOCUS_DOWN);
    }
    @OnClick({
            R.id.common_test_clear_btn,
            R.id.common_test_btn_1,
            R.id.common_test_btn_2,
            R.id.common_test_btn_3,
            R.id.common_test_btn_4,
            R.id.common_test_btn_5,
            R.id.common_test_btn_6,
    })
    @Override
    public void onClick(View v) {
        BtnIndex index = BTN_UNKNOWN;
        switch (v.getId()) {
            case R.id.common_test_btn_1:
                index = BTN_INDEX_1;
                break;
            case R.id.common_test_btn_2:
                index = BTN_INDEX_2;
                break;
            case R.id.common_test_btn_3:
                index = BTN_INDEX_3;
                break;
            case R.id.common_test_btn_4:
                index = BTN_INDEX_4;
                break;
            case R.id.common_test_btn_5:
                index = BTN_INDEX_5;
                break;
            case R.id.common_test_btn_6:
                index = BTN_INDEX_6;
                break;
        }
        if (index != BTN_UNKNOWN && mCommonTestCallback != null) {
            mCommonTestCallback.onBtnClicked(index);
        }
    }
    public void setCommonTestCallback (CommonTestCallback callback) {
        this.mCommonTestCallback = callback;
    }

    @Override
    protected void onStart() {
        ULog.i(TAG, "onStart");
        super.onStart();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.start();
        }
    }

    @Override
    protected void onResume() {
        ULog.i(TAG, "onResume");
        super.onResume();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.resume();
        }
    }

    @Override
    protected void onDestroy() {
        ULog.i(TAG, "onDestroy");
        super.onDestroy();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.destroy();
        }
    }

    @Override
    protected void onPause() {
        ULog.i(TAG, "onPause");
        super.onPause();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.pause();
        }
    }

    @Override
    protected void onRestart() {
        ULog.i(TAG, "onRestart");
        super.onRestart();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.restart();
        }
    }

    @Override
    protected void onStop() {
        ULog.i(TAG, "onStop");
        super.onStop();
        if (mCommonTestCallback != null) {
            mCommonTestCallback.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
