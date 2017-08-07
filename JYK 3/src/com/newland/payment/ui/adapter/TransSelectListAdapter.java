package com.newland.payment.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newland.base.CommonThread;
import com.newland.payment.common.Const;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.fragment.TransSelectListItemFragment;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 交易查询结果适配器
 *
 * @author CB
 * @date 2015-5-22 
 * @time 上午9:22:13
 */
public class TransSelectListAdapter extends FragmentPagerAdapter{
	
	private final List<Water> waters = new ArrayList<Water>();
	private Context context;

	private String strMerchantName;
	private String strMerchantNo;
	private String strPosNo;

	public TransSelectListAdapter(FragmentManager fm, Context context,
			List<Water> waters, String strMerchantName, String strMerchantNo,
			String strPosNo) {
		super(fm);
		this.waters.addAll(waters);
		this.context = context;
		this.strMerchantName = strMerchantName;
		this.strMerchantNo = strMerchantNo;
		this.strPosNo = strPosNo;
	}
	
	

	@Override
	public Fragment getItem(int position) {
		LoggerUtils.i("c:"+getCount() + ",i:" + position);
		//预加载
		if (position == (getCount() - 3)) {
			
			if (getCount() % Const.PAGE_SIZE == 0) {
				

				new CommonThread(new ThreadCallBack() {

					List<Water> addWater;
					
					@Override
					public void onMain() {
						waters.addAll(addWater);
						notifyDataSetChanged();
					}

					@Override
					public void onBackGround() {
						WaterServiceImpl waterServiceImpl = new WaterServiceImpl(
								context);
						
						int page = getCount() / Const.PAGE_SIZE + 1;
						
						LoggerUtils.i("get page " + page);
						
						addWater = waterServiceImpl.findByPage( page, Const.PAGE_SIZE);
						
						
					}
				}).start();
			}
			
		}

		return new TransSelectListItemFragment(waters.get(position),
				strMerchantName, strMerchantNo, strPosNo);
	}

	@Override
	public int getCount() {
		return waters.size();
	}

	public List<Water> getWaters() {
		return this.waters;
	}
}
