package com.express.subao.box;

import com.alibaba.fastjson.JSON;
import com.express.subao.box.handlers.SliderObjHandler;
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
 * Created by Hua on 15/12/23.
 */
public class StoreObj {

    public static final String OBJECT_ID = "objectId";
    public static final String TITLE = "title";
    public static final String IMG = "img";
    public static final String IMAGES = "images";
    public static final String SLIDER = "slider";

    private String objectId;
    private String title;
    private String img;
    private int comments;
    private List<SliderObj> sliderList;
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
}
