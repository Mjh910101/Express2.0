package com.express.subao.box.handlers;

import com.express.subao.box.StoreItemObj;
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
 * Created by Hua on 16/7/1.
 */
public class StoreItemObjHandler {

    public static List<StoreItemObj> getStoreItemObjList(JSONArray array) {
        List<StoreItemObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getStoreItemObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    private static StoreItemObj getStoreItemObj(JSONObject json) {
        StoreItemObj obj = new StoreItemObj();
        obj.setComments(JsonHandle.getInt(json,"comments"));
        obj.setCover(JsonHandle.getJSON(json,"cover"));
        obj.setCreatedAt(JsonHandle.getString(json,"createdAt"));
        obj.setDesc(JsonHandle.getString(json,"desc"));
        obj.setImagesList(JsonHandle.getArray(json,"images"));
        obj.setIntro(JsonHandle.getString(json,"intro"));
        obj.setObjectId(JsonHandle.getString(json,"objectId"));
        obj.setPrice(JsonHandle.getDouble(json,"price"));
        obj.setSell(JsonHandle.getInt(json,"sell"));
        obj.setStoreId(JsonHandle.getJSON(json,"store"));
        obj.setTagList(JsonHandle.getArray(json,"tag"));
        obj.setTitle(JsonHandle.getString(json,"title"));
        obj.setUpdatedAt(JsonHandle.getString(json,"updatedAt"));
        return obj;
    }

    private static StoreItemObj mStoreItemObj;

    public static void save(StoreItemObj obj) {
        mStoreItemObj = obj;
    }

    public static StoreItemObj getStoreItemObj() {
        return mStoreItemObj;
    }

}
