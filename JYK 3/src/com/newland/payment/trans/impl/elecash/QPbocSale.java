package com.newland.payment.trans.impl.elecash;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.SoundUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.Const.SoundType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.common.EmvResult;
import com.newland.pos.sdk.common.EmvTag;
import com.newland.pos.sdk.device.Device;
import com.newland.pos.sdk.emv.EmvCoreOperator;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;

import java.util.HashMap;


/**
 * 快速支付
 * @author linld
 * @date 2015-06-03
 */
public class QPbocSale  extends AbstractBaseTrans {
	
	private String arqcTLV = null;
	
	private EmvApplication emvApp;
	
	private EmvAppModule emvAppModule;
	
	private long amount =  0;
	
	private int qPbocPriority = 0;
	
	private boolean isNoPsw = false;
	
	private CommonBean<PubBean> commonBean = null;

	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_AMOUNT = 2;
		public final static int EMV_INIT = 3;
		public final static int INPUT_PIN = 4;
		public final static int PRE_ONLINE_PROCESS = 5;
		public final static int PACK_AND_COMM = 6;
		public final static int APPEND_WATER = 7;
		public final static int DO_QUERY = 12;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
		
	public QPbocSale(){
		amount = 0;
		qPbocPriority = com.newland.pos.sdk.common.TransConst.QpbocPriority.EC;
	}
	
