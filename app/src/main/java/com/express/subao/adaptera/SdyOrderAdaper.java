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
import com.express.subao.activitys.SdyOrderContentActivity;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.ColorHandle;
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
public class SdyOrderAdaper extends BaseAdapter {

    private Context context;
    private List<SdyOrderObj> itemList;
    private LayoutInflater inflater;

    private CallbackForString callback;

    public SdyOrderAdaper(Context context, List<SdyOrderObj> itemList) {
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
                    R.layout.layout_query_expres_list_items, null);
            holder = new HolderView();

            holder.img = (ImageView) convertView.findViewById(R.id.query_express_item_img);
            holder.companyIcon = (ImageView) convertView.findViewById(R.id.query_express_item_companyIcon);
            holder.codeIcon = (ImageView) convertView.findViewById(R.id.query_express_item_codeIcon);
            holder.statusStr = (TextView) convertView.findViewById(R.id.query_express_item_statusStr);
            holder.companyName = (TextView) convertView.findViewById(R.id.query_express_item_companyName);
            holder.code = (TextView) convertView.findViewById(R.id.query_express_item_code);
            holder.codeText = (TextView) convertView.findViewById(R.id.query_express_item_codeText);
            holder.stayTimeText = (TextView) convertView.findViewById(R.id.query_express_item_stayTimeText);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        SdyOrderObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);

        return convertView;
    }


    private void setOnClick(View convertView, final SdyOrderObj obj) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SdyOrderObjHandler.saveSdyOrder(obj);
                Bundle b = new Bundle();
                b.putBoolean("isShowMob", false);
                b.putString(SdyOrderObj.SDY_ORDER_ID, obj.getSdy_order_id());
                Passageway.jumpActivity(context, SdyOrderContentActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, SdyOrderObj obj) {
//        setImage(holder.img, obj.getExpreser().getCompanyInfo().getIco());
//        holder.companyName.setText(obj.getExpreser().getCompanyInfo().getName() + " " + obj.getExpreser().getExpress_id());
        holder.img.setVisibility(View.GONE);

        holder.statusStr.setVisibility(View.VISIBLE);
        holder.stayTimeText.setVisibility(View.GONE);
        switch (obj.getStatus()) {
            case "1":
                holder.statusStr.setText("待取件");
                holder.statusStr.setBackgroundResource(R.color.green);
                holder.stayTimeText.setVisibility(View.VISIBLE);
                holder.stayTimeText.setText(obj.getStayTime());
                break;
            case "3":
                holder.statusStr.setText("快遞員取出");
                holder.statusStr.setBackgroundResource(R.color.red);
                break;
            case "4":
                holder.statusStr.setText("管理員取出");
                holder.statusStr.setBackgroundResource(R.color.red);
                break;
            default:
                holder.statusStr.setVisibility(View.INVISIBLE);
                break;
        }

        holder.companyName.setText("快遞單號 " + obj.getMailno());

        switch (obj.getStatus()) {
            case "1":
                holder.codeText.setVisibility(View.VISIBLE);
                holder.code.setTextColor(ColorHandle.getColorForID(context, R.color.green));
                holder.code.setText("" + obj.getOpen_code());
                holder.companyIcon.setImageResource(R.drawable.sdy_code_green_icon);
                holder.codeIcon.setImageResource(R.drawable.open_code_green_icon);
                break;
            default:
                holder.codeText.setVisibility(View.GONE);
                holder.code.setTextColor(ColorHandle.getColorForID(context, R.color.text_gray_01));
                holder.code.setText(obj.getPickup_time());
                holder.companyIcon.setImageResource(R.drawable.sdy_code_gray_icon);
                holder.codeIcon.setImageResource(R.drawable.time_code_gray_icon);
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
        ImageView companyIcon;
        ImageView codeIcon;
        TextView code;
        TextView codeText;
        TextView statusStr;
        TextView stayTimeText;
    }

}
