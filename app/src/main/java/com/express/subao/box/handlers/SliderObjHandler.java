package com.express.subao.box.handlers;

import com.express.subao.box.SliderObj;
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
public class SliderObjHandler {

    public static List<SliderObj> getSliderObjList(JSONArray array) {
        List<SliderObj> sliderList = new ArrayList<SliderObj>(array.length());
        for (int i = 0; i < array.length(); i++) {
            sliderList.add(getSliderObj(JsonHandle.getJSON(array, i)));
        }
        return sliderList;
    }

    public static SliderObj getSliderObj(JSONObject json){
        SliderObj obj = new SliderObj();
        obj.setImg(JsonHandle.getString(json, "img"));
        obj.setImg(JsonHandle.getJSON(json, "img"));
        obj.setUrl(JsonHandle.getString(json, "url"));
        obj.setUrl(JsonHandle.getString(json, "objectId"));
        obj.setUrl(JsonHandle.getString(json, "additional"));
        obj.setUrl(JsonHandle.getString(json, "type"));
        return obj;
    }

}
