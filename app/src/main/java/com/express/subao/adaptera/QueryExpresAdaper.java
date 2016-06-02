package com.express.subao.adaptera;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.CheckExpressActivity;
import com.express.subao.activitys.ExpresContentActivity;
import com.express.subao.activitys.PayActivity;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.handlers.ExpresObjHandler;
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
 * Created by Hua on 15/12/25.
 */
public class QueryExpresAdaper extends BaseAdapter {

    private Context context;
    private List<ExpresObj> itemList;
    private LayoutInflater inflater;
    private boolean isHaveBox;

    private CallbackForString callback;

    public QueryExpresAdaper(Context context, List<ExpresObj> itemList, boolean isHaveBox) {
        initBaseAdapter(context);
        this.itemList = itemList;
        this.isHaveBox = isHaveBox;
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
                    R.layout.layout_query_expres_list_items, null);
            holder = new HolderView();

            holder.img = (ImageView) convertView.findViewById(R.id.query_express_item_img);
            holder.statusStr = (TextView) convertView.findViewById(R.id.query_express_item_statusStr);
            holder.companyName = (TextView) convertView.findViewById(R.id.query_express_item_companyName);
            holder.code = (TextView) convertView.findViewById(R.id.query_express_item_code);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        ExpresObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);

        return convertView;
    }


    private void setOnClick(View convertView, final ExpresObj obj) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHaveBox) {
                    ExpresObjHandler.saveExpresObj(obj);
                    Bundle b = new Bundle();
                    b.putBoolean("isShowMob", false);
                    Passageway.jumpActivity(context, ExpresContentActivity.class, b);
                } else {
                    Bundle b = new Bundle();
                    b.putString(CheckExpressActivity.CODE_KEY, obj.getExpreser().getExpress_id());
                    Passageway.jumpActivity(context, CheckExpressActivity.class, b);
                }
            }
        });
    }

    private void setView(HolderView holder, ExpresObj obj) {
        setImage(holder.img, obj.getExpreser().getCompanyInfo().getIco());
        holder.companyName.setText(obj.getExpreser().getCompanyInfo().getName() + " " + obj.getExpreser().getExpress_id());
        holder.statusStr.setVisibility(View.VISIBLE);
        holder.statusStr.setText(obj.getStatusStr());
        switch (obj.getStatus()) {
            case -1:
                holder.statusStr.setBackgroundResource(R.color.blue);
                break;
            case 0:
                holder.statusStr.setBackgroundResource(R.color.green);
                break;
            case 2:
                holder.statusStr.setBackgroundResource(R.color.red);
                break;
            default:
                holder.statusStr.setVisibility(View.INVISIBLE);
                break;
        }

        switch (obj.getStatus()) {
            case -1:
            case 0:
            case 2:
                if (isHaveBox) {
                    holder.code.setText("開箱碼" + obj.getVerify());
                } else {
                    holder.code.setText(obj.getAddress().getArea());
                }
                break;
            default:
                holder.code.setText(obj.getExpreser().getExpressAt());
                break;
        }
    }

    private void setImage(ImageView img, String ico) {
        int w = WinTool.getWinWidth(context) / 5;
        img.setLayoutParams(new LinearLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(img, ico);
    }


    class HolderView {
        ImageView img;
        TextView companyName;
        TextView code;
        TextView statusStr;
    }

}
