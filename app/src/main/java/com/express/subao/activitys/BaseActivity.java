package com.express.subao.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVOSCloud;
import com.express.subao.R;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.TitleHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 15/12/21.
 */
public class BaseActivity extends Activity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        AVOSCloud.initialize(this, "2jlEHyYTF7RDkqSaBF3gk0Qv", "GoSnsynYNuM88PLnmyI3XAwH");
        context = this;
    }

}
