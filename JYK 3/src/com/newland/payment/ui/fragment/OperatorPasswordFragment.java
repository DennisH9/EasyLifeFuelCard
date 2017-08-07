package com.newland.payment.ui.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.mvc.service.UserService;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.ui.listener.KeyDownListener;
import com.newland.payment.ui.view.OperatorPasswordDialog.InputEventListener;
import com.newland.payment.ui.view.OperatorPasswordFragmentDialog;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 信息提示
 * @author CB
 * @time 2015-4-17 下午4:23:40
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class OperatorPasswordFragment extends BaseFragment {
	
	private CommonBean<Integer> bean;
	
/*	*//**Title*//*
	private String title;
	
	*//**引导信息(请输入流水号)*//*
	private String content;*/
	
	/**蜂呜次数*/
	@SuppressWarnings("unused")
	private int beep;
	
	private OperatorPasswordFragmentDialog oper_dialog;
	
	OnClickListener listenerSure = null;
	
	OnClickListener listenerCancel = null;

	public OperatorPasswordFragment(long timeOut) {
		super(timeOut);
	}
	
	public static OperatorPasswordFragment newInstance(CommonBean<Integer> bean) {
		OperatorPasswordFragment fragment = new OperatorPasswordFragment(bean.getTimeOut());
		fragment.bean = bean;
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.empty_view,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		if (bean != null) {

			/*title = bean.getTitle();
			content = bean.getContent();*/
			bean.setResult(false);
			//setTitle(title);
			
			oper_dialog = new OperatorPasswordFragmentDialog(activity,
					R.style.swiping_dialog, 6,
					bean.getValue(), new InputEventListener() {
						@Override
						public void onConfirm(Dialog dialog, String value) {
							LoggerUtils.d("value change:" + value);
							UserService userService = new UserServiceImpl(activity);
							List<com.newland.payment.mvc.model.User> list = userService.findAllUsers();
							LoggerUtils.d("list user :" + list);
							if (userService.checkLogin("00", value) == Const.UserType.DIRECTOR) {
								bean.setResult(true);
								onSucess();
							} else {
								bean.setResult(false);
								ToastUtils.show(activity, getText(R.string.error_password));
								onFail();
							}
						}
						
						@Override
						public void onCancel() {
							//bean.setResult(false);
							OperatorPasswordFragment.this.onBack();
						}

					});
			
			oper_dialog.setKeyDownListener(new KeyDownListener() {
				
				@Override
				public boolean onHome() {
					// HOME键处理
					return doClickHomeEvent();
				}
				
				@Override
				public boolean onBack() {
					return doClickBackEvent();
				}
			});
		}
	}

	@Override
	protected void initClickEvent(View view) {
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if(oper_dialog != null){
			oper_dialog.show();
		}
		
	}
	
	private void dismissDialog(){
		if (oper_dialog != null) {
			oper_dialog.dismiss();
			oper_dialog = null;
		}
	}
	
	@Override
	public void onSucess() {
		dismissDialog();
		super.onSucess();
	}
	
	@Override
	public void onBack() {
		dismissDialog();
		super.onBack();
	}
	
	@Override
	public void onTimeOut() {
		dismissDialog();
		super.onTimeOut();
	}
	
	@Override
	public void onFragmentHide() {
		dismissDialog();
		super.onFragmentHide();
	}


	@Override
	protected void initEvent() {
		
	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}

}
