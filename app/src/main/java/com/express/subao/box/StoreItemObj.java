package com.express.subao.box;

import com.express.subao.handlers.JsonHandle;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
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
@Table(name = "tbl_store_item")
public class StoreItemObj {

    @Column(column = "updatedAt")
    private String updatedAt;

    @Column(column = "desc")
    private String desc;

    @Column(column = "storeId")
    private String storeId;

    @Id(column = "objectId")
    private String objectId;

    @Column(column = "cover")
    private String cover;

    @Column(column = "createdAt")
    private String createdAt;

    @Column(column = "title")
    private String title;

    @Column(column = "intro")
    private String intro;

    @Column(column = "comments")
    private int comments;

    @Column(column = "sell")
    private int sell;

    @Column(column = "price")
    private double price;

    @Column(column = "sum")
    private int sum;

    @Transient
    private List<String> imagesList;
    @Transient
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

    public void setStoreId(String id) {
        this.storeId = id;
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
        } else {
            this.cover = "";
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public boolean isStore(String id) {
        return storeId.equals(id);
    }

    public String getPriceSumForString() {
        double s = price * sum;
        return new DecimalFormat("0.00").format(s);
    }

    public double getPriceSum() {
        double s = price * sum;
        return s;
    }
}
