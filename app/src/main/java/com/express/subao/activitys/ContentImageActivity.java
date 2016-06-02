package com.express.subao.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.MessageHandler;
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
 * Created by Hua on 15/8/31.
 */
public class ContentImageActivity extends BaseActivity {

    public static final String TITLE_NAME = "title_name";
    public static final String IMAGE_ID = "image_id";

    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.content_iamge)
    private ImageView contentIamge;
    @ViewInject(R.id.comtent_scroll)
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_image);
        context = this;
        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick({R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            titleName.setText(b.getString(TITLE_NAME));
            DownloadImageLoader.loadImageForID(contentIamge, b.getInt(IMAGE_ID));
        } else {
            MessageHandler.showToast(context, "页面跑到外太空去了>_<");
            finish();
        }

        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }

}