	public QPbocSale(CommonBean<PubBean> commonBean){
		this.commonBean = commonBean;
		commonBean.setResult(false);
		amount = commonBean.getValue().getAmount();
		qPbocPriority = commonBean.getValue().getqPbocPriority();
	}
	
	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
		if (commonBean!=null) { 
			// 当其他交易主动调用时，不开启事务
			transcationManagerFlag = false;
		}
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		//设置交易类型,交易属性
		if (commonBean != null){
			pubBean = commonBean.getValue();
		}
		pubBean.setTransType(TransType.TRANS_SALE);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		pubBean.setqPbocPriority(qPbocPriority);
		emvApp = new EmvApplication(this);
		emvAppModule = emvApp.getEmvAppModule();
		return res;
	}
	
	
	@Override
	protected void release() {
		Device.lightTurnOff(1,1,1,1);
		if (emvAppModule!=null) {
			emvAppModule.emvSuspend(0);
			emvAppModule.emvRFSuspend(0);
			emvAppModule = null;
		}
		if (commonBean != null){
			goOnStep(commonBean);
		} else {
			super.release();
		}
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (amount == 0){
			if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC, true)){
				showToast(getText(R.string.card_unsupport_ic_card));
				pubBean.setResponseCode("EC");
				pubBean.setMessage("不支持IC卡交易");
				return FINISH;
			}
			if(!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_RF, false)){
				showToast(getText(R.string.card_check_support_rf_card));
				pubBean.setResponseCode("EC");
				pubBean.setMessage("不支持非接");
				return FINISH;
			}
						
			// 加入判断参数是否下载
			if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_AID_DOWN)) {
				showToast("请下载AID参数");
				pubBean.setResponseCode("EC");
				pubBean.setMessage("IC卡AID未下载");
				return FINISH;
			}
			
			if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN)) {
				showToast("请下载CAPK参数");
				pubBean.setResponseCode("EC");
				pubBean.setMessage("IC卡公钥未下载");
				return FINISH;
			}
		}else{
			//从消费入口进来,检查交易开关
			//走到这里qPboc已经完成交易，此处限制应该放到外面
		} 
		return Step.INPUT_AMOUNT;
	}

	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount() {
		if (amount == 0){
			//输入金额
			boolean result = stepProvider.inputAmount(pubBean);
			if(!result){
				pubBean.setResponseCode("EC");
				pubBean.setMessage("INPUT_AMOUNT-swipCard异常");
				return FINISH;
			}
			EmvModule emvModule = EmvAppModule.getInstance();
			emvModule.setRpc(true);
			emvModule.setQpbocPriority(true);
			//寻卡
			Device.lightTurnOn(1,0,0,0);
			result = stepProvider.swipCard(pubBean, ReadcardType.RFCARD);
			if(!result){
				pubBean.setResponseCode("EC");
				pubBean.setMessage("INPUT_AMOUNT-swipCard异常");
				return FINISH;
			}
			if (ReadcardType.RFCARD != pubBean.getCardInputMode()){
				showToast(getText(R.string.card_read_error));
				pubBean.setResponseCode("EC");
				pubBean.setMessage("INPUT_AMOUNT-RFCARD异常");
				return FINISH;
			}
			
		} else {
			pubBean.setAmount(amount);
			pubBean.setCurrency(commonBean.getValue().getCurrency());
		}
		
		pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_qPBOC);
		pubBean.setqPbocPriority(qPbocPriority);
		//这个亮灯动作在K21认证版本有做，生产版没有
		Device.lightTurnOn(0,1,0,0);
		
		if(pubBean.getForcePassword()){
			pubBean.getEmvBean().setQuickPayByPWD(1);
		}
		
		if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW)){
			pubBean.getEmvBean().setQPSFlag(1);
		}
		
		int result = emvApp.qPbocProcess(pubBean);
	
		LoggerUtils.e("qps:qPbocProcess end:"+result);
		initPubBean();
		switch (result){
		case EmvResult.EMV_FAIL:
			Device.lightTurnOff(1,1,1,0);
			Device.lightTurnOn(0,0,0,1);
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV QPBOC流程执行失败");
			return FINISH;
			
		case EmvConst.RFERR_PREPROCESS_AMTLIMITOVER:
			//交易失败要有提示音
			SoundUtils.getInstance().play(SoundType.WARNING);
			//RPC,预处理也放在K21,相关提示在这里过滤显示
			showToast("输入金额超限");
			pubBean.setResponseCode("EC");
			pubBean.setMessage("输入金额超限");
			return FINISH;
			
		case EmvConst.EMV_TRANS_QPBOC_GOONLINE:
			Device.lightBlink(0,0,1,0);
			//联机
			if (emvApp.checkIsPay()){
				LoggerUtils.d("111 qPboc脱机失败转联机,保存失败流水");
				saveFailWater();
			}
			//联机,是消费交易
			SoundUtils.getInstance().play(SoundType.BEEP);
//			pubBean.setTransType(TransType.TRANS_SALE);
//			pubBean.setTransName(getTitle(pubBean.getTransType()));
			pubBean.setIsoField55(emvAppModule.packField55(false));
			getCardInfoAddtion();
			return Step.DO_QUERY;
			
		case EmvConst.EMV_TRANS_QPBOC_ACCEPT:
			//脱机成功
			Device.lightTurnOn(0,0,1,0);
			//黑名单检测
			if (!CheckIsNotInBlk(pubBean.getPan())){
				showToast("该卡被列入黑名单\r\n交易拒绝");
				//交易失败要有提示音
				SoundUtils.getInstance().play(SoundType.WARNING);
				pubBean.setResponseCode("EC");
				pubBean.setMessage("卡片被列入黑名单");
				return FINISH;
			}	
			
			//交易成功要有提示音
			SoundUtils.getInstance().play(SoundType.BEEP);
			
			//脱机流水加1
			addTraceNo();
			pubBean.setIsoField60(new ISOField60(pubBean.getTransType(), pubBean.isFallBack()));
			pubBean.setInputMode(pubBean.getInputMode()+"2");
			pubBean.setIsoField55(emvAppModule.packField55(false));
			getCardInfoAddtion();
			pubBean.setInternationOrg(emvApp.getEmvOrgSheet());
			
			pubBean.setTransType(TransType.TRANS_EC_PURCHASE);
			pubBean.setTransName(getTitle(pubBean.getTransType()));
			
			byte[] balance = emvAppModule.getEmvData(0x9F5D);
			if(balance != null){
				pubBean.setEcBalance(Long.parseLong(BytesUtils.bcdToString(balance)));
			}
			pubBean.setEmv_Status(EmvStatus.EMV_STATUS_OFFLINE_SUCC);
			Water water = getWater(pubBean);
			//设置8A
			emvAppModule.setEmvData(0x8A, new byte[]{0x59,0x31});
			emvAppModule.setEmvAdditionInfo(water, null);
			emvAppModule.setEmvWaterInfo(water);
			
			try {
				addWater(water);
			} catch (Exception e) {
				pubBean.setResponseCode("EC");
				pubBean.setMessage("脱机流水保存失败");
				e.printStackTrace();
			}
			//离线上送笔数加1
			incOfflineUnSendNum();
			//更新结算数据
			changeSettle(pubBean);
			//交易成功要有提示音
			SoundUtils.getInstance().play(SoundType.BEEP);
			
			transResultBean.setIsSucess(true);
			transResultBean.setWater(water);
			
			return Step.TRANS_RESULT;
		
		default:
			//交易失败要有提示音
			SoundUtils.getInstance().play(SoundType.WARNING);
			
			Device.lightTurnOff(1,1,1,0);
			Device.lightTurnOn(0,0,0,1);
			//交易失败
			if (emvApp.checkIsPay()){
				LoggerUtils.d("111 qPboc保存失败流水");
				saveFailWater();
			}
			LoggerUtils.d("111 emvAppModule.emvGetErrorCode() = " + emvAppModule.emvGetErrorCode());
			if (EmvConst.EMV_TRANS_QPBOC_DENIAL == result){
				showToast("交易拒绝\r\n" + emvApp.getEmvErrMsg());
			} else {
				if (-2105 == emvAppModule.emvGetErrorCode()){
					showToast("交易终止\r\n请尝试其他交易方式");
				} else if (-1441 == emvAppModule.emvGetErrorCode()){
					showToast("交易终止\r\n" + emvApp.getMsgCheckEcOnly(TransType.TRANS_SALE));
				} else {
					showToast("交易终止\r\n" + emvApp.getEmvErrMsg());
				}
			}
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV QPBOC流程执行失败");
			return FINISH;
		}
		
	}

	@AnnStep(stepIndex = Step.DO_QUERY)
	public int step_do_query() {
		commonBean.setTitle(pubBean.getTransName());
		commonBean.setValue(pubBean);
		doQuery(commonBean);
		LoggerUtils.d("hjh   "+ commonBean.getResult() +"   "+commonBean.getStepResult());
		if (!commonBean.getResult()){
			switch(commonBean.getStepResult()){
				case SUCCESS:
					transResultBean.setIsSucess(false);
					transResultBean.setContent("优惠查询失败");
					return Step.TRANS_RESULT;
				default:
					break;
			}
			return FINISH;
		}
		return Step.INPUT_PIN;
	}

	/** 输入密码*/
	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin(){

		//闪付凭密的情况，强制要求输入密码
		if(pubBean.getForcePassword()){
			LoggerUtils.e("qps force psw");
			boolean result = stepProvider.inputPin(pubBean);
			if(!result){
				Device.lightTurnOff(1,1,1,0);
				Device.lightTurnOn(0,0,0,1);
				return FINISH;
			}
		}else{
			if (emvApp.checkIsQpbocOnlinePin()){
				//小额免密的情况
				if(quickPayNoPsw(pubBean,emvAppModule)){
					isNoPsw = true;
					return Step.PRE_ONLINE_PROCESS;	
				}
				
				boolean result = stepProvider.inputPin(pubBean);
				if(!result){
					Device.lightTurnOff(1,1,1,0);
					Device.lightTurnOn(0,0,0,1);
					return FINISH;
				}
			}
		}
		return Step.PRE_ONLINE_PROCESS;
	}
	
	/** 脚本上送、冲正 */
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {

		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			Device.lightTurnOff(1,1,1,0);
			Device.lightTurnOn(0,0,0,1);
			return FINISH;
		}

		return Step.PACK_AND_COMM;
	}




	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
			
		/** 数据组织到pubBean中 */
