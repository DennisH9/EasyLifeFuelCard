package com.newland.payment.trans.impl;

import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.MessageTipBean;

/**
 * 预授权完成通知
 * @version 1.0
 * @author spy
 * @date 2015年5月16日
 * @time 上午11:08:12
 */
public final class AuthSaleOff extends AbstractBaseTrans {
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int INPUT_OLD_DATE = 4;
		public final static int INPUT_OLD_AUTH_CODE = 5;
		public final static int INPUT_AMOUNT = 6;
		public final static int PRE_ONLINE_PROCESS = 7;
		public final static int EMV_SIMPLE_PROCESS = 9;
		public final static int PACK_AND_COMM = 10;
		public final static int APPEND_WATER = 11;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	

	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	@Override
	protected void checkPower() {
		super.checkPower();
	}

	@Override
	protected int init() {
		int res = super.init();	
		transResultBean.setIsSucess(false);		
		pubBean.setTransType(TransType.TRANS_AUTHSALEOFF);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		return res;
	}
	
	
	@Override
	protected void release() {
		super.release();
	}

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if(ParamsUtils.getString(ParamsConst.PARAMS_KEY_AUTH_SALE_MODE).equals("1")){
			MessageUtils.showTipMsg(getText(R.string.error_not_authoff));
			return FINISH;
		}
		return Step.SWIP_CARD;
	}
	
	/***/
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		boolean result = false;
		result = stepProvider.swipCard(pubBean, ReadcardType.ICCARD
										| ReadcardType.RFCARD 
										| ReadcardType.SWIPE
										| ReadcardType.HAND_INPUT);
		
		if(!result){
			return FINISH;
		}
		switch (pubBean.getCardInputMode()) {
		case ReadcardType.HAND_INPUT: // 手输卡号
			result = stepProvider.inputValidDate(pubBean,true);
			if (!result) {
				return FINISH;
			}
			return Step.INPUT_OLD_DATE;
		case ReadcardType.SWIPE:
			return Step.INPUT_OLD_DATE;
		case ReadcardType.RFCARD:
		case ReadcardType.ICCARD:
			return Step.EMV_SIMPLE_PROCESS;
		default:
			return FINISH;
		}
	}
	
	@AnnStep(stepIndex = Step.EMV_SIMPLE_PROCESS)
	public int step_EmvSimpleProcess() {
		
		boolean result = stepProvider.emvSimpleProcess(pubBean, new EmvApplication(this));
		
		if(!result){
			if(pubBean.isFallBack()){
				return Step.SWIP_CARD;
			}
			return FINISH;
		}
		
		return Step.INPUT_OLD_DATE;
	}
	
	/**
	 * 输入原交易日期
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_DATE)
	public int step_InputOldDate(){
		boolean result = stepProvider.inputOldTransDate(pubBean,false);
		if (!result) {
			return FINISH;
		}
		return Step.INPUT_OLD_AUTH_CODE;
	}
	
	/**
	 * 输入授权码
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_AUTH_CODE)
	public int step_InputOldAuthCode(){
		boolean result = stepProvider.inputOldAuthCode(pubBean);
		if (!result) {
			return FINISH;
		}
		return Step.INPUT_AMOUNT;
	}
	
	/**
	 * 输入金额
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount() {
		
		boolean result = stepProvider.inputAmount(pubBean, true, false);
		if(!result){
			return FINISH;
		}
		return Step.PRE_ONLINE_PROCESS;
	}
	
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {

		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			return FINISH;
		}

		return Step.PACK_AND_COMM;
	}
	
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		try {
			/**
			 * 采集数据
			 */
			initPubBean();
			ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
			pubBean.setIsoField60(isoField60);
			
			//组包
			packAuthSaleOff(pubBean, 0);
			
			} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.error_pack_exception));
			return Step.TRANS_RESULT;
		}
		int resCode = dealPackAndComm(true, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		return Step.APPEND_WATER;
	}
	
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		try {
			Water water = getWater(pubBean);
			if (com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST == water.getTransAttr()
					|| com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST_RF == water.getTransAttr()){
					new EmvApplication(this).getEmvAppModule().setEmvAdditionInfo(water, null);
			}
			
			addWater(water);
			transResultBean.setIsSucess(true);
			transResultBean.setWater(water);
			//删除冲正流水
			if(ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_REVERSE_TEST)){
				MessageTipBean tipBean = new MessageTipBean();
				tipBean.setTitle("警告，冲正测试 ！！!");
				tipBean.setContent("(该功能为测试人员使用测试，生产请勿使用)\r\n"
						+ "确认键测试冲正，按取消键继续...");
				tipBean.setCancelable(true);
				showUIMessageTip(tipBean);
				if(tipBean.getResult() == true){
					return FINISH;
				}
			}
			reverseWaterService.delete();
			//更新结算数据
			changeSettle(pubBean);
			
		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.result_response_exception) + e.getMessage());
		}
		return Step.TRANS_RESULT;
	}
	
	
	/**结果显示、打印处理*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		
		//启动电子签名
		if (transResultBean.getIsSucess()){
			doElecSign(transResultBean.getWater());
		}
		
		transResultBean.setTitle(pubBean.getTransName());
		showUITransResult(transResultBean);
			
		if(transResultBean.getIsSucess()){
			//电子签名上送,离线上送
			dealSendAfterTrade();
			return SUCC;
		}
		return FINISH;
	}


}
