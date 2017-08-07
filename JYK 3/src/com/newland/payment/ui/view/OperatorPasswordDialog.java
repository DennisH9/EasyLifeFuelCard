package com.newland.payment.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 操作员密码输入对话框
 * 
 * @author linst
 * @time 2015-5-8
 */
public class OperatorPasswordDialog extends Dialog {

	
	/** 密码 */
	public PasswordInputView passwordInputView;
	/** 键盘 */
	KeyboardNumber keyBoardNumber;
	/** 密码框长度*/
	private int pwdLenth = 0;
	/** 标题*/
	private TextView tvTitle;
	/** 下提示1*/
	private TextView tvTip1;
	/** 下提示2*/
	private TextView tvTip2;
	/** 下提示3*/
	private TextView tvTip3;
	/** 密码 */
	private LinearLayout llPassword;

	private String titleString = null;
	private String tip1String = null;
	private String tip2String = null;
	private String tip3String = null;

	private String tempString = null;
	private String passwordValue = null;
	
	private int currentPasswordLength = 0;

	
	private InputEventListener inputEventListener;
	public OperatorPasswordDialog(Context context, int theme, int pwdLenth,String title) {
		super(context, theme);
		this.pwdLenth = pwdLenth;
		titleString = title;

	}
	public OperatorPasswordDialog(Context context, int theme, int pwdLenth,int resource, InputEventListener inputEventListener) {
		super(context, theme);
		this.pwdLenth = pwdLenth;
		titleString = context.getString(resource);
		this.inputEventListener = inputEventListener;
		
	}
	
	public OperatorPasswordDialog(Context context, int pwdLenth) {
		super(context);
		this.pwdLenth = pwdLenth;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_password_dialog);

		// APP 获取 HOME keyEvent的设定关键代码。
		/*
		if (VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
			getWindow().addFlags(3);
		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
		}
		 */	
		initData();
	}

	private void initData() {	
		tvTitle = (TextView)findViewById(R.id.tv_operator_dialog_title);
		tvTip1 = (TextView)findViewById(R.id.tv_operator_dialog_tip1);
		tvTip2 = (TextView)findViewById(R.id.tv_operator_dialog_tip2);
		tvTip3 = (TextView)findViewById(R.id.tv_operator_dialog_tip3);
		llPassword = (LinearLayout) findViewById(R.id.ll_password);
		
		
		tvTitle.setText(titleString);
		tvTip1.setText(tip1String);
		tvTip2.setText(tip2String);
		tvTip3.setText(tip3String);

		passwordInputView = (PasswordInputView) findViewById(R.id.password_input_view);
		passwordInputView.setPwdViewLenth(pwdLenth);
		
		keyBoardNumber = (KeyboardNumber) findViewById(R.id.key_board_number_manager);
		keyBoardNumber.setEnterNotGone();

		// 键盘输入监听
		
		AbstractKeyBoardListener listener = new AbstractKeyBoardListener(pwdLenth) {

			@Override
			public void onEnter() {
//				密码长度不够点确定时摇动
				if (tempString == null || tempString.length() != pwdLenth) {
					ViewUtils.shakeAnimatie(llPassword);
				}
				if ( tempString != null && tempString.length() == pwdLenth) {
					passwordValue = tempString;

					if (inputEventListener != null) {
						inputEventListener.onConfirm(OperatorPasswordDialog.this, passwordValue);
						for (int start = 0; start < pwdLenth; start++) {
							this.onBackspeace();

						}

					}
				}
				
			}
			@Override
			public void onCancel() {
				dismiss();
				if (inputEventListener!=null)
					inputEventListener.onCancel();
			}
			
			@Override
			public void onChangeText(String text) {
				passwordInputView.setPassword(text);
				tempString = text;
				
				if (currentPasswordLength == pwdLenth && tempString.length() == pwdLenth) {
					ViewUtils.shakeAnimatie(llPassword);
				}
				currentPasswordLength = text.length();
			}
		};

		keyBoardNumber.setKeyBoardListener(listener);
	}

    public String getPassword(){
    	return passwordValue;
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void dismiss() {
		LoggerUtils.d("调用dismiss~~~~~~~~~~~~~~~~~~~~~~~~");
		super.dismiss();
	}


	public interface InputEventListener{
		public void onCancel();
		public void onConfirm(Dialog dialog, String value);
	}
	
	public void setMTitle(String title)
	{
		tvTitle.setText(title);
	}
	
	
}
