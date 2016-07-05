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

    private StoreObj storeObj;
    private StoreItemObj storeItemObj;

    public ShoppingCarObj(StoreObj storeObj) {
        this.storeObj = storeObj;
    }

    public ShoppingCarObj(StoreItemObj storeItemObj) {
        this.storeItemObj = storeItemObj;
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

}
