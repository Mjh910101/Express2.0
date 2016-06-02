package com.express.subao.box.handlers;

import com.express.subao.box.ExpresObj;
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
public class ExpresObjHandler {

    public static List<ExpresObj> getExpresObjList(JSONArray array) {
        List<ExpresObj> list = new ArrayList<ExpresObj>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getExpresObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    private static ExpresObj getExpresObj(JSONObject json) {
        ExpresObj obj = new ExpresObj();

        obj.setObjectId(JsonHandle.getString(json, ExpresObj.OBJECT_ID));
        obj.setTips(JsonHandle.getString(json, ExpresObj.TIPS));
        obj.setPrice(JsonHandle.getInt(json, ExpresObj.PRICE));
        obj.setStatus(JsonHandle.getInt(json, ExpresObj.STATUS));
        obj.setStatusStr(JsonHandle.getString(json, ExpresObj.STATUR_STE));
        obj.setTimeout_price(JsonHandle.getInt(json, ExpresObj.TIMEOUT_PRICE));
        obj.setVerify(JsonHandle.getString(json, ExpresObj.VERIFY));

        JSONObject infoJson = JsonHandle.getJSON(json, "info");

        JSONObject addressJson = JsonHandle.getJSON(infoJson, "address");
        obj.setAddress(addressJson);

        JSONObject expressJson = JsonHandle.getJSON(infoJson, "express");
        obj.setExpreser(ExpreserObjHandler.getExpreserObj(expressJson));

        return obj;
    }

    private static ExpresObj mExpresObj;

    public static void saveExpresObj(ExpresObj obj) {
        if (mExpresObj != null) {
            mExpresObj = null;
        }
        mExpresObj = obj;
    }

    public static ExpresObj getExpresObj() {
        return mExpresObj;
    }
}
