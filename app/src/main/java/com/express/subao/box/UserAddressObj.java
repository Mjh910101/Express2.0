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
 * Created by Hua on 16/7/12.
 */
public class UserAddressObj {

    private boolean isDeleted;
    private SdyBoxObj box;
    private String contact;
    private String receiver;
    private String default_flag;
    private String objectId;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public SdyBoxObj getBox() {
        return box;
    }

    public void setBox(SdyBoxObj box) {
        this.box = box;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDefault_flag() {
        return default_flag;
    }

    public void setDefault_flag(String default_flag) {
        this.default_flag = default_flag;
    }

    public boolean isDefault() {
        int d = 0;
        try {
            d = Integer.valueOf(default_flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d == 1;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isNull() {
        if (box != null) {
            return box.getPoint() == null;
        }
        return box == null;
    }

    public String getAddress() {
        if (box != null) {
            return box.getAddress();
        }
        return "";
    }
}
