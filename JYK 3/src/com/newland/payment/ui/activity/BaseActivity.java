package com.newland.payment.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.ActivityManager;
import com.newland.base.util.AndroidTools;
import com.newland.payment.R;
import com.newland.payment.ui.fragment.BaseFragment;
import com.newland.payment.ui.fragment.LoginFragment;
import com.newland.payment.ui.fragment.MainFragment;
import com.newland.payment.ui.fragment.OperatorManagementFragment;
import com.newland.payment.ui.fragment.SystemManageFragment;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 基础类 继承该类需将setContentView方法写在super.onCreate(savedInstanceState);前面
 * 
 * @ClassName: BaseYYFragmentActivity
 * @Description: 可以使用android.support.v4.app.Fragment的Activity基础类
 * @author jby
 * @date 2014年9月10日 下午2:00:40
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

	/** 默认状态为空 */
	public static final int STATUS_NONE = 0;
	public static final int STATUS_CREATE = 1;
	public static final int STATUS_START = 2;
	public static final int STATUS_RESUME = 3;
	public static final int STATUS_PAUSE = 4;
	public static final int STATUS_STOP = 5;
	public static final int STATUS_DESTROY = 6;
	public static final int STATUS_RESTART = 7;

	protected Context context;
	protected BaseActivity activity;
	/** 状态 **/
	protected int status = STATUS_NONE;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		status = STATUS_CREATE;
		super.onCreate(savedInstanceState);
		this.context = this;
		this.activity = this;
		fragmentManager = activity.getSupportFragmentManager();
		ActivityManager.addActivity(this);
		ViewUtils.inject(this);
		initData();
		initEvent();
	}

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 初始化点击事件
	 * 
	 * @return 返回"是否执行中" true为执行中,false为未执行中
	 */
	protected abstract void initClickEvent(View view);

	/**
	 * 初始化事件
	 */
	protected abstract void initEvent();

	/**
	 * 界面刷新数据
	 */
	protected void refresh() {
	};

	@Override
	public void onBackPressed() {
		// 一些回收资源的处理放到finish()中去,有的时候不一定是返回方式,可能直接调用finish()
		LoggerUtils.v(getClass().getSimpleName() + "->onBackPressed");
		setResult(android.app.Activity.RESULT_CANCELED);
		super.onBackPressed();
	}

	/**
	 * 获取activity目前状态
	 */
	public int getStatus() {
		return status;
	}

	@Override
	protected void onResume() {
		status = STATUS_RESUME;
		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		super.onResume();
		refresh();
	}

	@Override
	protected void onPause() {
		status = STATUS_PAUSE;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		status = STATUS_DESTROY;
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		status = STATUS_START;
		super.onStart();
	}

	@Override
	protected void onStop() {
		status = STATUS_STOP;
		super.onStop();
	}

	@Override
	protected void onRestart() {
		status = STATUS_RESTART;
		super.onRestart();
	}

	@Override
	public void finish() {
		super.finish();
	}

	/** 监听触屏事件 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {// 每一次操作屏幕，都要把值改回初始值，为60秒，1分钟
			MainActivity.getInstance().resetProgress();
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置内容区域的fragment
	 */
	public void switchContent(Fragment fragment) {
		switchContent(fragment, true);
	}

	/**
	 * 设置内容区域的fragment
	 */
	public void switchContent(Fragment fragment, boolean isAddToBack) {
		try {
			BaseFragment topFragment = (BaseFragment)AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
				// fragment被覆盖时调用onHide
				topFragment.onFragmentHide();
			}
			Fragment currentFragment = fragment;
			if (currentFragment != null) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				String simpleName = currentFragment.getClass().getSimpleName();
				fragmentTransaction.add(R.id.fl_fragment, currentFragment,
						simpleName);
				if (isAddToBack) {
					fragmentTransaction.addToBackStack(simpleName);
				}
				fragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
				
				((BaseFragment) currentFragment).onFragmentShow();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回设置首页fragment
	 */
	public void returnSettingMain() {
		try {
			BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
				topFragment.onFragmentHide();
			}
			FragmentManager fragmentManager = getSupportFragmentManager();
			String excludeName = SystemManageFragment.class.getSimpleName();
			boolean res = fragmentManager.popBackStackImmediate(excludeName, 0);
			if (!res) {
				switchContent(new SystemManageFragment());
			} else {
				topFragment = (BaseFragment)AndroidTools.getTopFragment(activity);
				if (topFragment!= null) {
					((BaseFragment) topFragment).onFragmentShow();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 返回登录fragment
	 * @param isManage 是否为操作员签到
	 */
	public void returnLogin(boolean isManage) {
		try {
			BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
				topFragment.onFragmentHide();
			}
			FragmentManager fragmentManager = getSupportFragmentManager();
			String excludeName = LoginFragment.class.getSimpleName();
			boolean res = fragmentManager.popBackStackImmediate(excludeName, 0);
			if (!res) {
				switchContent(new LoginFragment(isManage));
			} else {
				topFragment = (BaseFragment)AndroidTools.getTopFragment(activity);
				if (topFragment instanceof LoginFragment) {
					MainActivity.getInstance().hideTitle();
				}
				if (topFragment!= null) {
					((BaseFragment) topFragment).onFragmentShow();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 锁屏界面跳回00管理员界面
	 */
	public void returnManager() {
		try{
			BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
				topFragment.onFragmentHide();
			}
			String excludeName = LoginFragment.class.getSimpleName();		
			fragmentManager.popBackStackImmediate(excludeName, 0);
			switchContent(new OperatorManagementFragment(false));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回首页fragment
	 */
	public void returnMainMenu() {
		try{
			BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
				topFragment.onFragmentHide();
			}
			String excludeName = MainFragment.class.getSimpleName();
			boolean res = fragmentManager.popBackStackImmediate(excludeName, 0);
			if (!res) {
				switchContent(new MainFragment(R.string.common_main, App.mainMenuData.MAIN_MENU_ITEMS));
			} else {
				topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
				if (topFragment!= null){
					topFragment.onFragmentShow();
					int mainCount = AndroidTools.getMainFragmentsCount(activity);
					if (mainCount < 2) {
						((MainActivity) activity).setMainStyle();
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 返回第一层首页fragment
	 */
	public void returnFirstMainMenu() {
		try{
			BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
			if (topFragment != null && topFragment.isShowing()) {
					topFragment.onFragmentHide();
			}
			String excludeName = MainFragment.class.getSimpleName();
			boolean res = fragmentManager.popBackStackImmediate(excludeName, 0);
			if (!res) {
				switchContent(new MainFragment(R.string.common_main, App.mainMenuData.MAIN_MENU_ITEMS));
			} else {
				int mainCount = AndroidTools.getMainFragmentsCount(activity);
				if (mainCount > 1) {
					for (int i = 0; i < mainCount - 1; i++) {
						fragmentManager.popBackStackImmediate();
					}
				}	
				topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
				if (topFragment!= null) {
					((BaseFragment) topFragment).onFragmentShow();
				}
				MainActivity.getInstance().setMainStyle();
				MainActivity.getInstance().setTitle(R.string.common_main);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * (出栈)将栈中前N个fragment界面销毁
	 * @param n
	 */
	public void popBackFragment(int n){
		try {
			int count = fragmentManager.getBackStackEntryCount();
			if (n > count) {
				n = count;
			}
			BaseFragment topFragment = null;
			for (int i=0; i<n; i++) {
				topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
				if (topFragment != null) {
					if (topFragment.isShowing()) {
						topFragment.onFragmentHide();
					}
					fragmentManager.popBackStackImmediate();
				}
			}
			if (n < count) {
				topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
				if (topFragment!=null) {
					topFragment.onFragmentShow();
				}
			}
			int mainCount = AndroidTools.getMainFragmentsCount(activity);
			if (topFragment instanceof MainFragment && mainCount < 2) {
				((MainActivity) activity).setMainStyle();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * fragment返回
	 */
	public void backFragment() {
		popBackFragment(1);
	}

}
