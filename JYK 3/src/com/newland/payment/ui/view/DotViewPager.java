package com.newland.payment.ui.view;

import java.util.Timer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newland.base.util.DisplayUtils;
import com.newland.payment.R;

/**
 * 带点的viewpager
 * 
 * @author CB
 * @time 2015年1月21日 上午9:27:56
 */
public class DotViewPager extends FrameLayout{
	
	/** viewpager */
//	@ViewInject(R.id.view_pager)
	private ViewPager viewPager;
	/** 小圆点 */
//	@ViewInject(R.id.ll_dot)
	private LinearLayout llDot;
	
	private Context context;
/*	*//** 第几张 *//*
	private int pagerIndex = 0;*/
	/** 轮播线程 */
	private Timer timer;
	/** 选中时的资源 **/
	private int resCurrentDot = R.drawable.common_dot_current;
	/** 选中时的资源 **/
	private int resNormalDot = R.drawable.common_dot_ever;
	
	private PagerAdapter pagerAdapter;

	public DotViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	public DotViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public DotViewPager(Context context) {
		super(context);
		initData(context);
	}
	
	
	
	private void initData(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.dot_view_pager, this);
		ViewUtils.inject(this, view);
		viewPager.setOffscreenPageLimit(999);
		viewPager.setCurrentItem(1);
		initEvent();
	}

	private void initEvent() {

		// 翻页事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				selDotStyle(position);
		
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}

	public void setAdapter(PagerAdapter pagerAdapter,int resDotSize) {
		if (pagerAdapter != null) {
			viewPager.setAdapter(pagerAdapter);
			this.pagerAdapter = pagerAdapter;
			notifyDot();
		}
	}
	public void setAdapter(PagerAdapter pagerAdapter) {
		setAdapter(pagerAdapter, -1);
	}
	
	/**
	 * 更新小圆点
	 * @param resDotSize
	 */
	public void notifyDot() {
		notifyDot(-1);
	}
	/**
	 * 更新小圆点
	 * @param resDotSize
	 */
	public void notifyDot(int resDotSize) {


		int dotSize = DisplayUtils.getDimensPx(context, resDotSize == -1 ? R.dimen.dp16 : resDotSize);
		int dotPaddingSize = DisplayUtils.dip2px(context,context.getResources().getDimension(R.dimen.dp27));

		
		// 动态添加小圆点
		llDot.removeAllViews();
		for (int i = 0, count = this.pagerAdapter.getCount(); i < count; i++) {
			ImageView ivDot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
			params.leftMargin = dotPaddingSize;
			params.rightMargin = dotPaddingSize;
			ivDot.setLayoutParams(params);
			ivDot.setScaleType(ScaleType.FIT_XY);
			llDot.addView(ivDot);
		}
		selDotStyle(0);
		
		if (this.pagerAdapter.getCount() == 1) {
			llDot.setVisibility(View.GONE);
		} else {
			llDot.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 设置小圆点样式
	 * @param resCurrent
	 * @param resNormal
	 */
	public void selDotResource(int resCurrent,int resNormal) {
		resCurrentDot = resCurrent;
		resNormalDot = resNormal;
	}
	
	/**
	 * 设置小圆点位置
	 * @param gravity
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setDotPosition(int gravity, int left, int top, int right, int bottom) {
		LayoutParams params = (LayoutParams) llDot.getLayoutParams();
		params.gravity = gravity;
		params.setMargins(left, top, right, bottom);
		llDot.setLayoutParams(params);
	}
	
	/**
	 * 设置小圆点位置
	 * @param gravity
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setViewPagerPosition(int left, int top, int right, int bottom) {
		LayoutParams params = (LayoutParams) viewPager.getLayoutParams();
		params.setMargins(left, top, right, bottom);
		viewPager.setLayoutParams(params);
	}
	
	/**
	 * 设置选中小圆点样式
	 * @param position
	 */
	private void selDotStyle(int position){
		for (int i = 0, count = llDot.getChildCount(); i < count; i++) {
			ImageView ivDot = (ImageView) llDot.getChildAt(i);
			ivDot.setBackgroundResource(i==position ? resCurrentDot : resNormalDot);
		}
	}
	
	
	public void onDestroy() {
		timer.cancel();
		timer = null;
	}
	
	public int getCurrentItem() {
		return viewPager.getCurrentItem();
	}
}
