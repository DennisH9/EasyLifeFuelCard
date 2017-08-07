package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.newland.base.CommonThread;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

import java.util.List;

/**
 * 操作员改密
 *
 * @author CB
 * @date 2015-5-18 
 * @time 下午5:42:43
 */
public class OperatorChangePwdFragment extends BaseFragment {

	/** 卡号 */
//	@ViewInject(R.id.et_operatoe_change_no)
	EditText etNo;
	/** 密码 */
//	@ViewInject(R.id.et_operatoe_change_old_pwd)
	EditText etOldPwd;
	/** 密码1 */
//	@ViewInject(R.id.et_operatoe_change_new_pwd1)
	EditText etNewPwd1;  
	/** 密码2 */
//	@ViewInject(R.id.et_operatoe_change_new_pwd2)
	EditText etNewPwd2;

	private String noString;
	private String oldString;
	private String new1String;
	private String new2String;

	/** 键盘 */
//	@ViewInject(R.id.keyboard_password)
	KeyboardNumber keyboardNumber;
	private AbstractKeyBoardListener listener;
	UserServiceImpl serviceImpl = new UserServiceImpl(context);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mFragmentView = inflater.inflate(R.layout.operator_change_pwd_fragment,
				null);

		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	public static OperatorChangePwdFragment newInstance() {
		OperatorChangePwdFragment fragment = new OperatorChangePwdFragment();
		return fragment;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.common_operator_chang_pwd);
		keyboardNumber.setEnterNotGone();
		InputUtils.alwaysHideKeyBoard(etNo, InputType.TYPE_NUMBER_FLAG_SIGNED);
		InputUtils.alwaysHideKeyBoard(etOldPwd, InputType.TYPE_NUMBER_FLAG_SIGNED);
		InputUtils.alwaysHideKeyBoard(etNewPwd1, InputType.TYPE_NUMBER_FLAG_SIGNED);
		InputUtils.alwaysHideKeyBoard(etNewPwd2, InputType.TYPE_NUMBER_FLAG_SIGNED);

		etNo.setText(App.USER.getUserNo());
		etNo.setFocusable(false);
		etNewPwd1.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etNewPwd2.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etOldPwd.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		InputUtils.setMaxLength(etNewPwd1, 4);
		InputUtils.setMaxLength(etNewPwd2, 4);
		InputUtils.setMaxLength(etOldPwd, 4);
		
		listener = new AbstractKeyBoardListener(etOldPwd,InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD) {
			@Override
			public void onEnter() {
				if (!(StringUtils.isDigital(etOldPwd.getText().toString()) &&StringUtils.isDigital(etNewPwd1.getText().toString()) 
						&&StringUtils.isDigital(etNewPwd2.getText().toString()) && etOldPwd.getText().toString().length() == 4 
						&& etNewPwd1.getText().toString().length() == 4 && etNewPwd2.getText().toString().length() == 4 )) {
					ToastUtils.show(context,  context.getString(R.string.input_infomation_err));
					return;
				}
				else if (etNo.getText() != null && etOldPwd.getText() != null
						&& etNewPwd1.getText() != null
						&& etNewPwd2.getText() != null) {
					noString = App.USER.getUserNo(); 
					
					oldString = etOldPwd.getText().toString();
					new1String = etNewPwd1.getText().toString();
					new2String = etNewPwd2.getText().toString();
					
					
					if (new1String.equals(new2String)) {
						
						if (oldString.equals(new1String)) {
							ToastUtils.show(context,context.getString(R.string.input_new_old_password_same));
						}
						else {
							new CommonThread(new ThreadCallBack() {
								
				        		int result;
				        		
								@Override
								public void onMain() {
									if (result == 1) {
										if (serviceImpl.updateUserPassword(noString, oldString, new1String) == 1) {
											ToastUtils.show(activity, context.getString(R.string.cg_operator_password_scs));
											activity.backFragment();				
										}
										else {
											ToastUtils.show(activity, "修改失败 请联系管理员");

										}
										
									} else {
										ToastUtils.show(activity, context.getString(R.string.input_old_password_err));

									}
								}
								
								@Override
								public void onBackGround() {
									result = serviceImpl.checkLogin(noString, oldString);

								}
							}).start();
						}
			        	
						
					} 
					else {
						ToastUtils.show(activity, R.string.input_new_password_difference);
						
					}
						
					
				} else {
					ToastUtils.show(context,  context.getString(R.string.input_infomation_err));
				}
				super.onEnter();
			}
		};

		keyboardNumber.setKeyBoardListener(listener);
		
		etOldPwd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.setMaxLength(4);
				listener.setEditText(etOldPwd);
			}
		});
		etNewPwd1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.setMaxLength(4);
				listener.setEditText(etNewPwd1);
			}
		});
		etNewPwd2.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.setMaxLength(4);
				listener.setEditText(etNewPwd2);
			}
		});
	}

	public Boolean IsHasNum(String num)

	{
		List<User> users = serviceImpl.findAllUsers();

		Boolean isHasNo = false;
		if (users != null && users.size() > 0) {
			for (User user : users) {
				if (user.getUserNo().equals(num)) {
					isHasNo = true;
				}
			}
		}
		return isHasNo;
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

	}

}
