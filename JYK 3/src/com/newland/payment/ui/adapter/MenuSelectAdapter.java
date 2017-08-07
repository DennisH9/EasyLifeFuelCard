package com.newland.payment.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.payment.R;

/**
 * 主界面菜单适配器
 * 
 * @author shipy
 */
public class MenuSelectAdapter extends BaseListViewAdapter<String> {
	
	/** 选中的项 */
	private int checkIndex = 0;
	private AdapterView.OnItemClickListener itemClickListener = null;

	public MenuSelectAdapter(Context context, List<String> mDataList) {
		super(context, mDataList);
	}

	public void setSelfOnItemClickListener(AdapterView.OnItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mActiviy).inflate(
					R.layout.item_view, null);
		}

		TextView itemName = HolderUtils.get(convertView, R.id.item_name);
		ImageView itemCheckImage = HolderUtils.get(convertView, R.id.item_check);
		String menuSelectItem = getItem(position);
		if (menuSelectItem != null) {
			itemName.setText(menuSelectItem);
			if (checkIndex == position) {
				itemCheckImage.setImageResource(R.drawable.common_select_check);
			} else {
				itemCheckImage
						.setImageResource(R.drawable.common_select_uncheck);
			}

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(itemClickListener != null){
						itemClickListener.onItemClick(null, v, position, 0);
					}
					setCheck(position);
				}
			});

		}

		return convertView;
	}
	
	/**
	 * 获取选中的项
	 * @return
	 */
	public int getCheckPosition() {
		return checkIndex;
	}
	
	/**
	 * 更新选中项
	 * @param position
	 */
	public void setCheck(int position) {
		checkIndex = position;
		notifyDataSetChanged();
	}
}
