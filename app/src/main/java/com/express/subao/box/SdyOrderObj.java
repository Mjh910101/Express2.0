package com.express.subao.box;

import android.util.Log;

import com.express.subao.handlers.DateHandle;

import java.text.SimpleDateFormat;
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
 * Created by Hua on 16/5/18.
 */
public class SdyOrderObj {

    public final static String FEE = "fee";
    public final static String MAILAN = "mailman";
    public final static String MAILAN_MOBILE = "mailman_mobile";
    public final static String SDY_ORDER_ID = "sdy_order_id";
    public final static String OPEN_CODE = "open_code";
    public final static String STATUS = "status";
    public final static String MOBILE = "mobile";
    public final static String MAILNO = "mailno";
    public final static String OBJECT_ID = "objectId";
    public final static String CREATED_AT = "createdAt";
    public final static String UPDATED_AT = "updatedAt";
    public final static String PICKUP_TIMR = "pickup_time";
    public final static String DEVICE_ID = "device_id";

    private int fee;
    private String mailman;
    private String mailman_mobile;
    private String sdy_order_id;
    private String open_code;
    private String status;
    private String mobile;
    private String mailno;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String pickup_time;
    private String device_id;
    private SdyBoxObj boxObj;

    public String getBoxDeviceId() {
        if (boxObj == null) {
            return "";
        }
        return boxObj.getDevice_id();
    }

    public String getBoxAddress() {
        if (boxObj == null) {
            return "";
        }
        return boxObj.getAddress();
    }

    public void setBoxObj(SdyBoxObj boxObj) {
        this.boxObj = boxObj;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPickup_time() {
        if (pickup_time.indexOf(" ") > 0) {
            return pickup_time.substring(0, pickup_time.indexOf(" "));
        }
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getMailman() {
        if (mailman == null || mailman.equals("null")) {
            return "";
        }
        return mailman;
    }

    public void setMailman(String mailman) {
        this.mailman = mailman;
    }

    public String getMailman_mobile() {
        return mailman_mobile;
    }

    public void setMailman_mobile(String mailman_mobile) {
        this.mailman_mobile = mailman_mobile;
    }

    public String getSdy_order_id() {
        return sdy_order_id;
    }

    public void setSdy_order_id(String sdy_order_id) {
        this.sdy_order_id = sdy_order_id;
    }

    public String getOpen_code() {
        return open_code;
    }

    public void setOpen_code(String open_code) {
        this.open_code = open_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        if (createdAt == null || createdAt.equals("null")) {
            return "";
        }
        if (createdAt.indexOf(" ") > 0) {
            return createdAt.substring(0, createdAt.indexOf(" "));
        }
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    private final static String DATA_TYPE = "yyyy-MM-dd HH:mm:ss";

    public String getStayTime() {
        StringBuffer sb = new StringBuffer();
        sb.append("已存放");
        int s = 1;
        int m = s * 60;
        int h = m * 60;
        int d = h * 24;
        int y = d * 365;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATA_TYPE);
            Date date = sdf.parse(createdAt);
            long c = date.getTime();
            long n = DateHandle.getTime();
            Log.e("", "now time : " + n);
            Log.e("", "created time : " + c + " = " + DateHandle.format(c, DATA_TYPE));
            int stayTime = (int) (n - c / 1000);
            Log.e("", "stay time : " + stayTime);
            int year = stayTime / y;
            Log.e("", "year : " + stayTime + " " + y + " " + year);
            if (year > 0) {
                sb.append(year);
                sb.append("年");
            }
            int day = stayTime % y / d;
            Log.e("", "day : " + (stayTime % y) + " " + d + " " + day);
            if (day > 0) {
                sb.append(day);
                sb.append("天");
            }
            int hours = stayTime % y % d / h;
            Log.e("", "hours : " + (stayTime % y % d) + " " + h + " " + hours);
            if (hours > 0) {
                sb.append(hours);
                sb.append("小時");
            }
            int minutes = stayTime % y % d % h / m;
            Log.e("", "minutes : " + (stayTime % y % d % h) + " " + m + " " + minutes);
            if (minutes > 0) {
                sb.append(minutes);
                sb.append("分");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
