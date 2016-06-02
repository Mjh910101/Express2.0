package com.express.subao.box.handlers;

import com.express.subao.box.ExpreserObj;
import com.express.subao.handlers.JsonHandle;

import org.json.JSONArray;
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
 * Created by Hua on 15/12/24.
 */
public class ExpreserObjHandler {

    public static ExpreserObj getExpreserObj(JSONObject json) {
        ExpreserObj obj = new ExpreserObj();

        obj.setExpress_id(JsonHandle.getString(json, "express_id"));
        obj.setPostman(JsonHandle.getString(json, "postman"));
        obj.setExpressAt(JsonHandle.getString(json, "expressAt"));

        JSONObject companyInfo = JsonHandle.getJSON(json, "companyInfo");
        obj.setCompanyInfo(companyInfo);

        JSONArray tracking = JsonHandle.getArray(json, "tracking");
        obj.setTrackingList(tracking);

        return obj;
    }

}
