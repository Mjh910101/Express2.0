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
 * Created by Hua on 16/7/1.
 */
public class StoreItemObj {

    private String updatedAt;
    private String desc;
    private String storeId;
    private String objectId;
    private String cover;
    private String createdAt;
    private String title;
    private String intro;
    private int comments;
    private int sell;
    private double price;
    private List<String> imagesList;
    private List<String> tagList;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(JSONObject json) {
        if (json != null) {
            this.storeId = JsonHandle.getString(json, "objectId");
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(JSONObject json) {
        if (json != null) {
            this.cover = JsonHandle.getString(json, "url");
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(JSONArray array) {
        this.imagesList = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = JsonHandle.getJSON(array, i);
                if (json != null) {
                    this.imagesList.add(JsonHandle.getString(json, "url"));
                }
            }
        }
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(JSONArray array) {
        this.tagList = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                this.imagesList.add(JsonHandle.getString(array, i));
            }
        }
    }

    public String getSellText() {
        return "已售" + getSell();
    }

    public List<SliderObj> getSliderList() {
        List<SliderObj> list = new ArrayList<>();
        if (getImagesList() != null) {
            for (int i = 0; i < getImagesList().size(); i++) {
                SliderObj obj = new SliderObj();
                obj.setImg(getImagesList().get(i));
                list.add(obj);
            }
        }
        return list;

    }
}
