package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.UserAddressObj;

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
 * Created by Hua on 16/7/13.
 */
public class UserAddressAdaper extends BaseAdapter {

    private Context context;
    private List<UserAddressObj> itemList;
    private LayoutInflater inflater;

    private boolean isSet;
    private int click;

    public UserAddressAdaper(Context context, List<UserAddressObj> list) {
        initBaseAdapter(context);
        this.itemList = list;
        this.isSet = false;
        this.click = -1;
    }


    private void initBaseAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(List<UserAddressObj> list) {
        for (UserAddressObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
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
                    R.layout.layout_address_item, null);
            holder = new HolderView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        UserAddressObj obj = itemList.get(position);
        setView(holder, obj, position);
        setOnClick(convertView, obj, position);
        return convertView;
    }

    private void setOnClick(View view, UserAddressObj obj, final int p) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSet) {

                } else {
                    click = p;
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setView(HolderView holder, UserAddressObj obj, int p) {
        holder.userName.setText(obj.getReceiver());
        holder.tel.setText(obj.getContact());
        holder.address.setText(obj.getAddress());

        holder.onClick.setVisibility(View.INVISIBLE);
        if (click == p) {
            holder.onClick.setVisibility(View.VISIBLE);
            holder.onClick.setImageResource(R.drawable.choice_on_icon);
        }
        if (isSet) {
            holder.onClick.setVisibility(View.VISIBLE);
            holder.onClick.setImageResource(R.drawable.address_set_icon);
        }


        holder.defaultText.setVisibility(View.GONE);
        if (obj.isDefault()) {
            holder.defaultText.setVisibility(View.VISIBLE);
            if (click == -1 && !isSet) {
                holder.onClick.setVisibility(View.VISIBLE);
                holder.onClick.setImageResource(R.drawable.choice_on_icon);
            }
        }

    }

    public void setSetting(boolean b) {
        isSet = b;
        click = -1;
        notifyDataSetChanged();
    }

    public UserAddressObj getClickUserAddress() {
        if (click == -1) {
            for (UserAddressObj obj : itemList) {
                if (obj.isDefault()) {
                    return obj;
                }
            }
        }
        return itemList.get(click);
    }

    class HolderView {
        TextView userName;
        TextView tel;
        TextView address;
        TextView defaultText;
        ImageView onClick;

        HolderView(View view) {
            userName = (TextView) view.findViewById(R.id.addressItem_userNameText);
            tel = (TextView) view.findViewById(R.id.addressItem_userTel);
            address = (TextView) view.findViewById(R.id.addressItem_addressText);
            defaultText = (TextView) view.findViewById(R.id.addressItem_defaultText);
            onClick = (ImageView) view.findViewById(R.id.addressItem_onClick);
        }
    }

}
