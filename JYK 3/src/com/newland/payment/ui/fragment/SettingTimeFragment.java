package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.ui.listener.LongValueChangeListener;
import com.newland.payment.ui.view.SettingDateView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 设置系统时间界面
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午2:43:56
 */
public class SettingTimeFragment extends BaseSettingFragment{
	
	/** 日期 */
//	@ViewInject(R.id.sdv_date)
	SettingDateView sdvDate;
	/** 时间 */
//	@ViewInject(R.id.sdv_time)
	SettingDateView sdvTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_time_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		setTitle(R.string.setting_system_now_time);
		
		sdvDate.setTitle(R.string.setting_system_now_date);
		sdvTime.setTitle(R.string.setting_system_now_time);

		sdvDate.setContent(TimeUtils.getFormatTime(System.currentTimeMillis(), "yyyy-MM-dd"));
		sdvTime.setContent(TimeUtils.getFormatTime(System.currentTimeMillis(), "HH:mm:ss"));
		
		sdvTime.setTimeStyle();
		
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		sdvDate.setValueChangeListener(new LongValueChangeListener() {

			@Override
			public void onChange(long value) {
				LoggerUtils.i("time:" + TimeUtils.getFormatTime(value));
 				SystemClock.setCurrentTimeMillis(value);
				TimeUtils.updateSystemTime(
						TimeUtils.getFormatTime(value, "MMdd"),
						TimeUtils.getFormatTime(value, "HHmmss"),
						Integer.valueOf(TimeUtils.getFormatTime(value, "yyyy")));
			}
		});
		sdvTime.setValueChangeListener(new LongValueChangeListener() {

			@Override
			public void onChange(long value) {
				LoggerUtils.i("time:" + TimeUtils.getFormatTime(value));
				SystemClock.setCurrentTimeMillis(value);
				TimeUtils.updateSystemTime(
						TimeUtils.getFormatTime(value, "MMdd"),
						TimeUtils.getFormatTime(value, "HHmmss"),
						Integer.valueOf(TimeUtils.getFormatTime(value, "yyyy")));
			}
		});
	}

	@Override
	public void onFragmentHide() {
		sdvDate.dismissDialog();
		sdvTime.dismissDialog();
		super.onFragmentHide();
	}

	
}
