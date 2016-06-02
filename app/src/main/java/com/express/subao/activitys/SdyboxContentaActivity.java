package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.express.subao.R;
import com.express.subao.box.SdyBoxObj;
import com.express.subao.box.handlers.SdyBoxObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.TextHandeler;
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
 * Created by Hua on 16/5/18.
 */
public class SdyboxContentaActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.sdybox_content_title)
    private TextView title;
    @ViewInject(R.id.sdybox_content_address)
    private TextView address;
    @ViewInject(R.id.sdybox_content_cover)
    private ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdybox_content);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.sdy_detailed_text));

        SdyBoxObj obj = SdyBoxObjHandler.getSdyBoxObj();
        if (obj != null) {
            title.setText(obj.getTitle());
            address.setText(obj.getAddress());
            DownloadImageLoader.loadImage(cover, obj.getCover());
        }
    }

}
