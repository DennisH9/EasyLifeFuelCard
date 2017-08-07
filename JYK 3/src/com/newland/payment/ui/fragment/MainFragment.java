package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.trans.RequireHandler;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.adapter.MainMenuAdapter;
import com.newland.payment.ui.bean.MainMenuData;
import com.newland.payment.ui.bean.MainMenuItem;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 收单主界面
 *
 * @author CB
 * @time 2015-5-6 上午10:16:40
 */
@SuppressLint("ValidFragment")
public class MainFragment extends BaseFragment implements Observer{

	/** 91菜单 */
//	@ViewInject(R.id.gv_menu)
	GridView gvMenu;
	private List<MainMenuItem> mainMenuItems;
	private MainMenuAdapter mainMenuAdapter;
	private String title;

	public MainFragment(String title, List<MainMenuItem> mainMenuItems) {
		this.mainMenuItems = mainMenuItems;
		this.title = title;
	}
	public MainFragment(int title, List<MainMenuItem> mainMenuItems) {
		this.mainMenuItems = mainMenuItems;
		this.title = MainActivity.getInstance().getString(title);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.main_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView; 
	}

	@Override
	protected void initData() {
		// 设置头部
		
		setTitle(title);
		App.mainMenuData.addObserver(this);
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			setPagerAdapter(mainMenuItems);
		} else {
			setGridAdapter(mainMenuItems);
		}
	}

	@Override
	protected void initEvent() {

	}
	
	@Override
	protected void initClickEvent(View view) {
		
	}


	@Override
	public BaseBean getBean() {
		return null;
	}
	
	public void setGridAdapter(List<MainMenuItem> items) {
		mainMenuAdapter = new MainMenuAdapter(items);
		gvMenu.setAdapter(mainMenuAdapter);
	}
	
	
	public void setPagerAdapter(List<MainMenuItem> items) {
		
		List<List<MainMenuItem>> lists = new ArrayList<List<MainMenuItem>>();
		List<MainMenuItem> tempLists = new ArrayList<MainMenuItem>();
		
		for (int i = 0; i < items.size(); i++) {
			if (tempLists.size() == 8) {
				lists.add(tempLists);
				tempLists = new ArrayList<MainMenuItem>();
			}
			tempLists.add(items.get(i));
		}
		
		if (tempLists.size() > 0) {
			lists.add(tempLists); 
		}
		
	}
	
	@Override
	public void onFragmentShow() {
		super.onFragmentShow();
		LoggerUtils.d("111 Main onFragmentShow-------------App.User:" + App.USER);
//		if(App.mainMenuData.MAIN_MENU_ITEMS.equals(mainMenuItems)){
			RequireHandler.getInstance().request();
//		}

	}
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		
	}
	@Override
	public void update(Observable observable, Object data) {
		MainActivity.getInstance().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (App.SCREEN_TYPE == Const.ScreenType.IM_91) {
					List<MainMenuItem> tempList = MainMenuData.findDataByName(title,  App.mainMenuData.MAIN_MENU_ITEMS);
					if (tempList != null) {
						mainMenuItems.clear();
						mainMenuItems.addAll(tempList);
						mainMenuAdapter.notifyDataSetChanged();
					}
				} else {
					setPagerAdapter(App.mainMenuData.MAIN_MENU_ITEMS);
				}
			}
		});
		
	}
	
	@Override
	public void onDestroy() {
		App.mainMenuData.deleteObserver(this);
		super.onDestroy();
	}
}
