package com.express.subao.box;

import com.express.subao.box.handlers.StoreItemObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.box.handlers.UserAddressObjHandler;

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
public class OrderObj {

    private String objectId;
    private String ordersn;
    private String shipment;
    private String payment_type;
    private String shipment_type;
    private String status;
    private int total;
    private int count;
    private StoreObj store;
    private UserAddressObj address;
    private List<StoreItemObj> itemList;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getShipment_type() {
        return shipment_type;
    }

    public void setShipment_type(String shipment_type) {
        this.shipment_type = shipment_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public StoreObj getStore() {
        return store;
    }

    public void setStore(JSONObject json) {
        if (json != null) {
            this.store = StoreObjHandler.getStoreObj(json);
        }
    }

    public UserAddressObj getAddress() {
        return address;
    }

    public void setAddress(JSONObject json) {
        if (json != null) {
            this.address = UserAddressObjHandler.getUserAddressObj(json);
        }
    }

    public List<StoreItemObj> getItemList() {
        return itemList;
    }

    public void setItemList(JSONArray array) {
        if (array != null) {
            this.itemList = StoreItemObjHandler.getStoreItemObjList(array);
        }
    }

    public String getStoreName() {
        if (store == null) {
            return "";
        }
        return store.getTitle();
    }

    public String getItemMessage() {
        StringBuffer sb = new StringBuffer();

        sb.append("共計");
        sb.append(getItemSum());
        sb.append("件商品 ");
        sb.append("合計(含運費):");
        sb.append("MOP");
        sb.append(getTotal());

        return sb.toString();
    }

    public int getItemSum() {
        int sum = 0;
        if (!isNull()) {
            for (StoreItemObj obj : itemList) {
                sum += obj.getSum();
            }
        }
        return sum;
    }

    public boolean isNull() {
        return itemList == null;
    }

    public String getAddressInfo() {
        if (address != null) {
            return address.getAddress();
        }
        return "";
    }

    public String getUserName() {
        if (address != null) {
            return address.getReceiver();
        }
        return "";
    }

    public String getUserTel() {
        if (address != null) {
            return address.getContact();
        }
        return "";
    }
}
