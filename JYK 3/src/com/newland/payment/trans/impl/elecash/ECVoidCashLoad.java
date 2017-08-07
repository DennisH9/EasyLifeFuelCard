package com.newland.payment.trans.impl.elecash;
import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 电子现金充值撤销
 * @version 1.0
 * @author spy
 * @date 2015年5月16日
 * @time 上午11:08:12
 */
public final class ECVoidCashLoad extends AbstractBaseTrans {
	
	private Water oldWater = null;
	
	private String arqcTLV = null;

	private EmvApplication emvApp;
	private EmvAppModule emvAppModule;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_TRACENO = 2;
		public final static int SWIP_CARD = 3;
		public final static int EMV_INIT = 4;
		public final static int EMV_PROCESS = 5;
		public final static int PRE_ONLINE_PROCESS = 6;
		public final static int PACK_AND_COMM = 7;
		public final static int EMV_COMPLETE = 8;
		public final static int APPEND_WATER = 9;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	@Override
	protected void checkPower() {
		super.checkPower();
		checkManagerPassword = true;
	}

	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
		
		pubBean.setTransType(TransType.TRANS_EC_VOID_LOAD_CASH);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		return res;
	}
	
	
	@Override
	protected void release() {
		oldWater = null;
		emvApp = null;
		emvAppModule = null;
		super.release();
	}

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)) {
			showToast(getText(R.string.card_unsupport_ic_card));
			return FINISH;
		}
		return Step.INPUT_TRACENO;
	}
	
	/**
	 * 请输入凭证号(流水号)
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_TRACENO)
	public int step_InputTraceNo() {
		oldWater = stepProvider.inputOldTraceNo(pubBean, TransType.TRANS_EC_LOAD_CASH);
		if (oldWater == null) {
			return FINISH;
		} else {
			return Step.SWIP_CARD;
		}
	}
	
	/** 刷卡*/
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		boolean result = false;
		result = stepProvider.swipCard(pubBean, ReadcardType.ICCARD);

		if (!result) {
			return FINISH;
		}
		
		switch (pubBean.getCardInputMode()) {
		case ReadcardType.ICCARD: // 接触式读卡
			//与消费不同的地方
			//pubBean.setEcSelectMode(ECSelectMode.PATH_EC);
			return Step.EMV_INIT;
		default:
			return FINISH;
		}
	}
	
	@AnnStep(stepIndex = Step.EMV_INIT)
	public int step_EmvInit(){
		boolean result = stepProvider.emvInit(pubBean, emvApp);
		
		if(!result){
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
		
		return Step.PRE_ONLINE_PROCESS;	
	}
	
	/** 脚本上送、冲正 */
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
		
		LoggerUtils.d("111  p5_PackAndComm");
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
			
			if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
				iso8583.setField(35, pubBean.getTrackData2());
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
				iso8583.setField(36, pubBean.getTrackData3());
			}
			iso8583.setField(37, pubBean.getOldRefnum());
			if(!StringUtils.isEmpty(pubBean.getOldAuthCode())){
				iso8583.setField(38, pubBean.getOldAuthCode());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());
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
			iso8583.setField(61, pubBean.getIsoField61());
			iso8583.setField(64, "00");

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			pubBean.setEmvTransResult(EmvTransResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			return Step.TRANS_RESULT;
		}
		int resCode = 0;
		pubBean.setIsoField55(emvAppModule.packReversalField55(true));
		resCode = dealPackAndComm(true, true, false);
			
		if(resCode != STEP_CONTINUE){
			if (resCode == STEP_FINISH) {
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		pubBean.setIsoField55(iso8583.getField(55));
		return Step.EMV_COMPLETE;
	}
	
	@AnnStep(stepIndex = Step.EMV_COMPLETE)
	public int step_EmvComplete(){
		//这边参数需要设置么
		int emvResult = emvApp.completeApp(pubBean, true, true);
		if(emvResult == 0){
			//if (pubBean.getTransType() == TransType.TRANS_EC_PURCHASE) {
				byte[] balance = emvAppModule.getEmvData(0x9F79);
				if(balance != null){
					pubBean.setEcBalance(Long.parseLong(BytesUtils.bcdToString(balance)));
				}
			//}
			pubBean.setEmv_Status(TransConst.EmvStatus.EMV_STATUS_ONLINE_SUCC);
			return Step.APPEND_WATER;
			
		} else {
			transResultBean.setIsSucess(false);
			//这边错误信息在completeApp中设置了,这边不在重复设置
			
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
			addWater(water);
			transResultBean.setIsSucess(true);
			transResultBean.setWater(water);
			//删除冲正流水
			reverseWaterService.delete();
			//更新结算数据
			changeSettle(pubBean);
			//更新原流水状态
			oldWater.setTransStatus(TransConst.TransStatus.REV);
			updateWater(oldWater);
		}catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
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
		transResultBean = showUITransResult(transResultBean);

		if(transResultBean.getIsSucess()){
			//电子签名上送,离线上送
			dealSendAfterTrade();
			return SUCC;
		}
		return FINISH;
	}


}
