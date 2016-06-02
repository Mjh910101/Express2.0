package com.express.subao.receivers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.avos.avoscloud.LogUtil;
import com.express.subao.R;
import com.express.subao.activitys.SdyOrderContentActivity;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.handlers.JsonHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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
 * Created by Hua on 16/5/19.
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";
    public static final String PUSH_KEY = "isPush";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Get Broadcat");
        int code = (int) SystemClock.uptimeMillis();
        try {
            //获取消息内容
            JSONObject json = JsonHandle.getJSON(intent.getExtras().getString("com.avos.avoscloud.Data"));
            Log.i(TAG, json.toString());

            Bundle b = new Bundle();
            b.putString(SdyOrderObj.SDY_ORDER_ID, JsonHandle.getString(json, "sdy_order_id"));
            b.putBoolean(PUSH_KEY, true);
            Intent notifyIntent = new Intent(context, SdyOrderContentActivity.class);
            notifyIntent.putExtras(b);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pi = PendingIntent.getActivity(context, code, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(context.getResources().getText(R.string.app_name))
                    .setContentText(JsonHandle.getString(json, "alert"))
                    .setTicker(JsonHandle.getString(json, "alert"))
                    .setSmallIcon(R.mipmap.logo_icon)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pi);

            Log.i(TAG, "showNotification");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(code, builder.build());
        } catch (Exception e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

    }

}
