package com.express.subao.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.ShoppingCarAdapter;
import com.express.subao.adaptera.StoreItemAdapter;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.handlers.TextHandeler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * Created by Hua on 15/12/21.
 */
public class ShoppingCarFrameLayout extends BaseFragment {

    @ViewInject(R.id.shoppingCar_dataList)
    private ListView dataList;
    @ViewInject(R.id.shoppingCar_sumText)
    private TextView sumText;
    @ViewInject(R.id.shoppingCar_totalPriceText)
    private TextView totalPriceText;

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

    private void initShoppingCar() {
        initText();
        List<ShoppingCarObj> list = ShoppingCarHandler.getAllShoppingCar(context);
        mShoppingCarAdapter = new ShoppingCarAdapter(context, list, new ShoppingCarAdapter.NotifyListener() {

            @Override
            public void onNotify(List<StoreItemObj> list, boolean b) {
                if (!b) {
                    sumText.setBackgroundResource(R.color.title_bg);
                    setSumText(list);
                } else {
                    sumText.setBackgroundResource(R.color.red);
                    sumText.setText(R.string.delete_text);
                }
                setTotalPriceText(list);
            }
        });
        dataList.setAdapter(mShoppingCarAdapter);
    }

    private void initText() {
        totalPriceText.setText("0.0");
        sumText.setText(TextHandeler.getText(context, R.string.settlement_sum_text).replace("?", String.valueOf(0)));
    }

    private void setSumText(List<StoreItemObj> list) {
        sumText.setText(TextHandeler.getText(context, R.string.settlement_sum_text).replace("?", String.valueOf(list.size())));
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
        double sum=0;
        for(StoreItemObj obj:list){
            double p=obj.getPrice();
            double s=obj.getSum();
            sum=sum+(p*s);
        }
        totalPriceText.setText(new DecimalFormat("#.00").format(sum));
    }
}
