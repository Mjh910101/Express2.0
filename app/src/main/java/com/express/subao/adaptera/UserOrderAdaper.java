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
import com.express.subao.activitys.UserOrderContentActivity;
import com.express.subao.box.ItemObj;
import com.express.subao.box.OrderObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.handlers.ColorHandle;
import com.express.subao.tool.Passageway;
import com.express.subao.views.InsideListView;

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
 * Created by Hua on 16/7/28.
 */
public class UserOrderAdaper extends BaseAdapter {

    private Context context;
    private List<OrderObj> itemList;
    private LayoutInflater inflater;

    public UserOrderAdaper(Context context, List<OrderObj> list) {
        initBaseAdapter(context);
        this.itemList = list;
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
                    R.layout.layout_user_order_item, null);
            holder = new HolderView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        OrderObj obj = itemList.get(position);

        setView(holder, obj);
        setOnClick(convertView, obj);
        return convertView;
    }

    private void setOnClick(View view, final OrderObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", obj.getObjectId());
                Passageway.jumpActivity(context, UserOrderContentActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, OrderObj obj) {
        holder.storeName.setText(obj.getStoreName());
        holder.messageText.setText(obj.getItemMessage());

        switch (obj.getStatus()) {
            case "1":
                holder.statusText.setText("未發貨");
                holder.statusText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gray_01));
                holder.statusText.setBackgroundResource(R.drawable.gray_box_btn);
                break;
            default:
                holder.statusText.setText("已發貨");
                holder.statusText.setTextColor(ColorHandle.getColorForID(context, R.color.text_orange));
                holder.statusText.setBackgroundResource(R.drawable.orange_box_btn);
                break;
        }

        if (!obj.isNull()) {
            holder.dataList.setAdapter(new OrderItemAdapter(context, obj.getItemList()));
        }
    }

    public void add(List<OrderObj> list) {
        for (OrderObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {

        TextView storeName;
        TextView messageText;
        TextView statusText;
        InsideListView dataList;

        HolderView(View view) {
            storeName = (TextView) view.findViewById(R.id.userOrder_storeName);
            messageText = (TextView) view.findViewById(R.id.userOrder_messageText);
            statusText = (TextView) view.findViewById(R.id.userOrder_statusText);
            dataList = (InsideListView) view.findViewById(R.id.userOrder_dataList);
        }

    }
}
