package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class Auth  extends AbstractBaseTrans {
	
	private CardBean cardBean = null;
	/**
	 * 0x9F26
	 */
	private String arqcTLV = null;
	
	private EmvApplication emvApp;
	
	private EmvAppModule emvAppModule;
	
	private boolean isEmv = false;
	
	private boolean isNoPsw = false;
	
	private ThirdInvokeBean thirdInvokeBean = null;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_AMOUNT = 2;
		public final static int SWIP_CARD = 3;
		public final static int EMV_INIT_RF = 4;
		public final static int EMV_INIT = 5;
		public final static int EMV_PROCESS = 6;
		public final static int INPUT_PIN = 7;
		public final static int PRE_ONLINE_PROCESS = 8;
		public final static int PACK_AND_COMM = 9;
		public final static int EMV_COMPLETE = 10;
		public final static int APPEND_WATER = 11;
		
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public Auth(){
	}
	
	public Auth(CardBean bean){
		this.cardBean = bean;
	}
		
	public Auth(boolean isEmv){
		this.isEmv = isEmv;
	}

	public Auth(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		//设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_PREAUTH);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		pubBean.setAmount(0L);
		
		if (thirdInvokeBean != null && thirdInvokeBean.getAmountBean()!= null){
			pubBean.setAmount(thirdInvokeBean.getAmountBean().getAmount());
			pubBean.setCurrency(thirdInvokeBean.getAmountBean().getCurrency());
		}
		
		if (cardBean != null) {
			stepProvider.syncCardBean(pubBean, cardBean);
		}
		return res;
	}
	
	
	@Override
	protected void release() {
		cardBean = null;
		if (emvAppModule!=null) {
			emvAppModule.emvSuspend(0);
			emvAppModule.emvRFSuspend(0);
			emvAppModule = null;
		}
		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.INPUT_AMOUNT;
	}
	
	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount() {
		boolean result = stepProvider.inputAmount(pubBean);
		if(!result){
			return FINISH;
		}
		return Step.SWIP_CARD;
	}
	
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		if (cardBean == null){
			boolean result = false;
			int inputMode = ReadcardType.ICCARD | ReadcardType.RFCARD;
			if (!isEmv){
				inputMode |= ReadcardType.SWIPE;
			}
			result = stepProvider.swipCard(pubBean, inputMode);
			
			if(!result){
				return FINISH;
			}
		}

		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
			return Step.INPUT_PIN;
			
		case ReadcardType.ICCARD: // 接触式读卡
			return Step.EMV_INIT;
		case ReadcardType.RFCARD: // 非接式读卡
			return Step.EMV_INIT_RF;
			
		default:
			pubBean.setResponseCode("EC");
			pubBean.setMessage("读取卡片失败");
			return FINISH;
		}
	}

	@AnnStep(stepIndex = Step.EMV_INIT_RF)
	public int step_EmvInitRf(){
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		
		if(pubBean.getForcePassword()){
			pubBean.getEmvBean().setQuickPayByPWD(1);
		}
		
		if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW)){
			pubBean.getEmvBean().setQPSFlag(1);
		}
		
		boolean result = stepProvider.emvSimpleProcess(pubBean, emvApp);
		
		if(!result){
			return FINISH;
		}
		pubBean.setIsoField55(emvAppModule.packField55(false));
		
		return Step.INPUT_PIN;
		
	}
	
	@AnnStep(stepIndex = Step.EMV_INIT)
	public int step_EmvInit(){
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		
		boolean result = stepProvider.emvInit(pubBean, emvApp);
		
		if(!result){
			if(pubBean.isFallBack()){
				cardBean = null;
				return Step.SWIP_CARD;
			}
			return FINISH;
		}
		
		return Step.EMV_PROCESS;
		
	}
	

	@AnnStep(stepIndex = Step.EMV_PROCESS)
	public int step_EmvProcess(){
		int emvResult = emvApp.processApp(pubBean);
		LoggerUtils.d("111 processApp emvResult:" + emvResult);
		if(emvResult != 0){
			switch(emvResult){
			default:
				//比如密码取消 -1703
				return FINISH;
			}
		}
		
		
		
		pubBean.setIsoField55(emvAppModule.packField55(false));
		
		arqcTLV = emvAppModule.getArqc();
		
		if (emvApp.checkInputPin())
		{
			return Step.PRE_ONLINE_PROCESS;
		} else {
			// 	跳过PIN输密
			return Step.INPUT_PIN;	
		}
	}
		
	/** 输入密码*/
	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin(){
		
		boolean result = false;
		
		if(pubBean.getCardInputMode() == ReadcardType.RFCARD){
			//闪付凭密的情况，强制要求输入密码
			if(pubBean.getForcePassword()){
				result = stepProvider.inputPin(pubBean);
				if(!result){
					return FINISH;
				}
			}else{
				//小额免密的情况
				if(quickPayNoPsw(pubBean,emvAppModule)){
					isNoPsw = true;
					return Step.PRE_ONLINE_PROCESS;	
				}else{
					result = stepProvider.inputPin(pubBean);
					if(!result){
						return FINISH;
					}
				}
			}
		}else{
			result = stepProvider.inputPin(pubBean);
			if(!result){
				return FINISH;
			}
		}
		return Step.PRE_ONLINE_PROCESS;
	}
	
	/**脚本上送、冲正*/
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
		
		/** 数据组织到pubBean中 */
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
		
		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		try {
			iso8583.setField(0, pubBean.getMessageID());
			/** 刷卡交易不上送主帐号 */
			if (pubBean.getCardInputMode()!=ReadcardType.SWIPE) {
				iso8583.setField(2, pubBean.getPan());
			}
			iso8583.setField(3, pubBean.getProcessCode());
			iso8583.setField(4, pubBean.getAmountField());
			
			iso8583.setField(11, pubBean.getTraceNo());
			if (!StringUtils.isEmpty(pubBean.getExpDate())) {
				iso8583.setField(14, pubBean.getExpDate());
			}
			
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(25, pubBean.getServerCode());
			if ('1' == pubBean.getInputMode().charAt(2)){
				iso8583.setField(26, String.format("%02d", 12));
			}
			
			if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
				iso8583.setField(35, pubBean.getTrackData2());
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
				iso8583.setField(36, pubBean.getTrackData3());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());
			if ('1' == pubBean.getInputMode().charAt(2)) {
				iso8583.setField(52, pubBean.getPinBlock());
			}
			if ('1' == pubBean.getInputMode().charAt(2)
					|| !StringUtils.isEmpty(pubBean.getTrackData2())
					|| !StringUtils.isEmpty(pubBean.getTrackData3())){
				iso8583.setField(53, pubBean.getSecctrlInfo());
			}
			if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
					pubBean.getCardInputMode()==ReadcardType.RFCARD) {
				iso8583.setField(55, pubBean.getIsoField55());
			}
			
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(64, "00");

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			pubBean.setEmvTransResult(EmvTransResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			pubBean.setResponseCode("EC");
			pubBean.setMessage("打包失败");
			return Step.TRANS_RESULT;
		}
		
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
				pubBean.getCardInputMode()==ReadcardType.RFCARD) {
			pubBean.setIsoField55(emvAppModule.packReversalField55(true));			
		}
		
		int resCode = 0;
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD){
			resCode = dealPackAndComm(true, true, false);
		} else {
			resCode = dealPackAndComm(true, true, true);
		}
		
		if(resCode != STEP_CONTINUE){
			//连接出错
			if (STEP_FINISH == resCode){
				return TransConst.STEP_FINAL;
			}
			return resCode;
		}
		
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD) {
			pubBean.setIsoField55(iso8583.getField(55));
			return Step.EMV_COMPLETE;
		} else {
			return Step.APPEND_WATER;
		}
	}	
	
	
	@AnnStep(stepIndex = Step.EMV_COMPLETE)
	public int step_EmvComplete(){
		//这边参数需要设置么
		int emvResult = emvApp.completeApp(pubBean, true, true);
		if(emvResult == 0){
			pubBean.setEmv_Status(EmvStatus.EMV_STATUS_ONLINE_SUCC);
			return Step.APPEND_WATER;
			
		} else {
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
			return Step.TRANS_RESULT;
		}
	}
	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		// 对业务处理结果进行判断
		try{
			Water water = getWater(pubBean);
			if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
					pubBean.getCardInputMode()==ReadcardType.RFCARD) {
				emvAppModule.setEmvAdditionInfo(water, arqcTLV);
				emvAppModule.setEmvWaterInfo(water);
			}
			
			if(CheckIsNoSign(pubBean)){
				water.setNoSignFlag(true);
			}else{
				water.setNoSignFlag(false);
			}
			water.setNoPswFlag(isNoPsw);
			
			addWater(water);
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
			transResultBean.setIsSucess(true);
		}catch (Exception e) {
			pubBean.setResponseCode("EC");
			pubBean.setMessage("保存流水失败");
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}
		
		return Step.TRANS_RESULT;
	}
	
	/**结果显示、打印处理*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		LoggerUtils.d("Auth 结果显示、打印处理");
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
