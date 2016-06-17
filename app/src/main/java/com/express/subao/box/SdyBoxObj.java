package com.express.subao.box;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.express.subao.R;
import com.express.subao.handlers.ColorHandle;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.tool.WinTool;

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
 * Created by Hua on 16/5/13.
 */
public class SdyBoxObj {

    private String address;
    private String title;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private LatLng point;
    private String cover;
    private String device_id;
    private int num;

    public void setNum(int num) {
        this.num = num;
    }

    public String getNum() {
//        try {
////            return String.valueOf(num + 1);
//            int n = num + 1;
//            if (n < 10) {
//                return "00" + n;
//            } else if (n < 100) {
//                return "0" + n;
//            } else {
//                return "" + n;
//            }
//        } catch (Exception e) {
//            return "";
//        }
        return "";
    }

    public String getDevice_id() {
        if (device_id == null || device_id.equals("null")) {
            return "";
        }
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(JSONObject json) {
        if (json != null) {
            this.cover = JsonHandle.getString(json, "url");
        }
    }

    public String getAddress() {
        if (address == null || address.equals("null")) {
            return "";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
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

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(double lat, double lon) {
        this.point = new LatLng(lat, lon);
    }

    public boolean isHavePoint() {
        return point != null;
    }

    public int getIconDrawble() {
        return R.drawable.sdy_icon;
    }

    public View getOverlayView(Context context) {
        TextView view = new TextView(context);
        view.setBackgroundResource(R.drawable.sdy_box_icon);
        view.setText(getNum());
        view.setTextSize(9);
        view.setTextColor(ColorHandle.getColorForID(context, R.color.red));
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
