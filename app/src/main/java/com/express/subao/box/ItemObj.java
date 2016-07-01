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
 * Created by Hua on 15/12/30.
 */
public class ItemObj {

    public final static String IMG = "img";
    public final static String OBJECT_ID = "objectId";
    public final static String INTRO = "intro";
    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String ORIGIN_PRICE_STR = "origin_price_str";
    public final static String PRICE_STR = "price_str";
    public final static String ORIGIN = "origin";
    public final static String COMMENT_COUT = "comment_count";
    public final static String PRICE = "price";
    public final static String SELL = "sell";

    private String img;
    private String objectId;
    private String intro;
    private String title;
    private String content;
    private String origin_price_str;
    private String price_str;
    private double origin;
    private int comment_count;
    private double price;
    private double sell;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrigin_price_str() {
        return origin_price_str;
    }

    public void setOrigin_price_str(String origin_price_str) {
        this.origin_price_str = origin_price_str;
    }

    public String getPrice_str() {
        return price_str;
    }

    public void setPrice_str(String price_str) {
        this.price_str = price_str;
    }

    public double getOrigin() {
        return origin;
    }

    public void setOrigin(double origin) {
        this.origin = origin;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }
}
