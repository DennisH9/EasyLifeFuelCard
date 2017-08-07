package com.newland.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.newland.pos.sdk.util.LoggerUtils;

import android.app.Activity;

/**
 * Activity管理器
 * 
 * @author CB
 * @time 2014年10月27日 上午9:46:06
 */
public class ActivityManager {

    /** 保存所有的activity，在程序退出的时候集中销毁，完全退出程序 */
    private static List<Activity> activitys = new LinkedList<Activity>();
    /**
     * 添加Activity到容器中
     * @param activity
     */
    public static void addActivity(Activity activity)
    {
    	activitys.add(activity);
    }

    /**
     * 销毁某个activity
     * @param activity
     */
    public static void delActivity(Activity activity)
    {
        if (activity !=null && !activity.isFinishing()) {//销毁界面
        	LoggerUtils.d("remove:"+ activity);
            activity.finish();
        }
        activitys.remove(activity);
    }

    /**
     * 获取栈顶的activity
     * @return
     */
    public static Activity getTopActivity() {
        Activity activity = null;
        if (activitys != null && activitys.size() > 0) {
        	activity = activitys.get(activitys.size() - 1);
        }

        return activity;
    }

    /**
     * 结束(finish)所有的activity
     */
    public static void finishAllActivity(){
        //清空Activity
        Iterator<Activity> it = activitys.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (!activity.isFinishing()) {//退出的时候，需要销毁之前所有的界面，这样做主要是为了清理缓存的界面，更时将数据更新为新登录的用户,并不是程序完全退出
            	LoggerUtils.d("finish:"+activity);
                activity.finish();
            }
            it.remove();
        }
    }

    public static void finishAllActivityExcludeActivity(String classSimpleName){
        //清空Activity
        Iterator<Activity> it = activitys.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if(!activity .getClass().getSimpleName().equals(classSimpleName)){
                activity.finish();
                it.remove();
            }
        }
    }
    
    /**
     * 返回当前activity的数量
     */
    public static int size() {
    	return activitys.size();
    }
}
