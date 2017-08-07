package com.newland.payment.common;

import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.view.KeyEvent;

public class Const {
	
	public static final String YES = "1";
	public static final String NO = "0";
	
	/** 登陆用户名长度 */
	public static final int USER_NO_SIZE = 2;
	

	/** 每页长度 */
	public static final int PAGE_SIZE = 4;
	
	/**签名最大长度*/
	public static final int SIGNATURE_MAX_LEN = 900;
	
	/**签名上送失败标志*/
	public static final String SIGNATURE_FAIL_FLAG = "-fail";
	
	public static class DIGITS {

		/** 大写字母 */
		public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		/** 小写字母 */
		public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
		/** 数字 */
		public static final String NUMBER = "0123456789";
	}
	
	/**
	 * 处理要求
	 * @version 1.0
	 * @author spy
	 * @date 2015年6月2日
	 * @time 下午9:57:43
	 */
	public static class DealRequest{
		public static final int NONE = 0; 				//无
		public static final int PARAMS_TRANSMIT = 1;	//参数传递
		public static final int STATUS_UPLOAD = 2;		//状态上送
		public static final int RELOGIN = 3;			//重新登录
		public static final int CAPK_UPDATE = 4;		//公钥更新
		public static final int AID_DOWNLOAD = 5;		//AID下载
		public static final int TMS_DOWNLOAD = 6;		//TMS下载
		public static final int BLACK_LIST_DOWNLOAD = 7;//黑名单下载
	}
	
	public static class UserType{
		/** 一般操作员 */
		public final static int USER = 1;
		/** 主管 */
		public final static int DIRECTOR = 2;
		/** 系统管理员 */
		public final static int SYSTEM_NAMAGER = 3;
		/** 厂商管理员 */
		public final static int PRODUCER = 4;
	}
	

	public static final String APPS_NAME = "payment";
	public static final String SEPARATOR = java.io.File.separator;

	public static class ScreenType {
		public static final int IM_81 = 1;
		public static final int IM_91 = 2;
	}
	public static class SettingDateType {
		public static final int TIME = 1;
		public static final int DATE = 2;
	}
	public static class PathConst{
		public final static String SDCARD_PATH = Environment
				.getExternalStorageDirectory()  + SEPARATOR + APPS_NAME + SEPARATOR;	
//		public final static String SDCARD_PATH = Environment
//				.getDataDirectory()  + SEPARATOR + APPS_NAME + SEPARATOR;
		public final static String APPS_DATA = SDCARD_PATH + SEPARATOR;
	}
	
	public static class PrintStyleConst{
		/**
		 * 不带英文的中大号签购单
		 */
		public static String PRINT_WATER_WITHOUT_ENGLISH = "PRINT_WATER_WITHOUT_ENGLISH";
		/**
		 * 带英文的中大号签购单
		 */
		public static String PRINT_WATER_WITH_ENGLISH = "PRINT_WATER_WITH_ENGLISH";
		/**
		 * 不带英文的小号签购单
		 */
		public static String PRINT_WATER_WITHOUT_ENGLISH_SMALL = "PRINT_WATER_WITHOUT_ENGLISH_SMALL";
		/**
		 * 带英文的小号签购单
		 */
		public static String PRINT_WATER_WITH_ENGLISH_SMALL = "PRINT_WATER_WITH_ENGLISH_SMALL";
		/**
		 * 结算单
		 */
		public static String PRINT_SETTLE = "PRINT_SETTLE";
		/**
		 * 明细单
		 */
		public static String PRINT_ALL_WATER = "PRINT_ALL_WATER";
		/**
		 * 失败明细单
		 */
		public static String PRINT_FAIL_WATER = "PRINT_FAIL_WATER";
		/**
		 * 汇总单
		 */
		public static String PRINT_TOTAL = "PRINT_TOTAL";
		/**
		 * 商户信息
		 */
		public static String PRINT_PARAM_MERCHANTINFO = "PRINT_PARAM_MERCHANTINFO";
		/**
		 * 版本信息
		 */
		public static String PRINT_PARAM_VERSION = "PRINT_PARAM_VERSION";
		/**
		 * 交易控制参数
		 */
		public static String PRINT_PARAM_TRANSCCTRL = "PRINT_PARAM_TRANSCCTRL";
		/**
		 * 系统控制参数
		 */
		public static String PRINT_PARAM_SYSTEMCTRL = "PRINT_PARAM_SYSTEMCTRL";
		/**
		 * 其他控制参数
		 */
		public static String PRINT_PARAM_OTHER = "PRINT_PARAM_OTHER";
		/**
		 * 通讯参数
		 */
		public static String PRINT_PARAM_COMM = "PRINT_PARAM_COMM";

		
		
	}
public static enum PrintStyleConstEnum{
		/**
		 * 打印流水
		 */
		PRINT_WATER,
		/**
		 * 打印结算单
		 */
		PRINT_SETTLE,
		/**
		 * 打印明细
		 */
		PRINT_ALL_WATER,
		/**
		 * 打印失败流水
		 */
		PRINT_FAIL_WATER,
		/**
		 * 打印汇总单
		 */
		PRINT_TOTAL,
		/**
		 * 打印商户信息
		 */
		PRINT_PARAM_MERCHANTINFO,
		/**
		 * 打印版本号
		 */
		PRINT_PARAM_VERSION,
		/**
		 * 打印交易参数
		 */
		PRINT_PARAM_TRANSCCTRL,
		/**
		 * 打印系统参数
		 */
		PRINT_PARAM_SYSTEMCTRL,
		/**
		 * 打印其他设置
		 */
		PRINT_PARAM_OTHER,
		/**
		 * 打印通讯参数
		 */
		PRINT_PARAM_COMM,
		/**
		 * 打印EMV参数
		 */
		PRINT_PARAMS_EMV,
	}
	
