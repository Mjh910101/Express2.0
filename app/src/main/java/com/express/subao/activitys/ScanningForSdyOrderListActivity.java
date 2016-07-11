package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.SdyOrderAdaper;
import com.express.subao.adaptera.SdyOrderHaveOpenAdaper;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.dialogs.MessageDialog;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.views.InsideListView;
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
public class ScanningForSdyOrderListActivity extends BaseActivity {

    public final static int RequestCode = 10010;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.scanningSyd_progress)
    private ProgressBar progress;
    @ViewInject(R.id.scanningSyd_dataList)
    private ListView dataList;

    private int page = 1, pages = 1;
    private String qrCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_sdy_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode:
                downloadData(qrCode);
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
//        downloadData(qrCode);
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.box_express_text));

        Bundle b = getIntent().getExtras();
        if (b != null) {
            qrCode = b.getString("qrcode");
            downloadData(qrCode);
        } else {
            finish();
        }
    }

    private void downloadData(final String qrcode) {
        String url = Url.getUserOrderScan() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&qrcode=" + qrcode;

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
                                    setDataLisr(list, qrcode);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            } else {
                                showMessageDialog(JsonHandle.getString(json, "results"));
                            }
                        }
                    }

                });
    }

    private void showMessageDialog(String msg) {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage(msg);
        dialog.setCommitStyle("確定");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                finish();
            }
        });
    }

    public void setDataLisr(List<SdyOrderObj> list, String qrcode) {
        dataList.setAdapter(new SdyOrderHaveOpenAdaper(context, list, qrcode, new SdyOrderHaveOpenAdaper.OpenListener() {
            @Override
            public void onClick(SdyOrderObj obj) {
                openSdyOrder(obj);
            }
        }));
    }

    private void jumpOpenSdyDailog(){
        Bundle b = new Bundle();
        Passageway.jumpActivity(context, OpenSdyDailogActivity.class, ScanningForSdyOrderListActivity.RequestCode, b);
    }

    private void openSdyOrder(SdyOrderObj obj) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getUserOrderOpen();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("sdy_order_id", obj.getSdy_order_id());
        params.addBodyParameter("qrcode", qrCode);

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
                            JSONObject resultJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                jumpOpenSdyDailog();
                            } else {
                                showMessageDialog(JsonHandle.getString(resultJson, "message"));
                            }

                        }
                    }

                });
    }

}
