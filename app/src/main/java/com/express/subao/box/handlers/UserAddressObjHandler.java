package com.express.subao.box.handlers;

import com.express.subao.box.UserAddressObj;
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
 * Created by Hua on 16/7/12.
 */
public class UserAddressObjHandler {

    public static List<UserAddressObj> getUserAddressObjList(JSONArray array) {
        List<UserAddressObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getUserAddressObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static UserAddressObj getUserAddressObj(JSONObject json) {
        UserAddressObj obj = new UserAddressObj();

        obj.setBox(SdyBoxObjHandler.getSdyBoxObj(JsonHandle.getJSON(json, "box")));
        obj.setContact(JsonHandle.getString(json,"contact"));
        obj.setDefault_flag(JsonHandle.getString(json,"default_flag"));
        obj.setDeleted(JsonHandle.getBoolean(json,"isDeleted"));
        obj.setObjectId(JsonHandle.getString(json,"objectId"));
        obj.setReceiver(JsonHandle.getString(json,"receiver"));

        return obj;
    }

}
