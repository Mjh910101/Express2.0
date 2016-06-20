package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.box.handlers.ExpresObjHandler;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.ColorHandle;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.receivers.PushReceiver;
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
 * Created by Hua on 15/12/24.
 */
public class SdyOrderContentActivity extends BaseActivity {


    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.expres_content_price)
    private TextView contentPrice;
    @ViewInject(R.id.expres_content_verify)
    private TextView contentVerify;
    @ViewInject(R.id.expres_content_verifyTitle)
    private TextView contentVerifyTitle;
    @ViewInject(R.id.expres_content_tips)
    private TextView contentTips;
    @ViewInject(R.id.expres_content_area)
    private TextView contentArea;
    @ViewInject(R.id.expres_content_part)
    private TextView contentPart;
    @ViewInject(R.id.expres_content_code)
    private TextView contentCode;
    @ViewInject(R.id.expres_content_companyName)
    private TextView contentCompanyName;
    @ViewInject(R.id.expres_content_postman)
    private TextView contentPostman;
    @ViewInject(R.id.expres_content_expressAt)
    private TextView contentExpressAt;
    @ViewInject(R.id.expres_content_mobBox)
    private RelativeLayout mobBox;
    @ViewInject(R.id.expres_content_mobLine)
    private View mobLine;
    @ViewInject(R.id.expres_content_progress)
    private ProgressBar progress;

    private SdyOrderObj mSdyOrderObj;
    private boolean isPush = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expres_content);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    @OnClick({R.id.title_back, R.id.expres_content_openBtn, R.id.expres_content_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                close();
                break;
            case R.id.expres_content_openBtn:
                Passageway.jumpActivity(context, PayActivity.class);
                break;
            case R.id.expres_content_check:
                if (mSdyOrderObj != null) {
                    jumpCheckActivity();
                }
                break;
        }
    }

    private void close() {
        finish();
        if (isPush) {
            Passageway.jumpToActivity(context, MainActivity.class);
        }
    }

    private void jumpCheckActivity() {
//        Bundle b = new Bundle();
//        b.putString(CheckExpressActivity.CODE_KEY, mExpresObj.getExpreser().getExpress_id());
//        Passageway.jumpActivity(context, CheckExpressActivity.class, b);
        Bundle b = new Bundle();
        b.putString(WebActivity.TITLE, TextHandeler.getText(context, R.string.main_kdgz_text));
        b.putString(WebActivity.URL, "https://m.baidu.com/from=1013665e/s?word=" + "快遞" + mSdyOrderObj.getMailno());
        Passageway.jumpActivity(context, WebActivity.class, b);
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.sdy_content_text));

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean("isShowMob")) {
                mobBox.setVisibility(View.VISIBLE);
                mobLine.setVisibility(View.VISIBLE);
            }
            isPush = b.getBoolean(PushReceiver.PUSH_KEY);
            downloadData(b.getString(SdyOrderObj.SDY_ORDER_ID));
        }
    }

    public void setMessageView(SdyOrderObj obj) {
        contentPrice.setText("MOB");
        contentTips.setText("請盡快前往取件");
        contentArea.setText("位置：" + obj.getBoxAddress());
        contentPart.setText("");
        contentCode.setText("編號：" + obj.getBoxDeviceId());
        contentCompanyName.setText("快遞單號 : " + obj.getMailno());
        contentPostman.setText("快遞員 : " + obj.getMailman() + " " + obj.getMailman_mobile());
        contentExpressAt.setText("投件時間 : " + obj.getCreatedAt());

        switch (obj.getStatus()) {
            case "1":
                contentVerifyTitle.setVisibility(View.VISIBLE);
                contentVerify.setText(obj.getOpen_code());
                contentVerify.setTextColor(ColorHandle.getColorForID(context, R.color.green));
                contentTips.setVisibility(View.VISIBLE);
                break;
            case "3":
                contentVerifyTitle.setVisibility(View.GONE);
                contentVerify.setText("快遞員取出");
                contentVerify.setTextColor(ColorHandle.getColorForID(context, R.color.red));
                contentTips.setVisibility(View.GONE);
                break;
            case "4":
                contentVerifyTitle.setVisibility(View.GONE);
                contentVerify.setText("管理員取出");
                contentVerify.setTextColor(ColorHandle.getColorForID(context, R.color.red));
                contentTips.setVisibility(View.GONE);
                break;
            default:
                contentVerifyTitle.setVisibility(View.GONE);
                contentVerify.setText("已取件");
                contentVerify.setTextColor(ColorHandle.getColorForID(context, R.color.text_orange));
                contentTips.setVisibility(View.GONE);
                break;
        }
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getUserOrderDetail() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&sdy_order_id=" + id;

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
                                    mSdyOrderObj = SdyOrderObjHandler.getSdyOrderObj(JsonHandle.getJSON(json, "data"));
                                    setMessageView(mSdyOrderObj);
                                }
                            }

                        }
                    }

                });
    }

}