	public static class FileConst{
		/**
		 * 打印模板
		 */
		public final static String PRINT = "print.xml";
		/**
		 * 8583组解包规则模板
		 */
		public final static String CUPS8583 ="CUPS8583.xml";
		/**
		 * 8583组解包规则模板
		 */
		public final static String MPOSCUPS8583 ="MPOSCUPS8583.xml";
		/**
		 * 默认参数文件
		 */
		public final static String PARAMS = "defaultparams.properties";
		/**
		 * 默认LOGO小
		 */
		public final static String LOGO_IMG_SMALL= "CUP64x48.bmp";
		/**
		 * 默认LOGO中
		 */
		public final static String LOGO_IMG_NORMAL= "CUP128x48.bmp";
		/**
		 * 默认LOGO大
		 */
		public final static String LOGO_IMG_LARGE= "CUP370x80.bmp";
		/**
		 * 应答码文件
		 */
		public final static String ANSWERCODE = "answercode.properties";
		/**
		 * 银行码文件
		 */
		public final static String BANKCODE = "bankcode.properties";
		/**
		 * EMV错误码
		 */
		public final static String EMVERRORCODE = "emverrorcode.properties";
		/**
		 * 证书
		 */
		public final static String PEM_CERT = "cacert.pem";
		/**
		 * binA
		 */
		public final static String BINA = "BINA.properties";
		/**
		 * binB
		 */
		public final static String BINB = "BINB.properties";
	}
	
	
	/**
	 * 密钥算法
	 * @author linld
	 * @date 2015-05-20 
	 */
	public static class DesMode{
		public final static String DES = "0";
		public final static String DES3 = "1";
	}
	
	/**
	 * MAC类型
	 * @version 1.0
	 * @author spy
	 * @date 2015年5月16日
	 * @time 上午10:49:47
	 */
	public static class MacType {
		public final static int MAC_TYPE_ECB = 0;
		public final static int MAC_TYPE_X99 = 1;
		public final static int MAC_TYPE_X919 = 2;
		public final static int MAC_TYPE_9606 = 3;
	}
	/**
	 * 
	 * @version 1.0
	 * @author spy
	 * @date 2015年5月16日
	 * @time 上午1:07:54
	 */
	public static class WorkKeyType{
		/**
		 * MAC = 0 <br />
		 * PIN = 1 <br />
		 * TDK = 2 <br />
		 * DATA = 3
		 */
		public static final int MAC = 0;
		public static final int PIN = 1;
		public static final int TDK = 2;
		public static final int DATA = 3;
		
	}
	
	/**
	 * 主密钥索引<p>
	 * 
	 * 各索引若相同则表示使用同一组主密钥索引
	 * @author lance
	 *
	 */
	public static class MKIndexConst{
		
		/**
		 * 主密钥索引
		 */
		public static final int DEFAULT_MK_INDEX = 0x00;//0x01;
	}
	
	/**
	 * 工作密钥类型:{@link WorkingKeyType#PININPUT}
	 * @author lance
	 */
	public static class PinWKIndexConst{
		/**
		 * 默认PIN加密工作密钥索引
		 */
		public static final int DEFAULT_PIN_WK_INDEX = WorkKeyType.PIN + 1;
		
