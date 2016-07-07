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
import com.express.subao.activitys.OpenSdyDailogActivity;
import com.express.subao.activitys.ScanningForSdyOrderListActivity;
import com.express.subao.activitys.SdyOrderContentActivity;
import com.express.subao.activitys.SdyOrderOpenContentActivity;
import com.express.subao.box.SdyOrderObj;
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
public class SdyOrderHaveOpenAdaper extends BaseAdapter {

    private Context context;
    private List<SdyOrderObj> itemList;
    private LayoutInflater inflater;
    private String qrcode;

    private CallbackForString callback;
    private OpenListener listener;

    public SdyOrderHaveOpenAdaper(Context context, List<SdyOrderObj> itemList, String qrcode, OpenListener listener) {
        initBaseAdapter(context);
        this.itemList = itemList;
        this.qrcode = qrcode;
        this.listener = listener;
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
                    R.layout.layout_sdy_open_item, null);
            holder = new HolderView();

            holder.companyIcon = (ImageView) convertView.findViewById(R.id.query_express_item_companyIcon);
            holder.codeIcon = (ImageView) convertView.findViewById(R.id.query_express_item_codeIcon);
            holder.companyName = (TextView) convertView.findViewById(R.id.query_express_item_companyName);
            holder.code = (TextView) convertView.findViewById(R.id.query_express_item_code);
            holder.codeText = (TextView) convertView.findViewById(R.id.query_express_item_codeText);
            holder.stayTimeText = (TextView) convertView.findViewById(R.id.query_express_item_stayTimeText);
            holder.openText = (TextView) convertView.findViewById(R.id.query_express_item_openText);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        SdyOrderObj obj = itemList.get(position);
        setOnOpenBtn(holder.openText, obj);
        setView(holder, obj);
        setOnClick(convertView, obj);
        return convertView;
    }

    private void setOnOpenBtn(TextView view, final SdyOrderObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle b = new Bundle();
//                b.putString("qrcode", qrcode);
//                b.putString("orderid", obj.getSdy_order_id());
//                Passageway.jumpActivity(context, OpenSdyDailogActivity.class, ScanningForSdyOrderListActivity.RequestCode, b);
                if (listener != null) {
                    listener.onClick(obj);
                }
            }
        });
    }


    private void setOnClick(View convertView, final SdyOrderObj obj) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SdyOrderObjHandler.saveSdyOrder(obj);
                Bundle b = new Bundle();
                b.putBoolean("isShowMob", false);
                b.putString("qrcode", qrcode);
                b.putString(SdyOrderObj.SDY_ORDER_ID, obj.getSdy_order_id());
                Passageway.jumpActivity(context, SdyOrderOpenContentActivity.class, ScanningForSdyOrderListActivity.RequestCode, b);
            }
        });
    }

    private void setView(HolderView holder, SdyOrderObj obj) {
        holder.companyName.setText("快遞單號 " + obj.getMailno());

        switch (obj.getStatus()) {
            case "1":
                holder.codeText.setVisibility(View.VISIBLE);
                holder.code.setTextColor(ColorHandle.getColorForID(context, R.color.green));
                holder.code.setText("" + obj.getOpen_code());
                holder.companyIcon.setImageResource(R.drawable.sdy_code_green_icon);
                holder.codeIcon.setImageResource(R.drawable.open_code_green_icon);
                holder.openText.setClickable(true);
                holder.stayTimeText.setText(obj.getStayTime());
                holder.openText.setText("開箱");
                holder.openText.setTextColor(ColorHandle.getColorForID(context, R.color.text_orange_02));
                holder.openText.setBackgroundResource(R.drawable.orange_box_orange_background_btn_02);
                break;
            default:
                holder.codeText.setVisibility(View.GONE);
                holder.code.setTextColor(ColorHandle.getColorForID(context, R.color.text_gray_01));
                holder.code.setText(obj.getPickup_time());
                holder.companyIcon.setImageResource(R.drawable.sdy_code_gray_icon);
                holder.codeIcon.setImageResource(R.drawable.time_code_gray_icon);
                holder.openText.setClickable(false);
                holder.openText.setText("已開箱");
                holder.openText.setTextColor(ColorHandle.getColorForID(context, R.color.text_gray_01));
                holder.openText.setBackgroundResource(R.drawable.gray_box_gray_background_btn_02);
                break;
        }

    }

    class HolderView {
        TextView companyName;
        ImageView companyIcon;
        ImageView codeIcon;
        TextView code;
        TextView codeText;
        TextView stayTimeText;
        TextView openText;
    }

    public interface OpenListener {
        public void onClick(SdyOrderObj obj);
    }

}
