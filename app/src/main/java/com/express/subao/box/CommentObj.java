package com.express.subao.box;

import com.express.subao.handlers.DateHandle;

import java.util.Calendar;
import java.util.Date;

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
public class CommentObj {

    private String objectId;
    private String store;
    private String item;
    private String createdAt;
    private String content;
    private UserObj poster;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserObj getPoster() {
        return poster;
    }

    public void setPoster(UserObj poster) {
        this.poster = poster;
    }

    public String getUserAvatar() {
        if (poster != null) {
            return poster.getAvatar();
        }
        return "";
    }

    public String getUserName() {
        if (poster != null) {
            return poster.getNickname();
        }
        return "";
    }

    public String getTime() {
        try {
            long t = Long.valueOf(createdAt) * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(t));
            if (DateHandle.isToday(calendar)) {
                return DateHandle.getTimeString(calendar.getTime(), DateHandle.DATESTYP_8);
            } else {
                return DateHandle.getTimeString(calendar.getTime(), DateHandle.DATESTYP_9);
            }
        } catch (Exception e) {
            return "";
        }
    }
}
