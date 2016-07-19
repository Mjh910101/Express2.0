package com.express.subao.box.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.baidu.platform.comapi.map.C;
import com.express.subao.box.ItemObj;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.dao.DBHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.interfaces.CallbackForString;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * Created by Hua on 16/7/4.
 */
public class ShoppingCarHandler {

    public static void saveStore(Context context, StoreObj obj) {
        try {
            DBHandler.getDbUtils(context).saveOrUpdate(obj);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static void updateShoppingCar(final Context context, final ProgressBar progress, final List<StoreItemObj> list) {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getCartUpdate();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("data", getJsonForList(list).toString());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                updateShoppingCar(context, list);
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });
    }

    private static void updateShoppingCar(Context context, List<StoreItemObj> list) {
        try {
            DbUtils db = DBHandler.getDbUtils(context);
            for (StoreItemObj obj : list) {
                db.saveOrUpdate(obj);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void saveInShoppingCar(final Context context, final ProgressBar progress, final StoreItemObj obj) {

        progress.setVisibility(View.VISIBLE);
        String url = Url.getCartAdd();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("store_id", obj.getStoreId());
        params.addBodyParameter("item_id", obj.getObjectId());
        params.addBodyParameter("count", "1");
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));

        Log.e("store_id", obj.getStoreId());
        Log.e("item_id", obj.getObjectId());
        Log.e("sessiontoken", UserObjHandler.getSessionToken(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                saveInShoppingCar(context, obj);
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });
    }

    private static void saveInShoppingCar(Context context, StoreItemObj obj) {
        MessageHandler.showToast(context, "添加入購物車");
        try {
            DbUtils db = DBHandler.getDbUtils(context);
            StoreItemObj saveObj = db.findById(StoreItemObj.class, obj.getObjectId());
            if (saveObj == null) {
                obj.setSum(1);
                db.saveOrUpdate(obj);
                Log.e("Shopping Car Handler", "SAVE " + "ID " + obj.getObjectId() + "   Store ID " + obj.getStoreId() + "  SUM " + obj.getSum());
            } else {
                saveObj.setSum(saveObj.getSum() + 1);
                db.saveOrUpdate(saveObj);
                Log.e("Shopping Car Handler", "SAVE " + "ID " + saveObj.getObjectId() + "   Store ID " + obj.getStoreId() + "  SUM " + saveObj.getSum());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static List<StoreItemObj> getStoreItemForStoreId(Context context, String storeId) {
        List<StoreItemObj> list = new ArrayList<>();
        try {
            List<StoreItemObj> dbList = DBHandler.getDbUtils(context).findAll(Selector.from(StoreItemObj.class).where("storeId", "=", storeId));
            if (dbList != null) {
                list.addAll(dbList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<StoreItemObj> getAllStoreItem(Context context) {
        List<StoreItemObj> list = new ArrayList<>();
        try {
            List<StoreItemObj> dbList = DBHandler.getDbUtils(context).findAll(StoreItemObj.class);
            if (dbList != null) {
                list.addAll(dbList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<StoreObj> getAllStore(Context context) {
        List<StoreObj> list = new ArrayList<>();
        try {
            List<StoreObj> dbList = DBHandler.getDbUtils(context).findAll(StoreObj.class);
            if (dbList != null) {
                list.addAll(dbList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<ShoppingCarObj> getAllShoppingCar(Context context) {
        List<ShoppingCarObj> list = new ArrayList<>();
        List<StoreObj> storeList = getAllStore(context);
        for (StoreObj obj : storeList) {
            Log.e("Shopping Car Handler", "GET " + "STORE ID " + obj.getObjectId());
            List<StoreItemObj> storeItemList = getStoreItemForStoreId(context, obj.getObjectId());
            if (storeItemList != null && !storeItemList.isEmpty()) {
                addShoppingCarList(list, obj);
                addShoppingCarList(list, storeItemList);
            }
        }
        return list;
    }

    public static void addShoppingCarList(List<ShoppingCarObj> list, StoreObj obj) {
        ShoppingCarObj shoppingCarObj = new ShoppingCarObj(obj);
        list.add(shoppingCarObj);
    }

    public static void addShoppingCarList(List<ShoppingCarObj> list, StoreItemObj obj) {
        ShoppingCarObj shoppingCarObj = new ShoppingCarObj(obj);
        list.add(shoppingCarObj);
    }

    public static void addShoppingCarList(List<ShoppingCarObj> list, List<StoreItemObj> storeItemList) {
        for (StoreItemObj obj : storeItemList) {
            Log.e("Shopping Car Handler", "GET " + "ITEM ID " + obj.getObjectId() + "   Store ID " + obj.getStoreId() + "  SUM " + obj.getSum());
            addShoppingCarList(list, obj);
        }
    }


    public static void deleteItem(final Context context, final ProgressBar progress, final StoreItemObj obj) {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getCartRemove();
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("store_id", obj.getStoreId());
        params.addBodyParameter("item_id", obj.getObjectId());
        params.addBodyParameter("count", "-1");
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                deleteItem(context, obj);
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });
    }


    public static void deleteAllItem(final Context context, final ProgressBar progress, final List<StoreItemObj> list, final CallbackForString callback) {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getCartUpdate();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("data", getJsonForList(list, -1).toString());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);
                        callback.callback(result);
                    }

                });
    }

    private static JSONObject getJsonForList(List<StoreItemObj> list) {
        JSONArray array = new JSONArray();
        for (StoreItemObj obj : list) {
            JsonHandle.put(array, obj.toJson());
        }
        JSONObject json = new JSONObject();
        JsonHandle.put(json, "cartData", array);
        Log.e("json for cart", json.toString());
        return json;
    }

    private static JSONObject getJsonForList(List<StoreItemObj> list, int c) {
        JSONArray array = new JSONArray();
        for (StoreItemObj obj : list) {
            JsonHandle.put(array, obj.toJson(c));
        }
        JSONObject json = new JSONObject();
        JsonHandle.put(json, "cartData", array);
        Log.e("json for cart", json.toString());
        return json;
    }

    private static String getItemIdForList(List<StoreItemObj> list) {
        StringBuffer sb = new StringBuffer();
        for (StoreItemObj obj : list) {
            sb.append(obj.getObjectId());
            sb.append(",");
        }
        Log.e("Item Id List", sb.toString().substring(0, sb.length() - 1));
        return sb.toString().substring(0, sb.length() - 1);
    }

    private static String getStoreIdForList(List<StoreItemObj> list) {
        StringBuffer sb = new StringBuffer();
        List<String> idList = new ArrayList<>();
        for (StoreItemObj obj : list) {
            if (!idList.contains(obj.getStoreId())) {
                sb.append(obj.getStoreId());
                sb.append(",");
                idList.add(obj.getStoreId());
            }
        }
        Log.e("Store Id List", sb.toString().substring(0, sb.length() - 1));
        return sb.toString().substring(0, sb.length() - 1);
    }

    private static void deleteItem(Context context, StoreItemObj obj) {
        try {
            DBHandler.getDbUtils(context).deleteById(StoreItemObj.class, obj.getObjectId());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static void deleteAllItem(Context context) {
        try {
            DBHandler.getDbUtils(context).deleteAll(StoreItemObj.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<StoreItemObj>> choiseMap;

    public static void saveChoiseItem(Map<String, List<StoreItemObj>> map) {
        choiseMap = map;
    }

    public static Map<String, List<StoreItemObj>> getChoiseMap() {
        return choiseMap;
    }

    public static StoreObj getStoreForId(Context context, String id) {
        StoreObj obj = null;
        try {
            obj = DBHandler.getDbUtils(context).findById(StoreObj.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static List<ShoppingCarObj> getShoppingCarList(Context context, JSONArray array) {

        List<ShoppingCarObj> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject storeJson = JsonHandle.getJSON(array, i);
            StoreObj store = getStoreObj(storeJson);
            saveStore(context, store);
            addShoppingCarList(list, store);
            List<StoreItemObj> itemList = getStoreItemList(JsonHandle.getArray(storeJson, "items"), store.getObjectId());
            updateShoppingCar(context, itemList);
            addShoppingCarList(list, itemList);
        }

        return list;
    }

    private static List<StoreItemObj> getStoreItemList(JSONArray array, String storeId) {
        List<StoreItemObj> list = StoreItemObjHandler.getStoreItemObjList(array);
        for (StoreItemObj obj : list) {
            obj.setStoreId(storeId);
        }
        return list;
    }

    private static StoreObj getStoreObj(JSONObject json) {
        StoreObj obj = StoreObjHandler.getStoreObj(json);
        return obj;

    }

}
