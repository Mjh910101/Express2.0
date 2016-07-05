package com.express.subao.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.express.subao.R;
import com.express.subao.adaptera.StoreItemAdapter;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.fragments.BaseFragment;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    @Override
    public void onRestart() {

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
        List<ShoppingCarObj> list=ShoppingCarHandler.getAllShoppingCar(context);
    }

}
