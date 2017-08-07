package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.UserService;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 锁屏界面
 *
 * @author CB
 * @date 2015-5-18 
 * @time 上午9:43:44
 */
public class LockFragment extends BaseFragment{
	

	/** 用户名 */
//	@ViewInject(R.id.et_user)
	EditText etUser;
	/** 密码输入框 */
//	@ViewInject(R.id.et_password)
	EditText etPassword;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	/** 版本 */
//	@ViewInject(R.id.txt_version)
	TextView txtVersion;
	
	private AbstractKeyBoardListener keyBoardListener;
	
	public LockFragment() {
		super(0);
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.lock_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.sliding_menu_lock);
		MainActivity.getInstance().lock();
		InputUtils.alwaysHideKeyBoard(etUser, InputType.TYPE_CLASS_NUMBER);
		InputUtils.alwaysHideKeyBoard(etPassword, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		InputUtils.setMaxLength(etUser, App.USER.getUserNo().length());
		
		txtVersion.setText(AndroidTools.getApplicationVersionName(context));
		
	}
	
//	@OnClick({R.id.v_main,R.id.et_user,R.id.et_password})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.v_main) {
			keyboardNumber.setVisibility(View.GONE);

		} else if (i == R.id.et_user) {
			keyBoardListener.setEditText(etUser);
			keyboardNumber.setVisibility(View.VISIBLE);

		} else if (i == R.id.et_password) {
			keyBoardListener.setEditText(etPassword);
			keyboardNumber.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		keyBoardListener = new AbstractKeyBoardListener(etPassword,
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) {
			@Override
			public void onEnter() {
				
				if (etPassword.getText().toString().equals("")) {
					etPassword.setFocusable(true);
					etPassword.setFocusableInTouchMode(true);
					etPassword.requestFocus();
				} else if(etUser.getText().toString().equals("00")) {
					//主管
					new CommonThread(new ThreadCallBack() {
						boolean result;
						@Override
						public void onMain() {
							if (result) {
								App.USER.setUserNo(etUser.getText().toString());
								App.USER.setPassword(etPassword.getText().toString());
								MainActivity.getInstance().unLock();
								MainActivity.getInstance().returnManager();
							} else {
								ToastUtils.show(context, R.string.error_password);
							}
						}
						
						@Override
						public void onBackGround() {
							UserService userService = new UserServiceImpl(context);
							result = userService.checkLogin("00", etPassword.getText().toString()) == 2;
						}
					}).start();
				} else {
					//当前用户
					if ( !etUser.getText().toString().equals(App.USER.getUserNo())) {
						ToastUtils.show(context, R.string.error_user);
					} else if (!etPassword.getText().toString()
							.equals(App.USER.getPassword()) ) {
						ToastUtils.show(context, R.string.error_password);
					} else {
						MainActivity.getInstance().unLock();
						MainActivity.getInstance().returnMainMenu();
					}
				}
				
				
			}
		};
		
		keyboardNumber.setKeyBoardListener(keyBoardListener);

		etUser.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					keyBoardListener.setEditText(etUser);
					keyboardNumber.setVisibility(View.VISIBLE);
					keyBoardListener.setMaxLength(2);
				}
			}
		});
		etPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					int maxLength = etUser.getText().toString().equals("00") ? 6 : 4;
					if (etPassword.getText().toString().length() > maxLength) {
						etPassword.setText("");
					}
					keyBoardListener.setEditText(etPassword);
					keyboardNumber.setVisibility(View.VISIBLE);
					InputUtils.setMaxLength(etPassword, maxLength);
					keyBoardListener.setMaxLength(maxLength);
				}
			}
		});
	}
	
	@Override
	protected boolean onBackKeyDown() {
		return true;
	}
}
