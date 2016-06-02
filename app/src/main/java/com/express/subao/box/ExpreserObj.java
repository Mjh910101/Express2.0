package com.express.subao.box;

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
 * Created by Hua on 15/12/24.
 */
public class ExpreserObj {

    private String express_id;
    private String postman;
    private String expressAt;
    private CompanyInfo companyInfo;
    private List<Tracking> trackingList;

    public List<Tracking> getTrackingList() {
        return trackingList;
    }

    public void setTrackingList(JSONArray array) {
        trackingList = new ArrayList<Tracking>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                Tracking o = new Tracking(JsonHandle.getJSON(array, i));
                trackingList.add(o);
            }
        }
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(JSONObject json) {
        this.companyInfo = new CompanyInfo(json);
    }

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getPostman() {
        return postman;
    }

    public void setPostman(String postman) {
        this.postman = postman;
    }

    public String getExpressAt() {
        return expressAt;
    }

    public void setExpressAt(String expressAt) {
        this.expressAt = expressAt;
    }

    public class CompanyInfo {

        private CompanyInfo(JSONObject json) {
            if (json == null) {
                initCompanyInfo();
            } else {
                name = JsonHandle.getString(json, "name");
                ico = JsonHandle.getString(json, "ico");
            }
        }

        private void initCompanyInfo() {
            name = "";
            ico = "";
        }

        private String name;
        private String ico;

        public String getName() {
            return name;
        }

        public String getIco() {
            return ico;
        }
    }

    public class Tracking {

        private Tracking(JSONObject json) {
            if (json == null) {
                initTracking();
            } else {
                message = JsonHandle.getString(json, "message");
                logAt = JsonHandle.getString(json, "logAt");
            }
        }

        private void initTracking() {
            message = "";
            logAt = "";
        }

        private String message;
        private String logAt;

        public String getMessage() {
            return message;
        }

        public String getLogAt() {
            return logAt;
        }
    }

}
