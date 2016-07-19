package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
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
 * Created by Hua on 15/12/23.
 */
public class ForgetInpitPassWordActivity extends BaseActivity {


    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.forget_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.forget_passwordAgainInput)
    private EditText passwordAgainInput;
    @ViewInject(R.id.forget_passwordJudge)
    private ImageView passwordJudge;
    @ViewInject(R.id.forget_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_input);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setTextChangedListener();
    }

    @OnClick({R.id.title_back, R.id.forget_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.forget_confirm:
                submit();
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

    private void submit() {
        if (isCommit()) {
            forgotReset();
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.forget_password));

        Bundle b = getIntent().getExtras();
        if (b != null) {
            code = b.getString(SMSActivity.CODE);
        }
    }

    private boolean isCommit() {

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

    private void close() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("ok", true);
        i.putExtras(b);
        setResult(SMSActivity.REQUEST_CODE, i);
        finish();
    }


    private void forgotReset() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getForgotReset();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("verify", code);
        params.addBodyParameter("password", TextHandeler.getText(passwordInput));

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
                            MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            if (JsonHandle.getInt(json, "status") == 1) {
                                close();
                            } else {
                                finish();
                            }

                        }
                    }

                });
    }

}
