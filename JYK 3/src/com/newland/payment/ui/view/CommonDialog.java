package com.newland.payment.ui.view;

import java.util.concurrent.atomic.AtomicInteger;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.base.util.DisplayUtils;
import com.newland.payment.R;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 通用提示选择dialog
 * 
 * @author CB
 * @date 2015-5-19
 * @time 下午7:48:22
 */
public class CommonDialog extends Dialog {

	public static enum TimeOutOper{
		NONE,//不进行任何操作
		CANCEL,//取消
		SURE,//确定
		MIDDLE,//中键
	}
	private View view;
	private LinearLayout llMain;
	private TextView txtTitle;
	private TextView txtContent;
	private TextView txtCancel;
	private TextView txtSure;
	private TextView txtMiddle;
	private ImageView ivLine;
	private ImageView ivLineMiddle;

	private Context context;
	/**超时以秒为单位*/
	private int defaultTimeOut = -1;
	/**当前计数器*/
	private AtomicInteger currentCount = new AtomicInteger(defaultTimeOut);
	/**超时以秒为单位*/
	private TimeOutOper timeOutOper = TimeOutOper.NONE;

	public CommonDialog(Context context) {
		super(context);
//		super(context, R.style.common_full_dialog);
		this.context = context;

		view = View.inflate(context, R.layout.common_dialog_view, null);
		llMain = (LinearLayout) view.findViewById(R.id.ll_main);
		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		txtContent = (TextView) view.findViewById(R.id.txt_content);
		txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
		txtSure = (TextView) view.findViewById(R.id.txt_sure);
		txtMiddle = (TextView) view.findViewById(R.id.txt_middle);
		ivLine = (ImageView) view.findViewById(R.id.iv_line);
		ivLineMiddle = (ImageView) view.findViewById(R.id.iv_line_middle);
	}
	public CommonDialog(Context context, int timeOut, TimeOutOper timeOutOper){
		this(context);
		defaultTimeOut = timeOut;
		currentCount = new AtomicInteger(defaultTimeOut);
		this.timeOutOper = timeOutOper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		
		//关闭软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setBackgroundDrawableResource(R.color.transparent);
		setCancelable(false);
		// APP 获取 HOME keyEvent的设定关键代码。
		getWindow().addFlags(3);
		getWindow().addFlags(5);
//		if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
//			getWindow().addFlags(3);
//		} else {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//		}
		setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dg, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME) {
					return true;
				} 
				return false;
			}
		});
		
		this.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				view.postInvalidate();
				startCountDown();
			}
		});
	}

	/**
	 * 取消按钮监听器
	 * 
	 * @param resCancelText
	 * @param listenerCancel
	 */
	public void setCancelListener(int resCancelText,
			final View.OnClickListener listenerCancel) {

		if (listenerCancel == null) {
			ivLine.setVisibility(View.GONE);
			txtCancel.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtCancel.setVisibility(View.VISIBLE);
			txtCancel.setText(resCancelText);
			txtCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerCancel.onClick(v);
				}
			});
		}
	}

	public void setCancelListener(int resCancelText,
			final View.OnClickListener listenerCancel,int size) {

		if (listenerCancel == null) {
			ivLine.setVisibility(View.GONE);
			txtCancel.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtCancel.setVisibility(View.VISIBLE);
			txtCancel.setText(resCancelText);
			txtCancel.setTextSize(size);
			txtCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerCancel.onClick(v);
				}
			});
		}
	}
	
	/**
	 * 中间按钮监听器
	 * 
	 * @param resMiddelText
	 * @param listenerMiddle
	 */
	public void setMiddleListener(int resMiddelText,
			final View.OnClickListener listenerMiddle) {

		if (listenerMiddle == null) {
			ivLineMiddle.setVisibility(View.GONE);
			txtMiddle.setVisibility(View.GONE);
		} else {
			ivLineMiddle.setVisibility(View.VISIBLE);
			txtMiddle.setVisibility(View.VISIBLE);
			txtMiddle.setText(resMiddelText);
			txtMiddle.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerMiddle.onClick(v);
				}
			});
		}
	}

	/**
	 * 确定按钮监听器
	 * 
	 * @param resSureText
	 * @param listenerSure
	 */
	public void setSureListener(int resSureText,
			final View.OnClickListener listenerSure) {

		if (listenerSure == null) {
			ivLine.setVisibility(View.GONE);
			txtSure.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtSure.setVisibility(View.VISIBLE);
			txtSure.setText(resSureText);
			txtSure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerSure.onClick(v);
				}
			});
		}
	}
	
	/**
	 * 确定按钮监听器
	 * 
	 * @param resSureText
	 * @param listenerSure
	 */
	public void setSureListener(int resSureText,
			final View.OnClickListener listenerSure,int size) {

		if (listenerSure == null) {
			ivLine.setVisibility(View.GONE);
			txtSure.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtSure.setVisibility(View.VISIBLE);
			txtSure.setText(resSureText);
			txtSure.setTextSize(size);
			txtSure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerSure.onClick(v);
				}
			});
		}
	}
	private Thread countDownThread = null;
	private void startCountDown(){
		synchronized (this) {
			if (countDownThread != null) {
				countDownThread.interrupt();
				countDownThread = null;
			}
			if (defaultTimeOut < 0) {
				return;
			}
			countDownThread = new Thread(){
				public void run() {
					while(true){
						if (!this.equals(countDownThread)) {
							LoggerUtils.d("countDownThread not equal this");
							return;
						}
						int now = currentCount.getAndDecrement();
						//LoggerUtils.d("countDownThread:" + now);
						if (now > 0) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								LoggerUtils.d("countDownThread InterruptedException now:" + now);
								break;
							}
						} else {
							if (now == 0) {
								onTimeOut();
							}
							break;
						}
					}
				};
			};
			countDownThread.start();
		}
	}
	private void stopCountDown(){
		synchronized (this) {
			if (countDownThread != null) {
				countDownThread.interrupt();
				countDownThread = null;
			}
		}
	}
	private void resetTimeOut(){
		if (currentCount != null) {
			currentCount.set(defaultTimeOut);
		}
	}
	private void onTimeOut(){
		LoggerUtils.d("CommonDialog onTimeOut");
		//must be run on main thread ??
		view.post(new Runnable() {
			@Override
			public void run() {
				switch(timeOutOper){
				case CANCEL:
					if (txtCancel.getVisibility() == View.VISIBLE) {
						txtCancel.callOnClick();
					}
					break;
				case MIDDLE:
					if (txtMiddle.getVisibility() == View.VISIBLE) {
						txtMiddle.callOnClick();
					}
					break;
				case SURE:
					if (txtSure.getVisibility() == View.VISIBLE) {
						txtSure.callOnClick();
					}
					break;
				case NONE:
					break;
				default:
					break;
				}
			}
		});
	}
	@Override
	public void dismiss() {
		stopCountDown();
		super.dismiss();
	}

	public void setTitle(int resTitle) {
		if (resTitle != -1) {
			txtTitle.setText(resTitle);
		}
	}

	public void setTitle(String title) {
		if (title != null) {
			txtTitle.setText(title);
		}
	}

	public void setContent(String content) {
		if (content != null) {
			txtContent.setText(content);
		}

	}

	public void setContent(int resContent) {
		if (resContent != -1) {
			txtContent.setText(resContent);
		}

	}

	/**
	 * 设置dialog的宽度
	 * 
	 * @param resDp
	 *            资源文件中dp的源
	 */
	public void setWidth(int resDp) {
		FrameLayout.LayoutParams params = (LayoutParams) llMain
				.getLayoutParams();
		params.width = DisplayUtils.getDimensPx(context, resDp);
	}
	
}
