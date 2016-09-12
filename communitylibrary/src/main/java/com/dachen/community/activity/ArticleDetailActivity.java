package com.dachen.community.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.dachen.community.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener{

    private WebView webview;
//    private Button btn_left;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        initView();
    }

    private void initView() {
        tv_title.setText("帖子详情");
        webview = getViewById(R.id.webview);
        webview.setWebViewClient(new MyWebviewclient());
        //启用支持javascript
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webview.loadUrl("http://blog.csdn.net/knxw0001/article/details/39637273/");
    }

    private class MyWebviewclient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
    }
}
