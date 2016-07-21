package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.express.subao.activitys.StoreItemListActivity;
import com.express.subao.activitys.StoreItemListActivityV2;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.box.handlers.ShoppingCarHandler;
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
 * Created by Hua on 15/12/23.
 */
public class StoreAdapter extends BaseAdapter {

    private Context context;
    private List<StoreObj> itemList;
    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<StoreObj> itemList) {
        initBaseAdapter(context);
        this.itemList = itemList;
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

        final StoreObj obj = itemList.get(position);
        int w = WinTool.getWinWidth(context) / 2;
        int p = 15;

        ImageView view = new ImageView(context);
        view.setLayoutParams(new GridView.LayoutParams(w, w));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setPadding(p, p, p, p);
        DownloadImageLoader.loadImage(view, obj.getImg());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreObjHandler.saveStoreObj(obj);
                ShoppingCarHandler.saveStore(context, obj);
//                Passageway.jumpActivity(context, ItemListActivity.class);
                Passageway.jumpActivity(context, StoreItemListActivityV2.class);
            }
        });

        return view;
    }

}
