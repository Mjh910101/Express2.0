package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.dialogs.MessageDialog;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
public class ForgetActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.foeget_telInput)
    private EditText telInput;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

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
                            finish();
                        }
                        break;
                }
            }
        }
    }

    @OnClick({R.id.title_back, R.id.foeget_getCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.foeget_getCode:
                showMessageDialog();
                break;

        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.forget_password));
    }

    private void showMessageDialog() {
        String tel = TextHandeler.getText(telInput);
        if (tel.equals("")) {
            MessageHandler.showToast(context, TextHandeler.getText(context, R.string.tel_not_null));
        } else {
            MessageDialog dialog = new MessageDialog(context);
            dialog.setMessage(TextHandeler.getText(context, R.string.phone_message) + "\n" + TextHandeler.getText(telInput));
            dialog.setCommitStyle(TextHandeler.getText(context, R.string.confirm));
            dialog.setCommitListener(new MessageDialog.CallBackListener() {
                @Override
                public void callback() {
                    jumpSMSActivity();
                }
            });
            dialog.setCancelStyle(TextHandeler.getText(context, R.string.cancel));
            dialog.setCancelListener(null);
        }
    }

    private void jumpSMSActivity() {
        Bundle b = new Bundle();
        b.putBoolean(SMSActivity.IS_REGISTER, false);
        b.putString(SMSActivity.TEL, TextHandeler.getText(telInput));
        Passageway.jumpActivity(context, SMSActivity.class, SMSActivity.REQUEST_CODE, b);
    }

}