		//public static final int EXTERNAL_PIN_WK_INDEX = 0;
		
	}
	/**
	 * 工作密钥类型:{@link WorkingKeyType#MAC}
	 * @author lance
	 */
	public static class MacWKIndexConst{
		/**
		 * 默认MAC加密工作密钥索引
		 */
		public static final int DEFAULT_MAC_WK_INDEX = WorkKeyType.MAC + 1;
	}
	/**
	 * 工作密钥类型:{@link WorkingKeyType#DATAENCRYPT}
	 * @author lance
	 */
	public static class DataEncryptWKIndexConst{
		/**
		 * 默认磁道加密工作密钥索引
		 */
		public static final int DEFAULT_TRACK_WK_INDEX = WorkKeyType.TDK + 1;
		
		/**
		 * 默认数据加密工作密钥索引
		 */
		public static final int DEFAULT_DATA_WK_INDEX = WorkKeyType.DATA + 1;
		
	}
	
	public static class DeviceErrorCode {

		public static final int DRIVER_NOT_FOUND = 1000;

		public static final int INIT_DRIVER_FAIED = 1001;

		public static final int INIT_DEVICE_PARAMS_FAILED = 1002;

		/**获取磁道内容失败*/
		public static final int GET_TRACKTEXT_FAILED = 1003;

		public static final int GET_PININPUT_FAILED = 1004;

		public static final int GET_KEYBOARD_VALUE_FAILED = 1005;

		public static final int GET_PLAIN_ACCOUNT_NO_FAILED = 1006;

		public static final int MK_INDEX_NOTFOUND_ERROR = 1007;

		public static final int UPDATE_FIRMWARE_FAILED = 1008;

		public static final int LOAD_WORKINGKEY_FAILED = 1009;
	}
	
	public static class DateWheelType {
		
		public static final int YEAR = 0x01;
		
		public static final int MOUTH = 0x02;
		
		public static final int DAY = 0x04;
	}

	
	public static final Map<Integer,Integer> K21_KEYBOARD_KEYMAPPINGS = new HashMap<Integer,Integer>();
	static{
		K21_KEYBOARD_KEYMAPPINGS.put(0x30, KeyEvent.KEYCODE_0);
		K21_KEYBOARD_KEYMAPPINGS.put(0x31, KeyEvent.KEYCODE_1);
		K21_KEYBOARD_KEYMAPPINGS.put(0x32, KeyEvent.KEYCODE_2);
		K21_KEYBOARD_KEYMAPPINGS.put(0x33, KeyEvent.KEYCODE_3);
		K21_KEYBOARD_KEYMAPPINGS.put(0x34, KeyEvent.KEYCODE_4);
		K21_KEYBOARD_KEYMAPPINGS.put(0x35, KeyEvent.KEYCODE_5);
		K21_KEYBOARD_KEYMAPPINGS.put(0x36, KeyEvent.KEYCODE_6);
		K21_KEYBOARD_KEYMAPPINGS.put(0x37, KeyEvent.KEYCODE_7);
		K21_KEYBOARD_KEYMAPPINGS.put(0x38, KeyEvent.KEYCODE_8);
		K21_KEYBOARD_KEYMAPPINGS.put(0x39, KeyEvent.KEYCODE_9);
		K21_KEYBOARD_KEYMAPPINGS.put(0x1C, KeyEvent.KEYCODE_POUND);//"#"
		K21_KEYBOARD_KEYMAPPINGS.put(0x2E, KeyEvent.KEYCODE_STAR);//"*"
		K21_KEYBOARD_KEYMAPPINGS.put(0x0a, KeyEvent.KEYCODE_DEL);//退格键
		K21_KEYBOARD_KEYMAPPINGS.put(0x0d, KeyEvent.KEYCODE_ENTER);//回车键 确认键
		K21_KEYBOARD_KEYMAPPINGS.put(0x1b, KeyEvent.KEYCODE_ESCAPE);//ESC键 取消键
		K21_KEYBOARD_KEYMAPPINGS.put(0xd1, KeyEvent.KEYCODE_BACK);//Back键
	}
	
	public static class ElecSignType{
		/**
		 * 离线类电子签名
		 */
		public static final int ONLINE = 1;
		
		/**
		 * 联机类电子签名
		 */
		public static final int OFFLINE = 2;
		
		/**
		 * 离线类和联机类未上送的签名
		 */
		public static final int ONLINE_OFFLINE_UNSEND = 3;
		/**
		 * 结算上送所有签名,包括成功以及失败
		 */
		public static final int SETTLE_SEND = 4;
	}
	
	public static class SoundType{
		/**
		 * Beep提示音
		 */
		public static final int BEEP = 0;
	
		/**
		 * 长鸣音
		 */
		public static final int LONG_BEEP = 1;
		
		/**
		 * TIP提示音
		 */
		public static final int TIP = 2;
		
		/**
		 * 警示音
		 */
		public static final int WARNING = 3;
	
	}
	
}



