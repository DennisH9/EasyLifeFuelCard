package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 可控制是否滚动的scrollview
 *
 * @author CB
 * @time 2015-4-29 上午10:10:30
 */
public class CustomScrollView extends ScrollView{
	
	private boolean isScroll = false;

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}

	public CustomScrollView(Context context) {
		super(context);
		initData();
	}
	
	private void initData() {
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return false;
			}
		});
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
