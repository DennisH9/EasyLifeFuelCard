package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.newland.payment.R;
import com.newland.payment.ui.adapter.SettingSwitchAdapter;
import com.newland.payment.ui.bean.SettingSwicthBean;
import com.newland.pos.sdk.bean.BaseBean;

import java.util.List;

/**
 * 通用设置开关列表
 *
 * @author CB
 * @date 2015-5-13 
 * @time 下午8:55:58
 */
@SuppressLint("ValidFragment")
public class SettingSwitchListFragment extends BaseSettingFragment{
	
//	@ViewInject(R.id.list_view)
	ListView listView;
	
	private List<SettingSwicthBean> beans;
	private SettingSwitchAdapter adapter;
	private int resTitle;
	
	public SettingSwitchListFragment(int resTitle, List<SettingSwicthBean> beans) {
		this.resTitle = resTitle;
		this.beans = beans;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_switch_list_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		listView = (ListView) mFragmentView.findViewById(R.id.list_view);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		setTitle(resTitle);
		
		adapter = new SettingSwitchAdapter(context, beans);
		listView.setAdapter(adapter);
		
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
