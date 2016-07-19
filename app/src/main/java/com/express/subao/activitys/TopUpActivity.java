package com.express.subao.activitys;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
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
 * Created by Hua on 15/12/30.
 */
public class TopUpActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;

    @ViewInject(R.id.topUp_exchangeIcon)
    private ImageView exchangeIcon;
    @ViewInject(R.id.topUp_bankIcon)
    private ImageView bankIcon;
    @ViewInject(R.id.topUp_bankBox)
    private LinearLayout bankBox;
    @ViewInject(R.id.topUp_exchangeBox)
    private LinearLayout exchangeBox;
    @ViewInject(R.id.topUp_exchangeImage)
    private ImageView exchangeImage;
    @ViewInject(R.id.topUp_bank_01)
    private ImageView bank01;
    @ViewInject(R.id.topUp_bank_02)
    private ImageView bank02;
    @ViewInject(R.id.topUp_bank_03)
    private ImageView bank03;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        exchangeIcon.performClick();
    }

    @OnClick({R.id.title_back})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @OnClick({R.id.topUp_exchangeIcon, R.id.topUp_bankIcon})
    public void onTitle(View view) {
        initTitle();
        switch (view.getId()) {
            case R.id.topUp_exchangeIcon:
                exchangeIcon.setImageResource(R.drawable.top_up_on_right_icon);
                exchangeBox.setVisibility(View.VISIBLE);
                break;
            case R.id.topUp_bankIcon:
                bankIcon.setImageResource(R.drawable.top_up_on_left_icon);
                bankBox.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initTitle() {
        exchangeIcon.setImageResource(R.drawable.top_up_off_right_icon);
        bankIcon.setImageResource(R.drawable.top_up_off_left_icon);

        bankBox.setVisibility(View.GONE);
        exchangeBox.setVisibility(View.GONE);
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.main_cz_text));

        double w = WinTool.getWinWidth(context);
        double titleW = w / 2d;
        double titleH = titleW / 32d * 7d;
        setLayoutParams(exchangeIcon, titleW, titleH);
        setLayoutParams(bankIcon, titleW, titleH);
        setLayoutParams(exchangeImage, w, w / 640d * 455d);
        setLayoutParams(bank01, w, w / 640d * 595d);
        setLayoutParams(bank02, w, w / 640d * 585d);
        setLayoutParams(bank03, w, w / 640d * 276d);
    }

    private void setLayoutParams(ImageView view, double w, double h) {
        view.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
    }

}
