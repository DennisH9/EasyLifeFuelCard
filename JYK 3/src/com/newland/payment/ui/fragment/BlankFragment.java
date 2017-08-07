package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.TransUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.common.Const.ScreenType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.trans.bean.TransResultBean;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 账单界面
 * 
 * @author CB
 * @time 2015-4-21 上午9:24:23
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class BlankFragment extends BaseFragment {

	private Button BtnReturnMain;
	
	private TransResultBean bean;
	
	
	private BlankFragment(){
		super(0);
	}

	public static BlankFragment newInstance() {
		BlankFragment blankFragment = new BlankFragment();
		return blankFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mFragmentView = inflater.inflate(R.layout.blank_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
	}
	
	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	protected void initEvent() {

	}

	
	@Override
	protected boolean onBackKeyDown() {
		return true;
	}
	
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		activity.setExceptMainStyle();
	}

	@Override
	public void onFragmentShow() {
		super.onFragmentShow();
		activity.setLoginStyle();
	}

	@Override
	protected TransResultBean getBean() {
		return bean;
	}
}
