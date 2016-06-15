package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.express.subao.R;
import com.express.subao.box.SdyBoxObj;
import com.express.subao.box.handlers.SdyBoxObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

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

    private SdyBoxObj mSdyBoxObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdybox_content);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back, R.id.sdybox_content_cover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.sdybox_content_cover:
                jumpImageListActivity();
        }
    }

    public void jumpImageListActivity() {
        Bundle b = new Bundle();
        b.putStringArrayList("DataList",
                (ArrayList<String>) getImageList(mSdyBoxObj.getCover()));
        b.putInt("position", 0);
        Passageway.jumpActivity(context, ImageListActivity.class, b);
    }

    private List<String> getImageList(String imgURL) {
        List<String> imgList = new ArrayList<String>();
        imgList.add(imgURL);
        return imgList;
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.sdy_detailed_text));

        mSdyBoxObj = SdyBoxObjHandler.getSdyBoxObj();
        if (mSdyBoxObj != null) {
            title.setText(mSdyBoxObj.getTitle());
            address.setText(mSdyBoxObj.getAddress());
            DownloadImageLoader.loadImage(cover, mSdyBoxObj.getCover());
        }
    }

}
