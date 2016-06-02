package com.express.subao.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.express.subao.activitys.WebActivity;
import com.express.subao.tool.Passageway;

public class MyWebViewClient extends WebViewClient {

    public final static String KEY = "URL";
    public final static String COLOR = "COLOR";

    private Context context;
    private int color;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // view.loadUrl(url);// 如果不需要其他对点击链接事件的处理返回true，否则返回false
        Log.e("MyWebViewClient", url);
        Bundle b = new Bundle();
        b.putString(WebActivity.URL, url);
        b.putString(WebActivity.TITLE, "");
        Passageway.jumpActivity(context, WebActivity.class, b);

        return true;

    }
}
