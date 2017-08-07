package com.newland.payment.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.ui.bean.SettingSwicthBean;
import com.newland.payment.ui.view.SettingSwitchView;

/**
 * 参数开关适配器
 *
 * @author CB
 * @date 2015-5-13 
 * @time 下午8:05:10
 */
public class SettingSwitchAdapter extends BaseListViewAdapter<SettingSwicthBean>{

	public SettingSwitchAdapter(Context context, List<SettingSwicthBean> mDataList) {
		super(context, mDataList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = View.inflate(mActiviy, R.layout.setting_switch_item, null);
		}
		
		SettingSwitchView settingSwitchView = HolderUtils.get(convertView, R.id.setting_switch_view);
		
		SettingSwicthBean bean = getItem(position);
		
		settingSwitchView.setParamData(bean.getResTitle(), bean.getKey());
		
		settingSwitchView.addValueChangeListener(bean.getBooleanValueChangeListener());
		
		return convertView;
	}

}
