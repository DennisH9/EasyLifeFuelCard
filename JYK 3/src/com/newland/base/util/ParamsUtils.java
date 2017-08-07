package com.newland.base.util;

import java.util.Map;

import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 参数读写统一接口
 * 若存在MTMS客户端，参数保存在系统目录中文件，应用删除参数不丢失
 * 若不存在MTMS客户端，参数默认保存在应用目录中文件，应用删除参数丢失
 * @author chenkh 
 * @date 2015-03-12
 */
public class ParamsUtils {

	/**
	 * 参数保存到文件
	 * @param paramsMap
	 */
	public static int save(Map<String, String> paramsMap) {
		return App.paramsManager.commit(paramsMap)?1:-1;
	}

	/**
	 * 将文件中的参数清空
	 */
	public static boolean clean() {
		return App.paramsManager.delete();
	}

	/**
	 * 获取参数文件中所有参数
	 * @return
	 */
	public static Map<String, String> get() {
		return App.paramsManager.query();
	}

	/**
	 * 获取参数文件中指定参数（用于获取字符串）
	 */
	public static String getString(String key) {
		return getString(key, null);
	}
	
	/**
	 * 获取参数文件中指定参数（用于获取字符串）
	 */
	public static String getString(String key, String defaultValue) {
		String value = App.paramsManager.query(key);
		value = value == null ? defaultValue : value;
		LoggerUtils.i("Preferences get->key:"+key+", value:"+value);
		return value;
	}
	
	/**
	 * 设置字符串的参数
	 * @param key
	 * @param value
	 * @return
	 */
	public static int setString(String key, String value) {
		LoggerUtils.i("Preferences Set->key:"+key+",value:"+value);
		return App.paramsManager.commit(key, value)?1:-1;
	}

	/**
	 * 设置boolean的参数
	 * @param key 参数key
	 * @param value 默认值
	 * @return
	 */
	public static int setBoolean(String key, boolean value) {
		try {
			if (key != null && !"".equals(key)) {
				LoggerUtils.i("Preferences Set->key:"+key+",value:"+value);
				String vString = value?"1":"0";
				return App.paramsManager.commit(key, vString)?1:-1;
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 获取boolean的参数，默认值false
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	/**
	 * 获取boolean的参数
	 * @param key 参数key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		try {
			boolean value = false;
			if (key != null && !"".equals(key)) {
				String v_string = App.paramsManager.query(key);
				LoggerUtils.i("Preferences get->key:"+key+",value:"+v_string);
				if("1".equals(v_string)) {
					value = true;
				} else if ("0".equals(v_string)){
					value = false;
				} else {
					value = defaultValue;
				}
			}
			return value;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取参数文件中指定key的参数，默认值0
	 */
	public static long getLong(String key) {
		return getLong(key, 0);
	}
	
	/**
	 * 获取long的参数
	 * @param key 参数key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static long getLong(String key, long defaultValue) {
		try {
			long value = defaultValue;
			if (key != null && !"".equals(key)) {
				String v_string = App.paramsManager.query(key);
				value = Long.valueOf(v_string);
			}
			return value;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	

	/**
	 * 获取参数文件中指定key的参数，默认值0
	 */
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	/**
	 * 获取int的参数
	 * @param key 参数key
	 * @param defaultValue 默认值
	 */
	public static int getInt(String key, int defaultValue) {
		try {
			int value = defaultValue;
			if (key != null && !"".equals(key)) {
				String v_string = App.paramsManager.query(key);
				value = Integer.valueOf(v_string);
			}
			return value;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 设置int的参数
	 * @return 
	 */
	public static int setInt(String key, int value) {
		try {
			if (key != null && !"".equals(key)) {
				LoggerUtils.i("Preferences Set->key:"+key+",value:"+value);
				String v_string = String.valueOf(value);
				return App.paramsManager.commit(key, v_string)?1:-1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/** 
	 * 获取商户号 
	 */
	public static String getShopId(){
		return App.paramsManager.getMerchantNumber();
	}
	/** 
	 * 获取终端号 
	 */
	public static String getPosId(){
		return App.paramsManager.getTerminalNumber();
	}
	/** 
	 * 获取主密钥索引
	 */
	public static int getTMkIndex(){
		int index = App.paramsManager.getMtkIndex();
		LoggerUtils.i("Preferences get->key: getMtkIndex ,value:"+index);
		return index;
	}
	/** 
	 * 设置商户号 
	 */
	public static boolean setShopId(String shopId){
		return App.paramsManager.setMerchantNumber(shopId);
	}
	/** 
	 * 设置终端号 
	 */
	public static boolean setPosId(String posId){
		return App.paramsManager.setTerminalNumber(posId);
	}
	/** 
	 * 设置主密钥索引
	 */
	public static boolean setTMKIndex(int index) {
		LoggerUtils.i("Preferences set->key: setMtkIndex ,value:"+index);
		return App.paramsManager.setMtkIndex(index);
	}
}
