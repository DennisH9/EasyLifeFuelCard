package com.newland.base.util;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.newland.telephony.ApnEntity;
import android.newland.telephony.ApnUtils;
import android.telephony.TelephonyManager;

import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import java.lang.reflect.Method;

public class CommunicationUtils {

	final static private int CDMA = 0;
	final static private int GPRS = 1;
	final static private int WIFI = 2;
	final static private int CM = 1;
	final static private int CU = 2;
	final static private int CT = 3;	
	
	static private String name;
	static private String apn;
	static private String mcc;
	static private String mnc;	
	
	/**
	 * 设置移动数据开关状态
	 * 
	 * @param context
	 * @param enabled false-关 true-开
	 * 
	 */
	static private void setMobileDataStatus(Context context, boolean enabled){      
        ConnectivityManager connectivityManager =     
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
        Method setMobileDataEnabl;  
        try {  
            setMobileDataEnabl = connectivityManager.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);  
            setMobileDataEnabl.invoke(connectivityManager, enabled);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	 }

    /**
     * 获取移动数据开关状态  
     * @return
     */
	static private boolean getMobileDataStatus(Context context)  
    {  
		ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String methodName = "getMobileDataEnabled";  
        Class cmClass = connectivityManager.getClass();  
        Boolean isOpen = null;  
          
        try   
        {  
            Method method = cmClass.getMethod(methodName, new Class[]{});
  
            isOpen = (Boolean) method.invoke(connectivityManager, new  Object[]{});
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return isOpen;  
    }
    
	/**
	 * 系统通讯开关初始化
	 * 
	 * @param type 收单应用的通讯方式
	 * @param context
	 */
	static public void commSwitchInit(int type, Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		
		if(CDMA == type){
			
		}else if(GPRS == type){
			LoggerUtils.d("type GPRS");
			wifiManager.setWifiEnabled(false);
			if(!getMobileDataStatus(context)){
				LoggerUtils.d("OPEN MOBILE SWITCH");
				setMobileDataStatus(context, true);
			}			
		}else if(WIFI == type){
			if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
				wifiManager.setWifiEnabled(true);
			}
		}
	}
	
	/**
	 * 获取当前WiFiSSID
	 * 
	 * @return
	 */
	static public String getPreferWifi(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		
        int wifiState = wifiManager.getWifiState();
        if(wifiState != WifiManager.WIFI_STATE_ENABLED){
        	return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
	}	
	
	static public void SetDefaultApnEntity(Context context){
		name = getApnNameBySim(context, "中国移动");
		apn = getApnBySim(context, "cccccc");
		mcc = "460";
		mnc = getMncBySim(context, "02");	
	}
	
	/**
	 * 修改当前选择的apn
	 * @param context
	 * @return
	 */
	static public boolean updateApnEntity(Context context){
		ApnUtils apnUtils = new ApnUtils(context);
		ApnEntity newApnEntity = new ApnEntity();
		ApnEntity oldApnEntity = apnUtils.getPreferApn();
		
		if(oldApnEntity == null){
			return false;
		}

		if(StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(apn) 
				|| StringUtils.isNullOrEmpty(mcc) || StringUtils.isNullOrEmpty(mnc)){
			LoggerUtils.d("apn null false");
			return false;
		}		
		
		if(apnUtils.removeApn(oldApnEntity.getId()) == false){
			LoggerUtils.d("apn remove false");
			return false;
		}

		newApnEntity.setName(name);
		newApnEntity.setApn(apn);
		newApnEntity.setMcc(mcc);
		newApnEntity.setMnc(mnc);
		newApnEntity.setType("default,supl,net");
		
		LoggerUtils.d("updateApnEntity name:"+name);
		LoggerUtils.d("updateApnEntity apn:"+apn);
		LoggerUtils.d("updateApnEntity mcc:"+mcc);
		LoggerUtils.d("updateApnEntity mnc:"+mnc);
		
		int id=apnUtils.addNewApn(newApnEntity);
		if(id != -1){
			if(apnUtils.setDefault(id) >= 0){
				return true;
			}
		}
		
		LoggerUtils.d("apn edit false");
		return false;
	}
	/**
	 * 修改当前选择的apn
	 * @param context
	 * @return
	 */
	static public boolean updateApnEntity(Context context, String usingName,String psw){
		ApnUtils apnUtils = new ApnUtils(context);
		ApnEntity newApnEntity = new ApnEntity();
		ApnEntity oldApnEntity = apnUtils.getPreferApn();
		
		if(oldApnEntity == null){
			return false;
		}
		
		if(StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(apn) 
				|| StringUtils.isNullOrEmpty(mcc) || StringUtils.isNullOrEmpty(mnc)){
			LoggerUtils.d("apn null false");
			return false;
		}		
		
		if(apnUtils.removeApn(oldApnEntity.getId()) == false){
			LoggerUtils.d("apn remove false");
			return false;
		}
		
		newApnEntity.setName(name);
		newApnEntity.setApn(apn);
		newApnEntity.setMcc(mcc);
		newApnEntity.setMnc(mnc);
		newApnEntity.setPassword(psw);
		newApnEntity.setName(usingName);
		newApnEntity.setType("default,supl,net");
		
		LoggerUtils.d("updateApnEntity name:"+name);
		LoggerUtils.d("updateApnEntity apn:"+apn);
		LoggerUtils.d("updateApnEntity mcc:"+mcc);
		LoggerUtils.d("updateApnEntity mnc:"+mnc);
		
		int id=apnUtils.addNewApn(newApnEntity);
		if(id != -1){
			if(apnUtils.setDefault(id) >= 0){
				return true;
			}
		}
		
		LoggerUtils.d("apn edit false");
		return false;
	}
	
	
	/**
	 * 增加apn实体
	 * @param context
	 * @return
	 */
	static public boolean addApnEntity(Context context){
		ApnUtils apnUtils = new ApnUtils(context);
		ApnEntity apnEntity = new ApnEntity();

		
		if(StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(apn) 
				|| StringUtils.isNullOrEmpty(apn) || StringUtils.isNullOrEmpty(apn)){
			return false;
		}
		
		apnEntity.setName(name);
		apnEntity.setApn(apn);
		apnEntity.setMcc(mcc);
		apnEntity.setMnc(mnc);
		apnEntity.setType("default,supl,net");
		
		LoggerUtils.d("addApnEntity name:"+name);
		LoggerUtils.d("addApnEntity apn:"+apn);
		LoggerUtils.d("addApnEntity mcc:"+mcc);
		LoggerUtils.d("addApnEntity mnc:"+mnc);
		int id=apnUtils.addNewApn(apnEntity);
		if(id != -1){
			if(apnUtils.setDefault(apnEntity.getId()) >= 0){
				return true;
			}		
		}
		
		LoggerUtils.d("addApnEntity:add apn false");
		return false;
	}

	/**
	 * 获取当前apn数据
	 * 
	 * @param context
	 * @return
	 */
	static public boolean getPreferApn(Context context){
		ApnUtils apnUtils = new ApnUtils(context);
		ApnEntity apnEntity = apnUtils.getPreferApn();
		
		if(apnEntity == null){
			return false;
		}
		
		name = apnEntity.getName();
		apn = apnEntity.getApn();
		mcc = apnEntity.getMcc();
		mnc = apnEntity.getMnc();
		
		return true;
	}
	
	/**
	 * 返回SIM卡运营商
	 */
	
	static public int getServiceProvider(Context context){
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);   
		String operator = telManager.getSimOperator();
		if(operator!=null){
			if(operator.equals("46000") || operator.equals("46002")){   
				return CM;
			}else if(operator.equals("46001")){   
				return CU;
			}else if(operator.equals("46003")){   
				return CT;
			}   
		}
		return -1;
	}
	
