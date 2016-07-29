package com.express.subao.fragments.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.express.subao.R;
import com.express.subao.activitys.QueryExpressActivity;
import com.express.subao.activitys.TopUpActivity;
import com.express.subao.adaptera.CategoryAdapter;
import com.express.subao.adaptera.HelpAdapter;
import com.express.subao.box.CategoryObj;
import com.express.subao.box.HelpObj;
import com.express.subao.box.SliderObj;
import com.express.subao.box.handlers.AreaObjHandler;
import com.express.subao.box.handlers.CategoryObjHandler;
import com.express.subao.box.handlers.HelpObjHandler;
import com.express.subao.box.handlers.SliderObjHandler;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.views.InsideGridView;
import com.express.subao.views.InsideViewFlipper;
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
public class MainFrameLayout extends BaseFragment {

    @ViewInject(R.id.main_progress)
    private ProgressBar progress;
    @ViewInject(R.id.ppt_images)
    private InsideViewFlipper mViewFlipper;
    @ViewInject(R.id.ppt_ball)
    private LinearLayout pptBallBox;
    @ViewInject(R.id.main_helpGrid)
    private InsideGridView helpGrid;
    @ViewInject(R.id.main_categoryGrid)
    private GridView categoryGrid;
    @ViewInject(R.id.ppt_boxBg)
    private ImageView boxBg;

    private SliderView mSliderView;

    @Override
    public void onRestart() {
        pptBallBox.removeAllViews();
        mViewFlipper.removeAllViews();
        if (mSliderView != null) {
            mSliderView.stopFlish();
        }
        downloadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main, container,
                false);
        ViewUtils.inject(this, contactsLayout);

        downloadData();

        return contactsLayout;
    }

    @OnClick({R.id.main_queryExpress, R.id.main_topUP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_queryExpress:
                Passageway.jumpActivity(context, QueryExpressActivity.class);
                break;
            case R.id.main_topUP:
                Passageway.jumpActivity(context, TopUpActivity.class);
                break;
        }
    }


    public void setSliderView(JSONArray array) {
        if (array != null) {
            setSliderView(SliderObjHandler.getSliderObjList(array));
        }
    }

    private void setSliderView(List<SliderObj> list) {
        mSliderView = SliderView.initSliderView(context, list, mViewFlipper, pptBallBox,boxBg);
    }

    public void setHelpView(JSONArray array) {
        if (array != null) {
            List<HelpObj> helpObjList = HelpObjHandler.getHelpObjList(array);
            helpGrid.setAdapter(new HelpAdapter(context, helpObjList));
        }
    }

    public void setCategoryView(JSONArray array) {
        if (array != null) {
            List<CategoryObj> list = CategoryObjHandler.getCategoryObjList(array);
            categoryGrid.setAdapter(new CategoryAdapter(context, list));
        }
    }


    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getHomeUrl(AreaObjHandler.getAreaName(context));

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
                                JSONObject resultJson = JsonHandle.getJSON(json, "results");

                                if (resultJson != null) {
                                    JSONArray sliderArray = JsonHandle.getArray(resultJson, "slider");
                                    setSliderView(sliderArray);
                                    JSONArray helpArray = JsonHandle.getArray(resultJson, "help");
                                    setHelpView(helpArray);
                                    JSONArray categoryArray = JsonHandle.getArray(resultJson, "category");
                                    setCategoryView(categoryArray);
                                }
                            }

                        }

                    }

                });
    }

}
