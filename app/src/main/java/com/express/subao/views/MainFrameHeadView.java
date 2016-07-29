package com.express.subao.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.express.subao.R;
import com.express.subao.activitys.SdyOrderListActivity;
import com.express.subao.activitys.SdyboxMapActivity;
import com.express.subao.activitys.WebActivity;
import com.express.subao.box.SliderObj;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

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
 * Created by Hua on 16/6/29.
 */
public class MainFrameHeadView extends LinearLayout {

    private Context context;

    private View view;
    private List<SliderObj> list;
    private LayoutInflater inflater;

    private SliderView mSliderView;


    @ViewInject(R.id.ppt_images)
    private InsideViewFlipper mViewFlipper;
    @ViewInject(R.id.ppt_ball)
    private LinearLayout pptBallBox;
    @ViewInject(R.id.main_expressBox)
    private ImageView expressBox;
    @ViewInject(R.id.main_expressCheck)
    private ImageView expressCheck;
    @ViewInject(R.id.main_tellFriend)
    private ImageView tellFriend;
    @ViewInject(R.id.main_callMe)
    private ImageView callMe;
    @ViewInject(R.id.main_getExpress)
    private ImageView getExpress;
    @ViewInject(R.id.main_boxAddress)
    private ImageView boxAddress;
    @ViewInject(R.id.ppt_boxBg)
    private ImageView boxBg;

    public MainFrameHeadView(Context context, List<SliderObj> list) {
        super(context);
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_main_head, null);
        ViewUtils.inject(this, view);
        initLayout();
        addView(view);
    }

    @OnClick({R.id.main_tellFriend, R.id.main_getExpress, R.id.main_callMe, R.id.main_boxAddress})
    public void jumpWeb(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.main_tellFriend:
                b.putString(WebActivity.TITLE, "告訴朋友");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/11.html");
                break;
            case R.id.main_getExpress:
                b.putString(WebActivity.TITLE, "如何取件");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/12.html");
                break;
            case R.id.main_callMe:
                b.putString(WebActivity.TITLE, "聯繫我們");
                b.putString(WebActivity.URL, Url.getIndex() + "/html/andriod_contact.html");
                break;
            case R.id.main_boxAddress:
//                b.putString(WebActivity.URL, Url.getIndex() + "/html/15.html");
                jumpMapActivity();
                return;
        }
        Passageway.jumpActivity(context, WebActivity.class, b);
    }

    @OnClick({R.id.main_expressBox, R.id.main_expressCheck})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_expressBox:
                Passageway.jumpActivity(context, SdyOrderListActivity.class);
                break;
            case R.id.main_expressCheck:
                Bundle b = new Bundle();
//                b.putString(WebActivity.URL, "https://m.baidu.com/from=844b/s?word=%E5%BF%AB%E9%80%92%E5%8D%95%E5%8F%B7%E6%9F%A5%E8%AF%A2&ts=9273863&t_kt=0&ie=utf-8&rsv_iqid=15097551060046916401&rsv_t=72deMgPPNkm183EeiLmi7c4tOYHp0VHnafndB7E%252BxsATdrxKgzYXkgQVGw&sa=is_1&ms=1&rsv_pq=15097551060046916401&rsv_sug4=7846&ss=100&inputT=5348&rq=k");
//                b.putString(WebActivity.URL, "http://m.kuaidi100.com");
                b.putString(WebActivity.URL, "https://m.baidu.com");
                b.putString(WebActivity.TITLE, "百度查件");
//                b.putString(WebActivity.URL, "http://m.kuaidi100.com/result.jsp?nu=5124366058");
                Passageway.jumpActivity(context, WebActivity.class, b);
                break;
        }
    }

    private void jumpMapActivity() {
        Passageway.jumpActivity(context, SdyboxMapActivity.class);
    }

    private void initLayout() {
        initImageView();
        mSliderView = SliderView.initSliderView(context, list, mViewFlipper, pptBallBox, boxBg);
    }


    private void initImageView() {
        double w = WinTool.getWinWidth(context);

        double eBW = w * 257 / 640;
        double eBH = eBW / 257 * 200;
        setImageParams(expressCheck, eBW, eBH);

        double eCW = w * 383 / 640;
        double eCH = eCW / 383 * 200;
        setImageParams(expressBox, eCW, eCH);

        double w2 = w / 4;
        setImageParams(tellFriend, w2, w2);
        setImageParams(callMe, w2, w2);
        setImageParams(getExpress, w2, w2);
        setImageParams(boxAddress, w2, w2);

    }

    private void setImageParams(ImageView view, double w, double h) {
        view.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
    }

    public void startFlish() {
        if (mSliderView != null) {
            mSliderView.startFlish();
        }
    }

    public void stopFlish() {
        if (mSliderView != null) {
            mSliderView.stopFlish();
        }
    }

    public void onRestart() {
        pptBallBox.removeAllViews();
        mViewFlipper.removeAllViews();
        if (mSliderView != null) {
            mSliderView.stopFlish();
        }
    }
}
