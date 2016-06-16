package com.express.subao.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.QueryExpresAdaper;
import com.express.subao.adaptera.SdyOrderAdaper;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.box.handlers.ExpresObjHandler;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.views.InsideListView;
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
 * Created by Hua on 16/1/29.
 */
public class BoxExpressListActivity extends BaseActivity {

    private final static int NO_RECEIVED = 1;
    private final static int RECEIVED = 2;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.boxExpress_progress)
    private ProgressBar progress;
    @ViewInject(R.id.boxExpress_notReceovedText)
    private TextView notReceivedText;
    @ViewInject(R.id.boxExpress_receovedText)
    private TextView receivedText;
    @ViewInject(R.id.boxExpress_notReceovedDataList)
    private InsideListView notReceovedDataList;
    @ViewInject(R.id.boxExpress_receovedDataList)
    private InsideListView receovedDataList;
    @ViewInject(R.id.boxExpress_notReceovedDataText)
    private TextView notReceovedDataText;
    @ViewInject(R.id.boxExpress_receovedDataText)
    private TextView receovedDataText;
    @ViewInject(R.id.boxExpress_scroll)
    private ScrollView scroll;
    @ViewInject(R.id.boxExpress_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;

    private List<SdyOrderObj> notReceivedList;
    private List<SdyOrderObj> receivedList;

    private int page = 1, pages = 1;
    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_express_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnTouchListener();
        setOnRefreshListener();
        downloadData(true);
    }

    private void setOnRefreshListener() {
        swipeRefresh.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                isShow = true;
                page = 1;
                notReceivedList.removeAll(notReceivedList);
                receivedList.removeAll(receivedList);
                downloadData(false);
            }
        });
    }

    private void setOnTouchListener() {
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasuredHeight = scroll.getChildAt(0).getMeasuredHeight();
                        if (scrollY == 0) {
                            Log.e("", "滑动到了顶端 view.getScrollY()=" + scrollY);
                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {
                            Log.e("", "滑动到了底部 scrollY=" + scrollY);
                            Log.e("", "滑动到了底部 height=" + height);
                            Log.e("", "滑动到了底部 scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);
                            if (pages >= page) {
                                if (progress.getVisibility() == View.GONE) {
                                    downloadData(true);
                                }
                            } else {
                                if (isShow) {
                                    isShow = false;
                                    MessageHandler.showLast(context);
                                }
                            }
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
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
        titleName.setText(TextHandeler.getText(context, R.string.open_box_find_box_text));

        notReceivedList = new ArrayList<SdyOrderObj>();
        receivedList = new ArrayList<SdyOrderObj>();

        setTitleListSize();
    }

    private void setTitleListSize() {
        notReceivedText.setText(TextHandeler.getText(context, R.string.not_received_text).replace("?", String.valueOf(notReceivedList.size())));
//        receivedText.setText(TextHandeler.getText(context, R.string.received_text).replace("?", String.valueOf(receivedList.size())));
        receivedText.setText("已取件");

        notReceovedDataText.setVisibility(View.GONE);
        notReceovedDataList.setVisibility(View.GONE);
        receovedDataText.setVisibility(View.GONE);
        receovedDataList.setVisibility(View.GONE);
        if (notReceivedList.size() > 0) {
            notReceovedDataList.setVisibility(View.VISIBLE);
        } else {
            notReceovedDataText.setVisibility(View.VISIBLE);
        }
        if (receivedList.size() > 0) {
            receovedDataList.setVisibility(View.VISIBLE);
        } else {
            receovedDataText.setVisibility(View.VISIBLE);
        }
    }

    private void finishingList(List<SdyOrderObj> list) {
        for (SdyOrderObj obj : list) {
            if (obj.getStatus().equals("1")) {
                notReceivedList.add(obj);
            } else {
                receivedList.add(obj);
            }
        }
        setTitleListSize();

        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

        receovedDataList.setAdapter(new SdyOrderAdaper(context, receivedList));
        notReceovedDataList.setAdapter(new SdyOrderAdaper(context, notReceivedList));

//        scroll.smoothScrollTo(0, 0);
    }

    private void downloadData(boolean b) {
        if (b) {
            progress.setVisibility(View.VISIBLE);
        }
        String url = Url.getUserOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&page=" + page + "&limit=100";

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
                                    List<SdyOrderObj> list = SdyOrderObjHandler.getSdyOrderObjList(array);
                                    finishingList(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }


}