//		initPubBean();
	
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
	
		pubBean.setIsoField55(emvAppModule.packField55(false));
		arqcTLV = emvAppModule.getArqc();
		
		
		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(2, pubBean.getPan());
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
		iso8583.setField(55, pubBean.getIsoField55());
		iso8583.setField(60, pubBean.getIsoField60().getString());
		iso8583.setField(64, "00");

		//保存冲正55域
		pubBean.setIsoField55(emvAppModule.packReversalField55(true));
		int resCode = dealPackAndComm(true, true, true);
	
		if(resCode != STEP_CONTINUE){
			try{
				Device.lightTurnOff(1,1,1,0);
				Device.lightTurnOn(0,0,0,1);
				Thread.sleep(750); //交易失败，红灯常亮，此住延迟，只是为了看到红灯 - -
			}catch(Exception e){
				e.printStackTrace();
			}
			if (resCode == STEP_FINISH) {
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		if (qPbocPriority != com.newland.pos.sdk.common.TransConst.QpbocPriority.EMV){
			byte[] balance = emvAppModule.getEmvData(0x9F5D);
			if(balance != null){
				pubBean.setEcBalance(Long.parseLong(BytesUtils.bcdToString(balance)));
			}
		}
		
		pubBean.setEmv_Status(EmvStatus.EMV_STATUS_ONLINE_SUCC);
		Device.lightTurnOn(0,0,1,0);
		return Step.APPEND_WATER;
	}	
	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		// 对业务处理结果进行判断
		try{
			Water water = getWater(pubBean);
			//电子签名,取54域余额
			String balance = iso8583.getField(54);
			if (!StringUtils.isNullOrEmpty(balance)){
				if (balance.length() >= 20){
					water.setBalance(Long.parseLong(balance.substring(8, 20)));
				}
			}
			//设置8A
			emvAppModule.setEmvData(0x8A, new byte[]{0x00,0x00});
			
			emvAppModule.setEmvAdditionInfo(water, arqcTLV);
			emvAppModule.setEmvWaterInfo(water);
	LoggerUtils.d("dd qPboc联机流水,emvSatus:" + water.getEmvStatus()+ " transAttr:" + water.getTransAttr());	
	
			if(CheckIsNoSign(pubBean)){
				water.setNoSignFlag(true);
			}else{
				water.setNoSignFlag(false);
			}
			water.setNoPswFlag(isNoPsw);
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
			Device.lightTurnOff(1,1,1,0);
			Device.lightTurnOn(0,0,0,1);
			pubBean.setResponseCode("EC");
			pubBean.setMessage("保存流水失败");
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
			if (pubBean.getTransType() == TransType.TRANS_SALE){
				doElecSign(transResultBean.getWater());
			}
		}	
		try{
			transResultBean.setTitle(pubBean.getTransName());
			transResultBean = showUITransResult(transResultBean);
		}catch(Exception e){
			e.printStackTrace();
		}

		Device.lightTurnOff(1,1,1,1);
		if(transResultBean.getIsSucess()){
			if (pubBean.getTransType() == TransType.TRANS_SALE){
				//电子签名上送,离线上送
				dealSendAfterTrade();
			}
			
			if(commonBean != null){
				commonBean.setResult(true);
			}
			
			//TODO 交易结果集上送 ， 上送失败则凭 pos签购单人工处理
//			if (doSendInfo(pubBean) == false) {
//				//showToast("交易上送失败则凭 pos签购单人工处理");
//			} 
			return SUCC;
		}
		return FINISH;
	}
	
	private void saveFailWater(){
		pubBean.setPan(emvApp.getPanFrom5A());
		
		String value = emvAppModule.getEmvDataStr(EmvTag.TAG_5F34_IC_PANSN);
		if (!StringUtils.isEmpty(value)) {
			pubBean.setCardSerialNo("0" + value);
		} else{
			//取不到卡片序列号时，默认填000,lld,2014-2-17,szBctc
			pubBean.setCardSerialNo("000");
		}
		value = emvAppModule.getEmvDataStr(EmvTag.TAG_5F24_IC_APPEXPIREDATE);
		if (!StringUtils.isEmpty(value)) {
			LoggerUtils.d("111 ExpDate:" + value);
			pubBean.setExpDate(value.substring(0, 4));
		}
		
		pubBean.setInternationOrg(emvApp.getEmvOrgSheet());
		//设置8A
		emvAppModule.setEmvData(0x8A, new byte[]{0x5A,0x31});
		//增加流水号
		addTraceNo();
		emvAppModule.SaveEmvFailWater(activity, pubBean);
		
	}
	private void getCardInfoAddtion(){
		EmvAppModule emvAppModule = EmvAppModule.getInstance();
		EmvCoreOperator emvOperator = new EmvCoreOperator(emvAppModule);
		byte[] inbuf = new byte[100];
		byte[] outbuf = new byte[600];
		String data;
		String pid = "";// 证件号码
		String sw = "";
		String temp = "";
		int ret = 0;

		byte[] res = new byte[256];
		int[] pnLen = new int [1];
		ret = PublicLibJNIService.jnicardpowerup(res, pnLen);
		LoggerUtils.d("加油卡上电 :ret :"+ret);
		LoggerUtils.d("outbuf.length0 :"+outbuf.length);
		if (ret != 0) {
			PublicLibJNIService.jnicardpowerdown();
			return ;
		}
//		System.arraycopy(new byte[]{0x00,(byte)0xA4,0x00,0x00,0x02,(byte)0x06,(byte)0x01,(byte)0xFF}, 0, inbuf, 0, 8);
		System.arraycopy(new byte[]{0x00,(byte)0xA4,0x04,0x00,0x08,(byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x33,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x00}, 0, inbuf, 0, 14);
		ret = emvOperator.emv_icc_rw(0, inbuf, inbuf.length, outbuf, outbuf.length);
		if(ret <=0) {
			LoggerUtils.d("加油卡选择目录失败 :ret :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			return ;
		}
		HashMap<String, String> field55 = getTlvMap(pubBean.getIsoField55());
		String input = "80A800002383213600008000000000000100000000000001560000000000015617052400"+field55.get("9F37");
		inbuf = BytesUtils.hexToBytes(input);
		ret = emvOperator.emv_icc_rw(0, inbuf, inbuf.length, outbuf, outbuf.length);
		if(ret <= 2) {
			LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret0 :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			return ;
		}

		data = BytesUtils.bytesToHex(outbuf,2,ret-2);
		LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :data :"+data);
		sw = data.substring(data.length()-4);
		if (!sw.equals("9000")) {
			LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret1 :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			return ;
		}
		data = data.substring(0, data.length()-4);
		temp += data;
		LoggerUtils.d("data  ret  :"+temp);
		PublicLibJNIService.jnicardpowerdown();
		HashMap<String, String> cardMSG = getTlvMap(temp);
		pubBean.setBack_name(cardMSG.get("5F20"));
//		pubBean.setCardInfo(cardMSG);
		return ;
	}
}
