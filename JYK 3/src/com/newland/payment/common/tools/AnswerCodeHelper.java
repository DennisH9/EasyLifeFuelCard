package com.newland.payment.common.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;

/**
 * 应答码工具类
 * @author lst
 */
public class AnswerCodeHelper {
	private static Map<String, String> answerCodeMap;
	
	
	/**
	 * 根据消息码获取中文消息
	 */
	public static String getAnswerCodeCN(String key) {
		if (answerCodeMap == null) {
			answerCodeMap = getAnswerCodeDate(App.getInstance().getApplicationContext());
		}
		String res = answerCodeMap.get(key);
		if (res == null){
			res = App.getInstance().getApplicationContext().getString(R.string.response_unknow);
		}
		return res;
	}
	/**
	 * 获取应答码对应中文列表
	 * 
	 * @param 
	 * @return
	 */
	public static Map<String, String> getAnswerCodeDate(Context context){
		
		String tempString = null;
		InputStream is = null;
		answerCodeMap = new HashMap<String,String>();
		
		try {
			AssetManager am = context.getAssets();
			is = am.open(Const.FileConst.ANSWERCODE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
			String keyString = "";
			String valueString = "";
			
			while ((tempString = buffer.readLine()) != null) {
				
					keyString = tempString.substring(1, 3);
					valueString = tempString.substring(3, tempString.indexOf('}'));
					
					answerCodeMap.put(keyString, valueString);
			}
			if (answerCodeMap == null) {
				getAnswerCodeDate(context);
			}
			buffer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return answerCodeMap;
	}
	
}
