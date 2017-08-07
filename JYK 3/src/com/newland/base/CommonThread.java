package com.newland.base;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.newland.payment.interfaces.ThreadCallBack;

/**
 * 自定义线程
 * 
 * @author CB
 * @time 2014年12月25日 下午5:41:52
 */
@SuppressLint("HandlerLeak")
public class CommonThread {
	
	private Thread thread;
	private boolean isRuning = false;
	/** 是否只执行一次 */
	private boolean isOnce = true;
	private ThreadCallBack threadCallBack;
	/** 时间间隔 */
	private int interval = -1;
	private Object lock = new Object();
	private boolean isAsync = false;
	/** 是否启动 */
	private boolean isStart = false;
	
	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			CommonThread.this.threadCallBack.onMain();
			if (isAsync) {
				synchronized (lock) {
					try {
						lock.notify();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * 构造函数,默认执行一次
	 * @param thraedCallBack
	 */
	public CommonThread(ThreadCallBack thraedCallBack) {
		this(true,-1,thraedCallBack);
	}
	
	/**
	 * 构造函数
	 * @param isOnce 是否执行一次
	 * @param thraedCallBack
	 * @param interval 时间间隔，-1为无间隔(单位：毫秒)
	 */
	public CommonThread(boolean isOnce, int interval, 
			ThreadCallBack thraedCallBack) {
		this(isOnce, interval, false, thraedCallBack);
	}
	
	/**
	 * 构造函数
	 * @param isOnce 是否执行一次
	 * @param thraedCallBack
	 * @param interval 时间间隔，-1为无间隔(单位：毫秒)
	 */
	public CommonThread(boolean isOnce, int interval, boolean isAsync,
			ThreadCallBack thraedCallBack) {
		this.isOnce = isOnce;
		this.isAsync = isAsync;
		this.threadCallBack = thraedCallBack;
		this.interval = interval;
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					do {
						if (isRuning) {
							if (CommonThread.this.threadCallBack != null) {
								CommonThread.this.threadCallBack.onBackGround();
								
								//耗时操作结束后再次判断
								if (isRuning) {
									if (CommonThread.this.isAsync) {
										synchronized (lock) {
											handler.sendEmptyMessage(0);
											try {
												lock.wait();
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									} else {
										handler.sendEmptyMessage(0);
									}
								}
							}
							
							if (CommonThread.this.interval != -1) {
								try {
									Thread.sleep(CommonThread.this.interval);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						} else {
							Thread.sleep(100);
						}
					} while (!CommonThread.this.isOnce);
					
					//销毁线程
					thread = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		thread.setDaemon(true);
//		thread.start();
	}
	
	/**
	 * 开始
	 */
	public void start() {
		isRuning = true;
		if (!isStart) {
			thread.start();
			isStart = true;
		}
	}
	
	/**
	 * 暂停
	 */
	public void pause() {
		isRuning = false;
	}
	
	/**
	 * 停止，停止后线程销毁
	 */
	public void stop() {
		isRuning = false;
		isOnce = true;
		
	}
	
	/**
	 * 是否在运行
	 * @return
	 */
	public boolean isRunning() {
		return isRuning;
	}
	
	
}
