package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.ui.listener.OnListDialogClickListener;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.MenuSelectBean;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 菜单选择页面
 * 
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class MenuSelectFragment extends BaseFragment {
	
	private MenuSelectBean bean;
	
	/**输入超时时间，单位s*/
	@SuppressWarnings("unused")
	private int timeOut;
	
	/**Title*/
	private String title;
	
	/**引导信息(请输入流水号)*/
	@SuppressWarnings("unused")
	private String content;
	
	/**选择项*/
	private String[] items;
	
	private Dialog menu_select_dialog;
	
	OnClickListener listenerSure = null;
	
	OnClickListener listenerCancel = null;

	public MenuSelectFragment(long timeOut) {
		super(timeOut);
	}
	
	public static MenuSelectFragment newInstance(MenuSelectBean menuSelectBean) {
		MenuSelectFragment fragment = new MenuSelectFragment(menuSelectBean.getTimeOut());
		fragment.bean = menuSelectBean;
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

		title = bean.getTitle();
		content = bean.getContent();
		timeOut = bean.getTimeOut();
		items = bean.getItems();
		//setTitle(title);

	}

	@Override
	protected void initClickEvent(View view) {
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		AdapterView.OnItemClickListener itemSelectListener = new AdapterView.OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int which,
					long arg3) {
				bean.setItemIndex(which);
				activity.resetProgress();
			}
		};
		View.OnClickListener listenerCancel = new View.OnClickListener() {
			@Override
			public void onClick(View dialog) {
				onFail();
				//dismissDialog();
			}
		};
		OnListDialogClickListener listenerSure = new OnListDialogClickListener() {
			
			@Override
			public void onClick(int position) {
				bean.setItemIndex(position);
				onSucess();
			}
		};
				

		menu_select_dialog = MessageUtils.showMenuSelectDialog(activity, title,
				items, 0, itemSelectListener, listenerSure,
				listenerCancel);
		//menu_select_dialog.setCanceledOnTouchOutside(false);
		
	}
	
	@Override
	public void onFragmentHide() {
		LoggerUtils.d("MenuSelectFragment onFragmentHide ....");
		dismissDialog();
		super.onFragmentHide();
	}

	private void dismissDialog(){
		if (menu_select_dialog != null) {
			menu_select_dialog.dismiss();
			menu_select_dialog = null;
		}
	}

	@Override
	public void onStop() {
		dismissDialog();
		super.onStop();
	}

	@Override
	protected void initEvent() {
		
	}

	@Override
	public BaseBean getBean() {
		return this.bean;
	}

}
