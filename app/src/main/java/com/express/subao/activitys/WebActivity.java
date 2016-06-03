package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.client.MyWebChromeClient;
import com.express.subao.client.MyWebViewClient;
import com.express.subao.interfaces.CallbackForString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/11.
 */
public class WebActivity extends BaseActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";

    @ViewInject(R.id.title_back)
    private ImageView backBtn;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.web_contentWeb)
    private WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        context = this;
        ViewUtils.inject(this);

        initAcitvity();
    }

    @OnClick({R.id.title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void initAcitvity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);

        content.getSettings().setJavaScriptEnabled(true);
        MyWebViewClient clien = new MyWebViewClient(context);
        clien.notJump();
        content.setWebViewClient(clien);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String t = b.getString(TITLE);
            if (t == null || t.equals("null") || t.equals("")) {
                content.setWebChromeClient(new MyWebChromeClient(
                        new CallbackForString() {
                            @Override
                            public void callback(String result) {
                                titleName.setText(result);
                            }
                        }));
            } else {
                titleName.setText(t);
            }
            content.loadUrl(b.getString(URL));
        } else {
            finish();
        }

    }

}
