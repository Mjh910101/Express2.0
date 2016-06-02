package com.express.subao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
 * Created by Hua on 16/5/26.
 */
public class SortFatherLayout extends TouchLinearLayout {

    public SortFatherLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //获得子孩子的个数
        int childCount = getChildCount();
        //本身的宽度除以子View的个数，获得每个孩子的宽度
        int width = getWidth();
        //获得此本身的高度
        int height = getHeight();

        Log.e(TAG, "childCount : " + childCount + "  ,  width : " + width + "  ,  height : " + height);

        View childView = getChildAt(0);

        int[] position = new int[2];

        childView.getLocationOnScreen(position);
        Log.e(TAG, "position : " + position[0] + " , " + position[1]);
        Log.e(TAG, "position : " + childView.getWidth() + " , " + childView.getHeight());

        onInterceptTouchEvent(ev);
        return true;
    }

}
