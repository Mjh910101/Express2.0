package com.express.subao.adaptera;

import android.content.Context;
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
import com.express.subao.box.StoreItemObj;
import com.express.subao.download.DownloadImageLoader;
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
public class OrderItemAdapter extends BaseAdapter {

    private Context context;
    private List<StoreItemObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public OrderItemAdapter(Context context, List<StoreItemObj> itemList) {
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
                    R.layout.layout_order_item_item, null);
            holder = new HolderView(convertView);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        StoreItemObj obj = itemList.get(position);
        setView(holder, obj);
        return convertView;
    }

    private void setView(HolderView holder, StoreItemObj obj) {
        setRebateImage(holder.image, obj.getCover());
        holder.title.setText(obj.getTitle());
        holder.sum.setText("x" + obj.getSum());
        holder.price.setText("MOP:"+obj.getPrice());
    }

    private void setRebateImage(ImageView img, String s) {
        int w = WinTool.getWinWidth(context) / 4;

        img.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(img, s);
    }

    class HolderView {

        ImageView image;
        TextView title;
        TextView price;
        TextView sum;

        HolderView(View view) {
            image = (ImageView) view.findViewById(R.id.orderItem_image);
            title = (TextView) view.findViewById(R.id.orderItem_title);
            price = (TextView) view.findViewById(R.id.orderItem_price);
            sum = (TextView) view.findViewById(R.id.orderItem_sum);
        }

    }

}
