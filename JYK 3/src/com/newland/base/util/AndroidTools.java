package com.newland.base.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.newland.payment.ui.activity.BaseActivity;
import com.newland.payment.ui.fragment.BaseFragment;
import com.newland.payment.ui.fragment.MainFragment;
import com.newland.pos.sdk.util.LoggerUtils;


/**
 * 关于android的一些工具类<br/>
 * 包括：<br/>
 * 1.判断是否联网 {@link #isHaveInternet(Context)}　<br/>
 * 2.判断是否WIFI联网 {@link #isWifiConnect(Context)}<br/>
 * 3.判断是否TYPE_MOBILE联网 {@link #isMobileConnect(Context)} <br/>
 * 4.获取当前应用版本号 {@link #getApplicationVersionName(Context)}<br/>
 * 5.当前应用是否再后台允许 {@link #isInBackground(Context)}<br/>
 * 6.当前应用重启 {@link #reStartApp(Context)}<br/>
 * 7.获取栈顶Fragment {@link #getTopFragment(BaseActivity)}<br/>
 * 8.获取堆栈中的所有fragment {@link #getFragments(BaseActivity)}<br/>
 * 9.获取堆栈中的MainFragment对象数量 {@link #getMainFragmentsCount(BaseActivity)}<br/>
 * 10.设置是否显示系统状态栏 {@link #setStatusBarVisiable(Activity, boolean)}<br/>
 *
 * @author michael
 * @version 1.0
 */
public class AndroidTools {

    private static ConnectivityManager connectivityManager;
    private static NetworkInfo getNetworkInfo(Context context){
		if(connectivityManager==null){
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return connectivityManager.getActiveNetworkInfo();
	}
    
    /**
	 * 判断是否联网，无视联网方式
	 * @param context
	 * @return
	 */
	public static boolean isHaveInternet(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info!=null&&info.isConnected()) {
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 判断是否是wifi方式联网
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnect(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info!=null&&info.getType() == ConnectivityManager.TYPE_WIFI&&info.isConnected()) {
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * 判断是否为手机的联网方式（GPRS,UMTS等）
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnect(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info!=null&&info.getType() == ConnectivityManager.TYPE_MOBILE&&info.isConnected()) {
			return true;
		}
		else{
			return false;
		}
	}

    /**
     * 获取当前应用版本号
     * @param context
     * @return 当前应用版本号，在androidMainfest.xml中配置的版本号
     */
    public static String getApplicationVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version ="";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if(packInfo!=null){
            version = packInfo.versionName;
        }
        return version;
    }

    /**
     * 是否切换到 后台了
     * @param context
     * @return false 当前应用在前台，true 当前应用在后头
     */
    public static boolean isInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    LoggerUtils.i("前台:"+appProcess.processName+"|appProcess.importance:" +appProcess.importance);
                    return false;
                }else{
                    LoggerUtils.i("后台:"+appProcess.processName+"|appProcess.importance:" +appProcess.importance);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
	 * 应用立即重启
	 * @param context 
	 */
	public static void reStartApp(Context context){
		reStartApp(context, 0);
	}

	/**
	 * 应用延时重启
	 * @param context
	 * @param startDelay 启动延迟(单位毫秒)
	 */
	public static void reStartApp(Context context, long startDelay){
		//用本应用的包名获取本应用的启动Intent
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), -1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + startDelay, restartIntent); 
	}
	
	/**
	 * 获取指定acivity中fragment栈顶的fragment
	 * @param acivity  指定acivity
	 * @return
	 */
	public static Fragment getTopFragment(BaseActivity acitivy){
		List<Fragment> fragments = acitivy.getSupportFragmentManager().getFragments();
		if (fragments == null || fragments.isEmpty()) {
			return null;
		}
		int size = fragments.size();
		BaseFragment fragment = null;
		for (int i = size - 1; i >=0; i--) {
			fragment = (BaseFragment) fragments.get(i);
			if (fragment != null) {
				break;
			}
		}
		return fragment;
	}
	
	/**
	 * 获取指定activity的fragment堆栈中的所有fragment
	 * @param fragmentActivity
	 * @return
	 */
	public static List<Fragment> getFragments(BaseActivity fragmentActivity){
		return fragmentActivity.getSupportFragmentManager().getFragments();
	}
	
	/**
	 * 获取指定activity的fragment堆栈中的MainFragment对象数量
	 * @param fragmentActivity
	 * @return
	 */
	public static int getMainFragmentsCount(BaseActivity fragmentActivity){
		
		int count = 0;
		List<Fragment> fragments = fragmentActivity.getSupportFragmentManager().getFragments();
		if (fragments != null && !fragments.isEmpty()) {
			int size = fragments.size();
			BaseFragment fragment = null;
			for (int i = size - 1; i >=0; i--) {
				fragment = (BaseFragment) fragments.get(i);
				if (fragment instanceof MainFragment) {
					count++ ;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * 设置activity界面上的状态栏是否显示
	 * @param activity
	 * @param isVisiable
	 */
	public static void setStatusBarVisiable(Activity activity, boolean isVisiable) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (isVisiable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(lp);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
