package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.handlers.StoreItemObjHandler;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.tool.Passageway;
import com.express.subao.views.InsideViewFlipper;
import com.express.subao.views.SliderView;
import com.express.subao.views.VestrewWebView;
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
 * Created by Hua on 16/5/27.
 */
public class StoreItemContentActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_scanningIcon)
    private ImageView scanningIcon;
    @ViewInject(R.id.title_shoppingCarIcon)
    private ImageView shoppingCarIcon;
    @ViewInject(R.id.storeItemContent_titleText)
    private TextView titleText;
    @ViewInject(R.id.storeItemContent_sellText)
    private TextView sellText;
    @ViewInject(R.id.storeItemContent_commentsText)
    private TextView commentsText;
    @ViewInject(R.id.storeItemContent_descView)
    private VestrewWebView descView;
    @ViewInject(R.id.ppt_box)
    private RelativeLayout sliderLayout;
    @ViewInject(R.id.ppt_images)
    private InsideViewFlipper sliderFlipper;
    @ViewInject(R.id.ppt_ball)
    private LinearLayout ballLayout;
    @ViewInject(R.id.ppt_boxBg)
    private ImageView sliderBg;
    @ViewInject(R.id.storeItemContent_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.storeItemContent_originalText)
    private TextView originalText;
    @ViewInject(R.id.ppt_boxBg)
    private ImageView boxBg;

    private StoreItemObj mStoreItemObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_content);

        context = this;
        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick({R.id.title_back, R.id.storeItemContent_commentsLayout, R.id.storeItemContent_inShoppingCarBtn,
            R.id.title_shoppingCarIcon, R.id.title_scanningIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.storeItemContent_commentsLayout:
                jumpCommentsListActivity();
                break;
            case R.id.storeItemContent_inShoppingCarBtn:
                saveInShoppingCar(mStoreItemObj);
                break;
            case R.id.title_shoppingCarIcon:
                jumpShoppingCarActivity();
                break;
            case R.id.title_scanningIcon:
                jumpScanningActivity();
                break;
        }
    }

    private void jumpScanningActivity() {
        Passageway.jumpActivity(context, ScanningActivity.class);
    }

    private void jumpShoppingCarActivity() {
        Passageway.jumpActivity(context, ShoppingCarActivity.class);
    }

    private void saveInShoppingCar(StoreItemObj obj) {
        ShoppingCarHandler.saveInShoppingCar(context, progress, obj);
    }

    private void jumpCommentsListActivity() {
        Bundle b = new Bundle();
        b.putString("itemId", mStoreItemObj.getObjectId());
        b.putString("storeId", mStoreItemObj.getStoreId());
        Passageway.jumpActivity(context, CommentsListActivity.class, b);
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        scanningIcon.setVisibility(View.VISIBLE);
        shoppingCarIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.item_content_text));

        mStoreItemObj = StoreItemObjHandler.getStoreItemObj();
        if (mStoreItemObj == null) {
            finish();
        } else {
            setItemMessage(mStoreItemObj);
        }

    }

    public void setItemMessage(StoreItemObj obj) {
        titleText.setText(obj.getTitle());
        sellText.setText(obj.getSellText());
        originalText.setText("" + obj.getPrice());
        commentsText.setText(TextHandeler.getText(context, R.string.comments_sum_text).replace("0", String.valueOf(obj.getComments())) + "   >");
        SliderView.initSliderView(context, obj.getSliderList(), sliderFlipper, ballLayout,boxBg);
        setContentWeb(obj.getDesc());
    }

    private void setContentWeb(String desc) {
        descView.loadData(desc);
        WebSettings settings = descView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        settings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        settings.setLoadWithOverviewMode(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
