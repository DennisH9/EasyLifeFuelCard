package com.newland.payment.trans.bean.field;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 工作密钥用法
 * @version 1.0
 * @author spy
 * @date 2015年5月20日
 * @time 下午10:56:25
 */
public class ISOField62_WorkKey {

	private String PIK = null;
	private String PIKCheck = null;
	private String MAK = null;
	private String MAKCheck = null;
	private String TMK = null;
	private String TMKCheck = null;
	private String TDK = null;
	private String TDKCheck = null;
	private boolean isErr = false;	
	
	public ISOField62_WorkKey(String workKey){
		int len = workKey.length();
		LoggerUtils.d("dd 返回密钥长度:" + len);
		if(len == 100){
			int num = 2;
			while(num-- > 0){
				int keyType = Integer.parseInt(workKey.substring(0,2));
				switch (keyType) {
				case 1:
					MAK = workKey.substring(2, 34);
					MAKCheck = workKey.substring(34, 50);				
					break;
				case 2:
					PIK = workKey.substring(2, 34);
					PIKCheck = workKey.substring(34, 50);
					break;
				}
				workKey = workKey.substring(50);
			}
		}else if(len == 150){
			int num = 3;
			while(num-- > 0){
				int keyType = Integer.parseInt(workKey.substring(0,2));
				switch (keyType) {
				case 0:
					TMK = workKey.substring(2, 34);
					TMKCheck = workKey.substring(34, 50);
					break;
				case 1:
					MAK = workKey.substring(2, 34);
					MAKCheck = workKey.substring(34, 50);				
					break;
				case 2:
					PIK = workKey.substring(2, 34);
					PIKCheck = workKey.substring(34, 50);
					break;
				}
				workKey = workKey.substring(50);
			}
		}else{
			LoggerUtils.d("秘钥长度错误");
			isErr = true;
		}
//		if (Const.DesMode.DES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))) {
//			// isoField60.setNetManCode("001");
//			if (len >= 24) {
//				PIK = workKey.substring(0, 16);
//				PIKCheck = workKey.substring(16, 24);
//			}
//			if (len >= 48) {
//				MAK = workKey.substring(24, 40);
//				MAKCheck = workKey.substring(40, 48);
//			}
//		} else {
//			if (len >= 40) {
//				PIK = workKey.substring(0, 32);
//				PIKCheck = workKey.substring(32, 40);
//			}
//			if (len >= 80) {
//				MAK = workKey.substring(40, 56);
//				if(workKey.substring(56, 72).equals("0000000000000000")){
//					MAK = MAK + MAK;
//				}else{
//					MAK = workKey.substring(40, 72);
//				}
//				MAKCheck = workKey.substring(72, 80);
//			}
//			if (len >= 120) {
//				TDK = workKey.substring(80, 112);
//				TDKCheck = workKey.substring(112, 120);
//			}
//		}
	}
	
	public String getPIK() {
		return PIK;
	}

	public void setPIK(String pIK) {
		PIK = pIK;
	}

	public String getPIKCheck() {
		return PIKCheck;
	}

	public void setPIKCheck(String pIKCheck) {
		PIKCheck = pIKCheck;
	}

	public String getMAK() {
		return MAK;
	}

	public void setMAK(String mAK) {
		MAK = mAK;
	}

	public String getMAKCheck() {
		return MAKCheck;
	}

	public void setMAKCheck(String mAKCheck) {
		MAKCheck = mAKCheck;
	}

	public String getTDK() {
		return TDK;
	}

	public void setTDK(String tDK) {
		TDK = tDK;
	}

	public String getTDKCheck() {
		return TDKCheck;
	}
	public String getTMK() {
		return TMK;
	}

	public void setTMK(String tMK) {
		TMK = tMK;
	}

	public String getTMKCheck() {
		return TMKCheck;
	}

	public void setTMKCheck(String tMKCheck) {
		TMKCheck = tMKCheck;
	}

	public void setTDKCheck(String tDKCheck) {
		TDKCheck = tDKCheck;
	}
	public boolean isErr() {
		return isErr;
	}
	public void setErr(boolean isErr) {
		this.isErr = isErr;
	}

}
