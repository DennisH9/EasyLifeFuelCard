package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.adapter.TransSelectListAdapter;
import com.newland.pos.sdk.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易明细查询界面
 *
 * @author CB
 * @date 2015-5-16
 * @time 上午10:12:57
 */
public class TransSelectListFragment extends BaseFragment{

	/** 上一页 */
//	@ViewInject(R.id.txt_back_page)
	TextView txtBackPage;
	/** 下一页 */
//	@ViewInject(R.id.txt_next_page)
	TextView txtNextPage;
	/** 打印 */
//	@ViewInject(R.id.txt_print)
	TextView txtPrint;
	/** 搜索结果*/
//	@ViewInject(R.id.txt_search_waters_result)
	TextView txtSearchResult;
	/** 图标布局 */
//	@ViewInject(R.id.ll_right)
	LinearLayout llRight;
	/**  */
//	@ViewInject(R.id.view_pager)
	ViewPager viewPager;
	/**滑动提示 */
//	@ViewInject(R.id.slid_prompt)
	LinearLayout layout;
	private Water water;
	private List<Water> waters;
	private TransSelectListAdapter transSelectListAdapter;
	private ThirdInvokeBean thirdInvokeBean;
	private boolean isThirdinvoke = false;
	private int count;

	public static TransSelectListFragment newInstance(Water waterSearched) {
		TransSelectListFragment fragment = new TransSelectListFragment();
		fragment.water = waterSearched;
		return fragment;
	}
	public static TransSelectListFragment newInstance(Water waterSearched, boolean isThirdinvoke,ThirdInvokeBean thirdInvokeBean) {
		TransSelectListFragment fragment = new TransSelectListFragment();
		fragment.water = waterSearched;
		fragment.isThirdinvoke = isThirdinvoke;
		fragment.thirdInvokeBean = thirdInvokeBean;
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			mFragmentView = inflater.inflate(R.layout.trans_select_list_fragment,
					null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		txtBackPage = (TextView)mFragmentView.findViewById(R.id.txt_back_page);
		txtNextPage = (TextView)mFragmentView.findViewById(R.id.txt_next_page);
		txtPrint = (TextView)mFragmentView.findViewById(R.id.txt_print);
		txtSearchResult = (TextView)mFragmentView.findViewById(R.id.txt_search_waters_result);
		viewPager = (ViewPager)mFragmentView.findViewById(R.id.view_pager);
		layout = (LinearLayout)mFragmentView.findViewById(R.id.slid_prompt);

		if (water != null) {
			txtBackPage.setVisibility(View.GONE);
			txtNextPage.setVisibility(View.GONE);
		}


		setTitle(R.string.common_search_consumption_detail);

		new CommonThread(new ThreadCallBack() {

			@Override
			public void onMain() {
				viewPager.setAdapter(transSelectListAdapter);
				transSelectListAdapter.notifyDataSetChanged();
				if (waters.size() == 0) {
					txtSearchResult.setText(R.string.common_no_water);
					txtPrint.setVisibility(View.INVISIBLE);
					txtBackPage.setVisibility(View.INVISIBLE);
					txtNextPage.setVisibility(View.INVISIBLE);
					viewPager.setVisibility(View.GONE);
					layout.setVisibility(View.GONE);
				} else {
					if (count != 0) {
						txtSearchResult.setText((viewPager.getCurrentItem())+1 + "/" + count );
					}

				}
				txtBackPage.setEnabled(false);
				txtBackPage.setBackgroundResource(R.drawable.commom_disenabled);

				if (count == 1 ){
					txtNextPage.setEnabled(false);
					txtNextPage.setBackgroundResource(R.drawable.commom_disenabled);
				}
				if(isThirdinvoke){
					final Water waterSelect = transSelectListAdapter.getWaters().get(viewPager.getCurrentItem());

					if (water == null) {
						activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_WATER, null, waterSelect));

					}
					else {
						activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_WATER, null, water));
					}
					MainActivity.getInstance().backFragment();
				}

			}

			@Override
			public void onBackGround() {

				String strMerchantName = ParamsUtils.getString(
						ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME);
				String strMerchantNo = ParamsUtils.getShopId();
				String strPosNo = ParamsUtils.getPosId();
				if (water == null) {
					//如果是查询交易明细
					WaterService waterService = new WaterServiceImpl(
							context);
					waters = waterService.findByPage( 0, Const.PAGE_SIZE);
					count = waterService.getWaterCount();

					transSelectListAdapter = new TransSelectListAdapter(
							getChildFragmentManager(), context, waters,
							strMerchantName, strMerchantNo, strPosNo);

				} else {
					//如果是查询凭证号
					waters = new ArrayList<Water>();
					if (water != null) {
						waters.add(water);
					}
					transSelectListAdapter = new TransSelectListAdapter(
							getChildFragmentManager(), context, waters,
							strMerchantName, strMerchantNo, strPosNo);

				}


			}
		}).start();
	}

//	@OnClick({R.id.txt_back_page, R.id.txt_print,
//		R.id.txt_next_page})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_back_page) {
			if (viewPager.getCurrentItem() == 0) {
//				ToastUtils.show(context, R.string.common_back_page_last);

			} else {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

			}

		} else if (i == R.id.txt_print) {
			final Water waterSelect = transSelectListAdapter.getWaters().get(viewPager.getCurrentItem());

			if (water == null) {
				activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_WATER, null, waterSelect));

			} else {
				activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_WATER, null, water));

			}


		} else if (i == R.id.txt_next_page) {
			if (viewPager.getCurrentItem() == transSelectListAdapter.getCount() - 1) {
//				ToastUtils.show(context, R.string.common_next_page_last);
			} else {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

			}

		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				txtSearchResult.setText((viewPager.getCurrentItem())+1 + "/" + count );

				if (position == 0) {
					txtBackPage.setEnabled(false);
					txtBackPage.setBackgroundResource(R.drawable.commom_disenabled);
				} else {
					txtBackPage.setEnabled(true);
					txtBackPage.setBackgroundResource(R.drawable.common_btn_blue);
				}

				if (position == count - 1 ){
					txtNextPage.setEnabled(false);
					txtNextPage.setBackgroundResource(R.drawable.commom_disenabled);
				} else {
					txtNextPage.setEnabled(true);
					txtNextPage.setBackgroundResource(R.drawable.common_btn_blue);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	@Override
	   public void onDetach() {
	    super.onDetach();
		if(isThirdinvoke){
			try {
				synchronized(thirdInvokeBean) {
					thirdInvokeBean.notify();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    try {
	        java.lang.reflect.Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);
	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	   }
}
