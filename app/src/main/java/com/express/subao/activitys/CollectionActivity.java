package com.express.subao.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.RebateAdapter;
import com.express.subao.box.RebateObj;
import com.express.subao.box.handlers.RebateObjHandler;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
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
 * Created by Hua on 15/12/29.
 */
public class CollectionActivity extends BaseActivity {

    private final static int LIMIT = 20;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.collection_progress)
    private ProgressBar progress;
    @ViewInject(R.id.collection_tagLayout)
    private LinearLayout tagLayout;
    @ViewInject(R.id.collection_dataList)
    private ListView dataList;
    @ViewInject(R.id.collection_dataListRefresh)
    private SwipeRefreshLayout dataListRefresh;

    private List<String> onClickTagList;
    private int page = 1, pages = 1;
    private String discountTag;

    private RebateAdapter rebateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        downloadTag();
        setDataListScrollListener();
        setOnRefreshListener();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.user_collection_text));
        onClickTagList = new ArrayList<String>();
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
