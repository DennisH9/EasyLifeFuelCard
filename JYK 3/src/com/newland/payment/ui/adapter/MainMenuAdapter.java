package com.newland.payment.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.base.util.AndroidTools;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ToastUtils;
import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.TransController;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.bean.MainMenuItem;
import com.newland.payment.ui.fragment.LockFragment;
import com.newland.payment.ui.fragment.MainFragment;
import com.newland.payment.ui.fragment.OperatorManagementFragment;
import com.newland.payment.ui.fragment.PrintFragment;
import com.newland.payment.ui.fragment.TransSelectListFragment;
import com.newland.payment.ui.fragment.TransTotalListFragment;
import com.newland.payment.ui.fragment.TransVoucherSearchFragment;

/**
 * 主界面菜单适配器
 *
 * @author CB
 * @time 2015-4-20 上午10:50:21
 */
public class MainMenuAdapter extends BaseListViewAdapter<MainMenuItem>{
	
	private MainActivity activity;
	private Water water;
	String printStatus = null;

	public MainMenuAdapter(List<MainMenuItem> mDataList) {
		super(MainActivity.getInstance(), mDataList);
		this.activity = MainActivity.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mActiviy).inflate(R.layout.main_menu_item, null);
		}
		

		TextView txtName = HolderUtils.get(convertView, R.id.txt_name);
		ImageView ivIcon = HolderUtils.get(convertView, R.id.iv_icon);
		
		final MainMenuItem mainMenuItem = getItem(position);
		
		if (mainMenuItem != null) {
			txtName.setText(mainMenuItem.getName());
			
			ivIcon.setImageResource(mainMenuItem.getIcon());

				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if (ViewUtils.isFastClick()) {
							return;
						}
						
						if (mainMenuItem.getChilds() != null) {
							
							activity.switchContent(new MainFragment(mainMenuItem.getName(), mainMenuItem.getChilds()));
							
							
						} else {
							if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_operator_manage))) {
								
								//柜员管理
								MainActivity.getInstance().switchContent(OperatorManagementFragment.newInstance(true));
								
							} else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_search_consumption_detail))) {
								
								//查询交易明细
								MainActivity.getInstance().switchContent(TransSelectListFragment.newInstance(null));
								
							} else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_print_consumption_all))) {
								

									//打印交易汇总
								activity.commonDialog = MessageUtils.showCommonDialog(mActiviy, R.string.common_print_consumption_all, new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											
											activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_TOTAL, null, null));					

										}
									});
							}
							else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_reprint_trait_last))) {								
								
									//重打上一笔
									WaterServiceImpl impl = new WaterServiceImpl(mActiviy);

									water = impl.findLastWater();
									activity.commonDialog = MessageUtils.showCommonDialog(mActiviy, R.string.common_reprint_trait_last, new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											if(water == null){
												ToastUtils.show(mActiviy, R.string.common_no_water);
												return;
											}
											activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_WATER, null, water));

										}
									});
									
								}
								
							else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_reprint_trait_random))) {
								//打印任一笔
								MainActivity.getInstance().switchContent(new TransVoucherSearchFragment());
		 
							}
							else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_print_consumption_detail))) {
								
								activity.commonDialog = MessageUtils.showCommonDialog(mActiviy, R.string.common_print_consumption_detail, new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											activity.dismissDialog();
											//打印交易明细
											activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_ALL_WATER, null, null));					
											
										}
									});
								
							}
							else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_reprint_clearing_total_list))) {
								
								activity.commonDialog = MessageUtils.showCommonDialog(mActiviy, R.string.common_reprint_clearing_total_list, new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											
											activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_SETTLE, null, null));					
																
										}
									});
				
							}
							else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_search_consumption_all))) {
								
								//查询交易汇总
								MainActivity.getInstance().switchContent(new TransTotalListFragment());
								
							} else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_search_by_evidence))) {
								
								//按凭证号查询
								MainActivity.getInstance().switchContent(new TransVoucherSearchFragment());
								
							} else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_lock_terminal))) {
								
								//锁屏
								MainActivity.getInstance().switchContent(new LockFragment());
								
							} else if (mainMenuItem.getName().equals(mActiviy.getString(R.string.common_version))) {
								
								//版本
								
								String title = "V" + AndroidTools.getApplicationVersionName(mActiviy);
								
								activity.commonDialog = MessageUtils.showCommonDialog(mActiviy,R.string.common_version, title,
								new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										activity.dismissDialog();
									}
								}, null);

								
								
							} else if (mainMenuItem.getName().equals(MainActivity.getInstance().getResources().getString(R.string.common_operator_sign_in))){
								// 操作员签到
								MainActivity.getInstance().returnLogin(true);
							} else {
								AbstractBaseTrans trans = mainMenuItem.getAbstractBaseTrans();
								if (trans != null) {
									// 走流程
									TransController transController = new TransController(mActiviy);
									transController.start(trans);
								}
							}
						} 
					}
				});
			
		}
		
		return convertView;
	}
	
	
}
