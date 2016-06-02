package com.express.subao.box.handlers;

import com.express.subao.box.HelpObj;
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
 * Created by Hua on 15/12/21.
 */
public class HelpObjHandler {

    public static List<HelpObj> getHelpObjList(JSONArray array) {
        List<HelpObj> list = new ArrayList<HelpObj>(array.length());

        for (int i = 0; i < array.length(); i++) {
            list.add(getHelpObj(JsonHandle.getJSON(array, i)));
        }

        return list;

    }

    private static HelpObj getHelpObj(JSONObject json) {
        HelpObj obj = new HelpObj();

        obj.setTitle(JsonHandle.getString(json, HelpObj.TITLE));
        obj.setUrl(JsonHandle.getString(json, HelpObj.URL));
        obj.setImg(JsonHandle.getString(json, HelpObj.IMG));

        return obj;
    }

}
