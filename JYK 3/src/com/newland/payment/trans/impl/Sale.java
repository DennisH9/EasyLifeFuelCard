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
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.common.TransConst.QpbocPriority;
import com.newland.pos.sdk.device.Device;
import com.newland.pos.sdk.emv.EmvCoreOperator.ECSelectMode;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

//import com.newland.mtype.module.common.light.LightType;

public class Sale  extends AbstractBaseTrans {
	
	private CardBean cardBean = null;
	private AmountBean amountBean = null;
	/**
	 * 0x9F26
	 */
	CommonBean<PubBean> commonBean = new CommonBean<PubBean>();

	private String arqcTLV = null;
	
	private EmvApplication emvApp;
	
	private EmvAppModule emvAppModule;
	
	private int transType;
	
	private boolean isOnline = false;
	
	private boolean isShowLight = false;
	private MessageTipBean messageTipBean; 
	/**
	 * 是否是部分扣款
	 */
	private boolean isPartial = false;
	private Long inputAmount;//输入的金额，用于部分承兑
	private Integer qPbocPriority = null;
	
	private ThirdInvokeBean thirdInvokeBean = null;

	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_AMOUNT = 2;
		public final static int SWIP_CARD = 4;
		public final static int EMV_INIT = 5;
		public final static int EMV_PROCESS = 6;
		public final static int INPUT_PIN = 7;
		public final static int PRE_ONLINE_PROCESS = 8;
		public final static int PACK_AND_COMM = 9;
		public final static int EMV_COMPLETE = 10;
		public final static int APPEND_WATER = 11;
//		public final static int DO_QUERY = 12;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public Sale(){
		this(TransType.TRANS_SALE);
	}
	
	/**
	 * 用于电子现金普通支付
	 * @param transType
	 */
	public Sale(int transType){
		this.transType = transType;
	}
	
	public Sale(CardBean cardBean, AmountBean amoundBean){
		this(TransType.TRANS_SALE);
		this.cardBean = cardBean;
		this.amountBean = amoundBean;
	}
	public Sale(int transType, int qPbocPriority){
		this.transType = transType;
		this.qPbocPriority = qPbocPriority;
	}
