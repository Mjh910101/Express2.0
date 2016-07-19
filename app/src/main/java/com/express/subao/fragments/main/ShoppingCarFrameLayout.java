package com.express.subao.fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.ItemOrderLiatActivity;
import com.express.subao.adaptera.ShoppingCarAdapter;
import com.express.subao.adaptera.StoreItemAdapter;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * Created by Hua on 15/12/21.
 */
public class ShoppingCarFrameLayout extends BaseFragment {

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
    public void onRestart() {
        initShoppingCar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_shoppingcar, container,
                false);
        ViewUtils.inject(this, contactsLayout);

        initShoppingCar();
        return contactsLayout;
    }

    @OnClick({R.id.shoppingCar_choiceIcon, R.id.shoppingCar_sumText})
    private void onClick(View view) {
        switch (view.getId()) {
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
                Passageway.jumpActivity(context, ItemOrderLiatActivity.class);
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

    private void initShoppingCar() {
        initText();
        downloadData();
    }

    private void getShoppingCsrInData(){
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

    private void initText() {
        totalPriceText.setText("0.0");
        sumText.setText(TextHandeler.getText(context, R.string.settlement_sum_text).replace("?", String.valueOf(0)));
        sumText.setBackgroundResource(R.color.title_bg);
        choiceIcon.setImageResource(R.drawable.choice_off_icon);
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

    public void onEditorText(TextView view) {
        if (mShoppingCarAdapter != null) {
            boolean b = mShoppingCarAdapter.isSetting();
            if (b) {
                view.setText(TextHandeler.getText(context, R.string.editor_text));
                mShoppingCarAdapter.saveAll();
            } else {
                view.setText(TextHandeler.getText(context, R.string.save_text));
            }
            mShoppingCarAdapter.setSetting(!b);
        }
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
