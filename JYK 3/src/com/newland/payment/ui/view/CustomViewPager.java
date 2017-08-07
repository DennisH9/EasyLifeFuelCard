package com.newland.payment.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager{

	private boolean isScroll = false;
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomViewPager(Context context) {
		super(context);
	}

	/**
	 * 取消滚动
	 */
	public void stopScroll() {
		isScroll = false;
	}
	/**
	 * 开启滚动
	 */
	public void startScroll() {
		isScroll = true;
	}
	
	public boolean isScroll() {
		return isScroll;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return isScroll ? super.onInterceptTouchEvent(ev) : false;
	}

}
