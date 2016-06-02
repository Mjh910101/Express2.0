package com.express.subao.adaptera;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.activitys.ExpresContentActivity;
import com.express.subao.activitys.PayActivity;
import com.express.subao.activitys.RebateContentActivity;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.RebateObj;
import com.express.subao.box.handlers.ExpresObjHandler;
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
public class ExpresAdaper extends BaseAdapter {

    private Context context;
    private List<ExpresObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public ExpresAdaper(Context context, List<ExpresObj> itemList) {
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
                    R.layout.layout_expres_list_items, null);
            holder = new HolderView();

            holder.verify = (TextView) convertView.findViewById(R.id.expres_item_verify);
            holder.tips = (TextView) convertView.findViewById(R.id.expres_item_tips);
            holder.price = (TextView) convertView.findViewById(R.id.expres_item_price);
            holder.openBtn = (TextView) convertView.findViewById(R.id.expres_item_openBtn);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        ExpresObj obj = itemList.get(position);
        setView(holder, obj);
        setOnOpenBtn(holder.openBtn, obj);
        setOnClick(convertView, obj);

        return convertView;
    }

    private void setOnOpenBtn(TextView btn, ExpresObj obj) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Passageway.jumpActivity(context, PayActivity.class);
            }
        });
    }

    private void setOnClick(View convertView, final ExpresObj obj) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpresObjHandler.saveExpresObj(obj);
                Bundle b = new Bundle();
                b.putBoolean("isShowMob", true);
                Passageway.jumpActivity(context, ExpresContentActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, ExpresObj obj) {
        holder.verify.setText(obj.getVerify());
        holder.tips.setText(obj.getTips());
        holder.price.setText("MOP" + obj.getPrice());
    }


    class HolderView {
        TextView verify;
        TextView tips;
        TextView price;
        TextView openBtn;
    }
}
