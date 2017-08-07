package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.pos.sdk.common.TransConst.StepResult;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 脚本上送
 * @author linchunhui
 * @date 2015年5月16日 上午12:11:21
 * @modify by linld 2015-06-02
 */
public class ScriptResultAdvise extends AbstractBaseTrans {

	private ScriptResult scriptResult;
	private CommonBean<Integer> commonBean;
	private CommunicationBean communicationBean;
	private int maxTimes = 3;
	private int failTimes = 0;	// 冲正失败次数
	private int connectTimes = 0; // 连接失败次数
	
	public ScriptResultAdvise(CommonBean<Integer> commonBean){
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
		failTimes = ParamsUtils.getInt(ParamsTrans.PARAMS_TIMES_SCRIPT_HAVE_SEND, 0);
		maxTimes =  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES, 3);
		LoggerUtils.d("111 脚本通知init->res" + res);
		return res;
	}
	
	
	@AnnStep(stepIndex = 1)
	public int step_ScriptResultAdvise() {
		LoggerUtils.d("111 脚本通知-->");
		connectTimes = 0;
		pubBean.setTransType(TransType.TRANS_SCRIPT);
		initPubBean();
		while(failTimes <= maxTimes){
			scriptResult = scriptResultService.getScriptResult();;
			if (scriptResult == null){
				LoggerUtils.d("111 没有脚本数据，不执行脚本上送");
				commonBean.setResult(true);
				return FINISH;
			}
			

			scriptToPubBean(scriptResult, pubBean);
			LoggerUtils.d("111 脚本60域[" + pubBean.getIsoField60().getString() + "]");
			pubBean.getIsoField60().setFuncCode("00");
			pubBean.getIsoField60().setNetManCode("951");
			pubBean.getIsoField60().setPartSaleflag("");
			pubBean.getIsoField60().setAccountType("");
			
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			if (pubBean.getInputMode().startsWith("05")
				|| pubBean.getInputMode().startsWith("95")
				|| pubBean.getInputMode().startsWith("98")
				|| scriptResult.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				iso8583.setField(2, pubBean.getPan());
			}
			iso8583.setField(3, pubBean.getProcessCode());
			if (pubBean.getAmount() != null){
			iso8583.setField(4, pubBean.getAmountField());
			}
			iso8583.setField(11, pubBean.getTraceNo());
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isNullOrEmpty(pubBean.getCardSerialNo())){
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(32, pubBean.getAcqCenterCode());
			LoggerUtils.d("111 脚本上送打包系统参考号:" + pubBean.getSystemRefNum());
			iso8583.setField(37, pubBean.getSystemRefNum());
			if (!StringUtils.isNullOrEmpty(pubBean.getOldAuthCode())){
				iso8583.setField(38, pubBean.getOldAuthCode());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55()))
				iso8583.setField(55, pubBean.getIsoField55());
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField60().getString()))
				LoggerUtils.d("111 脚本上送60域:" + pubBean.getIsoField60().getString());
				iso8583.setField(60, pubBean.getIsoField60().getString());
			
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField61())) {
				iso8583.setField(61, pubBean.getIsoField61());
			}
			iso8583.setField(64, "00000000000000");
			int resCode = packAndComm();
			LoggerUtils.e("packAndComm resCode = "+resCode);
			switch (resCode){
			case STEP_FINISH: //连接失败,不计入冲正次数
				connectTimes++;
				if (connectTimes >= maxTimes){
					commonBean.setResult(false);
					return FINISH;
				}else{
					continue;
				}
			case STEP_CANCEL: //终止,退出冲正,退出交易
				commonBean.setResult(false);
				return FINISH;
			case STEP_CONTINUE: //成功
				LoggerUtils.d("脚本通知->packAndComm,成功！");
				break;
			default: //失败
				failTimes++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_SCRIPT_HAVE_SEND, failTimes);
				LoggerUtils.d("失败第"+failTimes+"次");
				continue;
			}
			
			break;
		} //end while
		
		//清除脚本流水
		scriptResultService.delete();
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_SCRIPT_HAVE_SEND, 0);
		
		if (failTimes > maxTimes){
			//脚本通知失败
			showToast(getText(R.string.result_script_fail));
		}else{
			//脚本通知成功
			showToast(getText(R.string.result_script_succse));
		}
		commonBean.setResult(true);
		return FINISH;
	}

	
	@Override
	protected void release() {
		stepGoOn();
	}
	
	private void stepGoOn() {
		commonBean.setStepResult(StepResult.SUCCESS);
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
		communicationBean.setContent(new String[]{getText(R.string.comm_script_waitting).toString()});
		communicationBean.setData(requst);
		communicationBean = showUICommunication(communicationBean);

		
		switch(communicationBean.getStepResult()) {
		case BACK:
			// 提示冲正取消，交易停止
			showToast(getText(R.string.result_trans_cancel));
//			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
//				//连接出错,如冲正,批上送,脚本通知等交易，可过滤处理
//				return STEP_FINISH; 
//			}
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
				LoggerUtils.d("脚本通知应答码："+responseCode);
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
