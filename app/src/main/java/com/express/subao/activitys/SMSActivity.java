package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.UserObj;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
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
 * Created by Hua on 15/12/22.
 */
public class SMSActivity extends BaseActivity {

    public final static int REQUEST_CODE = 18;

    public final static String IS_REGISTER = "ISREGISTER";
    public final static String TEL = "TEL";
    public final static String PASS = "PASS";
    public final static String RE_PASS = "RE_PASS";
    public final static String CODE = "CODE";

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.sms_progress)
    private ProgressBar progress;
    @ViewInject(R.id.sms_telInput)
    private TextView telText;
    @ViewInject(R.id.sms_getCode)
    private TextView getCodeText;
    @ViewInject(R.id.sms_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private UserObj mUserObj;


    private String tel = "", pass = "", re_pass = "", smsId;
    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle b = data.getExtras();
            if (b != null) {
                switch (requestCode) {
                    case SMSActivity.REQUEST_CODE:
                        if (b.getBoolean("ok")) {
                            close(data);
                        }
                        break;
                }
            }
        }
    }

    @OnClick({R.id.title_back, R.id.sms_confirmBtn,R.id.sms_getCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.sms_confirmBtn:
                if (isRegister) {
                    signUpVerify();
                } else {
                    jumpForgetInpitPassWordActivity();
                }
                break;
            case R.id.sms_getCode:
                if(isRegister){
                    signUpSendVerify();
                }else{
                    forgotSendVerify();
                }
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            isRegister = b.getBoolean(IS_REGISTER);
            pass = b.getString(PASS);
            re_pass = b.getString(RE_PASS);
            tel = b.getString(TEL);
            telText.setText(tel);
            if (isRegister) {
                titleName.setText(TextHandeler.getText(context, R.string.register_text));
                mUserObj = UserObjHandler.getUserObj();
                startClock();
            } else {
                titleName.setText(TextHandeler.getText(context, R.string.forget_password));
                forgotSendVerify();
            }
        }

    }


    private void jumpForgetInpitPassWordActivity() {
        String code=TextHandeler.getText(codeInput);
        if(!code.equals("")) {
            Bundle b = new Bundle();
            b.putString(SMSActivity.TEL, tel);
            b.putString(SMSActivity.CODE, code);
            Passageway.jumpActivity(context, ForgetInpitPassWordActivity.class, SMSActivity.REQUEST_CODE, b);
        }else {
             MessageHandler.showToast(context, TextHandeler.getText(context, R.string.code_not_null));
        }
    }

    private  void signUpSendVerify(){
        progress.setVisibility(View.VISIBLE);
        String url = Url.getSignUpSendVerify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", tel);

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

                        startClock();

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (resultsJson != null) {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }
                        }
                    }

                });
    }

    private void forgotSendVerify() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getForgotSendVerify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", tel);

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

                        startClock();

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (resultsJson != null) {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }
                        }
                    }

                });
    }


    private void signUpVerify() {

        progress.setVisibility(View.VISIBLE);
        String url = Url.getSignUpVerify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("verify", TextHandeler.getText(codeInput));

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
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                UserObjHandler.saveUserObj(context, mUserObj);
                                close();
                            }
                            MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));

                        }
                    }

                });
    }

    private void close() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("ok", true);
        i.putExtras(b);
       close(i);
    }

    private void close(Intent data) {
        setResult(REQUEST_CODE, data);
        finish();
    }

    private void startClock() {
        getCodeText.setClickable(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 90; i >= 0; i--) {
                    Message.obtain(clockHandler, i).sendToTarget();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler clockHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int time = msg.what;
            getCodeText.setText(time
                    + TextHandeler.getText(context, R.string.get_code_time));
            if (time == 0) {
                getCodeText
                        .setText(TextHandeler.getText(context, R.string.get_code));
                getCodeText.setClickable(true);
            }
        }

    };

}
