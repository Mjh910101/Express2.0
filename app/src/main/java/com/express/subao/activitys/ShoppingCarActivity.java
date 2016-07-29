package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.ShoppingCarAdapter;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.views.SliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

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
 * Created by Hua on 16/7/29.
 */
public class ShoppingCarActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.shoppingCar_dataList)
    private ListView dataList;
    @ViewInject(R.id.shoppingCar_sumText)
    private TextView sumText;
    @ViewInject(R.id.shoppingCar_totalPriceText)
    private TextView totalPriceText;
    @ViewInject(R.id.shoppingCar_choiceIcon)
    private ImageView choiceIcon;
    @ViewInject(R.id.shoppingCar_progress)
    private ProgressBar progress;

    private ShoppingCarAdapter mShoppingCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initShoppingCar();
    }

    @OnClick({R.id.title_back, R.id.shoppingCar_choiceIcon, R.id.shoppingCar_sumText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.shoppingCar_choiceIcon:
                onChoicerAllBtn();
                break;
            case R.id.shoppingCar_sumText:
                onSumText();
                break;
        }
    }

    private void onSumText() {
        if (mShoppingCarAdapter != null) {
            if (mShoppingCarAdapter.isSetting()) {
                mShoppingCarAdapter.deleteChoice();
            } else {
                Map<String, List<StoreItemObj>> map = mShoppingCarAdapter.getChoiseItemMap();
                ShoppingCarHandler.saveChoiseItem(map);
                Passageway.jumpActivity(context, StoreItemOrderLiatActivity.class);
            }
        }
    }

    private void onChoicerAllBtn() {
        if (mShoppingCarAdapter != null) {
            if (mShoppingCarAdapter.isChoiceAll()) {
                mShoppingCarAdapter.removeAllChoice();
            } else {
                mShoppingCarAdapter.choiceAll();
            }
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.shopping_car_text));
        initShoppingCar();
    }

    private void initShoppingCar() {
        initText();
        downloadData();
    }

    private void initText() {
        totalPriceText.setText("0.0");
        sumText.setText(TextHandeler.getText(context, R.string.settlement_sum_text).replace("?", String.valueOf(0)));
        sumText.setBackgroundResource(R.color.title_bg);
        choiceIcon.setImageResource(R.drawable.choice_off_icon);
    }

    private void getShoppingCsrInData() {
        List<ShoppingCarObj> list = ShoppingCarHandler.getAllShoppingCar(context);
        setShoppingCarData(list);
    }

    private void setShoppingCarData(List<ShoppingCarObj> list) {
        mShoppingCarAdapter = new ShoppingCarAdapter(context, list, new ShoppingCarAdapter.NotifyListener() {

            @Override
            public void onNotify(List<StoreItemObj> list, boolean isSetting, boolean isChoiceAll) {
                if (!isSetting) {
                    sumText.setBackgroundResource(R.color.title_bg);
                    setSumText(list);
                } else {
                    sumText.setBackgroundResource(R.color.red);
                    sumText.setText(R.string.delete_text);
                }
                if (isChoiceAll) {
                    choiceIcon.setImageResource(R.drawable.choice_on_icon);
                } else {
                    choiceIcon.setImageResource(R.drawable.choice_off_icon);
                }
                setTotalPriceText(list);
            }
        });
        mShoppingCarAdapter.setProgress(progress);
        dataList.setAdapter(mShoppingCarAdapter);
    }

    private void setSumText(List<StoreItemObj> list) {
        int s = 0;
        for (StoreItemObj obj : list) {
            s += obj.getSum();
        }
        String sum;
        if (s > 99) {
            sum = "...";
        } else {
            sum = String.valueOf(s);
        }

        sumText.setText(TextHandeler.getText(context, R.string.settlement_sum_text).replace("?", sum));
    }

    public void setTotalPriceText(List<StoreItemObj> list) {
        double sum = 0;
        for (StoreItemObj obj : list) {
            double p = obj.getPrice();
            double s = obj.getSum();
            sum = sum + (p * s);
        }
        totalPriceText.setText(new DecimalFormat("0.00").format(sum));
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getCart() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        getShoppingCsrInData();
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONArray resultsArray = JsonHandle.getArray(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                if (resultsArray != null) {
                                    ShoppingCarHandler.deleteAllItem(context);
                                    setShoppingCarData(ShoppingCarHandler.getShoppingCarList(context, resultsArray));
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "message"));
                            }

                        }
                    }

                });
    }

}
