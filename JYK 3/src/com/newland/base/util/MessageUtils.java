package com.newland.base.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.UserService;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.LongValueChangeListener;
import com.newland.payment.ui.listener.OnListDialogClickListener;
import com.newland.payment.ui.view.CommonDialog;
import com.newland.payment.ui.view.CommonDialog.TimeOutOper;
import com.newland.payment.ui.view.CommonInputDialog;
import com.newland.payment.ui.view.CommonListDialog;
import com.newland.payment.ui.view.OperatorPasswordDialog;
import com.newland.payment.ui.view.OperatorPasswordDialog.InputEventListener;
import com.newland.payment.ui.view.wheelview.DateWheelView2;
import com.newland.payment.ui.view.wheelview.TimeWheelView;

public class MessageUtils {

	/**
	 * 普通信息提示框，有标题为"提示"，有一个确认按钮，一个取消按钮
	 * @param context 上下文
	 * @param message 提示框提示信息
	 * @param confirmLinstener 确认监听
	 * @param cancelLinstener 取消监听
	 * @return
	 */
	public static Dialog showTipDialog(Context context, CharSequence message,
			DialogInterface.OnClickListener confirmLinstener,
			DialogInterface.OnClickListener cancelLinstener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(
				R.string.common_tip_title));
		builder.setMessage(message);
		if (cancelLinstener != null)
			builder.setNegativeButton(
					context.getResources().getString(R.string.common_cancel),
					cancelLinstener);
		builder.setPositiveButton(
				context.getResources().getString(R.string.common_sure),
				confirmLinstener);
		Dialog dialog = builder.create();
		dialog.show();

