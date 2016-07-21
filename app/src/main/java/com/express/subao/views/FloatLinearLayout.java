package com.express.subao.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

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
 * Created by Hua on 16/7/20.
 */
public class FloatLinearLayout extends LinearLayout {

    public FloatLinearLayout(Context context) {
        super(context);
    }

    public FloatLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public FloatLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private ListView tapList;
    private ListView itemList;

    public void initDataList(ListView tapList, ListView itemList) {
        this.tapList = tapList;
        this.itemList = itemList;
    }

    private int lastY;
    private int canScrollDistance;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = (int) ev.getRawY();
            canScrollDistance = ((ViewGroup) getParent()).getChildAt(0).getHeight();
            Log.e("distance", "Distance : " + canScrollDistance);
            return super.onTouchEvent(ev);
        }
        int currentY = (int) ev.getRawY();
        Log.e("distance", "currentY : " + currentY + " , lastY : " + lastY);
        if (currentY < lastY) {
            View view = ((ViewGroup) getParent());
            if (view.getScrollY() < canScrollDistance) {
                view.scrollBy(0, (int) (lastY - currentY));
                if (view.getScrollY() > canScrollDistance) {
                    view.scrollTo(0, canScrollDistance);
                }
                lastY = currentY;
                return true;
            }

        } else if (isTop(tapList) || isTop(itemList)) {
            Log.e("distance", "last***************** ");
            View view = ((ViewGroup) getParent());
            if (view.getScrollY() > 0) {
                view.scrollBy(0, (int) (lastY - currentY));
                lastY = currentY;
                if (view.getScrollY() < 0) {
                    view.scrollTo(0, 0);
                }
                return true;
            }
        }
        lastY = (int) ev.getRawY();
        return super.onTouchEvent(ev);
    }

    private boolean isTop(ListView list) {
        return list.getFirstVisiblePosition() == 0 && getChildAt(0).getTop() == 0;
    }

}
