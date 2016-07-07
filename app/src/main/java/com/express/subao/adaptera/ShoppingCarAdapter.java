package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.tool.WinTool;
import com.nostra13.universalimageloader.utils.L;

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
 * Created by Hua on 16/7/5.
 */
public class ShoppingCarAdapter extends BaseAdapter {

    private final static int TYPE_COUNT = 2;

    private Context context;
    private List<ShoppingCarObj> itemList;
    private LayoutInflater inflater;

    public ShoppingCarAdapter(Context context, List<ShoppingCarObj> itemList) {
        initAdapter(context);
        this.itemList = itemList;
    }

    private void initAdapter(Context context) {
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
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StoreHolder storeHolder;
        StoreItemHolder storeItemHolder;

        ShoppingCarObj obj = (ShoppingCarObj) getItem(position);

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case ShoppingCarObj.STORE:
                    convertView = inflater.inflate(R.layout.layout_shoppinacar_store_item, null);
                    storeHolder = new StoreHolder(convertView);
                    setStoreLayoutMessage(storeHolder, obj);
                    convertView.setTag(storeHolder);
                    break;
                case ShoppingCarObj.ITEM:
                    convertView = inflater.inflate(R.layout.layout_shoppinacar_items_item, null);
                    storeItemHolder = new StoreItemHolder(convertView);
                    setItemLayoutMessage(storeItemHolder, obj);
                    convertView.setTag(storeItemHolder);
                    break;
            }
        } else {
            switch (type) {
                case ShoppingCarObj.STORE:
                    storeHolder = (StoreHolder) convertView.getTag();
                    setStoreLayoutMessage(storeHolder, obj);
                    break;
                case ShoppingCarObj.ITEM:
                    storeItemHolder = (StoreItemHolder) convertView.getTag();
                    setItemLayoutMessage(storeItemHolder, obj);
                    break;
            }
        }

        return convertView;
    }

    private void setStoreLayoutMessage(StoreHolder holder, ShoppingCarObj obj) {
        holder.storeName.setText(obj.getStoreObj().getTitle());
        holder.status.setVisibility(View.GONE);
//        holder.delivery.setVisibility(View.GONE);
    }


    private void setItemLayoutMessage(StoreItemHolder holder, ShoppingCarObj obj) {
        holder.title.setText(obj.getStoreItemObj().getTitle());
        holder.sell.setText("已售 " + obj.getStoreItemObj().getSell());
        holder.price.setText("MOB " + obj.getStoreItemObj().getPrice());
        holder.sum.setText("x " + obj.getStoreItemObj().getSum());

        int w = WinTool.getWinWidth(context) / 4;
        holder.image.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(holder.image, obj.getStoreItemObj().getCover());

        holder.warnLayout.setVisibility(View.GONE);
        holder.toolLayout.setVisibility(View.GONE);
    }

    class StoreHolder {
        TextView storeName;
        TextView status;
        TextView delivery;

        StoreHolder(View view) {
            storeName = (TextView) view.findViewById(R.id.sCar_item_shopName);
            status = (TextView) view.findViewById(R.id.sCar_item_status);
            delivery = (TextView) view.findViewById(R.id.sCar_item_delivery);
        }
    }

    class StoreItemHolder {
        ImageView image;
        TextView noStock;
        TextView title;
        TextView sell;
        TextView price;
        TextView sum;
        TextView warnMessage;
        LinearLayout messageLayout;
        LinearLayout toolLayout;
        LinearLayout warnLayout;

        StoreItemHolder(View view) {
            image = (ImageView) view.findViewById(R.id.sCar_item_image);
            noStock = (TextView) view.findViewById(R.id.sCar_item_noStock);
            title = (TextView) view.findViewById(R.id.sCar_item_title);
            sell = (TextView) view.findViewById(R.id.sCar_item_sell);
            price = (TextView) view.findViewById(R.id.sCar_item_price);
            sum = (TextView) view.findViewById(R.id.sCar_item_sum);
            warnMessage = (TextView) view.findViewById(R.id.sCar_item_warnMessage);
            messageLayout = (LinearLayout) view.findViewById(R.id.sCar_item_messageLayout);
            toolLayout = (LinearLayout) view.findViewById(R.id.sCar_item_toolLayout);
            warnLayout = (LinearLayout) view.findViewById(R.id.sCar_item_warn);
        }
    }
}
