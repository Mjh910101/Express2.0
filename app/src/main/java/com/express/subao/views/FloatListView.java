package com.express.subao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
public class FloatListView extends ListView {
    public FloatListView(Context context) {
        super(context);
    }

    public FloatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public FloatListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int lastY;
    private int canScrollDistance;

    private ViewGroup scrollParent;

    public void setScrollParent(ViewGroup viewGroup) {
        scrollParent = viewGroup;
    }

    public void setScrollParentScrollDistance(int distance) {
        canScrollDistance = distance;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = (int) ev.getRawY();
//            canScrollDistance = ((ViewGroup) getParent()).getChildAt(0).getHeight();
            Log.e("distance", "Distance : " + canScrollDistance);
            return super.onTouchEvent(ev);
        }
        int currentY = (int) ev.getRawY();
        Log.e("distance", "currentY : " + currentY + " , lastY : " + lastY);
        if (currentY < lastY) {
//            View view = ((ViewGroup) getParent());
            View view = scrollParent;
            if (view.getScrollY() < canScrollDistance) {
                view.scrollBy(0, (int) (lastY - currentY));
                if (view.getScrollY() > canScrollDistance) {
                    view.scrollTo(0, canScrollDistance);
                }
                lastY = currentY;
                return true;
            }

        } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() == 0) {
//            View view = ((ViewGroup) getParent());
            View view = scrollParent;
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

}
