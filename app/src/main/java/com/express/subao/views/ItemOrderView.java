package com.express.subao.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.TextHandeler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

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
    @ViewInject(R.id.orderItem_freightTypeText)
    private TextView freightTypeText;
    @ViewInject(R.id.orderItem_freightSum)
    private TextView freightSum;
    @ViewInject(R.id.orderItem_remarkInput)
    private EditText remarkInput;

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
        sumText.setText(getItemSumPriceforStirng());
        dataList.setAdapter(new ItemAdapter());
    }

    public String getItemSum() {
        int s = 0;
        for (StoreItemObj obj : itemList) {
            s += obj.getSum();
        }
        return "共計" + s + "件商品";
    }

    public String getItemSumPriceforStirng() {
        return "合計 mob:" + new DecimalFormat("0.00").format(getItemSumPrice());
    }

    public double getItemSumPrice() {
        double s = 0;
        for (StoreItemObj obj : itemList) {
            s += obj.getPriceSum();
        }
        return s;
    }

    public List<StoreItemObj> getStoreItemList() {
        return itemList;
    }

    public void upload(JSONObject json) {
        freightTypeText.setText(JsonHandle.getString(json, "shipment_desc"));
        freightSum.setText(String.valueOf(JsonHandle.getInt(json, "shipment")));
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JsonHandle.put(json, "store_id", store.getObjectId());
        JsonHandle.put(json, "remark", TextHandeler.getText(remarkInput));
        return json;
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
