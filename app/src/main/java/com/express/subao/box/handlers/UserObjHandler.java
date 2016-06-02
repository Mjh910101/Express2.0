package com.express.subao.box.handlers;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.express.subao.R;
import com.express.subao.box.UserObj;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.SystemHandle;

import org.json.JSONObject;

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
 * Created by Hua on 15/12/22.
 */
public class UserObjHandler {

    public static UserObj getUserObj(JSONObject json) {
        UserObj obj = new UserObj();

        obj.setObjectId(JsonHandle.getString(json, UserObj.OBJECT_ID));
        obj.setUsername(JsonHandle.getString(json, UserObj.USER_NAME));
        obj.setSessionToken(JsonHandle.getString(json, UserObj.SESSION_TOKEN));
        obj.setMobilePhoneNumber(JsonHandle.getString(json, UserObj.MOBILE));
        obj.setAvatar(JsonHandle.getJSON(json, UserObj.ACATAR));
        obj.setNickname(JsonHandle.getString(json, UserObj.NICKNAME));

        return obj;
    }

    private static UserObj mUserObj;

    public static void saveUserObj(UserObj obj) {
        if (mUserObj != null) {
            mUserObj = null;
        }
        mUserObj = obj;
    }

    public static UserObj getUserObj() {
        return mUserObj;
    }

    private final static String KEY = "user_";

    public static void saveUserObj(Context context, UserObj obj) {
        saveUserObj(context, obj, true);
    }

    public static void saveUserObj(Context context, UserObj obj, boolean b) {
        if (b) {
            SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, obj.getSessionToken());
        }
        SystemHandle.saveStringMessage(context, KEY + UserObj.USER_NAME, obj.getUsername());
        SystemHandle.saveStringMessage(context, KEY + UserObj.OBJECT_ID, obj.getObjectId());
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE, obj.getMobilePhoneNumber());
        SystemHandle.saveStringMessage(context, KEY + UserObj.NICKNAME, obj.getNickname());
        saveUserAvatar(context, obj.getAvatar());
    }

    public static boolean isLigon(Context context) {
        return !getSessionToken(context).equals("");
    }

    public static String getSessionToken(Context context) {
        String s = SystemHandle.getString(context, KEY + UserObj.SESSION_TOKEN);
        Log.e("SESSION_TOKEN", s);
        return s;
    }

    public static void deleteUser(Context context) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.USER_NAME, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.OBJECT_ID, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.ACATAR, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.NICKNAME, "");
    }

    public static String getUserName(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.USER_NAME);
    }

    public static String getNickName(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.NICKNAME);
    }

    public static String getUserTel(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.MOBILE);
    }

    public static String getUserId(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.OBJECT_ID);
    }

    public static void setUserAvatar(Context context, ImageView view, int w) {
        String url = getUserAvatar(context);
        if (url.equals("")) {
            view.setImageResource(R.drawable.user_pic);
        } else {
            DownloadImageLoader.loadImage(view, url, w);
        }
    }

    private static String getUserAvatar(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.ACATAR);
    }

    public static void saveUserAvatar(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.ACATAR, url);
    }
}
