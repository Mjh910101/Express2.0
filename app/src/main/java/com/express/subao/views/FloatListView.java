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

        Log.e("distance", "******************** 1 *********************");

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = (int) ev.getRawY();
//            canScrollDistance = ((ViewGroup) getParent()).getChildAt(0).getHeight();

            Log.e("distance", "Distance : " + canScrollDistance);

            return super.onTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View view = scrollParent;
            if (view.getScrollY() <= 0) {
                lastY = 0;
            } else {
                lastY = (int) ev.getRawY();
            }
            return super.onTouchEvent(ev);
        }
        int currentY = (int) ev.getRawY();

        Log.e("distance", "currentY : " + currentY + " , lastY : " + lastY);

        if (currentY < lastY) {

            Log.e("distance", "******************** currentY < lastY *********************");

//            View view = ((ViewGroup) getParent());
            View view = scrollParent;
            if (view.getScrollY() < canScrollDistance) {

                Log.e("distance", "scrollBy : " + (lastY - currentY));

                view.scrollBy(0, (int) (lastY - currentY));
                if (view.getScrollY() > canScrollDistance) {

                    Log.e("distance", "scrollTo : " + canScrollDistance);

                    view.scrollTo(0, canScrollDistance);
                }
                lastY = currentY;
                return true;
            }

        } else if (getFirstVisiblePosition() == 0) {

            Log.e("distance", "******************** currentY > lastY *********************");

            View child = getChildAt(0);
            if (child != null) {
                if (child.getTop() == 0) {
                    //            View view = ((ViewGroup) getParent());
                    View view = scrollParent;

                    Log.e("distance", "ScrollY : " + view.getScrollY());

                    if (view.getScrollY() > 0) {

                        Log.e("distance", "scrollBy : " + (lastY - currentY));

                        view.scrollBy(0, (int) (lastY - currentY));
                        lastY = currentY;
                        if (view.getScrollY() < 0) {

                            Log.e("distance", "scrollTo : " + 0);

                            view.scrollTo(0, 0);
                        }
                        return true;
                    }
                }
            }
        }
        lastY = (int) ev.getRawY();
        return super.onTouchEvent(ev);
    }

}
