package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.UserObj;
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
 * Created by Hua on 15/12/23.
 */
public class LoginActivity extends BaseActivity {

    public final static int REQUEST_CODE = 102;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.login_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.login_telInput)
    private EditText telInput;
    @ViewInject(R.id.login_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back,R.id.login_finish, R.id.login_login, R.id.login_forgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
            case R.id.login_finish:
                finish();
                break;
            case R.id.login_login:
                submit();
                break;
            case R.id.login_forgetPassword:
                Passageway.jumpActivity(context,ForgetActivity.class,SMSActivity.REQUEST_CODE);
                break;
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.login_text));
    }

    private void submit() {
        if (isCommit()) {
            submitLogin();
        }
    }

    private boolean isCommit() {
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
        return true;
    }

    private void submitLogin() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getLogin();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("phone", telInput.getText().toString());
        params.addBodyParameter("password", passwordInput.getText().toString());

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
}
