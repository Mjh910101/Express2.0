package com.express.subao.box.handlers;

import com.express.subao.box.CommentObj;
import com.express.subao.box.UserObj;
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
 * Created by Hua on 16/7/1.
 */
public class CommentObjHandler {

    public static List<CommentObj> getCommentObjList(JSONArray array) {
        List<CommentObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getCommentObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static CommentObj getCommentObj(JSONObject json) {
        CommentObj obj = new CommentObj();

        obj.setContent(JsonHandle.getString(json, "content"));
        obj.setItem(JsonHandle.getString(json, "item"));
        obj.setObjectId(JsonHandle.getString(json, "objectId"));
        obj.setCreatedAt(JsonHandle.getString(json, "createdAt"));
        obj.setStore(JsonHandle.getString(json, "store"));
        obj.setPoster(UserObjHandler.getUserObj(JsonHandle.getJSON(json, "poster")));

        return obj;
    }

}
