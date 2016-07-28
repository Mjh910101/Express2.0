package com.express.subao.box;

import com.alibaba.fastjson.JSON;
import com.express.subao.box.handlers.SliderObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

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
 * Created by Hua on 15/12/23.
 */
@Table(name = "tbl_store")
public class StoreObj {

    public static final String OBJECT_ID = "objectId";
    public static final String TITLE = "title";
    public static final String IMG = "img";
    public static final String IMAGES = "images";
    public static final String SLIDER = "slider";

    @Id(column = "objectId")
    private String objectId;

    @Column(column = "title")
    private String title;

    @Column(column = "img")
    private String img;

    @Column(column = "comments")
    private int comments;

    @Transient
    private String shipment_min;
    @Transient
    private String shiptips;
    @Transient
    private String statusLabel;
    @Transient
    private int status;
    @Transient
    private List<SliderObj> sliderList;
    @Transient
    private List<String> tapList;

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public List<String> getTapList() {
        return tapList;
    }

    public void setTapList(List<String> tapList) {
        this.tapList = tapList;
    }

    public void setTapList(JSONArray array) {
        this.tapList = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                tapList.add(JsonHandle.getString(array, i));
            }
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setImg(JSONObject img) {
        if (img != null) {
            this.img = JsonHandle.getString(img, "url");
        } else {
            this.img = "";
        }
    }

    public List<SliderObj> getSliderList() {
        return sliderList;
    }

    public void setSliderList(JSONArray array) {
        sliderList = new ArrayList<>();

        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = JsonHandle.getJSON(array, i);
                if (json != null) {
                    sliderList.add(SliderObjHandler.getSliderObj(json));
                }
            }
        }
    }

    public String getShiptips() {
        return shiptips;
    }

    public void setShiptips(String shiptips) {
        this.shiptips = shiptips;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean equals(String id) {
        return objectId.equals(id);
    }

    public boolean isHaveShiptips() {
        if (shiptips == null) {
            return false;
        }
        if (shiptips.equals("")) {
            return false;
        }
        return true;
    }

    public String getShipment_min() {
        return shipment_min;
    }

    public void setShipment_min(String shipment_min) {
        this.shipment_min = shipment_min;
    }
}


