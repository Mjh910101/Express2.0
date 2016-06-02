package com.express.subao.box.handlers;

import com.express.subao.box.CategoryObj;
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
public class CategoryObjHandler {

    public static List<CategoryObj> getCategoryObjList(JSONArray array) {
        List<CategoryObj> list = new ArrayList<CategoryObj>(array.length());

        for (int i = 0; i < array.length(); i++) {
            list.add(getCategoryObj(JsonHandle.getJSON(array, i)));
        }

        return list;
    }

    private static CategoryObj getCategoryObj(JSONObject json) {
        CategoryObj obj = new CategoryObj();

        obj.setObjectId(JsonHandle.getString(json, CategoryObj.OBJECT_ID));
        obj.setTitle(JsonHandle.getString(json, CategoryObj.TITLE));
        obj.setImg(JsonHandle.getString(json, CategoryObj.IMG));
        obj.setLayout(JsonHandle.getString(json, CategoryObj.LAYOUT));

        return obj;
    }

    private static CategoryObj mCategoryObj;

    public static void setCategoryObj(CategoryObj obj) {
        if (mCategoryObj != null) {
            mCategoryObj = null;
        }
        mCategoryObj = obj;
    }

    public static CategoryObj getCategoryObj() {
        return mCategoryObj;
    }
}