//
//	public Sale(AmountBean bean){
//		this(TransType.TRANS_SALE);
//		this.amountBean = bean;
//		
//	}
	
	public Sale(ThirdInvokeBean thirdInvokeBean) {
		this(TransType.TRANS_SALE);
		this.thirdInvokeBean = thirdInvokeBean;
	}

	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		messageTipBean = new MessageTipBean();
		//设置交易类型,交易属性
		pubBean.setTransType(transType);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		
		if (cardBean != null) {
			stepProvider.syncCardBean(pubBean, cardBean);
		}

		if (amountBean != null){
			//TODO 最大金额限制检查
			LoggerUtils.d("lxb getAmount:" + amountBean.getAmount());
			pubBean.setAmount(amountBean.getAmount());
			pubBean.setCurrency(amountBean.getCurrency());
		}
		
		if (thirdInvokeBean != null && thirdInvokeBean.getAmountBean()!= null){
			LoggerUtils.d("111 thirdinvokeBean:" + thirdInvokeBean.toString());
			// 最大金额限制检查
			pubBean.setAmount(thirdInvokeBean.getAmountBean().getAmount());
			pubBean.setCurrency(thirdInvokeBean.getAmountBean().getCurrency());
		}
		if (thirdInvokeBean != null ){
			LoggerUtils.d("hjh22    thirdInvokeBean.getLiter() "+thirdInvokeBean.getLiter() +"    "+thirdInvokeBean.getOilType());
			if(thirdInvokeBean.getPrice()!= -1){
				pubBean.setPrice(thirdInvokeBean.getPrice());
			}
			if(thirdInvokeBean.getOilType()!=null){
				pubBean.setOilType(thirdInvokeBean.getOilType());
			}
			if(thirdInvokeBean.getLiter()!= -1){
				pubBean.setLiter(thirdInvokeBean.getLiter());
			}
			if(thirdInvokeBean.getMacKey()!=null){
				pubBean.setMacKey(thirdInvokeBean.getMacKey());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getIp() "+thirdInvokeBean.getIp());
			if(thirdInvokeBean.getIp()!=null){
				ParamsUtils.setString(ParamsConst.PARAMS_KEY_WIFI_SERVERIP2
						,thirdInvokeBean.getIp());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getPort() "+thirdInvokeBean.getPort());
			if(thirdInvokeBean.getPort()!=null){
				ParamsUtils.setInt(ParamsConst.PARAMS_KEY_WIFI_PORT2
						, Integer.parseInt(thirdInvokeBean.getPort()));
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getGasNo() "+thirdInvokeBean.getGasNo());
			if(thirdInvokeBean.getGasNo()!=null){
				pubBean.setGasNo(thirdInvokeBean.getGasNo());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getRefuelerNo() "+thirdInvokeBean.getRefuelerNo());
			if(thirdInvokeBean.getRefuelerNo()!=null){
				pubBean.setRefuelerNo(thirdInvokeBean.getRefuelerNo());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getOilTypeNo() "+thirdInvokeBean.getOilTypeNo());
			if(thirdInvokeBean.getOilTypeNo()!=null){
				pubBean.setOilTypeNo(thirdInvokeBean.getOilTypeNo());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getOilWeight() "+thirdInvokeBean.getOilWeight());
			if(thirdInvokeBean.getOilWeight()!=-1){
				pubBean.setOilWeight(thirdInvokeBean.getOilWeight());
			}
			LoggerUtils.d("hjh22    thirdInvokeBean.getPosNo() "+thirdInvokeBean.getPosNo());
			if(thirdInvokeBean.getPosNo()!=null){
				pubBean.setPosNo(thirdInvokeBean.getPosNo());
			}
		}
		isOnline = false;
		isShowLight = false;
		
		return res;
	}
	
	@Override
	protected void release() {
		cardBean = null;
		amountBean = null;
		if (emvAppModule!=null) {
			emvAppModule.emvSuspend(0);
			emvAppModule.emvRFSuspend(0);
			emvAppModule = null;
		}
		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (TransType.TRANS_EC_PURCHASE == transType) {
			if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)) {
				showToast(getText(R.string.card_unsupport_ic_card));
				return FINISH;
			}
		}
		return Step.INPUT_AMOUNT;
	}

	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount() {
		if (thirdInvokeBean == null || thirdInvokeBean.getAmountBean() == null) {
			boolean result = stepProvider.inputAmount(pubBean);
			if (!result) {
				pubBean.setResponseCode("EC");
				pubBean.setMessage("INPUT_AMOUNT异常");
				return FINISH;
			}
		}
		inputAmount = pubBean.getAmount();
		pubBean.setOrderAmount(inputAmount);
		return Step.SWIP_CARD;
	}
//	@AnnStep(stepIndex = Step.DO_QUERY)
//	public int step_do_query() {
//		commonBean.setTitle(pubBean.getTransName());
//		commonBean.setValue(pubBean);
//		doQuery(commonBean);
//		LoggerUtils.d("hjh   "+ commonBean.getResult() +"   "+commonBean.getStepResult());
//		if (!commonBean.getResult()){
//			switch(commonBean.getStepResult()){
//				case SUCCESS:
//					transResultBean.setIsSucess(false);
//					transResultBean.setContent("优惠查询失败");
//					return Step.TRANS_RESULT;
//				default:
//					break;
//			}
//			return FINISH;
//		}
//		return Step.SWIP_CARD;
//	}


	/** 刷卡*/
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		
		if (cardBean == null){
			boolean result = false;
			int inputMode = 0;
			if(transType == TransType.TRANS_SALE){
				inputMode = ReadcardType.RFCARD;
				isShowLight = true;
				if (isShowLight){
					Device.lightTurnOn(1,0,0,0);
				}
			} else if(transType == TransType.TRANS_INSERT_CARD_SALE){
				LoggerUtils.d("111 插卡消费");
				if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)){
					transResultBean.setIsSucess(false);
					transResultBean.setContent("不支持插卡");
					return Step.TRANS_RESULT;
				}
				inputMode = ReadcardType.ICCARD;
				Device.lightTurnOn(1,0,0,0);
				isShowLight = true;
			} else {
				LoggerUtils.d("111 电子现金消费");
				inputMode = ReadcardType.ICCARD;
				//电子现金-快捷键
				if ((qPbocPriority != null && qPbocPriority == QpbocPriority.EC) 
					&& (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_QPBOC, true))){
					inputMode |= ReadcardType.RFCARD;
					Device.lightTurnOn(1,0,0,0);
					isShowLight = true;
				}
			}
			EmvModule emvModule = EmvAppModule.getInstance();
			emvModule.setRpc(true);
			if (qPbocPriority == null){
				qPbocPriority = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_QPBOC_PRIORITY, com.newland.pos.sdk.common.TransConst.QpbocPriority.EMV);
			}
			LoggerUtils.d("111 qPbocPriority：" + qPbocPriority);
			pubBean.setqPbocPriority(qPbocPriority);
			if (QpbocPriority.EMV == qPbocPriority){		
				emvModule.setQpbocPriority(false);
			} else {
				emvModule.setQpbocPriority(true);
			}
			result = stepProvider.swipCard(pubBean, inputMode);
			LoggerUtils.d("step_SwipCard swipCard end!");
			if(!result){
				if (isShowLight){
					Device.lightTurnOff(1,1,1,1);
				}	
				return FINISH;
			}
		}
		LoggerUtils.d("pubBean.getCardInputMode()  "+pubBean.getCardInputMode());
		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
			if (isShowLight){
				Device.lightTurnOff(1,1,1,1);
			}	
			isOnline = true;
			return Step.INPUT_PIN;
			
		case ReadcardType.ICCARD: // 接触式读卡
			//TODO 这块可能有问题
			if( transType == TransType.TRANS_SALE 
			&& ParamsUtils.getBoolean(ParamsConst.PARAM_IS_HIDING_SWING)){
	
				qPbocPriority = null;
				MessageTipBean messageTipBeanIC = new MessageTipBean();
				messageTipBeanIC.setContent("请挥卡，使用云闪付");
				messageTipBeanIC.setCancelable(true);
				messageTipBeanIC.setTimeOut(60);
				showUIMessageTip(messageTipBeanIC);
				if (messageTipBeanIC.getResult() == true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return Step.SWIP_CARD;
				}
				else {
					return FINISH;
				}
			}
			if (isShowLight){
				Device.lightTurnOff(1,1,1,1);
			}	
			return Step.EMV_INIT;
		case ReadcardType.RFCARD: // 非接式读卡
			//转去qPboc
			if (doQpboc(pubBean)) {
				LoggerUtils.d("lxb doQpoc succ1111");
				return SUCC;
			} else {
				LoggerUtils.d("lxb doQpoc fail1111");
				return FINISH;
			}	
		default:
			if (isShowLight){
				Device.lightTurnOff(1,1,1,1);
			}	
			return FINISH;
		}

	}

	@AnnStep(stepIndex = Step.EMV_INIT)
	public int step_EmvInit(){

		LoggerUtils.d("step_EmvInit start!");
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		if(TransType.TRANS_EC_PURCHASE == transType){
			pubBean.setEcSelectMode(ECSelectMode.PATH_EC);
		} else {
			pubBean.setEcSelectMode(ECSelectMode.PATH_EMV);
		}
		
		boolean result = stepProvider.emvInit(pubBean, emvApp);
		
		if(!result) {
			
			if(pubBean.isFallBack() && transType != TransType.TRANS_EC_PURCHASE){
				cardBean = null;
				return Step.SWIP_CARD;
			}
			return FINISH;
		}
		
		if (TransType.TRANS_EC_PURCHASE == pubBean.getTransType()){
			if (!emvApp.checkIsEC()){
				showToast("不是电子现金卡\r\n或输入金额超限");
				return FINISH;
			}
		}
		
		byte[] data = emvAppModule.getEmvData(0x9F74);
		if (data == null){
			LoggerUtils.d("Sale->取不到电子现金授权码");
			if (emvApp.checkIsOnlyEC()){
				showToast("纯电子现金卡,获取不到电子现金授权码");
				return FINISH;
			}
		} else {
			//走电子现金	
			if (TransType.TRANS_SALE_EMV == pubBean.getTransType()){
				showToast("不支持该卡");
				return FINISH;
			}
			LoggerUtils.d("111 进行黑名单检查~~~~");
			//电子现金,检测黑名单
			if (!CheckIsNotInBlk(pubBean.getPan())){
				showToast("该卡被列入黑名单\r\n交易拒绝");
				return FINISH;
			}
			
		}
		
		return Step.EMV_PROCESS;
		
	}
	

	@AnnStep(stepIndex = Step.EMV_PROCESS)
	public int step_EmvProcess(){
		int emvResult = emvApp.processApp(pubBean);
		LoggerUtils.d("111 processApp emvResult:" + emvResult);
		if(emvResult != 0){
			switch(emvResult){
			case EmvConst.EMV_TRANS_ACCEPT:
				initPubBean();
				//脱机流水加1
				addTraceNo();
				pubBean.setIsoField60(new ISOField60(pubBean.getTransType(), pubBean.isFallBack()));
				pubBean.setInputMode(pubBean.getInputMode()+"2");
				pubBean.setIsoField55(emvAppModule.packField55(false));
				pubBean.setInternationOrg(emvApp.getEmvOrgSheet());
				
				byte[] balance = emvAppModule.getEmvData(0x9F79);
				if(balance != null){
					pubBean.setTransType(TransType.TRANS_EC_PURCHASE);
					pubBean.setEcBalance(Long.parseLong(BytesUtils.bcdToString(balance)));
				}else{
					pubBean.setTransType(TransType.TRANS_SALE);
				}
					
				pubBean.setEmv_Status(EmvStatus.EMV_STATUS_OFFLINE_SUCC);
				Water water = getWater(pubBean);
				
				emvAppModule.setEmvAdditionInfo(water, null);
				emvAppModule.setEmvWaterInfo(water);
				
				try {
					addWater(water);
					incOfflineUnSendNum();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//更新结算数据
				changeSettle(pubBean);
				
				transResultBean.setIsSucess(true);
				transResultBean.setWater(water);
				return Step.TRANS_RESULT;
				
			case EmvConst.EMV_TRANS_DENIAL:
				//添加脱机失败流水
				LoggerUtils.d("111 消费保存脱机失败流水,transType="+ pubBean.getTransType() );
				initPubBean();
				//脱机流水加1
				addTraceNo();
				pubBean.setInternationOrg(emvApp.getEmvOrgSheet());
				pubBean.setEmv_Status(EmvStatus.EMV_STATUS_OFFLINE_FAIL);
				emvAppModule.SaveEmvFailWater(activity, pubBean);
				pubBean.setEmvTransResult(EmvTransResult.FAIL);
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.trans_fail));
				return Step.TRANS_RESULT;
				
			default:
				//比如密码取消 -1703
				pubBean.setResponseCode("EC");
				pubBean.setMessage("EMV_PROCESS异常");
				return FINISH;
			}
		}
		
		//请求联机
		if (emvApp.checkIsOnlyEC()){
			showToast(emvApp.getMsgCheckEcOnly(TransType.TRANS_SALE));
			return FINISH;
		}
		
		isOnline = true;
		
		pubBean.setIsoField55(emvAppModule.packField55(false));
		
		arqcTLV = emvAppModule.getArqc();
		
		if (emvApp.checkInputPin())
		{
			//跳过PIN输密
			return Step.PRE_ONLINE_PROCESS;
				
		} else {
			
			return Step.INPUT_PIN;	
		}
	}
		
	/** 输入密码*/
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
			return Step.TRANS_RESULT;
		}

		int resCode = 0;
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
				pubBean.getCardInputMode()==ReadcardType.RFCARD) {
			//保存冲正55域
			pubBean.setIsoField55(emvAppModule.packReversalField55(true));
		}
		resCode = dealPackAndComm(true, true, false);
		LoggerUtils.d("dd dealPackAndComm->resCode:" + resCode);
		if(resCode != STEP_CONTINUE){
			
			if (resCode == STEP_FINISH) {
				if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
						pubBean.getCardInputMode()==ReadcardType.RFCARD){
			LoggerUtils.d("dd dealPackAndComm->连接失败isOnline置false");		
					isOnline = false;
					return Step.EMV_COMPLETE;
				}
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		
		/** IC卡交易，直接跳到Emv_Complete*/
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
				pubBean.getCardInputMode()==ReadcardType.RFCARD) {
			pubBean.setIsoField55(iso8583.getField(55));
			return Step.EMV_COMPLETE;
		}
		
		/** 以下是磁条卡,部分承兑及响应码显示*/
		String responseCode = pubBean.getResponseCode();
		if("00".equals(responseCode) 
//			||"11".equals(responseCode)
			|| "A2".equals(responseCode)
			|| "A4".equals(responseCode)
			|| "A5".equals(responseCode)
			|| "A6".equals(responseCode) ) {
			transResultBean.setIsSucess(true);	
			displayResponse(true, pubBean);
		} else { 
			if("10".equals(responseCode)) {
				isPartial = true;
			} else {
				//收到应答码,不冲正,删除冲正数据
				reverseWaterService.delete();							 
				transResultBean.setIsSucess(false);
				displayResponse(false, pubBean);
				return Step.TRANS_RESULT;
			}
		}
		
		if(true == isPartial) {
			messageTipBean.setContent(getText(R.string.trans_succse)
					+","+getText(R.string.sale_is_Partial));
			showUIMessageTip(messageTipBean);
		}
		return Step.APPEND_WATER;
	}		
		
	@AnnStep(stepIndex = Step.EMV_COMPLETE)
	public int step_EmvComplete(){
		//这边参数需要设置么
		LoggerUtils.d("dd isOnline:" + isOnline);
		int emvResult = emvApp.completeApp(pubBean, isOnline, true);
		if(emvResult == 0){
			if (pubBean.getTransType() == TransType.TRANS_EC_PURCHASE) {
				byte[] balance = emvAppModule.getEmvData(0x9F79);
				if(balance != null){
					pubBean.setEcBalance(Long.parseLong(BytesUtils.bcdToString(balance)));
				}
			}
			if (isOnline){
				//走联机,交易类型为-消费
				pubBean.setTransType(TransType.TRANS_SALE);
				pubBean.setEmv_Status(EmvStatus.EMV_STATUS_ONLINE_SUCC);
			}else{
				//不能联机,转脱机成功
				LoggerUtils.d("dd 消费-不能联机,转脱机成功!!!!!!!!!!!!!!!!!");
				pubBean.setEmv_Status(EmvStatus.EMV_STATUS_OFFLINE_SUCC);
			}
			
			return Step.APPEND_WATER;
			
		} else {
			transResultBean.setIsSucess(false);
//			transResultBean.setContent(getText(R.string.trans_fail));
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
			if (pubBean.getTransType() == TransType.TRANS_SALE){
				//联机消费,取54域可用余额,需判断是否为空
				String balance = iso8583.getField(54);
				if (!StringUtils.isNullOrEmpty(balance)){
					if (balance.length() >= 20){
						water.setBalance(Long.parseLong(balance.substring(8, 20)));
					}
				}		
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
		// 对业务处理结果进行判断
		
		//启动电子签名
		if (transResultBean.getIsSucess()){
			if (pubBean.getTransType() == TransType.TRANS_SALE && isOnline){
				doElecSign(transResultBean.getWater());
			}
		}
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);
		
		if(transResultBean.getIsSucess()){
			
			if (true == isPartial) {
				messageTipBean.setTitle(getText(R.string.common_pls_confirm));
				messageTipBean.setContent(getText(R.string.sale_is_Partial_transaction));
				messageTipBean.setCancelable(true);
				messageTipBean.setTimeOut(0);
				showUIMessageTip(messageTipBean);
				
				if(messageTipBean.getResult()) { 
					long subAmount = inputAmount - transResultBean.getWater().getAmount();
					messageTipBean.setTitle(getText(R.string.sale_partial_deduction));
					messageTipBean.setContent(getText(R.string.sale_unpaid_amount) 
							+ FormatUtils.formatAmount("" + subAmount)
							+ getText(R.string.sale_yuan) + "\r\n" 
							+ getText(R.string.sale_please_charge_separately));
					messageTipBean.setCancelable(false);
					showUIMessageTip(messageTipBean);
				} else {
					doVoidPartialSale(pubBean);
				}
				isPartial = false;
			}
			if (TransType.TRANS_SALE == pubBean.getTransType()	&& isOnline){
				//电子签名上送,离线上送
				dealSendAfterTrade();
			}
			
			//TODO 交易结果集上送 ， 上送失败则凭 pos签购单人工处理
//			if (doSendInfo(pubBean) == false) {
//				//showToast("交易上送失败则凭 pos签购单人工处理");
//			} 
			
			return SUCC;
		}
		return FINISH;
	}
}
