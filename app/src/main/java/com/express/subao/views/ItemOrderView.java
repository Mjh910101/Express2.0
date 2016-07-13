package com.express.subao.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
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
    @ViewInject(R.id.orderItem_number)
    private TextView number;
    @ViewInject(R.id.orderItem_sumText)
    private TextView sumText;

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
        number.setText(getItemSum());
        sumText.setText(getItemSumPrice());
        dataList.setAdapter(new ItemAdapter());
    }

    public String getItemSum() {
        int s = 0;
        for (StoreItemObj obj : itemList) {
            s += obj.getSum();
        }
        return "共計" + s + "件商品";
    }

    public String getItemSumPrice() {
        double s=0;
        for (StoreItemObj obj : itemList) {
            s += obj.getPriceSum();
        }
        return "合計 mob:" +new DecimalFormat("0.00").format(s);
    }

    class ItemAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        ItemAdapter() {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.layout_item_nopic_item, null);
                holder = new HolderView(convertView);
                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            setView(holder, itemList.get(position));
            return convertView;
        }

        private void setView(HolderView holder, StoreItemObj obj) {
            holder.name.setText(obj.getTitle());
            holder.price.setText("MOB " + obj.getPrice());
            holder.sum.setText("x" + obj.getSum());
        }
    }

    class HolderView {
        TextView name;
        TextView price;
        TextView sum;

        HolderView(View view) {
            name = (TextView) view.findViewById(R.id.item_itemName);
            price = (TextView) view.findViewById(R.id.item_itemPrice);
            sum = (TextView) view.findViewById(R.id.item_itemSum);
        }
    }

}
