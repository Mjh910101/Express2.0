package com.express.subao.fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.express.subao.R;
import com.express.subao.activitys.SdyOrderListActivity;
import com.express.subao.activitys.SdyboxMapActivity;
import com.express.subao.activitys.WebActivity;
import com.express.subao.adaptera.StoreAdapter;
import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.SliderObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;
import com.express.subao.views.HeaderGridView;
import com.express.subao.views.InsideGridView;
import com.express.subao.views.InsideViewFlipper;
import com.express.subao.views.MainFrameHeadView;
import com.express.subao.views.SliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
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
 * Created by Hua on 15/12/21.
 */
public class MainFrameLayoutV4 extends BaseFragment {

    @ViewInject(R.id.main_progress)
    private ProgressBar progress;
    @ViewInject(R.id.main_sortGrid)
    private HeaderGridView sortGrid;

    private MainFrameHeadView headView;

    @Override
    public void onRestart() {
        if (headView != null) {
            headView.onRestart();
        }
        downloadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_v4, container,
                false);
        ViewUtils.inject(this, contactsLayout);

        downloadData();
//        initTestStoreGrid();
        return contactsLayout;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopFlish();
    }

    @Override
    public void onStart() {
        super.onStart();
        startFlish();
    }

    public void startFlish() {
        if (headView != null) {
            headView.startFlish();
        }
    }

    public void stopFlish() {
        if (headView != null) {
            headView.stopFlish();
        }
    }

    public void setHeadView(JSONArray array) {
        if (array != null) {
            setHeadView(SliderObjHandler.getSliderObjList(array));
        }
    }

    private void setHeadView(List<SliderObj> list) {
        headView = new MainFrameHeadView(context, list);
        sortGrid.addHeaderView(headView);
    }


    public void setStoreGrid(JSONArray array) {
        if (array != null) {
            setStoreGrid(StoreObjHandler.getStoreObjList(array));
        }
    }

    private void setStoreGrid(List<StoreObj> list) {
        sortGrid.setAdapter(new StoreAdapter(context, list));
        sortGrid.setFocusable(false);
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getHome() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);
//        String url = Url.getHomeUrl(AreaObjHandler.getAreaName(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url,
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
                                JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                                if (resultsJson != null) {
                                    JSONArray sliderArray = JsonHandle.getArray(resultsJson, "slider");
                                    setHeadView(sliderArray);
                                    JSONArray storeArray = JsonHandle.getArray(resultsJson, "store");
                                    setStoreGrid(storeArray);
                                }
                            }

                        }

                    }

                });
    }

}
