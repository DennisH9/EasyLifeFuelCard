package com.newland.payment.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.newland.payment.ui.view.SettingEditView;



/**
 * 设置界面的基类
 *
 * @author CB
 * @date 2015-5-25 
 * @time 上午11:43:12
 */
public abstract class BaseSettingFragment extends BaseFragment{
	
	public final List<SettingEditView> settingEditViews = new ArrayList<SettingEditView>();
	
	public BaseSettingFragment(long outTime) {
		super(outTime);
	}
	
	public BaseSettingFragment() {
		super();
	}
	
	protected void addCheckView(SettingEditView settingEditView) {
		settingEditViews.add(settingEditView);
	}
	
	@Override
	public void onFragmentHide() {
		for (SettingEditView view : settingEditViews) {
			view.hideSystemKeyboard();
		}
		super.onFragmentHide();
	}

}
