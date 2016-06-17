package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.express.subao.R;
import com.express.subao.activitys.SdyboxContentaActivity;
import com.express.subao.box.AreaObj;
import com.express.subao.box.SdyBoxObj;
import com.express.subao.box.handlers.SdyBoxObjHandler;
import com.express.subao.interfaces.CallbackForString;
import com.express.subao.tool.Passageway;
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
 * Created by Hua on 16/5/18.
 */
public class SdyBoxAdapter extends BaseAdapter {

    private List<SdyBoxObj> itemList;
    private Context context;
    private LayoutInflater inflater;

    private AddressListener mAddressListener;


    public SdyBoxAdapter(Context context, List<SdyBoxObj> itemList, AddressListener listener) {
        initBaseAdapter(context);
        this.itemList = itemList;
        this.mAddressListener = listener;
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
                    R.layout.layout_sdybox_item, null);
            holder = new HolderView();

            holder.title = (TextView) convertView.findViewById(R.id.sdybox_title);
            holder.address = (TextView) convertView.findViewById(R.id.sdybox_address);
            holder.contentText = (TextView) convertView.findViewById(R.id.sdybox_contentText);
            holder.pic = (TextView) convertView.findViewById(R.id.sdybox_icon);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        SdyBoxObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClickContentText(holder.contentText, obj);
        serOnClick(convertView, obj);
        return convertView;
    }

    private void setOnClickContentText(TextView view, final SdyBoxObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdyBoxObjHandler.saveSdyBoxObj(obj);
                Passageway.jumpActivity(context, SdyboxContentaActivity.class);
            }
        });
    }

    private void serOnClick(View view, final SdyBoxObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressListener != null && obj.getPoint() != null) {
                    mAddressListener.onAddress(obj.getPoint());
                }
            }
        });
    }

    private void setView(HolderView holder, SdyBoxObj obj) {
        holder.title.setText(obj.getTitle());
        holder.address.setText(obj.getAddress());
        holder.pic.setText(obj.getNum());
    }

    public void addItems(List<SdyBoxObj> list) {
        for (SdyBoxObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {
        TextView title;
        TextView address;
        TextView contentText;
        TextView pic;
    }

    public interface AddressListener {
        public void onAddress(LatLng p);
    }
}
