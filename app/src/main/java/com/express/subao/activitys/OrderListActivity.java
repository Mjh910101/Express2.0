package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.client.MyWebChromeClient;
import com.express.subao.client.MyWebViewClient;
import com.express.subao.handlers.TextHandeler;
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
 * Created by Hua on 16/5/31.
 */
public class OrderListActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backBtn;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
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
        titleName.setText(TextHandeler.getText(context, R.string.query_express_text));

    }

}
