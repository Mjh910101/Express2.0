package com.express.subao.adaptera;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.ContentImageActivity;
import com.express.subao.box.RebateObj;
import com.express.subao.tool.Passageway;

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
 * Created by Hua on 15/12/29.
 */
public class RemainingMoneyAdapter extends BaseAdapter {

    private Context context;
    private List<RemainingMoneyObj> itemList;
    private LayoutInflater inflater;

    public RemainingMoneyAdapter(Context context) {
        initBaseAdapter(context);
        initItemList();
    }

    private void initItemList() {
        itemList = new ArrayList<RemainingMoneyObj>(3);

        itemList.add(new RemainingMoneyObj("充值", "余额：10000.00", "2015-12-20", "+100.00", R.drawable.chongzhi_image));
        itemList.add(new RemainingMoneyObj("支付快递费", "余额：950.00", "2015-12-20", "-50.00", R.drawable.kuaidi_image));
        itemList.add(new RemainingMoneyObj("购物消费", "余额：750.00", "2015-12-20", "-200.00", R.drawable.gouwu_image));
    }

    private void initBaseAdapter(Context context) {
        this.context = context;
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
                    R.layout.layout_remaining_money_list_items, null);
            holder = new HolderView();

            holder.tag = (TextView) convertView.findViewById(R.id.remainingMoney_item_tag);
            holder.money = (TextView) convertView.findViewById(R.id.remainingMoney_item_money);
            holder.time = (TextView) convertView.findViewById(R.id.remainingMoney_item_time);
            holder.increase = (TextView) convertView.findViewById(R.id.remainingMoney_item_increase);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        setView(holder, itemList.get(position));
        setOnClick(convertView, itemList.get(position));
        return convertView;
    }

    private void setOnClick(View view, final RemainingMoneyObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(ContentImageActivity.TITLE_NAME, "详细");
                b.putInt(ContentImageActivity.IMAGE_ID, obj.rid);
                Passageway.jumpActivity(context, ContentImageActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, RemainingMoneyObj obj) {
        holder.tag.setText(obj.tag);
        holder.money.setText(obj.money);
        holder.time.setText(obj.time);
        holder.increase.setText(obj.increase);
    }

    class HolderView {
        TextView tag;
        TextView money;
        TextView time;
        TextView increase;
    }

    class RemainingMoneyObj {
        String tag;
        String money;
        String time;
        String increase;
        int rid;

        RemainingMoneyObj(String tag, String money, String time, String increase, int rid) {
            this.tag = tag;
            this.money = money;
            this.time = time;
            this.increase = increase;
            this.rid = rid;
        }
    }

}
