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
import com.express.subao.box.CommentObj;
import com.express.subao.download.DownloadImageLoader;
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
 * Created by Hua on 16/7/4.
 */
public class CommentAdaper extends BaseAdapter {

    private List<CommentObj> itemList;
    private Context context;
    private LayoutInflater inflater;

    public CommentAdaper(Context context, List<CommentObj> itemList) {
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

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_comment_list_item, null);
            holder = new HolderView();

            holder.pic = (ImageView) convertView.findViewById(R.id.commentItem_pic);
            holder.userName = (TextView) convertView.findViewById(R.id.commentItem_userName);
            holder.time = (TextView) convertView.findViewById(R.id.commentItem_time);
            holder.content = (TextView) convertView.findViewById(R.id.commentItem_content);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        setView(holder, itemList.get(position));

        return convertView;
    }

    private void setView(HolderView holder, CommentObj obj) {
        int w = WinTool.getWinWidth(context) / 10;
        holder.pic.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(holder.pic, obj.getUserAvatar(), w / 2);

        holder.content.setText(obj.getContent());
        holder.time.setText(obj.getTime());
        holder.userName.setText(obj.getUserName());
    }

    public void addItem(List<CommentObj> list) {
        for (CommentObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {
        TextView userName;
        TextView time;
        TextView content;
        ImageView pic;
    }
}
