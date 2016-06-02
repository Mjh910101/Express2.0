package com.express.subao.box.handlers;

import com.express.subao.box.SdyOrderObj;
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
 * Created by Hua on 16/5/18.
 */
public class SdyOrderObjHandler {

    public static List<SdyOrderObj> getSdyOrderObjList(JSONArray array) {
        List<SdyOrderObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getSdyOrderObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static SdyOrderObj getSdyOrderObj(JSONObject json) {
        SdyOrderObj obj = new SdyOrderObj();

        obj.setObjectId(JsonHandle.getString(json, SdyOrderObj.OBJECT_ID));
        obj.setCreatedAt(JsonHandle.getString(json, SdyOrderObj.CREATED_AT));
        obj.setFee(JsonHandle.getInt(json, SdyOrderObj.FEE));
        obj.setMailman(JsonHandle.getString(json, SdyOrderObj.MAILAN));
        obj.setMailman_mobile(JsonHandle.getString(json, SdyOrderObj.MAILAN_MOBILE));
        obj.setMailno(JsonHandle.getString(json, SdyOrderObj.MAILNO));
        obj.setMobile(JsonHandle.getString(json, SdyOrderObj.MOBILE));
        obj.setOpen_code(JsonHandle.getString(json, SdyOrderObj.OPEN_CODE));
        obj.setSdy_order_id(JsonHandle.getString(json, SdyOrderObj.SDY_ORDER_ID));
        obj.setStatus(JsonHandle.getString(json, SdyOrderObj.STATUS));
        obj.setUpdatedAt(JsonHandle.getString(json, SdyOrderObj.UPDATED_AT));
        obj.setPickup_time(JsonHandle.getString(json, SdyOrderObj.PICKUP_TIMR));
        obj.setDevice_id(JsonHandle.getString(json, SdyOrderObj.DEVICE_ID));

        JSONObject boxJson=JsonHandle.getJSON(json,"box");
        if(boxJson!=null){
            obj.setBoxObj(SdyBoxObjHandler.getSdyBoxObj(boxJson));
        }

        return obj;
    }

    private static SdyOrderObj mSdyOrderObj;

    public static void saveSdyOrder(SdyOrderObj obj) {
        mSdyOrderObj = obj;
    }

    public static SdyOrderObj getSdyOrderObj() {
        return mSdyOrderObj;
    }

}
