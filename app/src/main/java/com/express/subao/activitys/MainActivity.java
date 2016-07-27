package com.express.subao.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.express.subao.R;
import com.express.subao.box.handlers.AreaObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.dialogs.MessageDialog;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.fragments.main.MainFrameLayout;
import com.express.subao.fragments.main.MainFrameLayoutV2;
import com.express.subao.fragments.main.MainFrameLayoutV3;
import com.express.subao.fragments.main.MainFrameLayoutV4;
import com.express.subao.fragments.main.RebateFrameLayout;
import com.express.subao.fragments.main.ShoppingCarFrameLayout;
import com.express.subao.fragments.main.UserFrameLayout;
import com.express.subao.fragments.main.UserFrameLayoutV2;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.PushHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.handlers.VersionHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
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


public class MainActivity extends BaseActivity {

    private final static long EXITTIME = 2000;
    private long EXIT = 0;

    private final static int MAIN = 1;
    private final static int REBATE = 2;
    private final static int SHOPPING_CAR = 3;
    private final static int USER = 4;

    private int nowTap;

    //    @ViewInject(R.id.title_addressName)
//    private TextView addressName;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_icon)
    private ImageView titleIcon;
    //    @ViewInject(R.id.title_scanningIcon)
//    private ImageView scanningIcon;
    @ViewInject(R.id.main_tap_mainIcon)
    private ImageView mainIcon;
    @ViewInject(R.id.main_tap_mainText)
    private TextView mainText;
    @ViewInject(R.id.main_tap_rebateIcon)
    private ImageView rebateIcon;
    @ViewInject(R.id.main_tap_rebateText)
    private TextView rebateText;
    @ViewInject(R.id.main_tap_shoppingCarIcon)
    private ImageView shoppingCarIcon;
    @ViewInject(R.id.main_tap_shoppingCarText)
    private TextView shoppingCarText;
    @ViewInject(R.id.main_tap_userIcon)
    private ImageView userIcon;
    @ViewInject(R.id.main_tap_userText)
    private TextView userText;
    @ViewInject(R.id.title_editorText)
    private TextView editorText;
    @ViewInject(R.id.main_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.main_tap_tapLayout)
    private LinearLayout tapLayout;

    private MainFrameLayoutV4 mainFrameLayout;
    private RebateFrameLayout rebateFrameLayout;
    private ShoppingCarFrameLayout shoppingCarFrameLayout;
    private UserFrameLayoutV2 userFrameLayout;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        DownloadImageLoader.initLoader(context);
        ViewUtils.inject(this);

        initApp();
    }

    private void initApp() {
        TitleHandler.setTitle(context, titleLayout);
        TitleHandler.setTitle(context, tapLayout);

        fragmentManager = getFragmentManager();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean("no_check")) {
                initActivity();
                return;
            }
        }
        checkVersion();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (nowTap==SHOPPING_CAR) {
            shoppingCarFrameLayout.onRestart();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - EXIT < EXITTIME) {
                finish();
            } else {
                MessageHandler.showToast(context, "再次點擊退出");
            }
            EXIT = System.currentTimeMillis();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AreaActivity.REQUEST_CODE:
