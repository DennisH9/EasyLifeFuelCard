package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReverseReasonCode;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.model.ReverseWater;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 冲正交易
 * @author linchunhui
 * @date 2015年5月16日 上午12:11:21
 */
public class Reverse extends AbstractBaseTrans {

	private ReverseWater reverseWater;
	private CommonBean<Integer> commonBean;
	private CommunicationBean communicationBean;
	private int maxTimes = 3;
	private int failTimes = 0;	// 冲正失败次数
	private int connectTimes = 0; // 连接失败次数
	
	public Reverse(CommonBean<Integer> commonBean){
		this.commonBean = commonBean;
	}
	
	@Override
	protected void checkPower() {
		super.checkPower();
		checkCardExsit = false;
		checkSettlementStatus = false;
		checkSingIn = false;
		checkWaterCount = false;
		transcationManagerFlag = false; // 不开启事务
	}

	@Override
	protected int init() {
		int res = super.init();
		failTimes = ParamsUtils.getInt(ParamsTrans.PARAMS_TIMES_REVERSAL_HAVE_SEND, 0);
		maxTimes =  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES, 3);
		return res;
	}
	
	@Override
	protected void release() {
		stepGoOn();
	}
	
	@AnnStep(stepIndex = 1)
	public int step_Reversal() {
		connectTimes = 0;
		while(failTimes <= maxTimes){
			reverseWater = reverseWaterService.getReverseWater();
			if (reverseWater == null){
				commonBean.setResult(true);
				return FINISH;
			}
			
			pubBean.setTransType(TransType.TRANS_REVERSAL);

			initPubBean();
			reverseToPubBean(reverseWater, pubBean);
			
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			
			if (pubBean.getInputMode().startsWith("01") 
				|| pubBean.getInputMode().startsWith("05")
				|| pubBean.getInputMode().startsWith("07")
				|| pubBean.getInputMode().startsWith("95")
				|| pubBean.getInputMode().startsWith("98")
				|| pubBean.getInputMode().startsWith("96")
				|| (pubBean.getInputMode().startsWith("02")&& reverseWater.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND)){
				iso8583.setField(2, pubBean.getPan());
			}
				
			iso8583.setField(3, pubBean.getProcessCode());
			iso8583.setField(4, String.format("%012d", pubBean.getAmount()));
			iso8583.setField(11, pubBean.getTraceNo());
			if (!StringUtils.isNullOrEmpty(pubBean.getExpDate()))
				iso8583.setField(14, pubBean.getExpDate());
			iso8583.setField(22, pubBean.getInputMode());
			if (pubBean.getInputMode().startsWith("05")
				|| pubBean.getInputMode().startsWith("95")
				|| pubBean.getInputMode().startsWith("98")
				|| pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				if (!StringUtils.isNullOrEmpty(pubBean.getCardSerialNo()))
					iso8583.setField(23, pubBean.getCardSerialNo());
			}
			
			iso8583.setField(25, pubBean.getServerCode());
			if (!StringUtils.isNullOrEmpty(pubBean.getOldAuthCode()))
				iso8583.setField(38, pubBean.getOldAuthCode());
			iso8583.setField(39, pubBean.getResponseCode());
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			if (pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				iso8583.setField(48, pubBean.getInputModeForTransIn());
			}
			iso8583.setField(49, pubBean.getCurrency());
			if (pubBean.getTransAttr() == com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD
		        || pubBean.getTransAttr() == com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_qPBOC
		        || pubBean.getTransAttr() == com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD_RF){
					if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55()))
						iso8583.setField(55, pubBean.getIsoField55());
			}
					
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField60().getString()))
				iso8583.setField(60, pubBean.getIsoField60().getString());
			
			if (pubBean.getTransType() == TransType.TRANS_VOID_SALE 
				|| pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE
		        || pubBean.getTransType() == TransType.TRANS_VOID_PREAUTH 
		        || pubBean.getTransType() == TransType.TRANS_AUTHSALE
		        || pubBean.getTransType() == TransType.TRANS_EC_VOID_LOAD_CASH){
			
				if (!StringUtils.isNullOrEmpty(pubBean.getIsoField61())) {
					iso8583.setField(61, pubBean.getIsoField61());
				}
			}
			iso8583.setField(64, "00000000000000");
			int resCode = packAndComm();
			LoggerUtils.e("packAndComm resCode = "+resCode);
			switch (resCode){
			case STEP_FINISH: //连接失败,不计入冲正次数
				connectTimes++;
				if (connectTimes > maxTimes){
					commonBean.setResult(false);
					return FINISH;
				}else{
					continue;
				}
			case STEP_CANCEL://取消,退出冲正,退出交易
				commonBean.setResult(false);
				return FINISH;
			case STEP_CONTINUE: //成功
				LoggerUtils.d("冲正->packAndComm,成功！");
				break;
			default: //失败
				failTimes++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_REVERSAL_HAVE_SEND, failTimes);
				LoggerUtils.d("失败第"+failTimes+"次");
				continue;
			}
			
			break;
		} //end while
		
		//清除冲正流水
		reverseWaterService.delete();
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_REVERSAL_HAVE_SEND, 0);
		
		if (failTimes > maxTimes){
			//冲正失败
			showToast(getText(R.string.result_reverse_fail));
		}else{
			//冲正成功
			showToast(getText(R.string.result_reverse_succse));
		}
		commonBean.setResult(true);
		return FINISH;
	}
	
	private void stepGoOn() {
		commonBean.setStepResult(com.newland.pos.sdk.common.TransConst.StepResult.SUCCESS);
		synchronized (commonBean.getWaitObj()) {
			commonBean.getWaitObj().notify();
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private int packAndComm(){
		
		String requst = null;
		try{
			requst = iso8583.pack();
			// 替换64域
			requst = replaceMac(requst);
		}catch (Exception e) {
			e.printStackTrace();
			showToast(getText(R.string.error_pack_exception)+e.getMessage());
			return TransConst.STEP_FINAL;
		}
		// 流水号增1
		addTraceNo();
		
		/** 通讯 */
		communicationBean = initCommunicationBean();
		communicationBean.setContent(new String[]{getText(R.string.comm_reverse_waitting).toString()});
		communicationBean.setData(requst);
		communicationBean = showUICommunication(communicationBean);

		reverseWaterService.changeField39Result(ReverseReasonCode.NO_ANSWER);
		
		switch(communicationBean.getStepResult()) {
		case BACK:
			// 提示冲正取消，交易停止
			showToast(getText(R.string.result_trans_cancel));
//			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
//				//连接出错,如冲正,批上送,脚本通知等交易，可过滤处理
//				return STEP_FINISH; 
//			}
//			return TransConst.STEP_FINAL;
			return STEP_CANCEL;
				
		case FAIL:
			//网络连接异常, 重试
			showToast(getText(R.string.result_network_offline));
			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
				//连接出错,如冲正,批上送,脚本通知等交易，可过滤处理
				return STEP_FINISH; 
			}
			return FINISH;
			
		case TIME_OUT:
			showToast(getText(R.string.result_network_offline));
			return FINISH;
			
		case SUCCESS:
			
			reverseWaterService.changeField39Result(ReverseReasonCode.OTHER_REASON);
			
			try {
				iso8583.initPack();
				iso8583.unpack(communicationBean.getData());
				int resCode = checkRespons(iso8583, pubBean);
				switch (resCode) {
				case 0:
					// 正常情况
					break;
				case 1:
					String data = communicationBean.getData();
					String resMac = data.substring(data.length()-16);
					String mac = getMac(data.substring(0, data.length()-16));
					if (!resMac.equals(mac)) {
						// MAC校验不对
						reverseWaterService.changeField39Result(ReverseReasonCode.MAC_ERROR);
						showToast(getText(R.string.response_check_fail_mac_error));
						return FINISH;
					} 
					break;
				default:
					// 校验返回数据其他情况
					showToast(getErrorInfo(resCode));
					return FINISH;
				}
				String responseCode = iso8583.getField(39);
				LoggerUtils.d("冲正应答码："+responseCode);
				if("00".equals(responseCode) 
					|| "12".equals(responseCode) 
					|| "25".equals(responseCode)){
					//成功,退出外面处理
					return STEP_CONTINUE;
				} else {
					showToast(getText(R.string.common_respon)+
							responseCode+"\r\n"+
							 AnswerCodeHelper.getAnswerCodeCN(responseCode));
					return FINISH;
				}
			}catch(Exception e){
				e.printStackTrace();
				//应答数据处理异常，重试
				return FINISH;
			}
		}
		
		return STEP_CONTINUE;
	}
	
}
