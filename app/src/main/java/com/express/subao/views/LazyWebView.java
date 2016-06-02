package com.express.subao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class LazyWebView extends VestrewWebView {

	public LazyWebView(Context context) {
		super(context);
	}

	public LazyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LazyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				View.MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
