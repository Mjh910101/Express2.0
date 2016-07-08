package com.express.subao.box.handlers;

import android.content.Context;
import android.util.Log;

import com.baidu.platform.comapi.map.C;
import com.express.subao.box.ItemObj;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.dao.DBHandler;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

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

    public static void updateShoppingCar(Context context, List<StoreItemObj> list) {
        try {
            DbUtils db = DBHandler.getDbUtils(context);
            for (StoreItemObj obj : list) {
                db.saveOrUpdate(obj);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void saveInShoppingCar(Context context, StoreItemObj obj) {
        try {
            DbUtils db = DBHandler.getDbUtils(context);
            StoreItemObj saveObj = db.findById(StoreItemObj.class, obj.getObjectId());
            if (saveObj == null) {
                obj.setSum(1);
                db.saveOrUpdate(obj);
            } else {
                saveObj.setSum(saveObj.getSum() + 1);
                db.saveOrUpdate(saveObj);
            }
            Log.e("Shopping Car Handler", "SAVE " + "ID " + obj.getObjectId() + "   Store ID " + obj.getStoreId() + "  SUM " + obj.getSum());
            if (saveObj != null) {
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


    public static void deleteItem(Context context, StoreItemObj obj) {
        try {
            DBHandler.getDbUtils(context).deleteById(StoreItemObj.class, obj.getObjectId());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
