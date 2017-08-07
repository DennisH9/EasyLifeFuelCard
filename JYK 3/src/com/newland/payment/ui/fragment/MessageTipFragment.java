package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 信息提示
 * @author CB
 * @time 2015-4-17 下午4:23:40
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class MessageTipFragment extends BaseFragment {
	
	private MessageTipBean bean;
	
	/**Title*/
	private String title;
	
	/**引导信息(请输入流水号)*/
	private String content;
	
	/**蜂呜次数*/
	@SuppressWarnings("unused")
	private int beep;
	
	/**能否取消*/
	private boolean canCancel = false;
	
	private Dialog msg_tip_dialog;
	
	OnClickListener listenerSure = null;
	
	OnClickListener listenerCancel = null;

	public MessageTipFragment(long timeOut) {
		super(timeOut);
	}
	
	public static MessageTipFragment newInstance(MessageTipBean msgTipBean) {
		MessageTipFragment fragment = new MessageTipFragment(msgTipBean.getTimeOut());
		fragment.bean = msgTipBean;
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

			title = bean.getTitle();
			content = bean.getContent();
			canCancel = bean.isCancelable();
			bean.setResult(false);
			//setTitle(title);
		}
	}

	@Override
	protected void initClickEvent(View view) {
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		listenerSure = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LoggerUtils.d("listenerSure click");
				bean.setResult(true);
				onSucess();
			}
		};
		listenerCancel = null;
		if (canCancel) {
			listenerCancel = new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					bean.setResult(false);
					onSucess();
					
				}
			};
		}
		msg_tip_dialog = MessageUtils.showCommonDialog(activity, title, content, listenerSure, listenerCancel);
		
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


	private void dismissDialog(){
		if (msg_tip_dialog != null) {
			msg_tip_dialog.dismiss();
			msg_tip_dialog = null;
		}
	}
	
	@Override
	protected void initEvent() {
		
	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}

}
