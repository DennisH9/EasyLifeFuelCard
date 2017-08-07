/**
 * 
 */
package com.newland.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.base.CommonThread;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.activity.MainActivity;

public class ToastUtils {
	private static Drawable iconLeft;
	private static Toast mToastView;
	private static View toastContentView ;
	private static ImageView toastIconView;
	private static TextView toastMessageView;//吐司的文字控件
	
	/**
	 * 显示toast提示
	 * @param context
	 * @param info
	 */
	@SuppressLint("InflateParams")
	private static void show(Context context, Object info, int duration) {

		if (info == null){
			return;
		}
		mToastView = new Toast(context);
		toastContentView = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
		toastMessageView = (TextView) toastContentView.findViewById(R.id.toast_message);
		toastIconView = (ImageView) toastContentView.findViewById(R.id.toast_icon);
		if(iconLeft != null){
			toastIconView.setImageDrawable(iconLeft);
		}
		mToastView.setDuration(duration > Toast.LENGTH_LONG ? Toast.LENGTH_LONG : duration);
		mToastView.setView(toastContentView);
		mToastView.setGravity(Gravity.TOP, 0, DisplayUtils.getDiaplay(context).heightPixels / 3);
		toastMessageView.setText(info.toString());
		mToastView.show();
		
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				mToastView.cancel();
			}
			
			@Override
			public void onBackGround() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 显示toast提示
	 * @param context
	 * @param info
	 */
	public static void show(Context context, Object info) {
		show(context, info, Toast.LENGTH_SHORT);
	}
	/**
	 * 显示toast提示(时间长一点)
	 * @param context
	 * @param info
	 */
	public static void showLong(Context context, Object info){
		show(context, info, Toast.LENGTH_LONG);
	}
	
	/**
	 * show
	 * @param context
	 * @param resId
	 */
	public static void show(Context context, int resId) {
		show(context, context.getString(resId));
	}
	

	public static void showOnUIThread(final Object info){
		MainActivity.getInstance().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				show(MainActivity.getInstance(), info);
			}
		});
	}
	
}
