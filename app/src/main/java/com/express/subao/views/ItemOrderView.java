package com.express.subao.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
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
 * Created by Hua on 16/7/12.
 */
public class ItemOrderView extends LinearLayout {

    @ViewInject(R.id.itemOrder_storeName)
    private TextView storeName;
    @ViewInject(R.id.itemOrder_dataList)
    private InsideListView dataList;

    private View view;
    private Context context;
    private LayoutInflater inflater;
    private StoreObj store;
    private List<StoreItemObj> itemList;

    public ItemOrderView(Context context, StoreObj store, List<StoreItemObj> itemList) {
        super(context);
        this.context = context;
        this.store = store;
        this.itemList = itemList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_order_item, null);
        ViewUtils.inject(this, view);
        initLayout();
        addView(view);
    }

    private void initLayout() {
        storeName.setText(store.getTitle());
    }



}
