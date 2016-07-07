package com.express.subao.box;

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
 * Created by Hua on 16/7/5.
 */
public class ShoppingCarObj {


    public final static int STORE = 0;
    public final static int ITEM = 1;

    private StoreObj storeObj;
    private StoreItemObj storeItemObj;

    private int type;

    public ShoppingCarObj(StoreObj storeObj) {
        this.storeObj = storeObj;
        this.type = STORE;
    }

    public ShoppingCarObj(StoreItemObj storeItemObj) {
        this.storeItemObj = storeItemObj;
        this.type = ITEM;
    }

    private boolean isStore() {
        return storeObj != null;
    }

    private boolean isItem() {
        return storeItemObj != null;
    }

    public StoreObj getStoreObj() {
        return storeObj;
    }


    public StoreItemObj getStoreItemObj() {
        return storeItemObj;
    }

    public int getType() {
        return type;
    }
}
