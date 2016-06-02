package com.express.subao.box;

import com.express.subao.handlers.JsonHandle;

import org.json.JSONArray;

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
 * Created by Hua on 15/12/22.
 */
public class AreaObj {

    private String name;
    private List<AreaObj> areaList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaObj> getAreaList() {
        return areaList;
    }

    public void setAreaList(JSONArray array) {
        areaList = new ArrayList<AreaObj>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                AreaObj obj = new AreaObj();
                obj.setName(JsonHandle.getString(array, i));
                areaList.add(obj);
            }
        }
    }
}
