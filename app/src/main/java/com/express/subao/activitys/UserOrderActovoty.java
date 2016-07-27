package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.handlers.ColorHandle;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
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
 * Created by Hua on 16/7/27.
 */
public class UserOrderActovoty extends BaseActivity {

    private final static String ALL = "";
    private final static String WAITING = "1";
    private final static String ASSESS = "0";
    private final static String SHIP = "2";

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.userOrder_clickOn_allTopLine)
    private View allTopLin;
    @ViewInject(R.id.userOrder_clickOff_allLine)
    private View allLine;
    @ViewInject(R.id.userOrder_clickOn_allBelowLine)
    private View allBelowLine;
    @ViewInject(R.id.userOrder_allText)
    private TextView allText;
    @ViewInject(R.id.userOrder_clickOn_waitingTopLine)
    private View waitingTopLin;
    @ViewInject(R.id.userOrder_clickOff_waitingLine)
    private View waitingLine;
    @ViewInject(R.id.userOrder_clickOn_waitingBelowLine)
    private View waitingBelowLine;
    @ViewInject(R.id.userOrder_waitingText)
    private TextView waitingText;
    @ViewInject(R.id.userOrder_clickOn_assessTopLine)
    private View assessTopLin;
    @ViewInject(R.id.userOrder_clickOff_assessLine)
    private View assessLine;
    @ViewInject(R.id.userOrder_clickOn_assessBelowLine)
    private View assessBelowLine;
    @ViewInject(R.id.userOrder_assessText)
    private TextView assessText;
    @ViewInject(R.id.userOrder_clickOn_shipTopLine)
    private View shipTopLin;
    @ViewInject(R.id.userOrder_clickOff_shipLine)
    private View shipLine;
    @ViewInject(R.id.userOrder_clickOn_shipBelowLine)
    private View shipBelowLine;
    @ViewInject(R.id.userOrder_shipText)
    private TextView shipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @OnClick({R.id.userOrder_allText, R.id.userOrder_waitingText, R.id.userOrder_assessText, R.id.userOrder_shipText})
    public void onType(View view) {
        switch (view.getId()) {
            case R.id.userOrder_allText:
                onClickType(ALL);
                break;
            case R.id.userOrder_waitingText:
                onClickType(WAITING);
                break;
            case R.id.userOrder_assessText:
                onClickType(ASSESS);
                break;
            case R.id.userOrder_shipText:
                onClickType(SHIP);
                break;
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText("我的訂單");
        TitleHandler.setTitle(context, titleLayout);
        onClickType(ALL);
    }

    private void onClickType(String type) {
        initType();
        switch (type) {
            case ALL:
                onAllType();
                break;
            case WAITING:
                onWaitingType();
                break;
            case ASSESS:
                onAssessType();
                break;
            case SHIP:
                onShipType();
                break;
        }
        downloadDaata(type);
    }

    private void onShipType() {
        shipTopLin.setVisibility(View.VISIBLE);
        shipLine.setVisibility(View.INVISIBLE);
        shipBelowLine.setVisibility(View.INVISIBLE);
        shipText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onAssessType() {
        assessTopLin.setVisibility(View.VISIBLE);
        assessLine.setVisibility(View.INVISIBLE);
        assessBelowLine.setVisibility(View.INVISIBLE);
        assessText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onWaitingType() {
        waitingTopLin.setVisibility(View.VISIBLE);
        waitingLine.setVisibility(View.INVISIBLE);
        waitingBelowLine.setVisibility(View.INVISIBLE);
        waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void onAllType() {
        allTopLin.setVisibility(View.VISIBLE);
        allLine.setVisibility(View.INVISIBLE);
        allBelowLine.setVisibility(View.INVISIBLE);
        allText.setTextColor(ColorHandle.getColorForID(context, R.color.red));
    }

    private void initType() {
        allTopLin.setVisibility(View.INVISIBLE);
        waitingTopLin.setVisibility(View.INVISIBLE);
        assessTopLin.setVisibility(View.INVISIBLE);
        shipTopLin.setVisibility(View.INVISIBLE);

        allLine.setVisibility(View.VISIBLE);
        waitingLine.setVisibility(View.VISIBLE);
        assessLine.setVisibility(View.VISIBLE);
        shipLine.setVisibility(View.VISIBLE);

        allBelowLine.setVisibility(View.VISIBLE);
        waitingBelowLine.setVisibility(View.VISIBLE);
        assessBelowLine.setVisibility(View.VISIBLE);
        shipBelowLine.setVisibility(View.VISIBLE);

        allText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        waitingText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        assessText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
        shipText.setTextColor(ColorHandle.getColorForID(context, R.color.black));
    }

    private void downloadDaata(String type) {

    }

}
