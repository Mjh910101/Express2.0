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
import com.express.subao.activitys.RebateContentActivity;
import com.express.subao.box.RebateObj;
import com.express.subao.box.handlers.RebateObjHandler;
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
 * Created by Hua on 15/12/24.
 */
public class RebateAdapter extends BaseAdapter {

    private Context context;
    private List<RebateObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public RebateAdapter(Context context, List<RebateObj> itemList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_tebate_list_items, null);
            holder = new HolderView();

            holder.img = (ImageView) convertView.findViewById(R.id.rebate_item_img);
            holder.title = (TextView) convertView.findViewById(R.id.rebate_item_title);
            holder.intro = (TextView) convertView.findViewById(R.id.rebate_item_intro);
            holder.time = (TextView) convertView.findViewById(R.id.rebate_item_time);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        setView(holder, itemList.get(position));
        setOnClick(convertView, itemList.get(position));

        return convertView;
    }

    private void setOnClick(View view, final  RebateObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RebateObjHandler.saveRebateObj(obj);
                Passageway.jumpActivity(context, RebateContentActivity.class);
            }
        });
    }

    private void setView(HolderView holder, RebateObj obj) {
        setRebateImage(holder.img, obj.getImg());
        holder.title.setText(obj.getTitle());
        holder.time.setText("優惠時間"+obj.getTips());
        holder.intro.setText(obj.getIntro());
    }

    private void setRebateImage(ImageView img, String s) {
        int w = WinTool.getWinWidth(context) / 4;

        img.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(img, s);
    }

    public void addItems(List<RebateObj> items) {
        for (RebateObj obj : items) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {
        TextView title;
        TextView intro;
        TextView time;
        ImageView img;
    }
}
