package com.express.subao.box.handlers;

import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreObj;
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
 * Created by Hua on 15/12/23.
 */
public class StoreObjHandler {

    public static List<StoreObj> getStoreObjList(JSONArray array) {
        List<StoreObj> list = new ArrayList<StoreObj>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getStoreObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    public static StoreObj getStoreObj(JSONObject json) {
        StoreObj obj = new StoreObj();

        obj.setImg(JsonHandle.getJSON(json, StoreObj.IMG));
        obj.setObjectId(JsonHandle.getString(json, StoreObj.OBJECT_ID));
        obj.setTitle(JsonHandle.getString(json, StoreObj.TITLE));

        JSONArray array = JsonHandle.getArray(json, StoreObj.SLIDER);
        obj.setSliderList(array);

        JSONArray tagList = JsonHandle.getArray(json, "tag");
        obj.setTapList(tagList);

        return obj;
    }

    private static StoreObj mStoreObj;

    public static void saveStoreObj(StoreObj obj) {
        if (mStoreObj != null) {
            mStoreObj = null;
        }
        mStoreObj = obj;
    }

    public static StoreObj getStoreObj() {
        return mStoreObj;
    }

}
