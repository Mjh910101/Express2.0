package com.express.subao.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;
import com.express.subao.R;
import com.express.subao.download.DownloadImageLoader;
import com.lidroid.xutils.ViewUtils;

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
        setContentView(R.layout.activity_main);
//        AVOSCloud.initialize(this, "2jlEHyYTF7RDkqSaBF3gk0Qv", "GoSnsynYNuM88PLnmyI3XAwH");
        context = this;
    }

}
