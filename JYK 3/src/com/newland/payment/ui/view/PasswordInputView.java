package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.newland.payment.R;
import com.newland.payment.interfaces.OnKeyBoardActionListener;

/**
 * 密码输入框
 * @author CB
 * @time 2015-4-20 下午3:12:02
 */
public class PasswordInputView extends FrameLayout {

	/** 密码 */
	private String value;
	/** 总布局 */
	private LinearLayout llMain;
	@SuppressWarnings("unused")
	private OnKeyBoardActionListener confirmListener;
	private int lenth = 0;
	private Context context;

	public PasswordInputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PasswordInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PasswordInputView(Context context) {
		super(context);
		init(context);
	}

	

	private void init(Context context) {
		this.context = context;

	}

	
	public void setPassword(String str) {
		
		
		
		if (this.lenth != 0) {
			if (str != null && str.length() < (this.lenth+1)) {
				this.value = str;
				for (int i = 0; i < this.lenth; i++) {
					View view = llMain.getChildAt(i);
					if (i < value.length()) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.INVISIBLE);
					}
				}
			}
		}
		else {
			if (str != null && str.length() < 7) {
				this.value = str;
				for (int i = 0; i < 6; i++) {
					View view = llMain.getChildAt(i);
					if (i < value.length()) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.INVISIBLE);
					}
				}
			}
		}
				
	}
	
	
	public void setPwdViewLenth(int lenth){
		this.lenth = lenth;
		switch (lenth) {
		case 4:
			llMain = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.password_input4_view, null);
	
			addView(llMain);
			break;
		case 6:
			llMain = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.password_input6_view, null);
			addView(llMain);
			break;
		case 8:
			llMain = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.password_input8_view, null);
			addView(llMain);
			break;
		case 12:
			llMain = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.password_input12_view, null);
			addView(llMain);
			break;
		default:
			llMain = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.password_input8_view, null);
			addView(llMain);
			break;
		}

	}
}
