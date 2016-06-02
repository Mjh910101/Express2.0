package com.express.subao.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
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
 * Created by Hua on 16/5/12.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.forget_progress)
    private ProgressBar progress;
    @ViewInject(R.id.register_telInput)
    private EditText telInput;
    @ViewInject(R.id.register_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.register_passwordAgainInput)
    private EditText passwordAgainInput;
    @ViewInject(R.id.register_passwordJudge)
    private ImageView passwordJudge;
    @ViewInject(R.id.register_getCode)
    private TextView getCodeText;
    @ViewInject(R.id.register_codeInput)
    private EditText verifyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back, R.id.register_registerBtn, R.id.register_getCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.register_registerBtn:
                forget();
                break;
            case R.id.register_getCode:
                getSmsCode();
                break;
        }
    }

    private void setTextChangedListener() {
        passwordAgainInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                passwordJudge.setVisibility(View.VISIBLE);
                if (passwordInput.getText().toString()
                        .equals(passwordAgainInput.getText().toString())) {
                    passwordJudge.setImageResource(R.drawable.password_true);
                } else {
                    passwordJudge.setImageResource(R.drawable.password_flase);
                }
            }
        });
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.forget_password));

        setTextChangedListener();
    }

    private void forget() {
        if (isForgetCommit()) {
            submitForget();
        }
    }

    public boolean isForgetCommit() {
        if (TextHandeler.getText(telInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.tel_not_null));
            return false;
        }

        if (TextHandeler.getText(passwordInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_not_null));
            return false;
        }
        if (TextHandeler.getText(passwordInput).length() < 6
                || TextHandeler.getText(passwordInput).length() > 20) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_length));
            return false;
        }
        return true;
    }

    public void submitForget() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getForgotReset();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("password", TextHandeler.getText(passwordInput));
        params.addBodyParameter("verify", TextHandeler.getText(verifyInput));

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
                                UserObjHandler.saveUserObj(UserObjHandler.getUserObj(resultsJson));
                                finish();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });

    }

    private void getSmsCode() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getForgotSendVerify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", TextHandeler.getText(telInput));

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
                                startClock();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });
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
            getCodeText.setText(String.valueOf(time) + "秒");
            if (time == 0) {
                getCodeText
                        .setText(TextHandeler.getText(context, R.string.get_code));
                getCodeText.setClickable(true);
            }
        }

    };

}
