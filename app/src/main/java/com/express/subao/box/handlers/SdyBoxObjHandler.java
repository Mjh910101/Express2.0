package com.express.subao.box.handlers;

import com.express.subao.box.SdyBoxObj;
import com.express.subao.handlers.JsonHandle;

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
 * Created by Hua on 16/5/13.
 */
public class SdyBoxObjHandler {

    public static List<SdyBoxObj> getSdyBoxObjList(JSONArray array) {
        List<SdyBoxObj> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getSdyBoxObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    public static SdyBoxObj getSdyBoxObj(JSONObject json) {
        SdyBoxObj obj = new SdyBoxObj();

        obj.setObjectId(JsonHandle.getString(json, "objectId"));
        obj.setAddress(JsonHandle.getString(json, "address"));
        obj.setTitle(JsonHandle.getString(json, "title"));
        obj.setCreatedAt(JsonHandle.getString(json, "createdAt"));
        obj.setUpdatedAt(JsonHandle.getString(json, "updatedAt"));

        JSONObject point = JsonHandle.getJSON(json, "point");
        if (point != null) {
            obj.setPoint(JsonHandle.getDouble(point, "latitude"), JsonHandle.getDouble(point, "longitude"));
        }
        obj.setCover(JsonHandle.getJSON(json, "cover"));
        obj.setDevice_id(JsonHandle.getString(json, "device_id"));

        return obj;
    }

    private static SdyBoxObj mSdyBoxObj;

    public static void saveSdyBoxObj(SdyBoxObj obj) {
        mSdyBoxObj = obj;
    }

    public static SdyBoxObj getSdyBoxObj() {
        return mSdyBoxObj;
    }

}
