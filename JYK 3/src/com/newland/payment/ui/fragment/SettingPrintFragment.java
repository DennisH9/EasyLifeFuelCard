package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 打印设置
 *
 * @author CB
 * @date 2015-5-20 
 * @time 上午9:41:26
 */
public class SettingPrintFragment extends BaseSettingFragment{


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_print_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		setTitle(R.string.setting_print);
	}
	
//	@OnClick({R.id.txt_setting_print,R.id.txt_param_print})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_print) {//打印设置
			activity.switchContent(new SettingPrintSettingFragment());

		} else if (i == R.id.txt_param_print) {//参数打印
			activity.switchContent(new SettingPrintParamsFragment());
		}
	}
	
	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

}
