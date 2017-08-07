package com.newland.payment.ui.view.wheelview;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 年月选择控件
 *
 * @author CB
 * @2015年4月16日 @下午8:51:53
 */
public class DateWheelView2 extends FrameLayout {
	/** 年 */
	private WheelView wwYear;
	/** 月 */
	private WheelView wwMouth;
	/** 日 */
	private WheelView wwDay;
	/** 年 */
	private TextView txtYear;
	/** 月 */
	private TextView txtMouth;
	/** 日 */
	private TextView txtDay;
	private OnDateWheelViewChangeListener onDateWheelViewChangeListener;

	private NumericWheelAdapter yearAdapter;
	private NumericWheelAdapter mouthAdapter;
	private NumericWheelAdapter dayAdapter;
	
	
	private long date;
	private int minYear;
	private int maxYear;
	private int minMouth;
	private int maxMouth;
	private int minDay;
	private int maxDay;

	public DateWheelView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public DateWheelView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DateWheelView2(Context context) {
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
				R.layout.date_wheel_view_2, this, true);

		wwYear = (WheelView) view.findViewById(R.id.ww_year);
		wwMouth = (WheelView) view.findViewById(R.id.ww_mouth);
		wwDay = (WheelView) view.findViewById(R.id.ww_day);
		txtYear = (TextView) view.findViewById(R.id.txt_year);
		txtMouth = (TextView) view.findViewById(R.id.txt_mouth);
		txtDay = (TextView) view.findViewById(R.id.txt_day);

	}

	protected void initDate() {
		
		minYear = 2015;
		maxYear = 2050;
		minMouth = 1;
		maxMouth = 12;
		minDay = 1;
		maxDay = 31;
		
		yearAdapter = new NumericWheelAdapter(minYear, maxYear);
		mouthAdapter = new NumericWheelAdapter(minMouth, maxMouth);
		dayAdapter = new NumericWheelAdapter(minDay, maxDay);
		
		wwYear.setAdapter(yearAdapter);
		wwMouth.setAdapter(mouthAdapter);
		wwDay.setAdapter(dayAdapter);
		
		wwYear.setCyclic(true);
		wwMouth.setCyclic(true);
		wwDay.setCyclic(true);
		
		date = getDate();
	}
	
	public long getDate() {
		
		Calendar calendar = Calendar.getInstance();

		calendar.set(Integer.valueOf(yearAdapter.getItem(wwYear.getCurrentItem())), 
				Integer.valueOf(mouthAdapter.getItem(wwMouth.getCurrentItem())) - 1, 
				Integer.valueOf(dayAdapter.getItem(wwDay.getCurrentItem())));
		
		return calendar.getTimeInMillis();
	}

	private void initEvent() {
		wwYear.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				LoggerUtils.i(newValue+"");
				Calendar calendar = Calendar.getInstance();
				calendar.set(Integer.valueOf(yearAdapter.getItem(newValue)), 
						Integer.valueOf(mouthAdapter.getItem(wwMouth.getCurrentItem())), 
						Integer.valueOf(dayAdapter.getItem(wwDay.getCurrentItem())));
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, getDate());
					date = getDate();
					LoggerUtils.i("date:"+date);
				}

				dayAdapter = new NumericWheelAdapter(minDay, getMaxDay());
				wwDay.setAdapter(dayAdapter);
				
			}
		});
		wwMouth.addChangingListener(new OnWheelChangedListener() {
			 
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, getDate());
					date = getDate();
					LoggerUtils.i("date:"+date);
				}
				
				dayAdapter = new NumericWheelAdapter(minDay, getMaxDay());
				wwDay.setAdapter(dayAdapter);
			}
		});
		wwDay.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				if (onDateWheelViewChangeListener != null) {
					onDateWheelViewChangeListener.onChanged(date, getDate());
					date = getDate();
					LoggerUtils.i("date:"+TimeUtils.getFormatTime(date));
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
	
	/**
	 * 根据年和月找出当月的日期
	 * @param year
	 * @param mouth
	 */
	private int getMaxDay() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Integer.valueOf(yearAdapter.getItem(wwYear.getCurrentItem())), 
				Integer.valueOf(mouthAdapter.getItem(wwMouth.getCurrentItem())) - 1, 
				Integer.valueOf(dayAdapter.getItem(wwDay.getCurrentItem())));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public void setDate(Long date) {
		LoggerUtils.i("setDate:"+TimeUtils.getFormatTime(date));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		LoggerUtils.i("YEAR:" + calendar.get(Calendar.YEAR) 
				+ "MONTH:" + calendar.get(Calendar.MONTH)
				+ "DAY_OF_MONTH:" + calendar.get(Calendar.DAY_OF_MONTH));
		wwYear.setCurrentItem(calendar.get(Calendar.YEAR)-minYear);
		wwMouth.setCurrentItem(calendar.get(Calendar.MONTH) + 1 -minMouth);
		wwDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH)-minDay); 
	}
	
	public void setNowDate() {
		setDate(System.currentTimeMillis());
	}

	public void setIsShowYear(boolean isShow) {
		wwYear.setVisibility(isShow ? View.VISIBLE : View.GONE);
		txtYear.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	public void setIsShowMouth(boolean isShow) {
		wwMouth.setVisibility(isShow ? View.VISIBLE : View.GONE);
		txtMouth.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	public void setIsShowDay(boolean isShow) {
		wwDay.setVisibility(isShow ? View.VISIBLE : View.GONE);
		txtDay.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	
	public void setAvailableYear(int min,int max) {
		minYear = min;
		maxYear = max;
		yearAdapter = new NumericWheelAdapter(minYear, maxYear);
		wwYear.setAdapter(yearAdapter);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		wwYear.setCurrentItem(calendar.get(Calendar.YEAR)-minYear);
	}
	
}
