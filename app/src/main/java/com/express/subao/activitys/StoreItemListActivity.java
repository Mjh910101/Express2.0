package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.ItemAdapter;
import com.express.subao.adaptera.ItemTagAdapter;
import com.express.subao.adaptera.StoreItemAdapter;
import com.express.subao.box.ItemObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.ItemObjHandler;
import com.express.subao.box.handlers.StoreItemObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.interfaces.CallbackForString;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;
import com.express.subao.views.InsideListView;
import com.express.subao.views.InsideViewFlipper;
import com.express.subao.views.SliderView;
import com.express.subao.views.TouchLinearLayout;
import com.express.subao.views.TouchScrollView;
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
 * Created by Hua on 16/5/26.
 */
public class StoreItemListActivity extends BaseActivity {

    private final static int LIMIT = 20;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.itemList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.itemList_itemTagList)
    private ListView itemTagList;
    @ViewInject(R.id.itemList_itemList)
    private ListView itemList;

    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.itemList_scrollView)
    private ScrollView scroll;
    //    @ViewInject(R.id.itemList_pic)
//    private ImageView pic;
    @ViewInject(R.id.itemList_toolLayout)
    private LinearLayout toolLayout;
    @ViewInject(R.id.itemList_listLayout)
    private LinearLayout listLayout;
    @ViewInject(R.id.itemList_fatherLayou)
    private TouchLinearLayout fatherLayou;
    @ViewInject(R.id.ppt_box)
    private RelativeLayout sliderLayout;
    @ViewInject(R.id.ppt_images)
    private InsideViewFlipper sliderFlipper;
    @ViewInject(R.id.ppt_ball)
    private LinearLayout ballLayout;
    @ViewInject(R.id.ppt_boxBg)
    private ImageView sliderBg;
    @ViewInject(R.id.itemList_commentsSumText)
    private TextView commentsSumText;

    private StoreObj mStoreObj;
    private int page = 1, pages = 1;
    private String tag = "";

    private boolean isDispatchScroll = false;
    private StoreItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
        setTestLayout();
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setTestLayout() {
        int w = WinTool.getWinWidth(context);
        int h = WinTool.getWinHeight(context);

        final int titleHeight = (int) TitleHandler.getTilteHeight(context) + getStatusBarHeight();

        listLayout.setLayoutParams(new LinearLayout.LayoutParams(w, h - titleHeight - 80));
        toolLayout.setLayoutParams(new LinearLayout.LayoutParams(w, 80));
//        sliderLayout.setLayoutParams(new LinearLayout.LayoutParams(w, 360));

        fatherLayou.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] position = new int[2];
                sliderLayout.getLocationOnScreen(position);
                Log.i("scroll", "************************************************************************");
                Log.e("scroll", "position[0] : " + position[0] + " , position[1] : " + position[1]);
                Log.e("scroll", "slider Width : " + sliderLayout.getWidth() + " , slider Height : " + sliderLayout.getHeight());
                Log.e("scroll", "x : " + (position[1] - titleHeight + sliderLayout.getHeight()));
                Log.e("scroll", "event X : " + event.getX() + " , " +
                        "event Y :" + event.getY());
                if (isDispatchScroll || position[1] - titleHeight + sliderLayout.getHeight() > 1) {
                    scroll.dispatchTouchEvent(event);
                    isDispatchScroll = false;
                } else {
                    if (event.getY() <= 80) {
                        toolLayout.dispatchTouchEvent(event);
                    } else {
                        if (event.getX() < listLayout.getWidth() / 3) {
                            itemTagList.dispatchTouchEvent(event);
                        } else {
                            itemList.dispatchTouchEvent(event);
                        }
//                        listLayout.dispatchTouchEvent(event);
                    }
                }
                return true;
            }
        });

    }


    public boolean isDispatchScroll() {
        if (itemList.getFirstVisiblePosition() == 0) {
            View firstItem = itemList.getChildAt(0);
            if (firstItem != null) {
                if (firstItem.getTop() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnClick({R.id.title_back, R.id.imageList_searchIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.imageList_searchIcon:
                jumpSearchItemActivity();
                break;
        }
    }

    private void jumpSearchItemActivity() {
        Bundle b = new Bundle();
        b.putString("storeId", mStoreObj.getObjectId());
        Passageway.jumpActivity(context, SearchItemActivity.class, b);
    }

    private void setDataListScrollListener() {
        itemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page <= pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadItem(tag);
                            }
                        } else {
                            MessageHandler.showLast(context);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("scroll", "count : " + firstVisibleItem);
                View firstItem = itemList.getChildAt(0);
                if (firstVisibleItem < 1 && firstItem != null && firstItem.getTop() == 0) {
                    Log.e("scroll", "top : " + firstItem.getTop());
                    isDispatchScroll = true;
                } else {
                    isDispatchScroll = false;
                }
            }
        });
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.item_list_text));


        mStoreObj = StoreObjHandler.getStoreObj();
        if (mStoreObj != null) {
//            titleName.setText(mStoreObj.getTitle());
//            downloadItemTag(mStoreObj.getObjectId());
            downloadStoreData(mStoreObj.getObjectId());
            SliderView.initSliderView(context, mStoreObj.getSliderList(), sliderFlipper, ballLayout);
            sliderBg.setVisibility(View.GONE);
        }
    }

    public void setStoreMessage(StoreObj obj) {
        setItemTagView(obj.getTapList());
        commentsSumText.setText(TextHandeler.getText(context, R.string.comments_sum_text).replace("0", String.valueOf(obj.getComments())) + "   >");
    }

    public void setItemTagView(JSONArray array) {

        List<String> tagList = new ArrayList<String>(array.length());
        for (int i = 0; i < array.length(); i++) {
            tagList.add(JsonHandle.getString(array, i));
        }
        setItemTagView(tagList);
    }

    public void setItemTagView(List<String> tagList) {
        final ItemTagAdapter adapter = new ItemTagAdapter(context, tagList);
        adapter.setCallback(new CallbackForString() {
            @Override
            public void callback(String result) {
                itemAdapter = null;
                page = 1;
                pages = 1;
                itemList.setAdapter(null);
                downloadItem(result);
            }
        });
        itemTagList.setAdapter(adapter);
//        itemTagList.setFocusable(false);

//        itemTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("click", "on item click : " + position + " , id : " + id);
//            }
//        });

        if (!tagList.isEmpty()) {
            downloadItem(tagList.get(0));
        }
    }


    public void setItemView(List<StoreItemObj> list) {
        list.addAll(list);
        if (itemAdapter == null) {
            itemAdapter = new StoreItemAdapter(context, list);
            itemList.setAdapter(itemAdapter);
        } else {
            itemAdapter.addItems(list);
        }
        if (page == 1) {
//            itemList.setFocusable(false);
            scroll.smoothScrollTo(0, 0);
        }
    }

    private void downloadItem(String tag) {
        this.tag = tag;
        progress.setVisibility(View.VISIBLE);

        String url = Url.getItem(mStoreObj.getObjectId()) + "?id=" + tag + "&limit=" + LIMIT + "&page=" + page;

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
                                    List<StoreItemObj> list = StoreItemObjHandler.getStoreItemObjList(array);
                                    setItemView(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }

                        }

                    }

                });
    }

    private void downloadStoreData(String id) {

        progress.setVisibility(View.VISIBLE);

        String url = Url.getStore(id) + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                    mStoreObj = StoreObjHandler.getStoreObj(resultsJson);
                                    setStoreMessage(mStoreObj);
                                }
                            }

                        }

                    }

                });
    }

}
