package com.express.subao.adaptera;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.StoreItemContentActivity;
import com.express.subao.box.ItemObj;
import com.express.subao.box.RebateObj;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.interfaces.CallbackForString;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;

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
 * Created by Hua on 15/12/30.
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<ItemObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public ItemAdapter(Context context, List<ItemObj> itemList) {
        initBaseAdapter(context);
        this.itemList = itemList;
    }

    public void setCallback(CallbackForString callback) {
        this.callback = callback;
    }

    private void initBaseAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void addItems(List<ItemObj> items) {
        for (ItemObj obj : items) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_item_list_items, null);
            holder = new HolderView();

            holder.title = (TextView) convertView.findViewById(R.id.item_item_title);
            holder.sell = (TextView) convertView.findViewById(R.id.item_item_sell);
            holder.priceStr = (TextView) convertView.findViewById(R.id.item_item_priceStr);
            holder.img = (ImageView) convertView.findViewById(R.id.item_item_img);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        ItemObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);
        return convertView;
    }

    private void setOnClick(View view, ItemObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Passageway.jumpActivity(context, StoreItemContentActivity.class);
            }
        });
    }

    private void setView(HolderView holder, ItemObj obj) {
        setRebateImage(holder.img, obj.getImg());
        holder.title.setText(obj.getTitle());
        holder.sell.setText("已售" + (int) obj.getSell());
        holder.priceStr.setText(obj.getPrice_str());
    }

    private void setRebateImage(ImageView img, String s) {
        int w = WinTool.getWinWidth(context) / 4;

        img.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(img, s);
    }

    class HolderView {
        TextView title;
        TextView sell;
        TextView priceStr;
        ImageView img;
    }

}
