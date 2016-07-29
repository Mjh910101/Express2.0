package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.UserOrderAdaper;
import com.express.subao.box.OrderObj;
import com.express.subao.box.handlers.OrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.ColorHandle;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
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
 * Created by Hua on 16/7/27.
 */
public class UserOrderListActivity extends BaseActivity {

    private final static String ALL = "";
    private final static String WAITING = "1";
    private final static String ASSESS = "0";
    private final static String SHIP = "2";
    private final static int LIMIT = 20;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.userOrder_clickOn_allTopLine)
    private View allTopLin;
    @ViewInject(R.id.userOrder_clickOff_allLine)
    private View allLine;
    @ViewInject(R.id.userOrder_clickOn_allBelowLine)
    private View allBelowLine;
    @ViewInject(R.id.userOrder_allText)
    private TextView allText;
    @ViewInject(R.id.userOrder_clickOn_waitingTopLine)
    private View waitingTopLin;
    @ViewInject(R.id.userOrder_clickOff_waitingLine)
    private View waitingLine;
    @ViewInject(R.id.userOrder_clickOn_waitingBelowLine)
    private View waitingBelowLine;
    @ViewInject(R.id.userOrder_waitingText)
    private TextView waitingText;
    @ViewInject(R.id.userOrder_clickOn_assessTopLine)
    private View assessTopLin;
    @ViewInject(R.id.userOrder_clickOff_assessLine)
    private View assessLine;
    @ViewInject(R.id.userOrder_clickOn_assessBelowLine)
    private View assessBelowLine;
    @ViewInject(R.id.userOrder_assessText)
    private TextView assessText;
    @ViewInject(R.id.userOrder_clickOn_shipTopLine)
    private View shipTopLin;
    @ViewInject(R.id.userOrder_clickOff_shipLine)
    private View shipLine;
    @ViewInject(R.id.userOrder_clickOn_shipBelowLine)
    private View shipBelowLine;
    @ViewInject(R.id.userOrder_shipText)
    private TextView shipText;
    @ViewInject(R.id.userOrder_progress)
    private ProgressBar progress;
    @ViewInject(R.id.userOrder_dataList)
    private ListView dataList;

    private String now_type = "";
    private int page = 1, pages = 1;
    private UserOrderAdaper userOrderAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @OnClick({R.id.userOrder_allText, R.id.userOrder_waitingText, R.id.userOrder_assessText, R.id.userOrder_shipText})
    public void onType(View view) {
        switch (view.getId()) {
            case R.id.userOrder_allText:
                onClickType(ALL);
                break;
            case R.id.userOrder_waitingText:
                onClickType(WAITING);
                break;
            case R.id.userOrder_assessText:
                onClickType(ASSESS);
                break;
            case R.id.userOrder_shipText:
                onClickType(SHIP);
                break;
        }
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page <= pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData(now_type);
                            }
                        } else {
                            MessageHandler.showLast(context);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText("我的訂單");
        TitleHandler.setTitle(context, titleLayout);
        onClickType(ALL);
    }

    private void onClickType(String type) {
        now_type = type;
        initType();
        switch (type) {
            case ALL:
                onAllType();
                break;
            case WAITING:
                onWaitingType();
                break;
            case ASSESS:
                onAssessType();
                break;
            case SHIP:
                onShipType();
                break;
        }
        downloadData(type);
    }

    private void onShipType() {
        shipTopLin.setVisibility(View.VISIBLE);
        shipLine.setVisibility(View.INVISIBLE);
        shipBelowLine.setBackgroundResource(R.color.whitle);
        shipText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onAssessType() {
        assessTopLin.setVisibility(View.VISIBLE);
        assessLine.setVisibility(View.INVISIBLE);
        assessBelowLine.setBackgroundResource(R.color.whitle);
        assessText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onWaitingType() {
        waitingTopLin.setVisibility(View.VISIBLE);
        waitingLine.setVisibility(View.INVISIBLE);
        waitingBelowLine.setBackgroundResource(R.color.whitle);
        waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onAllType() {
        allTopLin.setVisibility(View.VISIBLE);
        allLine.setVisibility(View.INVISIBLE);
        allBelowLine.setBackgroundResource(R.color.whitle);
        allText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void initType() {
        allTopLin.setVisibility(View.INVISIBLE);
        waitingTopLin.setVisibility(View.INVISIBLE);
        assessTopLin.setVisibility(View.INVISIBLE);
        shipTopLin.setVisibility(View.INVISIBLE);

        allLine.setVisibility(View.VISIBLE);
        waitingLine.setVisibility(View.VISIBLE);
        assessLine.setVisibility(View.VISIBLE);
        shipLine.setVisibility(View.VISIBLE);

        allBelowLine.setBackgroundResource(R.color.red);
        waitingBelowLine.setBackgroundResource(R.color.red);
        assessBelowLine.setBackgroundResource(R.color.red);
        shipBelowLine.setBackgroundResource(R.color.red);

        allText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        assessText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        shipText.setTextColor(ColorHandle.getColorForID(context, R.color.black));

        page = 1;
        pages = 1;
        userOrderAdaper = null;
    }

    private void setListData(List<OrderObj> list) {
        if (userOrderAdaper == null) {
            userOrderAdaper = new UserOrderAdaper(context, list);
            dataList.setAdapter(userOrderAdaper);
        }
    }

    private void downloadData(String type) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getOrder() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&status=" + type + "&page=" + page + "&limit=" + LIMIT;

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
                                    List<OrderObj> list = OrderObjHandler.getOrderObjList(array);
                                    setListData(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }

                        }

                    }

                });
    }


}
