package com.newland.payment.trans.manage.impl;


import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.DownloadEmvParamType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TLVUtils;

/**
 * TMS参数下载
 */
public final class EmvParamsDownload extends AbstractBaseTrans {

	private boolean mustSendReq = false;
	private String field62 = null;
	private int count = 0;
	private String oneMsg = "";
	private String[] tlvList = null;
	
	private String[] netManCode;
	
	private boolean isDelParams = true;
	
	EmvModule emvModule = EmvAppModule.getInstance();
	
	private DownloadEmvParamType emvParamType;
	
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SEND_STATUS = 2;
		public final static int PARAM_TRANS = 3;
		public final static int SEND_END_PARAM = 4;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public EmvParamsDownload(DownloadEmvParamType emvParamType){
		this.emvParamType = emvParamType;
		
		netManCode = new String[3];
		
		switch(emvParamType){
		case AID:
			netManCode[0] = "382";
			netManCode[1] = "380";
			netManCode[2] = "381";
			break;
		case CAPK:
			netManCode[0] = "372";
			netManCode[1] = "370";
			netManCode[2] = "371";
			break;
		}
	}
	
	@Override
	protected void checkPower(){
		super.checkPower();
		checkSingIn = false; // 不检查签到状态
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		checkCardExsit = false;
	}
	
	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_MAG);
		if (TransConst.DownloadEmvParamType.AID == this.emvParamType) {
			pubBean.setTransName(getText(R.string.setting_other_download_ic_param));
		} else if(TransConst.DownloadEmvParamType.CAPK == this.emvParamType) {
			pubBean.setTransName(getText(R.string.setting_other_download_ic_public_key));
		}
		return res;
	}
	
	@Override
	protected void release() {
		super.release();
	}

	@Override
	protected String[] getCommunitionTipMsg() {
		if (emvParamType == DownloadEmvParamType.AID) {
			return new String[]{getText(R.string.setting_other_download_ic_param_download)+(count!=0?("  "+count):"")};
		} else if (emvParamType == DownloadEmvParamType.CAPK){
			return new String[]{getText(R.string.setting_other_download_ic_public_key_download)+(count!=0?("  "+count):"")};
		}
		return null;
	}
	

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}

	/** 组包 */
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_StatusSend() {
		LoggerUtils.d("dd 通讯时间测试----------开始");
		pubBean.setTransType(TransType.TRANS_STATUS_SEND);
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode(netManCode[0]);
		pubBean.setIsoField60(isoField60);
		String countStr = String.format("1%02d", count);
		pubBean.setIsoField62(countStr);
		
		iso8583.initPack();
		
		iso8583.setField(0, pubBean.getMessageID());

		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		
		iso8583.setField(60, pubBean.getIsoField60().getString());
		iso8583.setField(62, pubBean.getIsoField62());
		
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			LoggerUtils.d("STEP_CONTINUE:" + STEP_CONTINUE);
			return resCode;
		}
		if(isDelParams){
			
			switch(emvParamType){
			case AID:
				emvModule.clearAllAID();
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, true);
				break;
			case CAPK:
				emvModule.clearAllCAPK();
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, true);
				break;
			}
			isDelParams = false;
		}
		field62 = iso8583.getField(62);
		LoggerUtils.d("field62:" + field62);
		if(field62.startsWith("31")){
			mustSendReq = false;
		} else if(field62.startsWith("32")){
			mustSendReq = true;
		} else if(field62.startsWith("33")){
			mustSendReq = false;
		} else {
			return TransConst.STEP_FINAL;
		}
		tlvList = TLVUtils.getTLVList(field62.substring(2));
		
		return Step.PARAM_TRANS;
	}
	
	/** */
	@AnnStep(stepIndex = Step.PARAM_TRANS)
	public int step_ParamTrans(){
		try {
			int offset = 1;
			switch(emvParamType){
			case AID:
				offset = 1;
				break;
			case CAPK:
				offset = 3;
				break;
			}
			for(int i=0; i<tlvList.length; i += offset){
				switch(emvParamType){
				case AID:
					oneMsg = tlvList[i];
					break;
				case CAPK:
					oneMsg = tlvList[i] + tlvList[i+1];
					break;
				}
				
				if(oneMsg.startsWith("9F06")){
					count++;
					pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
					initPubBean();
					
					ISOField60 isoField60 = new ISOField60();
					isoField60.setFuncCode(getF601FuncCode(pubBean.getTransType()));
					isoField60.setNetManCode(netManCode[1]);
					pubBean.setIsoField60(isoField60);
					//pubBean.setIsoField62(oneMsg);
					
					iso8583.initPack();
					
					iso8583.setField(0, pubBean.getMessageID());

					iso8583.setField(41, pubBean.getPosID());
					iso8583.setField(42, pubBean.getShopID());
					
					iso8583.setField(60, pubBean.getIsoField60().getString());
					iso8583.setField(62, oneMsg);
					
					int resCode = dealPackAndComm(false, false, true);
					if (STEP_CONTINUE != resCode){
						
						//连接出错
						if (STEP_FINISH == resCode){
							return Step.TRANS_RESULT;
						}
						LoggerUtils.d("STEP_CONTINUE:" + STEP_CONTINUE);
						return resCode;
					}
					
					field62 = iso8583.getField(62);
					
					if(field62.startsWith("31")){
						boolean result = false;
						switch(emvParamType){
						case AID:
							result = emvModule.setAID(field62.substring(2),ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_EC, true));
							break;
						case CAPK:
							result = emvModule.setCAPK(field62.substring(2));
							break;
						}
						if(result){
							continue;
						} else{
							String msg = null;
							if (emvParamType == DownloadEmvParamType.AID) {
								msg = getText(R.string.setting_other_download_ic_param_download)+(count!=0?("  "+count):"") + getText(R.string.common_fail);
							} else if (emvParamType == DownloadEmvParamType.CAPK){
								msg = getText(R.string.setting_other_download_ic_public_key_download)+(count!=0?("  "+count):"") + getText(R.string.common_fail);
							}
							showToast(msg);
							LoggerUtils.d("emvModule set param fail:" + emvParamType);
						}
					}
				}
				
			}
			if(mustSendReq){
				return Step.SEND_STATUS;
			}
	LoggerUtils.d("dd 通讯时间测试----------结束");		
			return Step.SEND_END_PARAM;
		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			switch(emvParamType){
			case AID:
				transResultBean.setContent(getText(R.string.result_ic_param_download_fail));
				break;
			case CAPK:
				transResultBean.setContent(getText(R.string.result_ic_public_key_download_fail));
				break;
			}
		}
		return TransConst.STEP_FINAL;
	}
	

	@AnnStep(stepIndex = Step.SEND_END_PARAM)
	public int step_SendEndParam(){
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60();
		isoField60.setFuncCode(getF601FuncCode(pubBean.getTransType()));
		isoField60.setNetManCode(netManCode[2]);
		pubBean.setIsoField60(isoField60);
		

		iso8583.initPack();
		
		iso8583.setField(0, pubBean.getMessageID());

		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		
		iso8583.setField(60, pubBean.getIsoField60().getString());
		
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return TransConst.STEP_FINAL;
			}
			LoggerUtils.d("STEP_CONTINUE:" + STEP_CONTINUE);
			return resCode;
		}
		
		//这边说明参数下载完成了
		switch(emvParamType){
		case AID:
			emvModule.SyncAIDFile();
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, false);
			transResultBean.setContent(getText(R.string.result_ic_param_download_success));
			break;
		case CAPK:
			emvModule.SyncCAPKFile();
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, false);
			transResultBean.setContent(getText(R.string.result_ic_public_key_download_success));
			break;
		}
		
		//参数文件更新后需要重新建立AID候选列表
		emvModule.buildAIDList();
		transResultBean.setIsSucess(true);
		return Step.TRANS_RESULT;
	}

	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_transReuslt(){
		LoggerUtils.d("111 capk count:" + count);
		transResultBean.setTitle(pubBean.getTransName());
		showToast(transResultBean.getContent());
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}


}
