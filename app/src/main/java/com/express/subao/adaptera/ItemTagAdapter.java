package com.express.subao.adaptera;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.AreaObj;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.interfaces.CallbackForString;
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
 * Created by Hua on 15/12/30.
 */
public class ItemTagAdapter extends BaseAdapter {

    private Context context;
    private List<String> itemList;
    private LayoutInflater inflater;
    private int onClickPosition = 0;

    private CallbackForString callback;

    public ItemTagAdapter(Context context, List<String> itemList) {
        initBaseAdapter(context);
        this.itemList = itemList;
        this.onClickPosition = 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final String tag = itemList.get(position);
        int p = 30;
        final TextView view = new TextView(context);
        view.setText(tag);
        view.setTextSize(17);
        view.setGravity(Gravity.CENTER);
        view.setPadding(0, p, 0, p);
        if (onClickPosition == position) {
            view.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
            view.setBackgroundResource(R.color.whitle);
        } else {
            view.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
            view.setBackgroundResource(R.color.lucency);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.callback(tag);
                    onClickPosition = position;
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }

}