	/**
	 * 返回默认apn
	 * @param context
	 * @return
	 */
	static public String getApnBySim(Context context, String date){
		int type = getServiceProvider(context);
		
		switch(type){
			case CT:
				return "ctnet";
			case CM:
				return "cmnet";
			case CU:
				return "3gnet";
			default:
				return date;
		}
	}
	/**
	 * 返回默认apn名称
	 * @param context
	 * @return
	 */
	static public String getApnNameBySim(Context context, String date){
		int type = getServiceProvider(context);
		
		switch(type){
			case CT:
				return "中国电信";
			case CM:
				return "中国移动";
			case CU:
				return "中国联通";
			default:
				return date;
		}
	}

	/**
	 * 获取SIM卡MNC
	 * @param context
	 * @param date
	 * @return
	 */
	static public String getMncBySim(Context context, String date){
		if(checkSimAndGetMccMnc(context)){
			return mnc;
		}
		
		LoggerUtils.d("无sim卡");
		return date;
	}	
	
	/**
	 * 返回默认mnc
	 * @param context
	 * @return
	 */
    public static boolean checkSimAndGetMccMnc(Context context) {  
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(context.getResources().getConfiguration().mcc == 0){
        	return false;
        }
        mcc = String.valueOf(context.getResources().getConfiguration().mcc);
        mnc = String.format("%02d", context.getResources().getConfiguration().mnc);
        
        LoggerUtils.d("mcc:"+mcc);
        LoggerUtils.d("mnc:"+mnc);
        
        return true;
    }
	
	/**
	 * 获取apn名称
	 * 
	 * @return
	 */
	static public String getName(){
		return name;
	}

	/**
	 * 设置apn名称
	 * @param nameString
	 */
	static public void setName(String nameString){
		name = nameString;
	}
	
	
	/**
	 * 获取apn
	 * 
	 * @return
	 */
	static public String getApn(){
		return apn;
	}
	
	/**
	 * 设置apn
	 * @param apnString
	 */
	static public void setApn(String apnString){
		apn = apnString;
	}
	
	/**
	 * 获取mcc
	 * 
	 * @return
	 */
	static public String getMcc(){
		return mcc;
	}	

	/**
	 * 设置mcc
	 * @param mccString
	 */
	static public void setMcc(String mccString){
		mcc = mccString;
	}	
	
	/**
	 * 获取mnc
	 * 
	 * @return
	 */
	static public String getMnc(){
		return mnc;
	}
	
	/**
	 * 设置mnc
	 * @param mncString
	 */
	static public void setMnc(String mncString){
		mnc = mncString;
	}
	
}
