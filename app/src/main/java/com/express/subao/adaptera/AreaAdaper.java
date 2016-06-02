package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.AreaObj;
import com.express.subao.box.HelpObj;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.interfaces.CallbackForString;
import com.express.subao.tool.WinTool;
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
 * Created by Hua on 15/12/22.
 */
public class AreaAdaper extends BaseAdapter {

    private Context context;
    private List<AreaObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public AreaAdaper(Context context, List<AreaObj> itemList) {
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
                    R.layout.layout_area_liat_items, null);
            holder = new HolderView();
            holder.title = (TextView) convertView.findViewById(R.id.areaItem_name);
            holder.dataList = (InsideListView) convertView.findViewById(R.id.areaItem_dataList);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        setView(holder, itemList.get(position));

        return convertView;
    }

    private void setView(HolderView holder, AreaObj obj) {
        holder.title.setText(obj.getName());
        holder.dataList.setAdapter(new InsidAdaper(obj.getAreaList()));
    }

    class HolderView {
        TextView title;
        InsideListView dataList;
    }

    class InsidAdaper extends BaseAdapter {

        List<AreaObj> areaList;

        private InsidAdaper(List<AreaObj> areaList) {
            this.areaList = areaList;
        }

        @Override
        public int getCount() {
            return areaList.size();
        }

        @Override
        public Object getItem(int position) {
            return areaList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String name = areaList.get(position).getName();

            TextView view = new TextView(context);
            view.setText(name);
            view.setPadding(3, 10, 10, 10);
            view.setTextSize(17);
            view.setTextColor(ColorHandler.getColorForID(context, R.color.black));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.callback(name);
                    }
                }
            });
            return view;
        }
    }

}
