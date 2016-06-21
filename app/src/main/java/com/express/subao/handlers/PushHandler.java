package com.express.subao.handlers;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.express.subao.activitys.MainActivity;
import com.express.subao.box.handlers.UserObjHandler;

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
 * Created by Hua on 16/5/23.
 */
public class PushHandler {

    private final static String PUSH_KEY = "IS_PUSH";

    public final static boolean STOP = true;
    public final static boolean START = false;

    public static void savePushState(Context context, boolean b) {
        SystemHandle.saveBooleanMessage(context, PUSH_KEY, b);
    }

    public static boolean isStop(Context context) {
        boolean b = SystemHandle.getBoolean(context, PUSH_KEY);
        Log.e("", PUSH_KEY + " : " + b);
        return b;
    }

    public static void startPush(Context context) {
        savePushState(context, START);
        PushService.subscribe(context, UserObjHandler.getUserTel(context), MainActivity.class);
        PushService.subscribe(context, UserObjHandler.getUserId(context), MainActivity.class);
        save();
    }

    private static void save() {
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.e("", "设备id: " + AVInstallation.getCurrentInstallation().getInstallationId());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void stopPush(Context context) {
        savePushState(context, STOP);
        PushService.unsubscribe(context, UserObjHandler.getUserTel(context));
        PushService.unsubscribe(context, UserObjHandler.getUserId(context));
        save();
    }

}
