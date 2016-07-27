package com.express.subao.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.CollectionActivity;
import com.express.subao.activitys.FeedBackActivity;
import com.express.subao.activitys.LoginActivity;
import com.express.subao.activitys.MainActivity;
import com.express.subao.activitys.ModifyUserActivity;
import com.express.subao.activitys.RegisterActivity;
import com.express.subao.activitys.RemainingMoneyListActivity;
import com.express.subao.activitys.UserExpressListActivity;
import com.express.subao.activitys.UserOrderActovoty;
import com.express.subao.activitys.WebActivity;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.handlers.PushHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;
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
 * Created by Hua on 15/12/21.
 */
public class UserFrameLayoutV2 extends BaseFragment {

    @ViewInject(R.id.user_loginBox)
    private LinearLayout loginBox;
    @ViewInject(R.id.user_compileBox)
    private LinearLayout compileBox;
    @ViewInject(R.id.user_usetName)
    private TextView userName;
    @ViewInject(R.id.user_usetPic)
    private ImageView usetPic;
    @ViewInject(R.id.user_item_pushSwitch)
    private RadioButton pushSwitch;


    @Override
    public void onRestart() {
        setLoginMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_user_v2, container,
                false);
        ViewUtils.inject(this, contactsLayout);
        setLoginMessage();
        initPushBtn();
        return contactsLayout;
    }

    @OnClick({R.id.user_item_help, R.id.user_item_about, R.id.user_item_inviteFriendsBox})
    public void onJumpWeb(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.user_item_help:
                b.putString(WebActivity.TITLE, "幫助");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/12.html");
                break;
            case R.id.user_item_about:
                b.putString(WebActivity.TITLE, "關於我們");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/andriod_version.html");
                break;
            case R.id.user_item_inviteFriendsBox:
                b.putString(WebActivity.TITLE, "邀請好友使用速寶");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/23.html");
                break;
        }
        Passageway.jumpActivity(context, WebActivity.class, b);

    }

    @OnClick({R.id.user_registerBtn, R.id.user_loginBtn, R.id.user_logoutBtn, R.id.user_compileBox,
            R.id.user_item_feedbackBox, R.id.user_item_collectionBox, R.id.user_remainingMoneyBox,
            R.id.user_item_subaoBox, R.id.user_item_push, R.id.user_item_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_registerBtn:
                Passageway.jumpActivity(context, RegisterActivity.class, RegisterActivity.REQUEST_CODE);
                break;
            case R.id.user_loginBtn:
                Passageway.jumpActivity(context, LoginActivity.class, LoginActivity.REQUEST_CODE);
                break;
            case R.id.user_logoutBtn:
                logout();
                break;
            case R.id.user_compileBox:
                Passageway.jumpActivity(context, ModifyUserActivity.class, ModifyUserActivity.REQUEST_CODE);
                break;
            case R.id.user_item_feedbackBox:
                Passageway.jumpActivity(context, FeedBackActivity.class);
                break;
            case R.id.user_item_collectionBox:
                Passageway.jumpActivity(context, CollectionActivity.class);
                break;
            case R.id.user_remainingMoneyBox:
                Passageway.jumpActivity(context, RemainingMoneyListActivity.class);
                break;
            case R.id.user_item_subaoBox:
                Passageway.jumpActivity(context, UserExpressListActivity.class);
                break;
//            case R.id.user_item_inviteFriendsBox:
//                Passageway.jumpActivity(context, ShareFriendsActivity.class);
//                break;
            case R.id.user_item_push:
                onClickPush();
                break;
            case R.id.user_item_order:
                Passageway.jumpActivity(context, UserOrderActovoty.class);
                break;
        }
    }

    private void onClickPush() {
        boolean b = pushSwitch.isChecked();
        if (b) {
            PushHandler.stopPush(context);
        } else {
            PushHandler.startPush(context);
        }
        initPushBtn();
    }

    private void initPushBtn() {
        if (PushHandler.isStop(context)) {
            pushSwitch.setChecked(false);
        } else {
            pushSwitch.setChecked(true);
        }
    }

    private void logout() {
        PushHandler.stopPush(context);
        PushHandler.savePushState(context, PushHandler.START);
        UserObjHandler.deleteUser(context);
        Passageway.jumpToActivity(context, MainActivity.class);
        ((Activity) (context)).finish();
//                isLogin();
    }

    public void setLoginMessage() {
        compileBox.setVisibility(View.VISIBLE);
        loginBox.setVisibility(View.GONE);
        userName.setText(TextHandeler.getText(context, R.string.welcome_user_text).replace("?", UserObjHandler.getNickName(context)));
        UserObjHandler.setUserAvatar(context, usetPic, WinTool.dipToPx(context, 30));
    }
}
