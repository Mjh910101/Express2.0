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
import com.express.subao.adaptera.ItemAdapter;
import com.express.subao.adaptera.ItemTagAdapter;
import com.express.subao.box.ItemObj;
import com.express.subao.box.RebateObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.ItemObjHandler;
import com.express.subao.box.handlers.RebateObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.interfaces.CallbackForString;
import com.express.subao.views.LazyWebView;
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
 * Created by Hua on 15/12/30.
 */
public class ItemListActivity extends BaseActivity {

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

    private StoreObj mStoreObj;
    private int page = 1, pages = 1;
    private String tag = "";

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

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

    private void setDataListScrollListener() {
        itemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
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
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

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
            downloadItemTag(mStoreObj.getObjectId());
        }
    }


    public void setItemTagView(JSONArray array) {

        List<String> tagList = new ArrayList<String>(array.length());
        for (int i = 0; i < array.length(); i++) {
            tagList.add(JsonHandle.getString(array, i));
        }
        ItemTagAdapter adapter = new ItemTagAdapter(context, tagList);
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

        if (!tagList.isEmpty()) {
            downloadItem(tagList.get(0));
        }
    }


    public void setItemView(List<ItemObj> list) {
        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(context, list);
            itemList.setAdapter(itemAdapter);
        } else {
            itemAdapter.addItems(list);
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
                                    List<ItemObj> list = ItemObjHandler.getItemObjList(array);
                                    setItemView(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }

                        }

                    }

                });
    }

    private void downloadItemTag(String id) {

        progress.setVisibility(View.VISIBLE);

        String url = Url.getItemTag(id);

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
                                    setItemTagView(array);
                                }
                            }

                        }

                    }

                });

    }

}
