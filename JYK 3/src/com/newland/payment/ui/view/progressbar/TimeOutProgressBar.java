package com.newland.payment.ui.view.progressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.LoggerUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 自定义超时进度条的控件
 * @author linchunhui
 * @date 2015年5月23日 上午11:30:47
 *
 */
public class TimeOutProgressBar extends FrameLayout {

//	/** 进度条背景 */
//	@ViewInject(R.id.fl_bg)
//	FrameLayout flBg;
	/** 进度条值 */
//	@ViewInject(R.id.iv_value)
	ImageView ivValue;
	
	/** 超时监听 */
	private TimeOutProgressBarListener listener;

	/** 数据线程池 */
	private ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
	@SuppressWarnings("rawtypes")
	/** 数据线程 */
	private ScheduledFuture future;
	/** UI线程 */
	@SuppressWarnings("rawtypes")
	private ScheduledFuture futureUi;
	/** 进度条当前宽度 */
	private int value = 0;
	/** 进度条最大宽度 */
	private int maxValue;
	private FrameLayout.LayoutParams params;
	/** 超时时间 */
	private long outTime;
	
	/** 是否暂停 */
	private boolean isPause = false;
	/** 进度条刷新时间 */
	private int refraeshTiem = 1000;
	
	private long speed;

	public TimeOutProgressBar(Context context) {
		super(context);
		init(context);
	}
	
	public TimeOutProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TimeOutProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {

		View view = View.inflate(context, R.layout.time_out_progress_bar, this);
		ViewUtils.inject(this, view);
		ivValue = (ImageView) view.findViewById(R.id.iv_value);
		maxValue = (int) context.getResources().getDimension(App.SCREEN_TYPE == Const.ScreenType.IM_81 ? R.dimen.width_81 : R.dimen.width_91);
		
	}
	
	/**
	 * 设置超时时间
	 * @param time
	 */
	public void setTimeOut(long time, TimeOutProgressBarListener listener) {
		
		this.outTime = time;
		this.listener = listener;
		value = maxValue;
	}
	
	/**
	 * 设置值的宽度
	 * @param width
	 */
	private void setValue(int width) {
		params = (LayoutParams) ivValue.getLayoutParams();
		params.width = width;
		ivValue.setLayoutParams(params);
	}
	
	/**
	 * 开始计时
	 */
	public void start() {

		LoggerUtils.i("TimeOutProgressBar start: " + outTime + ",maxValue:" + maxValue);
		
		if (outTime < 1) {
			//设置小于1的超时时间则为关闭
			return;
		}
		
		speed = (long) (maxValue * 1.0 / outTime * refraeshTiem);
		
		LoggerUtils.i("TimeOutProgressBar speed:" + speed);
		
		if (future != null) {
			future.cancel(true);
		}
		if (futureUi != null) {
			futureUi.cancel(true);
		}
		
		if (speed < 1) {
			speed = 1;
		}
			future = service.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					
					if (!isPause) {

						value -= speed;
						
						if (value <= 0) {

							if (listener != null) {
								listener.timeout();
								value = maxValue;
							}
						}
					}
					
					
				}
			}, 0,  refraeshTiem, TimeUnit.MILLISECONDS);
			
			futureUi = service.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {

					MainActivity.getInstance().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {

							setValue(value);
						}
					});
				}
			}, 0,  1000, TimeUnit.MILLISECONDS);
		
	}

	/**
	 * 关闭进度条
	 */
	public void close() {
		LoggerUtils.i("TimeOutProgressBar close ");
		if (future != null) {
			future.cancel(true);
		}
		if (futureUi != null) {
			futureUi.cancel(true);
		}
	}

	/**
	 * 超时时间
	 * @return
	 */
	public long getTimeOut() {
		return outTime;
	}
	
	/**
	 * 重置进度条
	 */
	public void resetProgress() {
		value = maxValue;
	}
	
	public void pause() {
		isPause = true;
	}
	
	public void resume() {
		isPause = false;
	}
}
