package com.newland.payment.trans.impl.elecash;
import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.common.EmvTag;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 电子现金现金圈存
 * @version 1.0
 * @author spy
 * @date 2015年5月22日
 * @time 下午2:49:45
 */
public final class ECLoad extends AbstractBaseTrans {
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int EMV_INIT = 3;
		public final static int EMV_PROCESS = 4;
		public final static int INPUT_PIN = 5;
		public final static int PRE_ONLINE_PROCESS = 6;
		public final static int PACK_AND_COMM = 7;
		public final static int EMV_COMPLETE = 8;
		public final static int APPEND_WATER = 9;
		public final static int SWIPE_OUT_CARD = 10;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	private PubBean outPubBean = null;
	
	private int transType;
	
	private String arqcTLV = null;
	
	private EmvApplication emvApp;
	
	private EmvAppModule emvAppModule;
	
	public ECLoad(int transType){
		this.transType = transType;
	}
	
	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
	}
	
	@Override
	protected int init() {
		int res = super.init();
		//设置交易类型,交易属性
		pubBean.setTransType(transType);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		//pubBean.setTransName(getText(R.string.common_elec_cash) + getText(R.string.load) + getText(R.string.amount_recharge));
		//pubBean.setTransAttr(TransAttr.ATTR_PBOC_EC);
		
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		return res;
	}
	
	
	@Override
	protected void release() {
		if (emvAppModule!=null) {
			emvAppModule.emvSuspend(0);
			emvAppModule = null;
		}
		emvApp = null;
		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)) {
			showToast(getText(R.string.card_unsupport_ic_card));
			return FINISH;
		}
		
		if(pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND)
			return Step.SWIPE_OUT_CARD;
		return Step.SWIP_CARD;
	}

	/***刷转出卡*/
	@AnnStep(stepIndex = Step.SWIPE_OUT_CARD)
	public int step_SwipCard_Out() {
		if(outPubBean == null){
			outPubBean = new PubBean();
		}
		outPubBean.setTransName(pubBean.getTransName());
		outPubBean.setTransType(pubBean.getTransType());
		boolean result = false;
		result = stepProvider.swipCard(outPubBean, ReadcardType.SWIPE | ReadcardType.ICCARD | ReadcardType.SWIPE_OUT);

		if (!result) {
			return FINISH;
		}
		
		switch (outPubBean.getCardInputMode()) {
		case ReadcardType.SWIPE:
			return Step.SWIP_CARD;
			
		case ReadcardType.ICCARD: // 接触式读卡
			result = stepProvider.emvSimpleProcess(outPubBean, new EmvApplication(this));
			if(!result){
				if(outPubBean.isFallBack()){
					return Step.SWIPE_OUT_CARD;
				}
				return FINISH;
			}
			/**
			 * 纯电子现金卡不能作为转出卡
			 */
			if (emvApp.checkIsOnlyEC()){
				showToast(emvApp.getMsgCheckEcOnly(pubBean.getTransType()));
				return FINISH;
			}
			//转入卡为IC卡,判断离线数据认证结果
			byte[] tvr = emvAppModule.getEmvData(EmvTag.TAG_95_TM_TVR);
			if (tvr == null || tvr.length != 5) {
				pubBean.setEmvTransResult(EmvTransResult.FAIL);
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.trans_fail));
				return Step.TRANS_RESULT;
			}
			LoggerUtils.d("111 转出卡为IC卡,TVR值：" + BytesUtils.bcdToString(tvr));
			if ((tvr[0] & 0x4c) != 0){
				transResultBean.setIsSucess(false);
				emvApp.DispOutICC(pubBean.getTransName(), "交易拒绝", emvApp.getEmvErrMsg());
				return FINISH;
			}
			
			//提示移卡
			transEndCheckCardExist();
			return Step.SWIP_CARD;
		default:
			return FINISH;
		}
	}
	
	
	/** 刷卡*/
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard_In() {
		boolean result = false;
		result = stepProvider.swipCard(pubBean, ReadcardType.ICCARD | ReadcardType.SWIPE_IN);

		if (!result) {
			return FINISH;
		}
		
		switch (pubBean.getCardInputMode()) {
		case ReadcardType.ICCARD: // 接触式读卡
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
		
		LoggerUtils.d("111 before get emv module");
		
		pubBean.setIsoField55(emvAppModule.packField55(false));
		
		arqcTLV = emvAppModule.getArqc();
		
		if(pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
			pubBean.setCardNoTransIn(pubBean.getPan());
			pubBean.setInputModeForTransIn(pubBean.getInputMode()+"20");
			
			pubBean.setPan(outPubBean.getPan());
			pubBean.setCardInputMode(outPubBean.getCardInputMode());
			pubBean.setInputMode(outPubBean.getInputMode());
			pubBean.setTrackData2(outPubBean.getTrackData2());
			pubBean.setTrackData3(outPubBean.getTrackData3());
		}
		
		return Step.INPUT_PIN;	
	}
	
	/** 输入密码 */
	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin() {
		//判断步骤跳转
		boolean result = false;
		
		switch(pubBean.getTransType()){
		case TransType.TRANS_EC_LOAD_CASH:
			return Step.PRE_ONLINE_PROCESS;
			
		case TransType.TRANS_EC_LOAD_NOT_BIND:
		case TransType.TRANS_EC_LOAD:
			break;
			
		}
		result = stepProvider.inputPin(pubBean);
		if(!result){
			return FINISH;
		}
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
		pubBean.setSecctrlInfo(getSecctrlInfo(pubBean));
		
		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		try {
			iso8583.setField(0, pubBean.getMessageID());
			/** 刷卡交易不上送主帐号 */
			
			if(pubBean.getCardInputMode() != ReadcardType.SWIPE){
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
			
			if (pubBean.getTransType() == TransType.TRANS_EC_LOAD_CASH
				 || (pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND && pubBean.getCardInputMode() == ReadcardType.SWIPE)){
				if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
					iso8583.setField(35, pubBean.getTrackData2());
				}
				if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
					iso8583.setField(36, pubBean.getTrackData3());
				}
			}
			
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			if (pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				iso8583.setField(48, pubBean.getInputModeForTransIn());
			}
			iso8583.setField(49, pubBean.getCurrency());
			if ('1' == pubBean.getInputMode().charAt(2)) {
				iso8583.setField(52, pubBean.getPinBlock());
			}
			if ('1' == pubBean.getInputMode().charAt(2)
					|| (!StringUtils.isEmpty(pubBean.getTrackData2()) && pubBean.getTransType() != TransType.TRANS_EC_LOAD)
					|| (!StringUtils.isEmpty(pubBean.getTrackData3()) && pubBean.getTransType() != TransType.TRANS_EC_LOAD)){
				iso8583.setField(53, pubBean.getSecctrlInfo());
			}
			
				iso8583.setField(55, pubBean.getIsoField55());
			iso8583.setField(60, pubBean.getIsoField60().getString());
			if (pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				iso8583.setField(62, StringUtils.strToHex(pubBean.getCardNoTransIn()));
			}
			iso8583.setField(64, "00");
			
			int resCode = 0;
			resCode = dealPackAndComm(true, false, false);
				
			if(resCode != STEP_CONTINUE){
				if (resCode == STEP_FINISH) {
					return Step.TRANS_RESULT;
				}
				return resCode;
			}

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			return Step.TRANS_RESULT;
		}
		//获取服务器返回的55域,用于Complete
		pubBean.setIsoField55(iso8583.getField(55));
		return Step.EMV_COMPLETE;
	}
	
	@AnnStep(stepIndex = Step.EMV_COMPLETE)
	public int step_EmvComplete(){
		
		//这边参数需要设置么
		int emvResult = emvApp.completeApp(pubBean, true, false);
		if(pubBean.isTSIComleted()){
			pubBean.getIsoField60().setFuncCode("00");
			ScriptResult scriptResult = scriptResultService.getScriptResult();
			scriptResult.getIsoField60().setFuncCode("00");
			scriptResult.setPan(pubBean.getCardNoTransIn());
			try {
				scriptResultService.updateScriptResult(scriptResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(emvResult == 0){
			//if (pubBean.getTransType() == TransType.TRANS_EC_LOAD_CASH) {
			byte[] balance = emvAppModule.getEmvData(0x9F79);
			if (balance != null) {
				pubBean.setEcBalance(Long.parseLong(BytesUtils
						.bcdToString(balance)));
			}
			//}
			pubBean.setEmv_Status(TransConst.EmvStatus.EMV_STATUS_ONLINE_SUCC);
			return Step.APPEND_WATER;
			
		} else {
			
			transResultBean.setIsSucess(false);
			//这边错误信息在completeApp中设置了,不在重复设置
			/*if(true){
				return Step.APPEND_WATER;
			}*/
			return Step.TRANS_RESULT;
		}
	}
	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		// 对业务处理结果进行判断
		try{
			Water water = getWater(pubBean);
				emvAppModule.setEmvAdditionInfo(water, arqcTLV);
				emvAppModule.setEmvWaterInfo(water);
			addWater(water);
			transResultBean.setIsSucess(true);
			transResultBean.setWater(water);
			//删除冲正流水
			reverseWaterService.delete();
			//更新结算数据
			changeSettle(pubBean);
			
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
