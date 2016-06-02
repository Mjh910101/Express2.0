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
            JSONObject json = JsonHandle.getJSON(array, i);
            SliderObj obj = new SliderObj();
            obj.setImg(JsonHandle.getString(json, "img"));
            obj.setUrl(JsonHandle.getString(json, "url"));
            sliderList.add(obj);
        }
        return sliderList;
    }

}
