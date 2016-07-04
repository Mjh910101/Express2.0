package com.express.subao.activitys;

import android.content.Context;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.StoreItemAdapter;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.handlers.StoreItemObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
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
 * Created by Hua on 16/7/1.
 */
public class SearchItemActivity extends BaseActivity {

    private final static int LIMIT = 20;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.searchItem_searchInput)
    private EditText searchInput;
    @ViewInject(R.id.searchItem_progress)
    private ProgressBar progress;
    @ViewInject(R.id.searchItem_dataList)
    private ListView dataList;

    private StoreItemAdapter itemAdapter;
    private String storeId, search;

    private InputMethodManager imm = null;

    private int page = 1, pages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @OnClick({R.id.title_back, R.id.searchItem_searchIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.searchItem_searchIcon:
                searchStoreItem(TextHandeler.getText(searchInput));
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
                                downloadItem(search);
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
        titleName.setText(TextHandeler.getText(context, R.string.item_list_text));
        imm = (InputMethodManager) searchInput.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            storeId = b.getString("storeId");
        } else {
            finish();
        }
    }

    private void searchStoreItem(String search) {
        this.search = search;
        if (!search.equals("")) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        searchInput.getApplicationWindowToken(), 0);
            }
            page = 1;
            pages = 1;
            downloadItem(search);
        } else {

        }
    }

    public void setItemView(List<StoreItemObj> list) {
        if (itemAdapter == null) {
            itemAdapter = new StoreItemAdapter(context, list);
            dataList.setAdapter(itemAdapter);
        } else {
            itemAdapter.addItems(list);
        }
//        listLayout.setFocusable(false);
    }

    private void downloadItem(String search) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getItem(storeId) + "?search=" + search + "&limit=" + LIMIT + "&page=" + page;

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

}
