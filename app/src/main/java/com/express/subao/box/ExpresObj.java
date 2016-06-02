package com.express.subao.box;

import com.express.subao.handlers.JsonHandle;

import org.json.JSONObject;

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
 * Created by Hua on 15/12/24.
 */
public class ExpresObj {

    public final static String OBJECT_ID = "objectId";
    public final static String VERIFY = "verify";
    public final static String TIPS = "tips";
    public final static String STATUS = "status";
    public final static String STATUR_STE = "statusStr";
    public final static String PRICE = "price";
    public final static String TIMEOUT_PRICE = "timeout_price";

    private String objectId;
    private String verify;
    private String tips;
    private String statusStr;
    private int status;
    private int price;
    private int timeout_price;
    private Address address;
    private ExpreserObj expreser;

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public ExpreserObj getExpreser() {
        return expreser;
    }

    public void setExpreser(ExpreserObj expreser) {
        this.expreser = expreser;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(JSONObject json) {
        this.address = new Address(json);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTimeout_price() {
        return timeout_price;
    }

    public void setTimeout_price(int timeout_price) {
        this.timeout_price = timeout_price;
    }

    public class Address {

        private Address(JSONObject json) {
            if (json == null) {
                initAddress();
            } else {
                area = JsonHandle.getString(json, "area");
                part = JsonHandle.getString(json, "part");
                code = JsonHandle.getString(json, "code");
            }
        }

        private void initAddress() {
            area = "";
            part = "";
            code = "";
        }

        private String area;
        private String part;
        private String code;

        public String getArea() {
            return area;
        }

        public String getPart() {
            return part;
        }

        public String getCode() {
            return code;
        }
    }

}
