package com.express.subao.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.express.subao.R;
import com.express.subao.adaptera.SdyBoxAdapter;
import com.express.subao.box.SdyBoxObj;
import com.express.subao.box.handlers.SdyBoxObjHandler;
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
 * Created by Hua on 16/5/13.
 */
public class SdyboxMapActivity extends BaseActivity {

    private final static int MAPZOOM = 18;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.boxMap_bmapView)
    private MapView mMapView;
    @ViewInject(R.id.boxMap_progress)
    private ProgressBar progress;
    @ViewInject(R.id.boxMap_dataList)
    private ListView dataList;
    @ViewInject(R.id.boxMap_mapIcon)
    private ImageView mapIcon;

    private BaiduMap mBaiduMap;
    private List<Overlay> mOverlayList;
    private List<SdyBoxObj> mSdyBoxObjList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_box_map);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back, R.id.boxMap_mapIcon, R.id.boxMap_positionIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.boxMap_mapIcon:
                setDataListVisibility();
                break;
            case R.id.boxMap_positionIcon:
                initMap();
                break;
        }
    }

    private void setDataListVisibility() {
        if (dataList.getVisibility() == View.VISIBLE) {
            dataList.setVisibility(View.GONE);
            mapIcon.setImageResource(R.drawable.map_narrow_icon);
        } else {
            dataList.setVisibility(View.VISIBLE);
            mapIcon.setImageResource(R.drawable.map_amp_icon);
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.box_map_text));
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        initMap();
    }

    private void initMap() {
//        GeoCoder mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener(getGeoCodeResultListener);
//        mSearch.geocode(new GeoCodeOption()
//                .city("澳门特别行政区").address("高士德大马路珍宝大夏"));
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
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);

        downloadData();
    }

    private void animateMap(LatLng point) {
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(MAPZOOM)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate, 500);
    }

    OnGetGeoCoderResultListener getGeoCodeResultListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                initMap(new LatLng(22.188811, 113.560326));
            } else {
                initMap(result.getLocation());
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                initMap(new LatLng(22.188811, 113.560326));
            } else {
                initMap(result.getLocation());
            }
        }
    };


    public void setMapPoint(List<SdyBoxObj> list) {
        addOverlay(list);
        dataList.setAdapter(new SdyBoxAdapter(context, list, new SdyBoxAdapter.AddressListener() {
            @Override
            public void onAddress(LatLng p) {
                animateMap(p);
            }
        }));
    }

    private void addOverlay(List<SdyBoxObj> list) {
        mOverlayList = new ArrayList<Overlay>();
        mSdyBoxObjList = new ArrayList<SdyBoxObj>();
        for (SdyBoxObj obj : list) {
            if (obj != null && obj.isHavePoint()) {
                mSdyBoxObjList.add(obj);
                mOverlayList
                        .add(addOverlay(obj));
            }
        }
    }

    private Overlay addOverlay(SdyBoxObj obj) {
        Log.e("", obj.getPoint().latitude + "   +   " + obj.getPoint().longitude);
        LatLng point = obj.getPoint();
        return addOverlay(point, obj);
    }

    private Overlay addOverlay(LatLng point, SdyBoxObj obj) {
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getOverlayIcon(icon, "999"));
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(obj.getOverlayView(context));
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap);
        return mBaiduMap.addOverlay(option);
    }


    private Bitmap getOverlayIcon(int icon, String str) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), icon);

        Bitmap newb = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);

        Canvas canvasTmp = new Canvas(newb);
        canvasTmp.drawColor(Color.TRANSPARENT);

        Paint p = new Paint();
        Typeface font = Typeface.create("宋体", Typeface.BOLD);
        p.setColor(Color.RED);
        p.setTypeface(font);
        p.setTextSize(16);
        canvasTmp.drawBitmap(bmp, 0, 0, p);
        canvasTmp.drawText(str, 10, 40, p);
        canvasTmp.save(Canvas.ALL_SAVE_FLAG);
        canvasTmp.restore();

        return newb;
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getSdyBoxes() + "?page=1&limit=100";

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
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
                            JSONArray array = JsonHandle.getArray(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                setMapPoint(SdyBoxObjHandler.getSdyBoxObjList(array));
                            }
                        }
                    }

                });
    }

}
