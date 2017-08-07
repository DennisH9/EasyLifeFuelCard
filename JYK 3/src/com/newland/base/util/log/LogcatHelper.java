package com.newland.base.util.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.newland.base.CommonThread;
import com.newland.base.util.FileUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 将LogCat中当前应用的日志保存到mtms管理的目录下
 * 日志保存在"mnt/sdcard/mtms/log/"目录下
 * 
 * @author cxy
 */

@SuppressLint("SimpleDateFormat")
public class LogcatHelper {

	private final int LOG_COUNT = 7; // 保存最近的7天的日志文件
	private LogDumper mLogDumper = null;
	private int mPId; // 应用进程号
	private boolean isOtherDate;
	private Context context;

	/**
	 * 初始化目录
	 * */
	public void init(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 使用延迟初始化,防止启动时多线程调用实例化多次
	 */
	private static class InnerClass{
		private static final LogcatHelper INSTANCE = new LogcatHelper(staticContext);
	}
	
	/**
	 * 用于第一次实例化使用
	 */
	private static Context staticContext = null;
	
	public static LogcatHelper getInstance(Context context) {
		if (staticContext == null) {
			staticContext = context;
		}
		return InnerClass.INSTANCE;
	}

	private LogcatHelper(Context context) {
		this.context = context;
		mPId = android.os.Process.myPid();
	}

	public void start() {
		if (mLogDumper == null) {

//			new CommonThread(new ThreadCallBack() {
//
//				@Override
//				public void onMain() {
//
//				}
//
//				@Override
//				public void onBackGround() {

					mLogDumper = new LogDumper(
							App.LOG_FILE_PAHT,
							String.valueOf(mPId),
							ParamsUtils.getString(
									ParamsConst.PARAMS_KEY_CONFIG_LOG_LEVEL));

					mLogDumper.start();
//				}
//			}).start();
		} else {
			if(!mLogDumper.isAlive()){
				LoggerUtils.d("mLogDumper is not alive");
				mLogDumper.start();
			}else{
				LoggerUtils.d("mLogDumper is alive");
			}
		}
		isOtherDate = false;
	}

	public void stop() {
		if (mLogDumper != null) {
			mLogDumper.stopLogs();
			mLogDumper = null;
		}
	}

	public boolean isAlive() {
		if (mLogDumper != null
				&& mLogDumper.getState() != Thread.State.TERMINATED) {
			return true;
		} else {
			if (isOtherDate) {
				return true;
			}
		}
		mLogDumper = null;
		return false;
	}

	/**
	 * 
	 * 日志收集线程
	 * 
	 * */
	private class LogDumper extends Thread {

		private Process logcatProc;
		private BufferedReader mReader = null;
		private boolean mRunning = true;
		String cmds = null;
		private String mPID;
		private String cur_date;
		private FileOutputStream out = null;

		@SuppressLint("SdCardPath")
		public LogDumper(String dirPath, String pid, String level) {
			mPID = pid;
			cur_date = getFileName();
			init(dirPath);
			checkOutTimeAndDelectLogFile(dirPath, cur_date);
			String dir = dirPath + cur_date.substring(0, 6) + "/";
			File file = new File(dir);
			if (!file.exists()) {
				LoggerUtils.d("创建文件.......");
				file.mkdirs();
			}

			try {
				out = new FileOutputStream(new File(dir, cur_date + ".log"),
						true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if(StringUtils.isEmpty(level)){
				level = "*:v";
			}

			/**
			 * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s v:Verbose d:Debug i:Info
			 * w:Warn e:Error f:Fatal s:Silent
			 * 
			 * 根据项目调试需要在此调整日志输出级别,示例： cmds = "logcat *:e *:w | grep \"(" + mPID
			 * + ")\""; //打印Error和Warn级别日志信息 cmds = "logcat  | grep \"(" + mPID
			 * + ")\""; //打印所有日志信息 cmds = "logcat -s way"; //打印标签过滤信息
			 * */
			//cmds = "su_no_uid -c logcat -v time ";
			cmds = "logcat " + level + " | grep \"(" + mPID + ")\""; // 默认打印Error级别日志信息
		}

		public void stopLogs() {
			mRunning = false;
		}

		@Override
		public void run() {
			try {
				logcatProc = Runtime.getRuntime().exec(cmds);
				mReader = new BufferedReader(new InputStreamReader(
						logcatProc.getInputStream()), 1024);
				String line = null;
				while (mRunning && (line = mReader.readLine()) != null) {
					if (!mRunning) {
						break;
					}
					if (line.length() == 0) {
						continue;
					}
					if (out != null && line.contains(mPID)) {
						out.write((getDateEN() + "  " + line + "\n").getBytes());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (logcatProc != null) {
					logcatProc.destroy();
					logcatProc = null;
				}
				if (mReader != null) {
					try {
						mReader.close();
						mReader = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					out = null;
				}
				if (mRunning) {
					isOtherDate = true;
					LogcatHelper.getInstance(context).stop();
					LogcatHelper.getInstance(context).start();
				}
			}
		}

		@SuppressLint("SimpleDateFormat")
		private String getFileName() {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String date = format.format(new Date(System.currentTimeMillis()));
			return date;
		}

		private String getDateEN() {
			SimpleDateFormat format1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date1 = format1.format(new Date(System.currentTimeMillis()));
			return date1;
		}

		/**
		 * 检查过期日志并删除
		 * @param dirPath 日志目录
		 * @param curDate 当前日期
		 */
		private void checkOutTimeAndDelectLogFile(String dirPath, String curDate) {
			try {
				File pathFile = new File(dirPath);
				File[] files = pathFile.listFiles();
				String destDate = findLastMD(curDate);
				if (files == null || files.length == 0) {
					return;
				}
				for (File file : files) {
					if (file.getName().compareTo(destDate.substring(0, 6)) < 0) {
						FileUtils.deleteFile(file, true);
					} else if (file.getName().compareTo(
							destDate.substring(0, 6)) == 0) {
						File[] logFiles = file.listFiles();
						for (File logFile : logFiles) {
							if (destDate.compareTo(logFile.getName().replace(
									".log", "")) >= 0) {
								logFile.delete();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String findLastMD(String curDate) {
			int y = Integer.valueOf(curDate.substring(0, 4));
			int m = Integer.valueOf(curDate.substring(4, 6));
			int d = Integer.valueOf(curDate.substring(6, 8));
		
			d = d - LOG_COUNT;
			if (d <= 0) {
				d = 30 + d;
				if (m == 1) {
					y = y - 1;
					m = 12;
				} else {
					m = m - 1;
				}
			}
			
			String year = String.valueOf(y);
			String mounth = String.valueOf(m);
			mounth = mounth.length() == 1 ? ("0" + mounth) : mounth;
			String day = String.valueOf(d);
			day = day.length() == 1 ? ("0" + day) : day;

			return year + mounth + day;
		}
	}
}