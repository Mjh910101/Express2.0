package com.express.subao.box.handlers;

import com.express.subao.box.OrderObj;
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
 * Created by Hua on 16/7/28.
 */
public class OrderObjHandler {

    public static List<OrderObj> getOrderObjList(JSONArray array){
        List<OrderObj> list=new ArrayList<>();

        for(int i=0;i<array.length();i++){
            list.add(getOrderObj(JsonHandle.getJSON(array,i)));
        }

        return list;
    }

    public static OrderObj getOrderObj(JSONObject json) {
        OrderObj obj=new OrderObj();

        obj.setAddress(JsonHandle.getJSON(json,"receive"));
        obj.setCount(JsonHandle.getInt(json,"count"));
        obj.setItemList(JsonHandle.getArray(json,"items"));
        obj.setObjectId(JsonHandle.getString(json,"objectId"));
        obj.setOrdersn(JsonHandle.getString(json,"ordersn"));
        obj.setPayment_type(JsonHandle.getString(json,"payment_type"));
        obj.setShipment(JsonHandle.getString(json,"shipment"));
        obj.setShipment_type(JsonHandle.getString(json,"shipment_type"));
        obj.setStatus(JsonHandle.getString(json,"status"));
        obj.setCount(JsonHandle.getInt(json,"count"));
        obj.setStore(JsonHandle.getJSON(json,"store"));
        obj.setTotal(JsonHandle.getInt(json,"total"));

        return obj;
    }

}
