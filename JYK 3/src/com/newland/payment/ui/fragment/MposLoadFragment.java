package com.newland.payment.ui.fragment;

import com.newland.payment.R;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.pos.sdk.bean.BaseBean;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 获取母POS序列号和母POS传输密钥
 */
public class MposLoadFragment extends BaseFragment {
	TextView txtLoadKeyInfoText;
	
	private InputInfoBean bean;
	
	public MposLoadFragment(long timeOut) {
		super(60L);
	}

	public static MposLoadFragment newInstance(InputInfoBean infoBean) {
		MposLoadFragment fragment = new MposLoadFragment(0L);
		fragment.bean = infoBean;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_mpos_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		activity.setTitle(bean.getTitle());
		activity.setExceptMainStyle();
		beginMpos();
		return mFragmentView;

	}

	public void beginMpos() {
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initClickEvent(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

}
