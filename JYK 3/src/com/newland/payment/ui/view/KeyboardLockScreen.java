package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.OnKeyBoardValueChangeListener;

/**
 * 数字键盘输入器
 * 
 * @author CB
 * @time 2015-4-20 上午9:10:06
 */
public class KeyboardLockScreen extends FrameLayout {

	/** 最大位数 */
	private int maxSize = 99;
	/** 输入的值 */
	private String value = "";
	
	private Context context;
	private OnKeyBoardValueChangeListener onKeyBoardValueChangeListener;

	public KeyboardLockScreen(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public KeyboardLockScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public KeyboardLockScreen(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initData();
	}

	private void initData() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.keyboard_lock_screen, this, true);
		ViewUtils.inject(this, view);
	}


//	@OnClick({ R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5,
//			R.id.iv_6, R.id.iv_7, R.id.iv_8, R.id.iv_9, R.id.iv_0,
//			R.id.iv_cancel, R.id.iv_backspeace })
	private void initClickEvent(View view) {

		int i = view.getId();
		if (i == R.id.iv_1) {
			value += 1;

		} else if (i == R.id.iv_2) {
			value += 2;


		} else if (i == R.id.iv_3) {
			value += 3;


		} else if (i == R.id.iv_4) {
			value += 4;


		} else if (i == R.id.iv_5) {
			value += 5;


		} else if (i == R.id.iv_6) {
			value += 6;


		} else if (i == R.id.iv_7) {
			value += 7;


		} else if (i == R.id.iv_8) {
			value += 8;


		} else if (i == R.id.iv_9) {
			value += 9;


		} else if (i == R.id.iv_0) {
			value += 0;


		} else if (i == R.id.iv_cancel) {
			value = "0";

		} else if (i == R.id.iv_backspeace) {
			if (value.length() > 1) {
				value = value.substring(0, value.length() - 1);
			} else {
				value = "";
			}

		}

		if (value.length() > maxSize) {
			value = value.substring(0, value.length() - 1);
		} else if (onKeyBoardValueChangeListener != null) {
			onKeyBoardValueChangeListener.onChange(value);
		}

	}

	/**
	 * 设置数值改变监听
	 * 
	 * @param onKeyBoardValueChangeListener
	 */
	public void setOnKeyBoardValueChangeListener(
			OnKeyBoardValueChangeListener onKeyBoardValueChangeListener) {
		this.onKeyBoardValueChangeListener = onKeyBoardValueChangeListener;
	}

	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

}