		return dialog;
	}		
	
	/**
	 * 显示信息提示框，只有一个确认按钮
	 * @param context
	 * @param resId 提示字符串id
	 * @param listenerSure 
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			int resContent, OnClickListener listenerSure) {
		final CommonDialog commonDialog = showCommonDialog(context, null,
				context.getString(resContent), false, false, R.string.common_sure, -1,
				R.string.common_cancel, listenerSure, null,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
		return commonDialog;
	}
	
	/**
	 * 显示主管密码验证，验证通过弹出提示框
	 * @param context 上下文
	 * @param resId 提示字符串id
	 * @param isUseSafePassword 是否使用数字密码键盘，带主管密码验证功能
	 * @param listenerCancel 提示框确认键监听
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			int resContent, boolean isUseSafePassword, OnClickListener listenerSure) {
		
		final CommonDialog commonDialog = showCommonDialog(context, null,
				context.getString(resContent), isUseSafePassword, false,
				R.string.common_sure, -1, R.string.common_cancel, listenerSure,
				null, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
		return commonDialog;
	}
	
	/**
	 * 显示主管密码验证，验证通过弹出提示框
	 * @param context 上下文
	 * @param resId 提示字符串id
	 * @param isUseSafePassword 是否使用数字密码键盘，带主管密码验证功能
	 * @param listenerCancel 提示框确认键监听
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			int resContent, boolean isUseSafePassword,boolean isUseManagePassword, OnClickListener listenerSure) {
		
		final CommonDialog commonDialog = showCommonDialog(context, null,
				context.getString(resContent), isUseSafePassword, isUseManagePassword,
				R.string.common_sure, -1, R.string.common_cancel, listenerSure,
				null, new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
		return commonDialog;
	}
	
	/**
	 * 显示信息提示框，有两个按键
	 * @param context 上下文
	 * @param resId 提示信息资源id
	 * @param listenerSure 确认按钮
	 * @param listenerCancel 取消按钮
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			int resId, 
			OnClickListener listenerSure, 
			OnClickListener listenerCancel) {
		return showCommonDialog(context, context.getString(resId), listenerSure, listenerCancel);
	}
	/**
	 * 显示通用Dialog，不带标题，两个按钮
	 * @param context
	 * @param message 提示信息
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			CharSequence message, 
			OnClickListener listenerSure, 
			OnClickListener listenerCancel) {
		return showCommonDialog(context, null, message, false, false, R.string.common_sure, -1,
				R.string.common_cancel, listenerSure, null, listenerCancel);
		
	}
	

	/**
	 * 显示信息提示框，带标题，两个按钮
	 * @param context 上下文
	 * @param resTitle 标题资源id
	 * @param message 提示信息
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context, int resTitle,
			CharSequence message, 
			OnClickListener listenerSure, 
			OnClickListener listenerCancel) {
		return showCommonDialog(context, context.getString(resTitle), message, false, false, R.string.common_sure, -1,
				R.string.common_cancel, listenerSure, null, listenerCancel);
		
	}
	
	/**
	 * 显示信息提示框，不带标题，两个按钮，并且按钮文件可设置
	 * @param context 上下文
	 * @param message 提示信息
	 * @param resSureText 确认按钮位置显示提示文件
	 * @param resCancelText 取消按钮位置显示提示文件
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @return
	 */
	public static CommonDialog showCommonDialog(Context context,
			CharSequence message, int resSureText,int resCancelText,
			OnClickListener listenerSure, OnClickListener listenerCancel) {
		return showCommonDialog(context, null, message, false, false,resSureText, -1,
				resCancelText, listenerSure, null, listenerCancel);
	}

	/**
	 * 显示信息提示框，带标题，两个按钮
	 * @param context 上下文
	 * @param message 提示信息
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @return
	 */
	public static Dialog showCommonDialog(Context context, String title,
			CharSequence message, 
			OnClickListener listenerSure, 
			OnClickListener listenerCancel) {
		return showCommonDialog(context, title, message, false, false, R.string.common_sure, -1,
				R.string.common_cancel, listenerSure, null, listenerCancel);
		
	}
	
	/**
	 * 显示信息提示框，带标题，两个按钮
	 * @param context 上下文
	 * @param message 提示信息
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @param timeOut 超时
	 * @param timeOutOper 超时后的操作
	 * @return
	 */
	public static Dialog showCommonDialog(Context context, String title,
			CharSequence message, 
			OnClickListener listenerSure, 
			OnClickListener listenerCancel,
			int timeOut, TimeOutOper timeOutOper) {
		return showCommonDialog(context, title, message, false, false, R.string.common_sure, -1,
				R.string.common_cancel, listenerSure, null, listenerCancel, timeOut, timeOutOper);
		
	}
	

	public static CommonDialog showCommonDialog(final Context context, String title,
			CharSequence message, boolean isUseSafePassword, boolean isUseManagePassword, int resSureText, int resMiddelText, int resCancelText,
			OnClickListener listenerSure, OnClickListener listenerMiddle, 
			OnClickListener listenerCancel) {
		return showCommonDialog( context, title,
				message, isUseSafePassword, isUseManagePassword, 
				resSureText, resMiddelText, resCancelText,
				listenerSure, listenerMiddle, 
				listenerCancel,
				-1, TimeOutOper.NONE);
	}
	public static CommonDialog showCommonDialog(final Context context, String title,
			CharSequence message, boolean isUseSafePassword, boolean isUseManagePassword, int resSureText, int resMiddelText, int resCancelText,
			OnClickListener listenerSure, OnClickListener listenerMiddle, 
			OnClickListener listenerCancel,
			int timeOut, TimeOutOper timeOutOper) {
		final CommonDialog commonDialog = getCommonDialog(context, title, message, resSureText, resMiddelText, resCancelText, listenerSure, listenerMiddle, listenerCancel, timeOut, timeOutOper);
		OperatorPasswordDialog operatorPasswordDialog;
		if (isUseSafePassword) {
			operatorPasswordDialog = new OperatorPasswordDialog(context,R.style.swiping_dialog,6,R.string.pls_input_main_password,new InputEventListener() {
				
				@Override
				public void onConfirm(Dialog dialog, String value) {
					OperatorPasswordDialog operatorPasswordDialog = (OperatorPasswordDialog) dialog;
					if (operatorPasswordDialog.getPassword().equals(ParamsUtils.getString(ParamsConst.PARAMS_SAFE_PASSWORD))) {
						operatorPasswordDialog.dismiss();
						commonDialog.show();
						MainActivity.getInstance().commonDialog = commonDialog;
					} else {
						operatorPasswordDialog.dismiss();
						ToastUtils.show(MainActivity.getInstance(), R.string.error_password);
					}				
				}
				
				@Override
				public void onCancel() {
					
				}

			});
			operatorPasswordDialog.show();
			MainActivity.getInstance().operatorPasswordDialog = operatorPasswordDialog;
		} else if (isUseManagePassword) {
			operatorPasswordDialog = new OperatorPasswordDialog(context,R.style.swiping_dialog,6,R.string.pls_input_main_password,new InputEventListener() {
				
				@Override
				public void onConfirm(Dialog dialog, String value) {
					final OperatorPasswordDialog operatorPasswordDialog = (OperatorPasswordDialog) dialog;
					
					new CommonThread(new ThreadCallBack() {
						int result;
						@Override
						public void onBackGround() {
							UserService service = new UserServiceImpl(context);
							result = service.checkLogin("00", operatorPasswordDialog.getPassword());
						}

						@Override
						public void onMain() {
							operatorPasswordDialog.dismiss();
							if (result == 2) {
								commonDialog.show();
							} else {
								ToastUtils.show(context, R.string.error_password);
							}	
						}
						
					}).start();
					
								
				}
				
				@Override
				public void onCancel() {
					
				}

			});

			operatorPasswordDialog.show();
		}else {
			commonDialog.show();
			MainActivity.getInstance().commonDialog = commonDialog;
		}
		return commonDialog;
	}
	
	/**
	 * 显示信息提示框，只有一个确认按钮
	 * @param context
	 * @param resId 提示字符串id
	 * @param listenerSure 
	 * @return
	 */
	public static CommonDialog getCommonDialog(Context context,
			int resContent, OnClickListener listenerSure) {
		final CommonDialog commonDialog = getCommonDialog(context, null,
				context.getString(resContent), R.string.common_sure, -1,
				-1, listenerSure, null,null, -1, TimeOutOper.NONE);
		
		return commonDialog;
	}
	
	/**
	 * 显示信息提示框(默认三个按钮，不需要显示的按钮则将listener传空即可)
	 * @param context
	 * @param message 提示信息
	 * @param resSureText 确认按钮文件资源id
	 * @param resMiddelText 中间按钮文件资源id
	 * @param resCancelText 取消按钮文件资源id
	 * @param listenerSure 确认按钮监听
	 * @param listenerMiddle 中间按钮监听
	 * @param listenerCancel 取消按钮监听
	 * @return
	 */
	public static CommonDialog getCommonDialog(Context context, String title,
			CharSequence message, int resSureText, int resMiddelText, int resCancelText,
			OnClickListener listenerSure, OnClickListener listenerMiddle, 
			OnClickListener listenerCancel, int timeOut, TimeOutOper timeOutOper) {

		final CommonDialog commonDialog = new CommonDialog(context, timeOut, timeOutOper);
		if (title != null) {
			commonDialog.setTitle(title);
		}
		commonDialog.setContent(message.toString());
		commonDialog.setSureListener(resSureText, listenerSure);
		commonDialog.setCancelListener(resCancelText, listenerCancel);
		commonDialog.setMiddleListener(resMiddelText, listenerMiddle);
		
		
		return commonDialog;
	}
	
	
	
	/**
	 * 显示菜单选择对话框
	 * @param context
	 * @param title 提示框标题
	 * @param items 菜单数据源
	 * @param defaultSelected 默认选择索引
	 * @param itemSelectListener 选项选中监听
	 * @param listenerSure 确认监听
	 * @param listenerCancel 取消监听
	 * @return
	 */
	public static Dialog showMenuSelectDialog(Context context, String title,
			String[] items, int defaultSelected,
			AdapterView.OnItemClickListener itemSelectListener,
			OnListDialogClickListener listenerSure,
			View.OnClickListener listenerCancel) {
		CommonListDialog dialog = new CommonListDialog(context, title, items,
				defaultSelected, itemSelectListener, listenerSure,
				listenerCancel);
		dialog.show();
		
		return dialog;
	}
	
	/**
	 * 显示时间对话框(时 分 秒)
	 * @param context
	 * @param title 对话框标题
	 * @param date 初始化时间
	 * @param listener 时间选择监听
	 * @return
	 */
	public static Dialog showCommonTimeDialog(Context context,
			String title, 
			long date, 
			final LongValueChangeListener listener){
		
		View view = LayoutInflater.from(context).inflate(R.layout.common_time_dialog, null);
		
		/** 日期控件 */
		final TimeWheelView timeWheelView = (TimeWheelView) view.findViewById(R.id.time_wheel_view);
		/** 标题 */
		TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
		/** 取消按钮 */
		TextView txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
		/** 确定按钮 */
		TextView txtSure = (TextView) view.findViewById(R.id.txt_sure);

		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(view);
		
		txtTitle.setText(title);
		timeWheelView.setDate(date);
		
		txtCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		
		txtSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) {
					listener.onChange(timeWheelView.getDate());
				}
			}
		});
		
		return dialog;
	}
	/**
	 * 显示日期对话框(年 月 日)
	 * @param context 上下文
	 * @param title 对话框标题
	 * @param listener 日期选择监听器
	 * @return
	 */
	public static Dialog showCommonDateDialog(Context context,String title, final LongValueChangeListener listener){
		return showCommonDateDialog(context, title, System.currentTimeMillis(), 1950, 2050,true, true, true, listener);
	}
	/**
	 * 显示日期对话框(年 月 日)
	 * @param context 上下文
	 * @param title 对话框标题
	 * @param date 初始化对话框中日期
	 * @param isShowYear 是否显示年
	 * @param isShowMouth 是否显示月
	 * @param isShowDay 是否显示日
	 * @param listener 日期选择监听
	 * @return
	 */
	public static Dialog showCommonDateDialog(Context context,
			String title,
			Long date, 
			int minYear,
			int maxYear,
			boolean isShowYear,
			boolean isShowMouth, 
			boolean isShowDay, 
			final LongValueChangeListener listener){
		
		View view = LayoutInflater.from(context).inflate(R.layout.common_date_dialog, null);
		
		/** 日期控件 */
		final DateWheelView2 dateWheelView = (DateWheelView2) view.findViewById(R.id.date_wheel_view);
		/** 标题 */
		TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
		/** 取消按钮 */
		TextView txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
		/** 确定按钮 */
		TextView txtSure = (TextView) view.findViewById(R.id.txt_sure);

		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(view);
		
		txtTitle.setText(title);
		if (date != null) {
			dateWheelView.setDate(date);
		}
		dateWheelView.setIsShowYear(isShowYear);
		dateWheelView.setIsShowMouth(isShowMouth);
		dateWheelView.setIsShowDay(isShowDay);
		dateWheelView.setAvailableYear(minYear, maxYear);
		
		txtCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		
		txtSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) {
					listener.onChange(dateWheelView.getDate());
				}
			}
		});
		
		return dialog;
	}

	/**
	 * 显示输入弹出窗
	 */
	public static CommonInputDialog showInputDialog (Context context, int resTitle, String content,
			View.OnClickListener sureListener) {
		CommonInputDialog dialog = getInputDialog(context, resTitle, content, sureListener);
		dialog.show();
		
		return dialog;
	}
	
	/**
	 * 获取输入弹出窗
	 */
	public static CommonInputDialog getInputDialog (Context context, int resTitle, String content,
			View.OnClickListener sureListener) {
		
		CommonInputDialog dialog = new CommonInputDialog(context);
		dialog.setTitle(resTitle);
		dialog.setContent(content);
		dialog.setSureListener(R.string.common_sure, sureListener);
		dialog.setCancelListener(R.string.common_cancel, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		return dialog;
	}
	
	public static void showTipMsg(final String msg) {

		MainActivity.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtils.show(MainActivity.getInstance()
						.getApplicationContext(), msg);
			}
		});
	}	
}
