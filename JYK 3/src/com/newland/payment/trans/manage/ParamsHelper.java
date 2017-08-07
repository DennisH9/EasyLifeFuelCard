package com.newland.payment.trans.manage;

import java.io.UnsupportedEncodingException;

import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 解析参数工具类
 *
 * @author CB
 * @date 2015-5-20 
 * @time 下午11:45:13
 */
public class ParamsHelper {
	
	private String data;

	public ParamsHelper(String data) {
		LoggerUtils.i("binary data: " + data);
		this.data = data;
	}
	
	/**
	 * 往后取N个字符串
	 * @param length
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String get(int length) throws UnsupportedEncodingException {
		String tempString = "";
		String key = "";
		if (!StringUtils.isEmpty(data)) { 
			key = StringUtils.hexToStr(data.substring(0,4));
			//LoggerUtils.i("key: " + key);
			data = data.substring(4);
			tempString = data.substring(0, length*2);
			data = data.substring(length*2);
			if(!"26".equals(key)) { 
				tempString = StringUtils.hexToStr(tempString).trim();
			}
		}
		//LoggerUtils.i("tempString: " + tempString);
		return tempString;
	}
	
	/**
	 * 获取数据
	 * @param length
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String getData(int length) throws UnsupportedEncodingException {
		String tempString = "";
		if (!StringUtils.isEmpty(data)) {
			tempString = data.substring(0, length);
			data = data.substring(length);
		}
		return tempString;
	}
}
