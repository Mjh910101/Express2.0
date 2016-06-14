package com.express.subao.fragments.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.RebateAdapter;
import com.express.subao.box.RebateObj;
import com.express.subao.box.handlers.RebateObjHandler;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

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
public class RebateFrameLayout extends BaseFragment {

    private final static int LIMIT = 30;


    @ViewInject(R.id.rebate_progress)
    private ProgressBar progress;
    @ViewInject(R.id.rebate_tagLayout)
    private LinearLayout tagLayout;
    @ViewInject(R.id.rebate_dataList)
    private ListView dataList;
    @ViewInject(R.id.rebate_dataListRefresh)
    private SwipeRefreshLayout dataListRefresh;

    private List<String> onClickTagList;
    private int page = 1, pages = 1;
    private String discountTag;

    private RebateAdapter rebateAdapter;

    @Override
    public void onRestart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        onClickTagList = new ArrayList<String>();
        View contactsLayout = inflater.inflate(R.layout.layout_rebate, container,
                false);
        ViewUtils.inject(this, contactsLayout);

        downloadTag();
        setDataListScrollListener();
        setOnRefreshListener();

        return contactsLayout;
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData();
                            }
                        } else {
                            MessageHandler.showLast(context);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

            }
        });
    }

    private void setOnRefreshListener() {
        dataListRefresh.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        dataListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                overloading();
            }
        });
    }

    public void setTagView(JSONArray array) {
        List<String> tagList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            tagList.add(JsonHandle.getString(array, i));
        }
        setTagView(tagList);
    }

    public void setTagView(List<String> tagList) {
        for (String s : tagList) {
            tagLayout.addView(getTagView(s));
        }
        downloadData();
    }

    private TextView getTagView(final String s) {
        final TextView view = new TextView(context);
        view.setText(s);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.rebate_off_tap_bg);
        view.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
        view.setTextSize(18);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickTagList.contains(s)) {
                    onClickTagList.remove(s);
                    view.setBackgroundResource(R.drawable.rebate_off_tap_bg);
                    view.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
                } else {
                    onClickTagList.add(s);
                    view.setBackgroundResource(R.drawable.rebate_on_tap_bg);
                    view.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                }
                overloading();
            }
        });
        return view;
    }

    public void setDataList(List<RebateObj> list) {
        if (rebateAdapter == null) {
            rebateAdapter = new RebateAdapter(context, list);
            dataList.setAdapter(rebateAdapter);
        } else {
            rebateAdapter.addItems(list);
        }

        if (dataListRefresh.isRefreshing()) {
            dataListRefresh.setRefreshing(false);
        }
    }

    private void overloading() {
        page = 1;
        pages = 1;
        rebateAdapter = null;
        downloadData();
    }

    private void downloadTag() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getTag("discount");

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
                                JSONArray array = JsonHandle.getArray(json, "results");

                                if (array != null) {
                                    setTagView(array);
                                }

                            }

                        }

                    }

                });
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getDiscount() + "?tag=" + getDiscountTag() + "&limit=" + LIMIT + "&page=" + page;

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
                                JSONArray array = JsonHandle.getArray(json, "results");

                                if (array != null) {
                                    List<RebateObj> list = RebateObjHandler.getDiscountObjList(array);
                                    setDataList(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }

                        }

                    }

                });
    }

    public String getDiscountTag() {
        StringBuffer sb = new StringBuffer();
        if (onClickTagList.isEmpty()) {
            return "";
        }

        for (String s : onClickTagList) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

}
