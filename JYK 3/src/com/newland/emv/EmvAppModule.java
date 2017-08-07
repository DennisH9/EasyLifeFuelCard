package com.newland.emv;


import java.io.File;

import android.content.Context;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.MenuSelectBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.bean.PasswordBean;
import com.newland.pos.sdk.common.EmvTag;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.interfaces.EmvUiListener;
import com.newland.payment.mvc.model.EmvFailWater;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.impl.EmvFailWaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TLVUtils;
import com.newland.pos.sdk.util.TLVUtils.PackTLV;



public class EmvAppModule extends EmvModule implements EmvUiListener{

	private AbstractBaseTrans uiTrans = null;
	
	private EmvAppModule(){
		super();
		this.initEmvEnv(this, 
				App.getInstance().getApplicationContext().getFilesDir() + File.separator + "emv" + File.separator);
	}
	
	private static class InnerInstance{
		private static final EmvAppModule INSTANCE = new EmvAppModule();
	}
	
	public static EmvAppModule getInstance(){
		return InnerInstance.INSTANCE;
	}
	
	public void setUiTrans(AbstractBaseTrans uiTrans) {
		this.uiTrans = uiTrans;
	}
	
	/**
	 * 设置流水中用于打印什么的信息
	 * @param water
	 * @param arqc
	 */
	public void setEmvAdditionInfo(Water water, String arqc){
		int[] tagList = new int[30];
		int len = 0;
		
		if(StringUtils.isEmpty(arqc)){
			tagList[len++]=0x9F26; //TC
		}
		//TVR
		tagList[len++] = EmvTag.TAG_95_TM_TVR;
		//TSI
		tagList[len++] = EmvTag.TAG_9B_TM_TSI;
		//AID
		tagList[len++] = EmvTag.TAG_4F_IC_AID;//0x9F06
		//ATC
		tagList[len++] = EmvTag.TAG_9F36_IC_ATC;
		//APP_LABEL
		tagList[len++] = EmvTag.TAG_50_IC_APPLABEL;
		//APP_PRE_NAME
		tagList[len++] = EmvTag.TAG_9F12_IC_APNAME;
		//SIGNATURE_FLAG
		tagList[len++] = 0xDF42;
		//UNP_NUM
		tagList[len++] = EmvTag.TAG_9F37_TM_UNPNUM;
		//AIP
		tagList[len++] = EmvTag.TAG_82_IC_AIP;
		//CVM
		tagList[len++] = EmvTag.TAG_9F34_TM_CVMRESULT;
		//IAD
		tagList[len++] = EmvTag.TAG_9F10_IC_ISSAPPDATA;
		//TERM_CAP
		tagList[len++] = EmvTag.TAG_9F33_TM_CAP;
		
		//卡产品标识(9F63)
		tagList[len++] = EmvTag.TAG_9F63_IC_PRODUCTID;
		byte[] buffer = new byte[9216];
		int ret = 0;
		ret = FetchData(tagList, len, buffer, buffer.length);
		
		if(ret <= 0){
			return;
		}
		
		byte[] payload = new byte[ret];
		System.arraycopy(buffer, 0, payload, 0, ret);
		

		String data = BytesUtils.bytesToHex(payload);
		if(!StringUtils.isEmpty(arqc)){
			data += arqc; //9F26,ARQC
			arqc = null;
		}

		water.setEmvAddition(data);
		
	}
	
	/**
	 * 设置交易流水信息
	 * @param water
	 */
	public void setEmvWaterInfo(Water water){

		boolean isArpcErr = false;
		water.setEmvAuthResp(getEmvDataStr(EmvTag.TAG_8A_TM_ARC));
		water.setTvr(getEmvDataStr(EmvTag.TAG_95_TM_TVR));
		water.setTsi(getEmvDataStr(EmvTag.TAG_9B_TM_TSI));
		byte[] tvr = getEmvData(EmvTag.TAG_95_TM_TVR);
		if(water.getEmvStatus() == EmvStatus.EMV_STATUS_ONLINE_SUCC){
			if((tvr[4] & 0x40) != 0){
				isArpcErr = true;
			} 
		}
		String field55 = packField55(isArpcErr);
		PackTLV pack = TLVUtils.newPackTLV();
		byte[] tag;
		if(isArpcErr){
			tag = getEmvData(EmvTag.TAG_91_TM_ISSAUTHDT);
			if(tag != null && tag.length > 0){
				//field55 += BytesUtils.bytesToHex(TLVUtils.addTLV(EmvTag.TAG_9F26_IC_AC, tag));
				pack.append(EmvTag.TAG_9F26_IC_AC, tag);
			}
		}
		//电子现金脱机成功必须加9f74
		if(water.getEmvStatus() == EmvStatus.EMV_STATUS_OFFLINE_SUCC){
			tag = getEmvData(0x9F74);
			if(tag != null && tag.length > 0){
				//field55 += BytesUtils.bytesToHex(TLVUtils.addTLV(0x9F74, tag));
				pack.append(0x9F74, tag);
			}
		}
		//脱机交易55域中保存8A
		LoggerUtils.d("111 water.getEmvAuthResp():" + water.getEmvAuthResp());
		if("5931".equals(water.getEmvAuthResp())
				||"5933".equals(water.getEmvAuthResp())
				||"5A31".equals(water.getEmvAuthResp())
				||"5A33".equals(water.getEmvAuthResp())){
			tag = getEmvData(EmvTag.TAG_8A_TM_ARC);
			//field55 +=  BytesUtils.bytesToHex(TLVUtils.addTLV(EmvTag.TAG_8A_TM_ARC, tag));
			pack.append(EmvTag.TAG_8A_TM_ARC, tag);
		}
		
		field55 += BytesUtils.bytesToHex(pack.pack());
		water.setField55(field55);
		
	}
	
