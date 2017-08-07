package com.newland.payment.ui.view.wheelview;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.newland.payment.R;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 时间选择控件
 *
 * @author CB
 * @2015年4月16日 @下午8:51:53
 */
public class TimeWheelView extends FrameLayout {
	/** 时 */
	private WheelView wwHour;
	/** 分 */
	private WheelView wwMinute;
	/** 秒 */
	private WheelView wwSecond;
	private OnDateWheelViewChangeListener onDateWheelViewChangeListener;

	private NumericWheelAdapter hourAdapter;
	private NumericWheelAdapter minuteAdapter;
	private NumericWheelAdapter secondAdapter;
	
	
	private long date;
	private int minHour;
	private int maxHour;
	private int minMinute;
	private int maxMinute;
	private int minSecond;
	private int maxSecond;

	public TimeWheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TimeWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TimeWheelView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		initView(context);
		initData();
		initEvent();
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.time_wheel_view, this, true);

		wwHour = (WheelView) view.findViewById(R.id.ww_hour);
		wwMinute = (WheelView) view.findViewById(R.id.ww_minute);
		wwSecond = (WheelView) view.findViewById(R.id.ww_second);

	}

	protected void initData() {

		minHour = 0;
		maxHour = 23;
		minMinute = 0;
		maxMinute = 59;
		minSecond = 0;
		maxSecond = 23;
		
		hourAdapter = new NumericWheelAdapter(minHour, maxHour);
		minuteAdapter = new NumericWheelAdapter(minMinute, maxMinute);
		secondAdapter = new NumericWheelAdapter(minSecond, maxSecond);
		
		wwHour.setAdapter(hourAdapter);
		wwMinute.setAdapter(minuteAdapter);
		wwSecond.setAdapter(secondAdapter);
		
		wwHour.setCyclic(true);
		wwMinute.setCyclic(true);
		wwSecond.setCyclic(true);
		
		date = getDate();
		setNowDateToView();
	}
	
	public long getDate() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hourAdapter.getItem(wwHour.getCurrentItem())));
		calendar.set(Calendar.MINUTE, Integer.valueOf(minuteAdapter.getItem(wwMinute.getCurrentItem())));
		calendar.set(Calendar.SECOND, Integer.valueOf(secondAdapter.getItem(wwSecond.getCurrentItem())));
		
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取时间字符串
	 * @return
	 */
	public String getTimeString () {
		return TimeUtils.getFormatTime(getDate(), "hhmmss");
	}

	private void initEvent() {
		wwHour.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Integer.valueOf(hourAdapter.getItem(newValue)), 
						Integer.valueOf(minuteAdapter.getItem(wwMinute.getCurrentItem())), 
						Integer.valueOf(secondAdapter.getItem(wwSecond.getCurrentItem())));
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, calendar.getTimeInMillis());
					date = calendar.getTimeInMillis();
					LoggerUtils.i("date:"+date);
				}
				
			}
		});
		wwMinute.addChangingListener(new OnWheelChangedListener() {
			 
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, getDate());
					date = getDate();
					LoggerUtils.i("date:"+date);
				}
				
			}
		});
		wwSecond.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, getDate());
					date = getDate();
					LoggerUtils.i("date:"+date);
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
	

	
	private void setDateToView() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		wwHour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		wwMinute.setCurrentItem(calendar.get(Calendar.MINUTE));
		wwSecond.setCurrentItem(calendar.get(Calendar.SECOND)); 
	}
	
	private void setNowDateToView() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		wwHour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		wwMinute.setCurrentItem(calendar.get(Calendar.MINUTE));
		wwSecond.setCurrentItem(calendar.get(Calendar.SECOND)); 
	}

	/**
	 * 设置年月日
	 */
	public void setDate(int year, int mouth, int day){

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		calendar.set(year, mouth, day);
		date = calendar.getTimeInMillis();
	}
	/**
	 * 设置年月日
	 */
	public void setDate(long date){
		this.date = date;
		setDateToView();
	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
}
