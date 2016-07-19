package com.express.subao.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.express.subao.R;
import com.express.subao.client.MyWebChromeClient;
import com.express.subao.client.MyWebViewClient;
import com.express.subao.handlers.TitleHandler;
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
public class OrdinaryWebActivity extends BaseActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";

    @ViewInject(R.id.title_back)
    private ImageView backBtn;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.web_contentWeb)
    private WebView content;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

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
        TitleHandler.setTitle(context, titleLayout);
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);

        content.getSettings().setJavaScriptEnabled(true);
        content.setWebViewClient(new MyWebViewClient(context));
        content.setWebChromeClient(new MyWebChromeClient(
                new CallbackForString() {

                    @Override
                    public void callback(String result) {
                        titleName.setText(result);
                    }
                }));

        Bundle b = getIntent().getExtras();
        if (b != null) {
            titleName.setText(b.getString(TITLE));
            content.loadUrl(b.getString(URL));
        } else {
            finish();
        }

    }



}
