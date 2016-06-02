package com.express.subao.box.handlers;

import android.content.Context;

import com.express.subao.box.AreaObj;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.SystemHandle;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 15/12/22.
 */
public class AreaObjHandler {

    public static List<AreaObj> getAreaObjList(JSONArray array) {
        List<AreaObj> list = new ArrayList<AreaObj>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = JsonHandle.getJSON(array, i);
            if (json != null) {
                List<String> keyList = JsonHandle.getKeys(json);
                if (!keyList.isEmpty()) {
                    list.add(getAreaObj(json, keyList.get(0)));
                }
            }
        }
        return list;
    }

    private static AreaObj getAreaObj(JSONObject json, String key) {
        AreaObj obj = new AreaObj();
        obj.setName(key);
        JSONArray a = JsonHandle.getArray(json, key);
        obj.setAreaList(a);
        return obj;
    }

    private final static String KEY = "area_name_key";

    public static String getAreaName(Context context) {
        String n = SystemHandle.getString(context, KEY);
        return n;
    }

    public static void saveAreaName(Context context, String name) {
        SystemHandle.saveStringMessage(context, KEY, name);
    }
}
