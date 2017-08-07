package com.newland.payment.ui.view.wheelview;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.newland.payment.R;

/**
 * 年月选择控件
 *
 * @author CB
 * @2015年4月16日 @下午8:51:53
 */
public class DateWheelView extends FrameLayout {
	/** 年 */
	private WheelView wwYear;
	/** 月 */
	private WheelView wwMouth;
	private OnDateWheelViewChangeListener onDateWheelViewChangeListener;
	private long date;

	public DateWheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public DateWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DateWheelView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		initView(context);
		initDate();
		initEvent();
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.date_wheel_view, this, true);

		wwYear = (WheelView) view.findViewById(R.id.ww_year);
		wwMouth = (WheelView) view.findViewById(R.id.ww_mouth);

	}

	protected void initDate() {

		wwYear.setAdapter(new NumericWheelAdapter(2015, 2050));
		wwMouth.setAdapter(new NumericWheelAdapter(1, 12));
		wwYear.setCyclic(true);
		wwMouth.setCyclic(true);
		
		date = getDate();
	}
	
	public long getDate() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(wwYear.getCurrentItem() + 2015,wwMouth.getCurrentItem(),1);
		return calendar.getTimeInMillis();
	}

	private void initEvent() {
		wwYear.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(newValue + 2015,wwMouth.getCurrentItem(),1);
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, calendar.getTimeInMillis());
					date = calendar.getTimeInMillis();
				}
			}
		});
		wwMouth.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(wwYear.getCurrentItem() + 2015,newValue,1);
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, calendar.getTimeInMillis());
					date = calendar.getTimeInMillis();
				}
			}
		});
	}
	
	/**
	 * 添加监听
	 * @param onDateWheelViewChangeListener
	 */
	public void addOnDateWheelViewChangeListener(OnDateWheelViewChangeListener onDateWheelViewChangeListener) {
		this.onDateWheelViewChangeListener = onDateWheelViewChangeListener;
	}
	
}
