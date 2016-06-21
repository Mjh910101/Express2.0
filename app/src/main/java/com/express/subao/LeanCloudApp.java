package com.express.subao;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

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
 * Created by Hua on 16/6/2.
 */

public class LeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
//        AVOSCloud.initialize(this,"{{appid}}","{{appkey}}");
        AVOSCloud.initialize(this, "2jlEHyYTF7RDkqSaBF3gk0Qv", "GoSnsynYNuM88PLnmyI3XAwH");
        AVOSCloud.setDebugLogEnabled(true);

    }

}
