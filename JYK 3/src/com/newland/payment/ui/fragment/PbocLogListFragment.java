package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.PbocDetailBean;

import java.util.List;

/**
 * 电子现金交易明细
 *
 * @author spy
 * @date 2015-5-16 
 * @time 上午10:12:57
 */
@SuppressLint("ValidFragment")
public class PbocLogListFragment extends BaseFragment{

	/** 上一页 */
//	@ViewInject(R.id.txt_back_page)
	TextView txtBackPage;
	/** 下一页 */
//	@ViewInject(R.id.txt_next_page)
	TextView txtNextPage;
	
//	@ViewInject(R.id.txt_print)
	TextView txtPrint;
	/**  */
//	@ViewInject(R.id.view_pager)
	ViewPager viewPager;
//	@ViewInject(R.id.txt_search_waters_result)
	TextView txtSearchWatersResult;
	
	/**滑动提示 */
//	@ViewInject(R.id.slid_prompt)
	LinearLayout layout;
	
	private List<PbocDetailBean> beans;
	private FragmentPagerAdapter pbocLogListAdapter;
	private CommonBean<List<PbocDetailBean>> bean;
	
	public PbocLogListFragment(long timeOut) {
		super(timeOut);
	}
	public static PbocLogListFragment newInstance(CommonBean<List<PbocDetailBean>> bean){
		PbocLogListFragment fragment = new PbocLogListFragment(bean.getTimeOut());
		fragment.bean = bean;
		fragment.beans = bean.getValue();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			mFragmentView = inflater.inflate(R.layout.pboc_log_list_fragment,
					null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(bean.getTitle());
		
		txtPrint.setVisibility(View.GONE);
		
		new CommonThread(new ThreadCallBack() {

			@Override
			public void onMain() {
				viewPager.setAdapter(pbocLogListAdapter);
				pbocLogListAdapter.notifyDataSetChanged();
				if (pbocLogListAdapter.getCount() == 0) {
					layout.setVisibility(View.GONE);
				}
				
				txtBackPage.setEnabled(false);
				txtBackPage.setBackgroundResource(R.drawable.commom_disenabled);
	
				if (beans.size() == 1 ){
					txtNextPage.setEnabled(false);
					txtNextPage.setBackgroundResource(R.drawable.commom_disenabled);
				}
			}

			@Override
			public void onBackGround() {
				pbocLogListAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
					
					@Override
					public int getCount() {
						if(beans == null)
							return 0;
						return beans.size();
					}
					
					@Override
					public Fragment getItem(int arg0) {
						if(beans == null)
							return new PbocLogItemFragment(new PbocDetailBean(), bean);
						txtSearchWatersResult.setText((viewPager.getCurrentItem() + 1) +"/" + beans.size());
						return new PbocLogItemFragment(beans.get(arg0), bean);
					}
				};
				
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

			} else {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

			}

		} else if (i == R.id.txt_print) {
		} else if (i == R.id.txt_next_page) {
			if (viewPager.getCurrentItem() == pbocLogListAdapter.getCount() - 1) {

			} else {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

			}

		}
	}

	@Override
	protected void initEvent() {
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {

				txtSearchWatersResult.setText((viewPager.getCurrentItem())+1 + "/" + beans.size() );
				
				if (position == 0) {
					txtBackPage.setEnabled(false);
					txtBackPage.setBackgroundResource(R.drawable.commom_disenabled);
				} else {
					txtBackPage.setEnabled(true);
					txtBackPage.setBackgroundResource(R.drawable.common_btn_blue);
				}
				
				if (position == beans.size() - 1 ){
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
	public BaseBean getBean() {
		return this.bean;
	}
}
