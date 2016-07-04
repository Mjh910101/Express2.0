package com.express.subao.activitys;

import android.content.Context;
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
import com.express.subao.adaptera.CommentAdaper;
import com.express.subao.box.CommentObj;
import com.express.subao.box.handlers.CommentObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
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
 * Created by Hua on 16/5/30.
 */
public class CommentsListActivity extends BaseActivity {

    private final static int LIMIT = 20;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.comment_progress)
    private ProgressBar progress;
    @ViewInject(R.id.comment_commentInput)
    private EditText commentInput;
    @ViewInject(R.id.comment_dataList)
    private ListView dataList;

    private InputMethodManager imm = null;

    private String storeId = "", itemId = "";
    private int page = 1, pages = 1;

    private CommentAdaper mCommentAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @OnClick({R.id.title_back, R.id.comment_sendBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.comment_sendBtn:
                sendComment();
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

    private void sendComment() {
        String text = TextHandeler.getText(commentInput);
        if (!text.equals("")) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        commentInput.getApplicationWindowToken(), 0);
            }
            sendComment(text);
        } else {

        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.comment_text));

        imm = (InputMethodManager) commentInput.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            itemId = b.getString("itemId");
            storeId = b.getString("storeId");
            downloadData();
        }

    }

    public void setDataList(List<CommentObj> list) {
        if(mCommentAdaper==null){
            mCommentAdaper=new CommentAdaper(context,list);
            dataList.setAdapter(mCommentAdaper);
        }else{
            mCommentAdaper.addItem(list);
        }
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getStoreItemComment() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&store_id=" + storeId + "&item_id=" + itemId + "&page=" + page + "&limit=" + LIMIT;

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
                                    setDataList(CommentObjHandler.getCommentObjList(array));
                                    page += 1;
                                }
                            }
                            pages = JsonHandle.getInt(json, "pages");

                        }

                    }

                });
    }

    private void sendComment(String msg) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getStoreItemComment();
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("store_id", storeId);
        params.addBodyParameter("item_id", itemId);
        params.addBodyParameter("content", msg);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
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
                                commentInput.setText("");
                                page = 1;
                                downloadData();
                            }

                        }

                    }

                });
    }
}

