package com.express.subao.box.handlers;

import com.express.subao.box.ItemObj;
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
 * Created by Hua on 15/12/30.
 */
public class ItemObjHandler {

    public static List<ItemObj> getItemObjList(JSONArray array) {
        List<ItemObj> list = new ArrayList<ItemObj>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getItemObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    private static ItemObj getItemObj(JSONObject json) {
        ItemObj obj = new ItemObj();

        obj.setObjectId(JsonHandle.getString(json, ItemObj.OBJECT_ID));
        obj.setContent(JsonHandle.getString(json, ItemObj.CONTENT));
        obj.setComment_count(JsonHandle.getDouble(json, ItemObj.COMMENT_COUT));
        obj.setImg(JsonHandle.getString(json, ItemObj.IMG));
        obj.setIntro(JsonHandle.getString(json, ItemObj.INTRO));
        obj.setOrigin(JsonHandle.getDouble(json, ItemObj.ORIGIN));
        obj.setOrigin_price_str(JsonHandle.getString(json, ItemObj.ORIGIN_PRICE_STR));
        obj.setPrice(JsonHandle.getInt(json, ItemObj.PRICE));
        obj.setPrice_str(JsonHandle.getString(json, ItemObj.PRICE_STR));
        obj.setSell(JsonHandle.getInt(json, ItemObj.SELL));
        obj.setTitle(JsonHandle.getString(json, ItemObj.TITLE));

        return obj;
    }

}
