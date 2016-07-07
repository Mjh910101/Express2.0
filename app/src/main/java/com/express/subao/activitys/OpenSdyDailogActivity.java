package com.express.subao.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
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
 * Created by Hua on 16/7/5.
 */
public class OpenSdyDailogActivity extends BaseActivity {

    @ViewInject(R.id.openDialog_numText)
    private TextView numText;
    @ViewInject(R.id.openDialog_progress)
    private ProgressBar progress;

    private int num = 20;
    private String qrcode = "", orderid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_sdy_dailog);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.openDialog_noOpenBtn, R.id.openDialog_openBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openDialog_openBtn:
            case R.id.openDialog_noOpenBtn:
                finish();
                break;
        }
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
//        if (b != null) {
//            qrcode = b.getString("qrcode");
//            orderid = b.getString("orderid");
//            openSdyOrder();
//            startRun();
//        } else {
//            finish();
//        }

        startRun();

    }

    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (num >= 0) {
                    Message.obtain(handler, num).sendToTarget();
                    num -= 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message.obtain(handler, -100).sendToTarget();
            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what<0){
//                openSdyOrder();
            }else {
                numText.setText(String.valueOf(msg.what));
            }
        }
    };

    private void openSdyOrder() {
//        progress.setVisibility(View.VISIBLE);

        String url = Url.getUserOrderOpen();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("sdy_order_id", orderid);
        params.addBodyParameter("qrcode", qrcode);

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
                                startRun();
//                                MessageHandler.showToast(context,"已開箱");
//                                finish();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getInt(resultJson, "message"));
                            }

                        }
                    }

                });
    }
}
