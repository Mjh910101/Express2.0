package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.express.subao.R;
import com.express.subao.box.SdyBoxObj;
import com.express.subao.box.UserAddressObj;
import com.express.subao.box.handlers.SdyBoxObjHandler;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.box.handlers.UserAddressObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MapHandler;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 16/7/13.
 */
public class UserAddressActivity extends BaseActivity {

    private final static int MAPZOOM = 18;
    public final static int RC = 12012;
    public final static String IS_NEW = "new";

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_editorText)
    private TextView editorText;
    @ViewInject(R.id.addressEditor_nameInput)
    private EditText nameInput;
    @ViewInject(R.id.addressEditor_telInput)
    private EditText telInput;
    @ViewInject(R.id.addressEditor_sdyBoxText)
    private TextView sdyBoxText;
    @ViewInject(R.id.addressEditor_addressText)
    private TextView addressText;
    @ViewInject(R.id.addressEditor_progress)
    private ProgressBar progress;
    @ViewInject(R.id.addressEditor_addressMap)
    private MapView mMapView;
    @ViewInject(R.id.addressEditor_defaultIcon)
    private ImageView defaultIcon;
    @ViewInject(R.id.addressEditor_deleteBtn)
    private RelativeLayout deleteBtn;

    private UserAddressObj address;
    private SdyBoxObj userBox;
    private boolean isDefault = false;

    private BaiduMap mBaiduMap;

    private Marker onCurrentMarker;
    private Marker onClickMarker;

    private boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_address_editor);

        context = this;
        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC:
                setResultSdyBox(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_back, R.id.addressEditor_sdyBoxText, R.id.addressEditor_defaultIcon,
            R.id.title_editorText, R.id.addressEditor_deleteBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.addressEditor_sdyBoxText:
                jumpSdyBoxMapActivity();
                break;
            case R.id.addressEditor_defaultIcon:
                setDefaultIcon(!isDefault);
                break;
            case R.id.title_editorText:
                uploadUserAddress();
                break;
            case R.id.addressEditor_deleteBtn:
                deleteAddress();
                break;
        }
    }

    private void jumpSdyBoxMapActivity() {
        Bundle b = new Bundle();
        b.putBoolean(SdyboxMapActivity.IS_SET, true);
        Passageway.jumpActivity(context, SdyboxMapActivity.class, RC, b);
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText("編輯收貨地址");
        editorText.setVisibility(View.VISIBLE);
        editorText.setText("保存");

        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            isNew = b.getBoolean(IS_NEW);
            if (isNew) {
                initMap();
                setDefaultIcon(false);
            } else {
                address = UserAddressObjHandler.getUserAddressObj();
                if (address == null) {
                    finish();
                } else {
                    deleteBtn.setVisibility(View.VISIBLE);
                    if (!address.isNull()) {
                        userBox = address.getBox();
                        setSdyBox(userBox);
                    } else {
                        initMap();
                    }
                    setDefaultIcon(address.isDefault());
                    setMessage(address);
                }
            }
        }

    }

    private void initMap() {
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                initMap(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    private void initMap(LatLng point) {
        animateMap(point);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.position_icon);
        removeOnCurrentMarker();
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        onCurrentMarker = (Marker) mBaiduMap.addOverlay(option);
    }

    private void animateMap(LatLng point) {
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(MAPZOOM)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate, 500);

    }

    private void removeOnCurrentMarker() {
        removeMarker(onCurrentMarker);
        onCurrentMarker = null;
    }

    private void removeOnClickMarker() {
        removeMarker(onClickMarker);
        onClickMarker = null;
    }

    private void removeMarker(Marker marker) {
        if (marker != null) {
            marker.remove();
        }
    }

    private Marker setSdyIcon(SdyBoxObj obj) {
        BitmapDescriptor icon_marka = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_icon);
        removeOnClickMarker();
        OverlayOptions option = new MarkerOptions().position(obj.getPoint()).icon(icon_marka);
        onClickMarker = (Marker) mBaiduMap.addOverlay(option);
        return onClickMarker;
    }

    public void setResultSdyBox(Intent i) {
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                if (b.getBoolean(SdyboxMapActivity.IS_SET)) {
                    userBox = SdyBoxObjHandler.getSdyBoxObj();
                    setSdyBox(userBox);
                }
            }
        }
    }

    public void setSdyBox(SdyBoxObj obj) {
        sdyBoxText.setText(obj.getTitle());
        addressText.setText(obj.getAddress());
        animateMap(obj.getPoint());
        setSdyIcon(obj);
    }

    public void setDefaultIcon(boolean b) {
        isDefault = b;
        if (isDefault) {
            defaultIcon.setImageResource(R.drawable.choice_on_icon);
        } else {
            defaultIcon.setImageResource(R.drawable.choice_off_icon);
        }
    }


    private void uploadUserAddress() {
        if (isCommit()) {
            postUserAddress();
        }
    }

    public boolean isCommit() {
        if (TextHandeler.getText(nameInput).equals("")) {
            MessageHandler.showToast(context, "請輸入收貨人");
            return false;
        }
        if (TextHandeler.getText(telInput).equals("")) {
            MessageHandler.showToast(context, "請輸入聯繫電話");
            return false;
        }
        if (userBox == null) {
            MessageHandler.showToast(context, "請選擇收貨箱體");
            return false;
        }
        return true;
    }


    private void postUserAddress() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getUserAddress();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("receiver", TextHandeler.getText(nameInput));
        params.addBodyParameter("contact", TextHandeler.getText(telInput));
        params.addBodyParameter("default_flag", getDefaultForText());
        params.addBodyParameter("box_id", userBox.getObjectId());
        if (!isNew) {
            params.addBodyParameter("address_id", address.getObjectId());
        }

        Log.e("box_id", userBox.getObjectId());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            if (JsonHandle.getInt(json, "status") == 1) {
                                String msg = "";
                                if (isNew) {
                                    msg = "添加成功";
                                } else {
                                    msg = "修改成功";
                                }
                                MessageHandler.showToast(context, msg);
                                close();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "results"));
                            }
                        }
                    }

                });
    }


    private void deleteAddress() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getUserAddressRemove();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("address_id", address.getObjectId());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            if (JsonHandle.getInt(json, "status") == 1) {
                                close();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "results"));
                            }
                        }
                    }

                });
    }

    private void close() {
        Bundle b = new Bundle();
        b.putBoolean("is_post", true);
        Intent i = new Intent();
        i.putExtras(b);
        setResult(RC, i);
        finish();
    }

    public String getDefaultForText() {
        if (isDefault) {
            return "1";
        }
        return "0";
    }

    public void setMessage(UserAddressObj obj) {
        nameInput.setText(obj.getReceiver());
        telInput.setText(obj.getContact());
    }
}
