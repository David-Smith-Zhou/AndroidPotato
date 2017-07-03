package com.androidpotato.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidpotato.R;


/**
 * Created by DavidSmith on 2016/6/11 0011.
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
//    private String webUrl = "http://blog.csdn.net/yuzhiboyi/article/details/7733054";
    private String webUrl = "http://www.baidu.com";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activity_webview);
        init();
    }
    private void init() {
        WebView webView = (WebView) findViewById(R.id.page_activity_webview_WV);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(webUrl);
    }
}
