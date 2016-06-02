package com.express.subao.box.handlers;

import com.express.subao.box.RebateObj;
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
 * Created by Hua on 15/12/24.
 */
public class RebateObjHandler {

    public static List<RebateObj> getDiscountObjList(JSONArray array) {
        List<RebateObj> list = new ArrayList<RebateObj>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getDiscountObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    public static RebateObj getDiscountObj(JSONObject json) {
        RebateObj obj = new RebateObj();

        obj.setObjectId(JsonHandle.getString(json, RebateObj.OBJECT_ID));
        obj.setTitle(JsonHandle.getString(json, RebateObj.TITLE));
        obj.setImg(JsonHandle.getString(json, RebateObj.IMG));
        obj.setCreatedAt(JsonHandle.getString(json, RebateObj.CREATED_AT));
        obj.setIntro(JsonHandle.getString(json, RebateObj.INERO));
        obj.setTag(JsonHandle.getString(json, RebateObj.TAG));
        obj.setTips(JsonHandle.getString(json, RebateObj.TIPS));
        obj.setContent(JsonHandle.getString(json, RebateObj.CONTENT));

        return obj;
    }

    private static RebateObj mRebateObj;

    public static void saveRebateObj(RebateObj obj) {
        if (mRebateObj != null) {
            mRebateObj = null;
        }
        mRebateObj = obj;
    }

    public static RebateObj getRebateObj() {
        return mRebateObj;
    }

}
