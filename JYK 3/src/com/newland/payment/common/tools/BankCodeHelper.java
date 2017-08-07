package com.newland.payment.common.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.activity.App;

/**
 * 银行码工具类
 * @author lst
 */
public class BankCodeHelper {

	private static Map<String, String> bankCodeMap;
	
	
	/**
	 * 根据消息码获取中文消息
	 */
	public static String getBankCodeCN(String key) {
		if (bankCodeMap == null) {
			bankCodeMap = getBankCodeDate(App.getInstance().getApplicationContext());
		}
		if (!bankCodeMap.containsKey(key)) {
			if (key.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_LOCALCODE))) {
				return key;
			}		
			return null;
		}
		return bankCodeMap.get(key);
	}
	/**
	 * 获取应答码对应中文列表
	 * 
	 * @param 
	 * @return
	 */
	public static Map<String, String> getBankCodeDate(Context context){
		
		String tempString = null;
		InputStream is = null;
		bankCodeMap = new HashMap<String,String>();
		
		try {
			AssetManager am = context.getAssets();
			is = am.open(Const.FileConst.BANKCODE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
			String keyString = "";
			String valueString = "";
			
			while ((tempString = buffer.readLine()) != null) {
				
					keyString = tempString.substring(1, 5);
					valueString = tempString.substring(7, tempString.indexOf('}'));
					
					bankCodeMap.put(keyString, valueString);
			}
			if (bankCodeMap == null) {
				getBankCodeDate(context);
			}
			buffer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bankCodeMap;
	}
	

}
