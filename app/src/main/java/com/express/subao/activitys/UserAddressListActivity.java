package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.UserAddressAdaper;
import com.express.subao.box.UserAddressObj;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.box.handlers.UserAddressObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
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
 * Created by Hua on 16/7/13.
 */
public class UserAddressListActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_editorText)
    private TextView editorText;
    @ViewInject(R.id.addressList_progress)
    private ProgressBar progress;
    @ViewInject(R.id.addressList_dataList)
    private ListView dataList;
    @ViewInject(R.id.addressList_setLayout)
    private RelativeLayout setLayout;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private int page = 1, pages = 1;

    private UserAddressAdaper mUserAddressAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UserAddressActivity.RC:
                refreshData(data);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    @OnClick({R.id.title_back, R.id.title_editorText, R.id.addressList_newAddressBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                close();
                break;
            case R.id.title_editorText:
                onSetBtn();
                break;
            case R.id.addressList_newAddressBtn:
                jumpAddressActivity();
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
                                downloadData();
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

    private void jumpAddressActivity() {
        Bundle b = new Bundle();
        b.putBoolean(UserAddressActivity.IS_NEW, true);
        Passageway.jumpActivity(context, UserAddressActivity.class, UserAddressActivity.RC, b);

    }

    private void close() {
        if (mUserAddressAdaper != null) {
            UserAddressObj obj = mUserAddressAdaper.getClickUserAddress();
            if (obj != null) {
                UserAddressObjHandler.saveUserAddressObj(obj);
            }
        }
        finish();
    }

    private void onSetBtn() {
        if (mUserAddressAdaper != null) {
            if (setLayout.getVisibility() == View.GONE) {
                setLayout.setVisibility(View.VISIBLE);
                editorText.setText("完成");
                mUserAddressAdaper.setSetting(true);
            } else {
                setLayout.setVisibility(View.GONE);
                editorText.setText("管理");
                mUserAddressAdaper.setSetting(false);
            }
        }
    }

    private void setDataView(List<UserAddressObj> list) {
        if (mUserAddressAdaper == null) {
            mUserAddressAdaper = new UserAddressAdaper(context, list);
            dataList.setAdapter(mUserAddressAdaper);
        } else {
            mUserAddressAdaper.add(list);
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        editorText.setVisibility(View.VISIBLE);
        editorText.setText("管理");
        titleName.setText("選擇收貨地址");

        downloadData();
    }


    private void refreshData(Intent data) {
        if (data != null) {
            Bundle b = data.getExtras();
            if (b != null) {
                if (b.getBoolean("is_post")) {
                    onSetBtn();
                    page = 1;
                    pages = 1;
                    mUserAddressAdaper = null;
                    downloadData();
                }
            }
        }
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getUserAddress() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&page=" + page + "&limit=20";

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
                                    List<UserAddressObj> list = UserAddressObjHandler.getUserAddressObjList(array);
                                    setDataView(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

}
