package com.express.subao.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.ColorHandler;
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
 * Created by Hua on 16/5/10.
 */
public class LoginDialogActivity extends BaseActivity {

    private final static int LOGIN = R.id.loginDialog_loginBtn;
    private final static int REGISTER = R.id.loginDialog_registerBtn;
    public final static int RESULT = 12045;

    @ViewInject(R.id.loginDialog_loginBtn)
    private TextView loginBtn;
    @ViewInject(R.id.loginDialog_registerBtn)
    private TextView registerBtn;
    @ViewInject(R.id.loginDialog_loginLayout)
    private LinearLayout loginLayout;
    @ViewInject(R.id.loginDialog_registerLayout)
    private LinearLayout registerLayout;
    @ViewInject(R.id.login_passwordInput)
    private EditText loginPasswordInput;
    @ViewInject(R.id.login_telInput)
    private EditText loginTelInput;
    @ViewInject(R.id.loginDialog_progress)
    private ProgressBar progress;
    @ViewInject(R.id.register_telInput)
    private EditText registerTelInput;
    @ViewInject(R.id.register_passwordInput)
    private EditText registerPasswordInput;
    @ViewInject(R.id.register_passwordAgainInput)
    private EditText registerPasswordAgainInput;
    @ViewInject(R.id.register_passwordJudge)
    private ImageView passwordJudge;
    @ViewInject(R.id.register_getCode)
    private TextView getCodeText;
    @ViewInject(R.id.register_codeInput)
    private EditText registerVerifyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dialog);

        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    @OnClick({R.id.loginDialog_loginBtn, R.id.loginDialog_registerBtn, R.id.login_loginBtn,
            R.id.register_registerBtn, R.id.register_getCode, R.id.login_forgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginDialog_loginBtn:
            case R.id.loginDialog_registerBtn:
                onType(view.getId());
                break;
            case R.id.login_loginBtn:
                login();
                break;
            case R.id.register_registerBtn:
                register();
                break;
            case R.id.register_getCode:
                getSmsCode();
                break;
            case R.id.login_forgetPassword:
                jumpForgetActivity();
                break;
        }
    }

    private void jumpForgetActivity() {
        Passageway.jumpActivity(context, ForgetPasswordActivity.class);
    }

    private void setTextChangedListener() {
        registerPasswordAgainInput.addTextChangedListener(new TextWatcher() {

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
                if (registerPasswordInput.getText().toString()
                        .equals(registerPasswordAgainInput.getText().toString())) {
                    passwordJudge.setImageResource(R.drawable.password_true);
                } else {
                    passwordJudge.setImageResource(R.drawable.password_flase);
                }
            }
        });
    }

    private void onType(int type) {
        initType();
        switch (type) {
            case LOGIN:
                loginBtn.setBackgroundResource(R.color.whitle);
                loginBtn.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case REGISTER:
                registerBtn.setBackgroundResource(R.color.whitle);
                registerBtn.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                registerLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initType() {
        loginBtn.setBackgroundResource(R.color.text_orange);
        registerBtn.setBackgroundResource(R.color.text_orange);

        loginBtn.setTextColor(ColorHandler.getColorForID(context, R.color.whitle));
        registerBtn.setTextColor(ColorHandler.getColorForID(context, R.color.whitle));

        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
    }

    private void initActivity() {
        setTextChangedListener();
        onType(LOGIN);
    }

    private void login() {
        if (isLoginCommit()) {
            submitLogin();
        }
    }

    private boolean isLoginCommit() {
        if (TextHandeler.getText(loginTelInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.tel_not_null));
            return false;
        }
        if (TextHandeler.getText(loginTelInput).length() != 8 && TextHandeler.getText(loginTelInput).length() != 11) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.tel_not_error));
            return false;
        }
        if (TextHandeler.getText(loginPasswordInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_not_null));
            return false;
        }
        return true;
    }

    private void register() {
        if (isRegisterCommit()) {
            submitRegister();
        }
    }

    public boolean isRegisterCommit() {
        if (TextHandeler.getText(registerTelInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.tel_not_null));
            return false;
        }

        if (TextHandeler.getText(registerTelInput).length() != 8 && TextHandeler.getText(registerTelInput).length() != 11) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.tel_not_error));
            return false;
        }

        if (TextHandeler.getText(registerPasswordInput).equals("")) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_not_null));
            return false;
        }
        if (TextHandeler.getText(registerPasswordInput).length() < 6
                || TextHandeler.getText(registerPasswordInput).length() > 20) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_length));
            return false;
        }
        return true;
    }

    private void submitLogin() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getLogin();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", loginTelInput.getText().toString());
        params.addBodyParameter("password", loginPasswordInput.getText().toString());

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
                                UserObjHandler.saveUserObj(context, UserObjHandler.getUserObj(resultsJson));
                                finish();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });
    }

    public void submitRegister() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getSignUp();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", TextHandeler.getText(registerTelInput));
        params.addBodyParameter("password", TextHandeler.getText(registerPasswordInput));
        if (TextHandeler.getText(registerVerifyInput).equals("1") || TextHandeler.getText(registerTelInput).length() == 8) {
            params.addBodyParameter("isdebug", "1");
        } else {
            params.addBodyParameter("verify", TextHandeler.getText(registerVerifyInput));
        }

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
                                UserObjHandler.saveUserObj(context, UserObjHandler.getUserObj(resultsJson));
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

        String url = Url.getSignUpVerify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", TextHandeler.getText(registerTelInput));

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


    private void close() {
        setResult(RESULT);
        finish();
    }

}
