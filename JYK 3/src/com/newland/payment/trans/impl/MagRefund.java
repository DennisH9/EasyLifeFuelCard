package com.newland.payment.trans.impl;

import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;

/**
 * 退货
 * @version 1.0
 * @author spy
 * @date 2015年5月16日
 * @time 上午11:08:12
 */
public final class MagRefund extends AbstractBaseTrans {
	
	private int m_TransType = TransType.TRANS_REFUND;
	private int m_CardInpuMode = ReadcardType.SWIPE;
	private boolean isInputPin = false;
	private ThirdInvokeBean thirdInvokeBean = null;
	private EmvModule emvModule;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int EMV_SIMPLE_PROCESS = 3;
//		public final static int INPUT_REF_NO = 4;
//		public final static int INPUT_DATE = 5;
//		public final static int INPUT_AMOUNT = 6;
		public final static int PRE_ONLINE_PROCESS = 4;
		public final static int PACK_AND_COMM = 5;
		public final static int APPEND_WATER = 6;
		public final static int INPUT_PIN = 7;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public MagRefund(int transType){
		m_TransType = transType;
	}
	
	public MagRefund(ThirdInvokeBean thirdInvokeBean) {
		this.thirdInvokeBean = thirdInvokeBean;
	}

	@Override
	protected void checkPower() {
		super.checkPower();
		checkManagerPassword = false;
	}

	@Override
	protected int init() {
		int res = super.init();	
		pubBean.setTransType(m_TransType);
		pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_MAG);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		transResultBean.setIsSucess(false);

		if (thirdInvokeBean != null ){
			if(thirdInvokeBean.getOldRefnum()!= null){
				pubBean.setOldRefnum(thirdInvokeBean.getOldRefnum());
			}
			if(thirdInvokeBean.getOldDate()!= null){
				pubBean.setOldDate(thirdInvokeBean.getOldDate());
			}
			if(thirdInvokeBean.getAmountBean()!= null){
				pubBean.setAmount(thirdInvokeBean.getAmountBean().getAmount());
			}
		}
		PublicLibJNIService.jnicardpowerdown();
		return res;
	}
	
	@Override
	protected void release() {
		if (emvModule!=null) {
			emvModule.emvSuspend(0);
			emvModule.emvRFSuspend(0);
			emvModule = null;
		}
		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SWIP_CARD;
	}

	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		boolean result = false;
		m_CardInpuMode = ReadcardType.RFCARD;
		result = stepProvider.swipCard(pubBean, m_CardInpuMode);
		if(!result){
			return FINISH;
		}
		switch (pubBean.getCardInputMode()) {
//		case ReadcardType.SWIPE: // 磁条卡
//			return Step.INPUT_REF_NO;
		case ReadcardType.ICCARD: // 接触式读卡
		case ReadcardType.RFCARD: // 非接式读卡
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
		return Step.INPUT_PIN;

	}

	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin() {
		boolean result = stepProvider.inputPin(pubBean);
		if(!result){
			pubBean.setResponseCode("EC");
			pubBean.setMessage("INPUT_PIN异常");
			return FINISH;
		}
		return Step.PRE_ONLINE_PROCESS;
	}

	/**
	 * 输入原参考号
	 * @return
	 */
	/*
	@AnnStep(stepIndex = Step.INPUT_REF_NO)
	public int step_InputRefNo(){
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(getText(R.string.common_pls_input) + getText(R.string.common_old_ref_no));
		infoBean.setShortContent(getText(R.string.common_old_ref_no));
		infoBean.setMode(InputInfoBean.INPUT_MODE_STRING);
		infoBean.setMaxLen(12);
		infoBean.setMinLen(12);
		infoBean.setEmptyFlag(false);
		
		infoBean = showUIInputInfo(infoBean);
		
		switch(infoBean.getStepResult()){
		case SUCCESS:
			//设置原参考号
			pubBean.setOldRefnum(infoBean.getResult());
			break;
			
		case BACK:
		case TIME_OUT:
		case FAIL:
		default:
			return FINISH;
		}
		return Step.INPUT_DATE;
	}
	*/

	/**
	 * 日期输入
	 * @return
	 */
	/*
	@AnnStep(stepIndex = Step.INPUT_DATE)
	public int step_InputDate(){
		boolean result = stepProvider.inputOldTransDate(pubBean,false);
		if (!result) {
			return FINISH;
		}
		return Step.INPUT_AMOUNT;
	}
	*/
	

//	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
//	public int step_InputAmount() {
//		boolean result = stepProvider.inputAmount(pubBean, true, true);
//		if(!result){
//			return FINISH;
//		} /* else {
//			//金额确认
//			if (!confirmAmount(pubBean.getTransName(), pubBean.getAmount())){
//				return Step.INPUT_AMOUNT;
//			}
//			//退货最大金额限制检查
//			if (!checkRefundAmtLimit(pubBean.getTransName(), pubBean.getAmount())){
//				return Step.INPUT_AMOUNT;
//			}
//		}*/
//		return Step.PRE_ONLINE_PROCESS;
//	}

	
	/**脚本上送、冲正*/
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {
		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			return FINISH;
		}
		return Step.PACK_AND_COMM;
	}
		
	
	/** 打包、通讯、解包*/
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		/** 采集数据*/

		initPubBean();

		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);

		pubBean.setIsoField61("000000" + "000000" + pubBean.getOldDate());
		pubBean.setIsoField63("000");

		//组包
		packRefund(pubBean, 0);

		int resCode = dealPackAndComm(true, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return TransConst.STEP_FINAL;
			}
			return resCode;
		}
		
		return Step.APPEND_WATER;
			
	}
	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		try {
			Water water = getWater(pubBean);
			if (com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST == water.getTransAttr()
					|| com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST_RF == water.getTransAttr()){
					new EmvApplication(this).getEmvAppModule().setEmvAdditionInfo(water, null);
			}			
			
			//修改原交易流水状态为已退货
			WaterService waterService = new WaterServiceImpl(MainActivity.getInstance());
			Water oldWater = waterService.findByReferNum(pubBean.getOldRefnum());
			if(oldWater != null){
				oldWater.setTransStatus(TransStatus.RETURN);
				updateWater(oldWater);
			}
			
			// 保存交易流水
			addWater(water);
			transResultBean.setWater(water);
			// 删除冲正流水
			reverseWaterService.delete();

			// 更新结算数据
			changeSettle(pubBean);

		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}
		return Step.TRANS_RESULT;
	}
		
	/** 结果显示,打印*/
	@AnnStep(stepIndex =  Step.TRANS_RESULT)
	public int step_TransResult(){
		
		//启动电子签名
		if (transResultBean.getIsSucess()){
			doElecSign(transResultBean.getWater());
		}	
		
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);

		if(transResultBean.getIsSucess()){
			//电子签名上送,离线上送
			dealSendAfterTrade();
			return SUCC;
		}
		return FINISH;
	}


}
