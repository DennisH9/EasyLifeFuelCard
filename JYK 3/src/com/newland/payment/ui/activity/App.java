package com.newland.payment.ui.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Instrumentation;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.newland.base.ActivityManager;
import com.newland.base.util.DisplayUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.log.LogcatHelper;
import com.newland.mtms.inf.IAppEventManager;
import com.newland.mtms.inf.IParamsManager;
import com.newland.mtms.inf.MtmsManagerFactory;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.ScreenType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.mvc.model.User;
import com.newland.payment.ui.bean.MainMenuData;
import com.newland.payment.ui.bean.MainMenuItem;
import com.newland.pos.sdk.util.LoggerUtils;

public class App extends Application {

	private static App app;
	/** 界面超时时间（秒） */
	public static Long FRAGMENT_TIME = 60l;
	/** 当前登陆的操作员 */
	public static User USER;
	/** 打印模板数据 */
//	public static PrinterXmlParse printerXmlParse;
	
	/** 应用事件管理*/
	public static IAppEventManager appEventManager;
	/** 参数管理 */
	public static IParamsManager paramsManager;
	/** 电子签名jbig格式文件存储目录 */
	public static String SIGNATURE_JBIG_DIR;
	/** 电子签名bmp格式文件存储目录 */
	
	public static String LOG_FILE_PAHT;
	public static String SIGNATURE_BMP_DIR;
	public static String PRINT_LOGO_DIR;

	public static final MainMenuData mainMenuData = new MainMenuData();
	
	//屏幕类型
	public static int SCREEN_TYPE = ScreenType.IM_91;
	// 获取单例
	public static App getInstance() {
		return app;
	}

	@Override
	public void onCreate() {
		app = this;
		super.onCreate();
		
		appEventManager = MtmsManagerFactory.getInstance(this).getAppEventManager();
		paramsManager = MtmsManagerFactory.getInstance(this).getParamsManager();

		SIGNATURE_JBIG_DIR = MtmsManagerFactory.getInstance(this).getFilePathManager().getSignaturesPath() + "/jbig/";
		
		SIGNATURE_BMP_DIR = MtmsManagerFactory.getInstance(this).getFilePathManager().getSignaturesPath() + "/bmp/";
		
		PRINT_LOGO_DIR = MtmsManagerFactory.getInstance(this).getFilePathManager().getScriptPath() + "/bmp/";
		
		LOG_FILE_PAHT = MtmsManagerFactory.getInstance(this).getFilePathManager().getLogPath();
		
		/** 记录日志 */
		LoggerUtils.configPrint(true);
		LogcatHelper.getInstance(this).start();
		
		//启动界面初始化数据任务
		SCREEN_TYPE = DisplayUtils.getScreenType(app) == 1 ? Const.ScreenType.IM_91 : Const.ScreenType.IM_81;
		DisplayMetrics metrics = DisplayUtils.getDiaplay(this);;
		LoggerUtils.i("density:"+metrics.density+",widthPixels:"+metrics.widthPixels+",heightPixels:"+metrics.heightPixels);
		
		// 初始化默认操作员
		USER = new User();
		String remeberedUserNo = ParamsUtils.getString(ParamsConst.PARAMS_REMEMMBER_NO);
		App.USER.setUserNo(remeberedUserNo);
	}

	/**
	 * 检查菜单项是否需要显示
	 */
	public static void checkMenu(List<MainMenuItem> mainMenuItems) {
		
		for (int i = mainMenuItems.size()-1; i >= 0; i--) {
			MainMenuItem mainMenuItem = mainMenuItems.get(i);

			// 从配置文件检测是否需要加载该节点
			if (!mainMenuItem.getParamsKey().equals(ParamsConst.PARAMS_NULL)
					&& "0".equals(ParamsUtils.getString(
							mainMenuItem.getParamsKey()))) {
				mainMenuItems.remove(i);
			} else if (mainMenuItem.getChilds() != null) {
				checkMenu(mainMenuItem.getChilds());
				if(mainMenuItem.getChilds() == null || mainMenuItem.getChilds().size() == 0){
					mainMenuItems.remove(i);
				}
			}
		}
	}

	public Handler getAppHanlder() {
		return handler;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x533:
				final int key = (Integer) msg.obj;
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Instrumentation inst = new Instrumentation();
							inst.sendKeyDownUpSync(Const.K21_KEYBOARD_KEYMAPPINGS.get(key));
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 退出程序
	 * 
	 * 退出程序只需要把Activity全部销毁就可以，application系统过段时间会自动回收，不会马上销毁
	 * 要想完整的退出程序，在Activity跳转的时候及时销毁没用的，对Activity生命周期进行严格的控制
	 */
	public void exit() {
		ActivityManager.finishAllActivity();
	}

}
