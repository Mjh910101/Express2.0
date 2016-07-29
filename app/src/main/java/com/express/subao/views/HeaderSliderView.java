package com.express.subao.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.express.subao.R;
import com.express.subao.box.SliderObj;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.tool.AnimationTool;
import com.express.subao.tool.PPTFlish;
import com.express.subao.tool.WinTool;

import java.util.ArrayList;
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
 * Created by Hua on 15/11/6.
 */
public class HeaderSliderView extends LinearLayout {
    private Context context;

    private View view;
    private ImageView boxBg;
    private LayoutInflater inflater;

    private InsideViewFlipper mViewFlipper;
    private LinearLayout pptBallBox;


    public HeaderSliderView(Context context, List<SliderObj> list) {
        super(context);
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_slide, null);

        mViewFlipper = (InsideViewFlipper) view.findViewById(R.id.ppt_images);
        boxBg = (ImageView) view.findViewById(R.id.ppt_boxBg);
        pptBallBox = (LinearLayout) view.findViewById(R.id.ppt_ball);

        SliderView.initSliderView(context, list, mViewFlipper, pptBallBox, boxBg);

        addView(view);
    }

}
