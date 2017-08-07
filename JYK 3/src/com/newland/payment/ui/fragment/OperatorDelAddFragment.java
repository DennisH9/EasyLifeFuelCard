package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.newland.base.CommonThread;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.DuplicatedUserException;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

import java.util.List;

/**
 * 操作员处理界面
 *
 * @author CB
 * @time 2015-4-20 下午4:14:26
 */ 
@SuppressLint({ "ValidFragment", "InflateParams" })
public class OperatorDelAddFragment extends BaseFragment{
	
	UserServiceImpl serviceImpl = new UserServiceImpl(context);
	RelativeLayout showLayout;
	private AbstractKeyBoardListener listener;
	/** 操作员号 */
//	@ViewInject(R.id.number_input_view)
	EditText numberInputView;
	/** 操作员密码 */
//	@ViewInject(R.id.password_input_view)
	EditText passwordInputView;
	/** 密码键盘 */
//	@ViewInject(R.id.keyboard_password)
	KeyboardNumber keyboardPassword;
	/** 密码界面 */
//	@ViewInject(R.id.tv_operator_pwd_title)
	LinearLayout linearLayout;
	/** 密码下line */
//	@ViewInject(R.id.pwd_line)
	ImageView pwdLine;
	
	/** 操作员号*/ 
	private String operatorNo = null;
	/** 操作员密码*/
	private String operatorPassword = null;
	/** 添加还是删除*/
	private Boolean isAdd = false;
	/** 是否号码存在*/
	private Boolean isHasNo = false;
	/** 操作员实体*/
	private User user ;
	List<User> users;
	

	public static OperatorDelAddFragment newInstance(Boolean flag) {
		OperatorDelAddFragment fragment = new OperatorDelAddFragment();
		fragment.isAdd = flag;
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.operator_del_add_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		keyboardPassword.setEnterNotGone();
		if (isAdd) {
			setTitle(R.string.common_operator_add);
		}else {
			setTitle(R.string.common_operator_del);
			linearLayout.setVisibility(View.GONE);
			pwdLine.setVisibility(View.INVISIBLE);
		}
		user = new User();
		
		InputUtils.setMaxLength(numberInputView, 2);
		InputUtils.setMaxLength(passwordInputView, 4);
		
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				
			}
			
			@Override
			public void onBackGround() {
				users = serviceImpl.findAllUsers();
				
			}
		}).start();

		InputUtils.alwaysHideKeyBoard(numberInputView, InputType.TYPE_NUMBER_FLAG_SIGNED);
		InputUtils.alwaysHideKeyBoard(passwordInputView, InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		passwordInputView.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
				//键盘输入监听
				listener = new AbstractKeyBoardListener(2,numberInputView){
			@Override
			public void onEnter() {
				if (isAdd && passwordInputView.getText() != null && numberInputView.getText() != null) {
					
					operatorNo = StringUtils.fill(numberInputView.getText().toString(), "0", 2, true); 
					operatorPassword = passwordInputView.getText().toString();
					if (operatorNo.equals("00") || operatorNo.equals("99")) {
						ToastUtils.show(context, "无效操作员号");
					}
					else {
						if (operatorNo.length() == 2 && operatorPassword.length() == 4) {
							doUserAddDel();
						}
						else { 
							ToastUtils.show(activity, context.getString(R.string.common_pls_input_right_info));
						}
					}
				}else if (!isAdd && numberInputView.getText() != null) {
					operatorNo = StringUtils.fill(numberInputView.getText().toString(), "0", 2, true); 

					if (operatorNo.equals("00") || operatorNo.equals("99")) {
						ToastUtils.show(context, "无效操作员号");
					}
					else {
						if (operatorNo.length() == 2 ) {
							doUserAddDel();
						}
						else {
							ToastUtils.show(activity, context.getString(R.string.common_pls_input_right_info));
						}
					}					
				}else {
					ToastUtils.show(activity, context.getString(R.string.common_pls_input_right_info));
				}
				super.onEnter();
			}
			@Override
					public void onCancel() {
						super.onCancel();
					}
		};
		 
		passwordInputView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.setEditText(passwordInputView);
				listener.setMaxLength(4);
			}
		});
		numberInputView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.setEditText(numberInputView);
				listener.setMaxLength(2);
			}
		});

			keyboardPassword.setKeyBoardListener(listener);
	}
	public void doUserAddDel(){
		
		if (isAdd) {
			user.setUserNo(operatorNo);
			user.setPassword(operatorPassword);
			
			if (IsHasNum(operatorNo)) {
				
				ToastUtils.show(activity, context.getString(R.string.operator_contain));
				
			} 
			else{

	        	new CommonThread(new ThreadCallBack() {
					
					@Override
					public void onMain() {
						
					}
					
					@Override
					public void onBackGround() {
						try {
							serviceImpl.addUser(user);
						} catch (DuplicatedUserException e) {
							e.printStackTrace();
						}
						
					}
				}).start();
				ToastUtils.show(activity, context.getString(R.string.add_operator_scs));
				activity.backFragment();
			} 
		}else {
			if (IsHasNum(operatorNo)) {
				//不允许删除自身操作员号
				if (App.USER.getUserNo().equals(operatorNo)) {
					ToastUtils.show(context, R.string.del_self_err);
				}
				else {				
		        	new CommonThread(new ThreadCallBack() {
						
						@Override
						public void onMain() {
							
						}
						
						@Override
						public void onBackGround() {
	
							serviceImpl.deleteUser(operatorNo);
						}
					}).start();
				
					ToastUtils.show(activity, context.getString(R.string.del_operator_password_scs));
					activity.backFragment();
				}
			}
			else {
				ToastUtils.show(activity, context.getString(R.string.operator_no_contain));
			}			
		}	
	}
	
	public Boolean IsHasNum(String num)
	{	
		isHasNo = false;
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
	protected void initEvent() {
	}

	@Override
	public BaseBean getBean() {
		return null;
	}
	public String getOperatorNo() {
		return operatorNo;
	}
}
