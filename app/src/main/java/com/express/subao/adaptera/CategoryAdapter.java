package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.express.subao.activitys.CategoryActivity;
import com.express.subao.box.CategoryObj;
import com.express.subao.box.HelpObj;
import com.express.subao.box.handlers.CategoryObjHandler;
import com.express.subao.download.DownloadImageLoader;
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
 * Created by Hua on 15/12/21.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryObj> imageList;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<CategoryObj> imageList) {
        initBaseAdapter(context);
        this.imageList = imageList;
    }

    private void initBaseAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CategoryObj obj = imageList.get(position);
        double w = WinTool.getWinWidth(context) / 2d;
        double h = w / 320d - 185d;

        ImageView view = new ImageView(context);
        view.setLayoutParams(new GridView.LayoutParams((int) w, (int) h));
        DownloadImageLoader.loadImage(view, obj.getImg());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryObjHandler.setCategoryObj(obj);
                Passageway.jumpActivity(context, CategoryActivity.class);
            }
        });

        return view;
    }

}