	/**
	 * 保存EMV失败流水
	 * @param pubBean
	 * @return
	 */
	public boolean SaveEmvFailWater(Context context, PubBean pubBean){
		EmvFailWater failWater = new EmvFailWater();
		EmvFailWaterService failWaterSevice = new EmvFailWaterServiceImpl(context);
		try {
			failWater.setTransType(pubBean.getTransType());
			failWater.setTransAttr(pubBean.getTransAttr());
			failWater.setEmvStatus(pubBean.getEmv_Status());
			failWater.setPan(pubBean.getPan());
			failWater.setAmount(pubBean.getAmount());
			LoggerUtils.d("111 保存失败流水流水号[" + pubBean.getTraceNo() + "]");
			failWater.setTrace(pubBean.getTraceNo());
			failWater.setTime(pubBean.getTime());
			failWater.setDate(pubBean.getDate());
			failWater.setExpDate(pubBean.getExpDate());
			LoggerUtils.d("111 保存失败流水22域[" + pubBean.getInputMode() + "]");
			failWater.setInputMode(pubBean.getInputMode() + "2");
			failWater.setCardSerialNo(pubBean.getCardSerialNo());
			if (null != pubBean.getTrackData2()){
				failWater.setTrack2(pubBean.getTrackData2());
			}
			
			if (null != pubBean.getTrackData3()){
				failWater.setTrack3(pubBean.getTrackData3());
			}
			//批次号
			failWater.setBatchNum(ParamsUtils.getString( 
					ParamsConst.PARAMS_KEY_BASE_BATCHNO));
			failWater.setOper(pubBean.getCurrentOperNo());
			failWater.setInterOrg(pubBean.getInternationOrg());
			failWater.setBatchUpFlag(0);
			failWater.setCurrency(pubBean.getCurrency());
			
			//以下数据直接从EMV内核取
			//授权响应码
			failWater.setEmvAuthResp(getEmvDataStr(EmvTag.TAG_8A_TM_ARC));
			failWater.setTvr(getEmvDataStr(EmvTag.TAG_95_TM_TVR));
			failWater.setTsi(getEmvDataStr(EmvTag.TAG_9B_TM_TSI));
			
			//55域 
			String field55 = packField55(false);
			byte[] tag;
			PackTLV pack = TLVUtils.newPackTLV();
			//脱机交易55域中保存8A
			LoggerUtils.d("111 failWater.getEmvAuthResp():" + failWater.getEmvAuthResp());
			if("5931".equals(failWater.getEmvAuthResp())
					||"5933".equals(failWater.getEmvAuthResp())
					||"5A31".equals(failWater.getEmvAuthResp())
					||"5A33".equals(failWater.getEmvAuthResp())){
				tag = getEmvData(EmvTag.TAG_8A_TM_ARC);
				//field55 +=  BytesUtils.bytesToHex(TLVUtils.addTLV(EmvTag.TAG_8A_TM_ARC, tag));
				pack.append(EmvTag.TAG_8A_TM_ARC, tag);
			}
			
			//电子现金加9f74
			tag = getEmvData(0x9F74);
			if(tag != null && tag.length > 0){
//				field55 += BytesUtils.bytesToHex(TLVUtils.addTLV(0x9F74, tag));
				pack.append(0x9F74, tag);
			}
			
			field55 +=  BytesUtils.bytesToHex(pack.pack());
			failWater.setField55(field55);
			//没用到,暂时不存
		//	failWater.setEmvAddition();	
			
			failWaterSevice.addEmvFailWater(failWater);
			
		}catch (Exception e) {
			LoggerUtils.e("保存EMV失败流水异常！");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public MenuSelectBean showUIMenuSelect(MenuSelectBean menuSelectBean){
		return uiTrans.showUIMenuSelect(menuSelectBean);
	}
	
	public MessageTipBean showUIMessageTip(MessageTipBean msgTipBean){
		return uiTrans.showUIMessageTip(msgTipBean);
	}
	
	public AmountBean showUIInputAmount(AmountBean amountBean){
		return uiTrans.showUIInputAmount(amountBean);
	}
	
	public PasswordBean showUIPinInput(boolean isPlainPin, PasswordBean passwordBean){
		if(isPlainPin){
			return uiTrans.showUIPinOfflineInput(passwordBean);
		}else{
			return uiTrans.showUIPinInput(passwordBean);
		}
	}
	
	public int getEmvTransSerial(){
		return ParamsUtils.getInt(ParamsTrans.PARAMS_EMV_TRANS_SERIAL);
	}
	
	public void setEmvTransSerial(int value){
		ParamsUtils.setInt(ParamsTrans.PARAMS_EMV_TRANS_SERIAL, value);
	}
	
	public void showToastMsg(final String msg) {

		MainActivity.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtils.showLong(MainActivity.getInstance()
						.getApplicationContext(), msg);
			}
		});
	}
	
}
