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
import com.express.subao.box.UserObj;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.dialogs.MessageDialog;
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
public class RegisterActivity extends BaseActivity {

    public final static int REQUEST_CODE = 101;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.register_telInput)
    private EditText telInput;
    @ViewInject(R.id.register_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.register_passwordAgainInput)
    private EditText passwordAgainInput;
    @ViewInject(R.id.register_passwordJudge)
    private ImageView passwordJudge;
    @ViewInject(R.id.register_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        setTextChangedListener();
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

    @OnClick({R.id.title_back, R.id.register_registerBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.register_registerBtn:
                submit();
                break;
        }
    }

    private void close(Intent data) {
        setResult(REQUEST_CODE, data);
        finish();
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
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.register_text));
    }

    private void submit() {
        if (isCommit()) {
            showDialog();
        }
    }

    private void showDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage(TextHandeler.getText(context, R.string.phone_message) + "\n" + TextHandeler.getText(telInput));
        dialog.setCommitStyle(TextHandeler.getText(context, R.string.confirm));
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                signUp();
            }
        });
        dialog.setCancelStyle(TextHandeler.getText(context, R.string.cancel));
        dialog.setCancelListener(null);
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
        if (TextHandeler.getText(passwordInput).length() < 6
                || TextHandeler.getText(passwordInput).length() > 20) {
            MessageHandler.showToast(context,
                    getResources().getString(R.string.pass_length));
            return false;
        }
        return true;
    }

    public void signUp() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getSignUp();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("phone", TextHandeler.getText(telInput));
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
                            if(JsonHandle.getInt(json,"status")==1){
                                jumpSmsActivity(UserObjHandler.getUserObj(resultsJson));
                            }else{
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }

                        }
                    }

                });

    }

    private void jumpSmsActivity(UserObj obj) {
        UserObjHandler.saveUserObj(obj);
        Bundle b = new Bundle();
        b.putBoolean(SMSActivity.IS_REGISTER, true);
        b.putString(SMSActivity.TEL, TextHandeler.getText(telInput));
        Passageway.jumpActivity(context, SMSActivity.class, SMSActivity.REQUEST_CODE, b);
    }


}
