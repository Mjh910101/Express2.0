package com.express.subao.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

public class MessageHandler {

    public static void logException(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void showToast(Context context, String msg) {
        if (context != null) {
            showToast(context, msg, 0);
        }
    }

    public static void showToast(Context context, int code) {
        if (context != null) {
            String msg = "";
            switch (code) {
                case 210:
                    msg="用戶名和密碼不匹配";
                    break;
                case 211:
                    msg="找不到用戶";
                    break;
            }
            showToast(context, msg, 0);
        }
    }

    public static void showFailure(Context context) {
        showToast(context, "网络不佳");
    }

    public static void showLast(Context context) {
        showToast(context, "没有数据了");
    }

    public static void showFailure(Context context, Exception e) {
        showFailure(context);
        logException(e);
    }

    public static boolean showException(Context context, JSONObject error) {
        if (error != null) {
            String msg = JsonHandle.getString(error, "error");
            Log.e("error", msg);
            if (!msg.equals("")) {
                showToast(context, msg);
                return true;
            }
        }
        return false;
    }

}