//                initAddarss();
                refreshActivity();
                break;
            case RegisterActivity.REQUEST_CODE:
            case LoginActivity.REQUEST_CODE:
            case ModifyUserActivity.REQUEST_CODE:
                refreshActivity();
                break;
            case LoginDialogActivity.RESULT:
                if (!UserObjHandler.isLigon(context)) {
                    finish();
                } else {
                    initPush();
                }
                break;
            case VersionActivity.UPLOAD_REQUEST_CODE:
                if (data != null) {
                    Bundle b = data.getExtras();
                    if (b != null) {
                        if (b.getBoolean("isFinish")) {
                            finish();
                        } else {
                            initActivity();
                        }
                    }
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private void refreshActivity() {
        switch (nowTap) {
            case MAIN:
                mainFrameLayout.onRestart();
                break;
            case USER:
                userFrameLayout.onRestart();
                break;
        }
    }

    @OnClick({R.id.title_addressName, R.id.title_scanningIcon, R.id.main_tap_scanningIcon, R.id.title_editorText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_addressName:
                Passageway.jumpActivity(context, AreaActivity.class, AreaActivity.REQUEST_CODE);
                break;
            case R.id.title_scanningIcon:
//                Passageway.jumpActivity(context, ExpressListActivity.class);
//                Passageway.jumpActivity(context, ScanningActivity.class);
                showBanDialog();
                break;
            case R.id.main_tap_scanningIcon:
                Passageway.jumpActivity(context, ScanningActivity.class);
                break;
            case R.id.title_editorText:
                if (shoppingCarFrameLayout != null) {
                    shoppingCarFrameLayout.onEditorText(editorText);
                }
                break;
        }
    }

    private void showBanDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage("此功能暫未開放");
        dialog.setCommitStyle("OK");
        dialog.setCommitListener(null);
    }

    @OnClick({R.id.main_tap_mainBox, R.id.main_tap_rebateBox, R.id.main_tap_shoppingCarBox, R.id.main_tap_userBox})
    public void onClickTap(View view) {
        switch (view.getId()) {
            case R.id.main_tap_mainBox:
                setTap(MAIN);
                break;
            case R.id.main_tap_rebateBox:
                setTap(REBATE);
                break;
            case R.id.main_tap_shoppingCarBox:
                setTap(SHOPPING_CAR);
//                showBanDialog();
                break;
            case R.id.main_tap_userBox:
                setTap(USER);
                break;
        }
    }

    private void initActivity() {
//        initAddarss();
//        scanningIcon.setVisibility(View.VISIBLE);

        setTap(MAIN);
        if (!UserObjHandler.isLigon(context)) {
            jumpLoginActivity();
        } else {
            initPush();
        }
//        setTap(MAIN);

    }

    private void initPush() {
        PushService.setDefaultPushCallback(context, MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
        if (!PushHandler.isStop(context)) {
            PushHandler.startPush(context);
        }
    }

    private void jumpLoginActivity() {
        Passageway.jumpActivity(context, LoginDialogActivity.class, LoginDialogActivity.RESULT);
    }

//    private void initAddarss() {
//        addressName.setVisibility(View.VISIBLE);
//        String n = AreaObjHandler.getAreaName(context);
//        if (n.equals("")) {
//            n = TextHandeler.getText(context, R.string.area_text);
//        }
//        addressName.setText(n);
//    }


    public void setTap(int tap) {
        nowTap = tap;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        clearSelection();
        hideFragments(transaction);
        switch (tap) {
            case MAIN:
                onMainTap(transaction);
                break;
            case REBATE:
                onRebateTap(transaction);
                break;
            case SHOPPING_CAR:
                onShoppingCarTap(transaction);
                break;
            case USER:
                onUserTap(transaction);
                break;
        }
        transaction.commit();
    }

    private void onUserTap(FragmentTransaction transaction) {
        titleName.setText(TextHandeler.getText(context, R.string.user_text));
        userIcon.setImageResource(R.drawable.main_tap_on_user_icon);
        userText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
        if (userFrameLayout == null) {
            userFrameLayout = new UserFrameLayoutV2();
            transaction.add(R.id.main_content, userFrameLayout);
        } else {
            transaction.show(userFrameLayout);
        }
    }

    private void onShoppingCarTap(FragmentTransaction transaction) {
        titleName.setText(TextHandeler.getText(context, R.string.shopping_car_text));
        editorText.setVisibility(View.VISIBLE);
        shoppingCarIcon.setImageResource(R.drawable.main_tap_on_shoppingcar_icon);
        shoppingCarText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
        if (shoppingCarFrameLayout == null) {
            shoppingCarFrameLayout = new ShoppingCarFrameLayout();
            transaction.add(R.id.main_content, shoppingCarFrameLayout);
        } else {
            transaction.show(shoppingCarFrameLayout);
            shoppingCarFrameLayout.onRestart();
            editorText.setText(TextHandeler.getText(context, R.string.editor_text));
        }
    }

    private void onRebateTap(FragmentTransaction transaction) {
        titleName.setText(TextHandeler.getText(context, R.string.rebate_text));
        rebateIcon.setImageResource(R.drawable.main_tap_on_rebate_icon);
        rebateText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
        if (rebateFrameLayout == null) {
            rebateFrameLayout = new RebateFrameLayout();
            transaction.add(R.id.main_content, rebateFrameLayout);
        } else {
            transaction.show(rebateFrameLayout);
        }
    }

    private void onMainTap(FragmentTransaction transaction) {
        mainIcon.setImageResource(R.drawable.main_tap_on_main_icon);
        mainText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
//        addressName.setVisibility(View.VISIBLE);
//        scanningIcon.setVisibility(View.VISIBLE);
        titleIcon.setVisibility(View.VISIBLE);
        if (mainFrameLayout == null) {
            mainFrameLayout = new MainFrameLayoutV4();
            transaction.add(R.id.main_content, mainFrameLayout);
        } else {
            transaction.show(mainFrameLayout);
            mainFrameLayout.startFlish();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mainFrameLayout != null) {
            transaction.hide(mainFrameLayout);
        }
        if (rebateFrameLayout != null) {
            transaction.hide(rebateFrameLayout);
        }
        if (shoppingCarFrameLayout != null) {
            transaction.hide(shoppingCarFrameLayout);
        }
        if (userFrameLayout != null) {
            transaction.hide(userFrameLayout);
        }
    }

    private void clearSelection() {
        mainIcon.setImageResource(R.drawable.main_tap_off_main_icon);
        rebateIcon.setImageResource(R.drawable.main_tap_off_rebate_icon);
        shoppingCarIcon.setImageResource(R.drawable.main_tap_off_shoppingcar_icon);
        userIcon.setImageResource(R.drawable.main_tap_off_user_icon);

        mainText.setTextColor(ColorHandler.getColorForID(context, R.color.black));
        rebateText.setTextColor(ColorHandler.getColorForID(context, R.color.black));
        shoppingCarText.setTextColor(ColorHandler.getColorForID(context, R.color.black));
        userText.setTextColor(ColorHandler.getColorForID(context, R.color.black));

//        addressName.setVisibility(View.GONE);
//        scanningIcon.setVisibility(View.GONE);

        titleIcon.setVisibility(View.GONE);
        editorText.setVisibility(View.GONE);

        titleName.setText(TextHandeler.getText(context, R.string.app_name));

        if (mainFrameLayout != null) {
            mainFrameLayout.stopFlish();
        }
    }


    private void checkVersion() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getVersion();

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                        initActivity();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1 && resultJson != null) {
                                JSONObject android = JsonHandle.getJSON(resultJson, "android");
                                if (android != null) {
                                    jumpVersionActivity(VersionHandler.detectionVersion(context, JsonHandle.getInt(android, "version")),
                                            VersionHandler.detectionVersion(context, JsonHandle.getInt(android, "versionShort")),
                                            JsonHandle.getString(android, "changelog"),
                                            JsonHandle.getString(android, "update_url"));
                                }
                            }

                        }

                    }

                });
    }

    private void jumpVersionActivity(boolean version, boolean versionShort, String changelog, String update_url) {
        if (version) {
            Bundle b = new Bundle();
            b.putString("changelog", changelog);
            b.putString("update_url", update_url);
            if (versionShort) {
                b.putBoolean("must", true);
            } else {
                b.putBoolean("must", false);
            }
            Passageway.jumpActivity(context, VersionActivity.class, VersionActivity.UPLOAD_REQUEST_CODE, b);
        } else {
            initActivity();
        }
    }

}
