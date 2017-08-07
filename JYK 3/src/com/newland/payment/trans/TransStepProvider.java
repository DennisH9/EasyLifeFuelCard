package com.newland.payment.trans;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.SoundUtils;
import com.newland.base.util.TransUtils;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.Const.SoundType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.trans.bean.NewRefundBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.bean.PasswordBean;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.common.EmvResult;
import com.newland.pos.sdk.common.EmvTransType;
import com.newland.pos.sdk.common.TransConst;
import com.newland.pos.sdk.common.TransConst.PinInputMode;
import com.newland.pos.sdk.emv.EmvCoreOperator.ECSelectMode;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 通用交易步骤提供者
 * @version 1.0
 * @author spy
 * @date 2015年5月30日
 * @time 上午10:29:58
 */
public class TransStepProvider {
	private AbstractBaseTrans trans = null;

	public TransStepProvider(AbstractBaseTrans trans) {
		this.trans = trans;
	}
	
	/**
	 * 同步cardbean信息到pubBean中
	 * @param pubBean
	 * @param cardBean
	 */
	public void syncCardBean(final PubBean pubBean, final CardBean cardBean){
		pubBean.setCardInputMode(cardBean.getTrueInput());
		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
			
			pubBean.setTransAttr(TransConst.TransAttr.ATTR_MAG);
			pubBean.setPan(cardBean.getPan());
			pubBean.setInputMode("02");
			
			LoggerUtils.d("999 二磁数据[" + cardBean.getTk2() + "]");
			pubBean.setTrackData2(trans.dealTrack(pubBean.getCardInputMode(),cardBean.getTk2()));
			LoggerUtils.d("999 三磁数据[" + cardBean.getTk3() + "]");
			pubBean.setTrackData3(trans.dealTrack(pubBean.getCardInputMode(), cardBean.getTk3()));
			/**<重要,对于FALLBACK 行为，现在MASTER有明确定义,要判断2磁的servicecode*/
			if (!cardBean.getIsIcCard()){
				pubBean.setFallBack(false);
			}
			break;
		case ReadcardType.ICCARD: // 接触式读卡
			pubBean.setTransAttr(TransConst.TransAttr.ATTR_EMV_STANDARD);
			pubBean.setEcSelectMode(ECSelectMode.PATH_EMV);
			break;
		case ReadcardType.RFCARD: // 非接式读卡
			pubBean.setTransAttr(TransConst.TransAttr.ATTR_EMV_STANDARD_RF);
			pubBean.setEcSelectMode(ECSelectMode.PATH_EMV);
			break;
		case ReadcardType.HAND_INPUT:
			pubBean.setTransAttr(TransConst.TransAttr.ATTR_INPUT_PAN);
			pubBean.setInputMode("01");
			pubBean.setPan(cardBean.getPan());
			break;
		}
	}
	
	/**
	 * 刷卡界面
	 * @param pubBean
	 * @param InputMode
	 * @return
	 */
	public boolean swipCard(PubBean pubBean, int InputMode) {
		
		CardBean cardBean = new CardBean();
		if (pubBean.isFallBack()) {
			// fallback 情况
			cardBean.setContent(trans.getText(R.string.card_read_fail_fallback));
			cardBean.setInputMode(ReadcardType.SWIPE);
			cardBean.setIsCheckIcc(false);
//			pubBean.setFallBack(false); //需要传出去设置60.5域,lld,2015-10-16 
		} else {
			// 正常情况
			if ((InputMode & ReadcardType.SWIPE_OUT) != 0) {
				cardBean.setContent(trans.getText(R.string.card_swipe_out_card));
				InputMode &= ~ReadcardType.SWIPE_OUT;
			}
			if ((InputMode & ReadcardType.SWIPE_IN) != 0) {
				cardBean.setContent(trans.getText(R.string.card_swipe_in_card));
				InputMode &= ~ReadcardType.SWIPE_IN;
			}
			if ((InputMode & ReadcardType.ALLOW_IC_SWIPE) != 0) {
				cardBean.setIsCheckIcc(false);
				InputMode &= ~ReadcardType.ALLOW_IC_SWIPE;
			} else {
				cardBean.setIsCheckIcc(true);
			}
			LoggerUtils.d("111 IsCheckIcc:" + cardBean.getIsCheckIcc());
			cardBean.setInputMode(trans.dealCardInputMode(InputMode));
			
		}

		if (pubBean.getTransType() == TransType.TRANS_SALE
				|| pubBean.getTransType() == TransType.TRANS_EC_PURCHASE) {
			cardBean.setTranstype((byte)EmvConst.EMV_TRANS_RF_SALE);
			if(!pubBean.getForcePassword() 
					&& ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW)){
				cardBean.setIsSupportSeekRFCardSale(0);
				LoggerUtils.d("not do on k21");
			}else{
				cardBean.setIsSupportSeekRFCardSale(1);
			}
		} else {
			cardBean.setTranstype((byte)0);
			cardBean.setIsSupportSeekRFCardSale(0);
		}
		cardBean.setAmountContent(pubBean.getAmount());
		cardBean.setTitle(pubBean.getTransName());
//		cardBean = trans.showReadCard(cardBean);

		if (pubBean.getTransType() == TransType.TRANS_REFUND) {
			NewRefundBean refundBean = new NewRefundBean();
			refundBean.setCardBean(cardBean);
			if(pubBean.getAmount()!=null){
				refundBean.setOldAmount(pubBean.getAmount());
			}
			if(pubBean.getOldRefnum()!=null) {
				refundBean.setOldRefNo(pubBean.getOldRefnum());
			}
			if(pubBean.getOldDate()!=null) {
				refundBean.setOldDate(pubBean.getOldDate());
			}
			trans.showReadForRefund(refundBean);
			pubBean.setAmount(refundBean.getOldAmount());
			pubBean.setCurrency(refundBean.getCurrency());
			pubBean.setOldRefnum(refundBean.getOldRefNo());
			pubBean.setOldDate(refundBean.getOldDate());
			cardBean = refundBean.getCardBean();
		} else {
			cardBean = trans.showReadCard(cardBean, pubBean);
		}

		switch (cardBean.getStepResult()) {
		case SUCCESS:
			try {
				syncCardBean(pubBean, cardBean);
				return true;
			} catch (Exception e) {
				trans.showToast("磁道数据异常，请重试！");
				e.printStackTrace();
				pubBean.setResponseCode("EC");
				pubBean.setMessage("读取卡片失败");
				return false;
			}
		case BACK:
			pubBean.setResponseCode("CC");
			pubBean.setMessage("用户取消交易");
			break;
		case FAIL:
		case TIME_OUT:
		default:
			pubBean.setResponseCode("EC");
			pubBean.setMessage("读取卡片失败");
		}
		return false;
	}
	
	
	/**
	 * 输入金额
	 * @param pubBean
	 * @return
	 */
	public boolean inputAmount(PubBean pubBean){
		return inputAmount(pubBean, false, false);
	}
	
	/**
	 * 输入金额
	 * @param pubBean
	 * @param isConfirmAmount 是否确认金额
	 * @param isCheckRefund 是否检验退货金额
	 * @return
	 */
	public boolean inputAmount(PubBean pubBean, boolean isConfirmAmount, boolean isCheckRefund){
		//外部已经输入金额直接跳过输入金额界面,用于第三方调用 
		//if (pubBean.getAmount() != null && pubBean.getAmount() > 0){
			//TODO 最大金额限制检查
		//	return true;
		//}
		
		AmountBean amountBean = new AmountBean();
		amountBean.setTitle(pubBean.getTransName());
		amountBean.setContent(trans.getText(R.string.expense_collection_input_money));
		amountBean.setConfirmAmount(isConfirmAmount);
		amountBean.setCheckRefundAmount(isCheckRefund);
		
		amountBean = trans.showUIInputAmount(amountBean);

		switch(amountBean.getStepResult()) {		
		case SUCCESS:
			pubBean.setAmount(amountBean.getAmount());
			pubBean.setCurrency(amountBean.getCurrency());
			break;
		case BACK:
		case FAIL:
		case TIME_OUT:
		default:
			return false;
		}
		return true;
	}
	/**
	 * Emv初始化
	 * @param pubBean
	 * @param emvApp
	 * @return
	 */
	public boolean emvInit(PubBean pubBean, EmvApplication emvApp){
		int emvResult = 0;
		try{
			emvResult = emvApp.initEmvApp(pubBean);
		}catch(Exception e){
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV初始化失败");
			e.printStackTrace();
			
		}
		
		LoggerUtils.d("111 initEmvApp result:" + emvResult);
		if(emvResult != 0){
			if (emvResult == EmvResult.EMV_FALLBACK) {
				pubBean.setFallBack(true);
				return false;
			} 
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV初始化失败");
			return false;
		}
		
		MessageTipBean tipBean = new MessageTipBean();
		
		tipBean.setTitle(trans.getText(R.string.common_cardno_comfirm));
		tipBean.setContent(pubBean.getPan());
		tipBean.setCancelable(true);
		//设置IC显示卡号超时
		tipBean.setTimeOut(30);
		trans.showUIMessageTip(tipBean);
		
		switch(tipBean.getStepResult()){
		case SUCCESS:
			if(!tipBean.getResult()){
				pubBean.setResponseCode("EC");
				pubBean.setMessage("EMV初始化失败");
				return false;
			}
			break;
		case TIME_OUT://超时
		default:
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV初始化失败");
			return false;
		}
		return true;
	}
	
	/**
	 * Emv简化流程
	 * @param pubBean
	 * @param emvApp
	 * @return
	 */
	public boolean emvSimpleProcess(PubBean pubBean, EmvApplication emvApp){
		int emvResult = 0;
		LoggerUtils.d("111 pubBean:" + pubBean);
		LoggerUtils.d("111 emvApp:" + emvApp);
		
		// 加入判断参数是否下载
		if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_AID_DOWN)) {
			trans.showToast("请下载AID参数");
			pubBean.setResponseCode("EC");
			pubBean.setMessage("AID参数未下载");
			return false;
		}
		pubBean.getEmvBean().setTransMode(pubBean.getTransType());
		pubBean.getEmvBean().setL2_9CTransType(BytesUtils.hexStringToBytes(trans.getProcessCode(pubBean.getTransType()))[0]);
		try{
			if (pubBean.getCardInputMode() == ReadcardType.ICCARD) {
				//接触走PBOC简化流程
				pubBean.setTransAttr(TransConst.TransAttr.ATTR_EMV_PREDIGEST);
				pubBean.getEmvBean().setEnableOnlyEc(false);
				emvResult = emvApp.simpleProcess(pubBean);
				
			} else {
				//非接走qPboc联机借贷记流程
				pubBean.setTransAttr(TransConst.TransAttr.ATTR_EMV_PREDIGEST_RF);
				pubBean.setqPbocPriority(TransConst.QpbocPriority.EMV);
				pubBean.getEmvBean().setEnableOnlyEc(false);
				emvResult = emvApp.qPbocProcess(pubBean);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV简易流程执行失败");
		}
		LoggerUtils.d("111 initEmvApp result:" + emvResult);
		if(emvResult != 0){
			if (emvResult == EmvResult.EMV_FALLBACK) {
				pubBean.setFallBack(true);
				return false;
			} 
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV简易流程执行失败");
			return false;
		}
		
		//提示移卡,主要非接需要
		SoundUtils.getInstance().play(SoundType.BEEP); // 喇叭响
		
		MessageTipBean tipBean = new MessageTipBean();
		
		tipBean.setTitle(trans.getText(R.string.common_cardno_comfirm));
		tipBean.setContent(pubBean.getPan());
		tipBean.setCancelable(true);
		
		trans.showUIMessageTip(tipBean);
		
		switch(tipBean.getStepResult()){
		case SUCCESS:
			if(!tipBean.getResult()){
				pubBean.setResponseCode("CC");
				pubBean.setMessage("用户取消交易");
				return false;
			}
			break;
		default:
			pubBean.setResponseCode("EC");
			pubBean.setMessage("确认卡号失败");
			return false;
		}
		return true;
	}
		
	/**
	 * 输入PIN,带输入模式
	 * @param pubBean
	 * @param inputMode
	 * @return
	 */
	public boolean inputPin(PubBean pubBean, PinInputMode inputMode){

		PasswordBean passwordBean = new PasswordBean();
		passwordBean.setPinMaxLen(6);
		passwordBean.setTitle(pubBean.getTransName());
		passwordBean.setPinInputMode(inputMode);
		passwordBean.setPan(pubBean.getPan());
		String content = trans.getText(R.string.expense_collection_input_password);
		switch (pubBean.getTransType()) {
		case EmvTransType.TRANS_SALE:
			content = "请输入支付密码";
			break;
			
		case EmvTransType.TRANS_VOID_SALE:
			content = "请输入卡号密码";
			break;

		default:
			break;
		}
		passwordBean.setContent(content);
		
		if ((pubBean.getAmount() != null && pubBean.getAmount() != 0)) {
			passwordBean.setAmountContent(pubBean.getAmount());
		}
		
		passwordBean = trans.showUIPinInput(passwordBean);

		switch (passwordBean.getStepResult()) {
		case SUCCESS:
			pubBean.setPinBlock(passwordBean.getPin());
			break;
		case BACK:
			pubBean.setResponseCode("CC");
			pubBean.setMessage("用户取消交易");
			return false;
		case FAIL:
		case TIME_OUT:
		default:
			pubBean.setResponseCode("EC");
			pubBean.setMessage("输入密码失败");
			return false;
		}
		return true;
	}
	/**
	 * 输入PIN
	 * @param pubBean
	 * @return
	 */
	public boolean inputPin(PubBean pubBean){
		return inputPin(pubBean, PinInputMode.PINTYPE_WITH_PAN);
	}
	/**
	 * 联机前处理
	 * @param pubBean
	 * @return
	 */
	public boolean preOnlineProcess(PubBean pubBean) {
		
		// 脚本执行结果上送
		if(!trans.doScriptAdvise()){
			// 脚本执行结果上送返回false，平台连接失败，交易返回
			pubBean.setResponseCode("EC");
			pubBean.setMessage("联机预处理失败");
			return false;
		}

		// 执行冲正
		if (!trans.doReverseWater()){
			// 冲正返回false，平台连接失败，交易返回
			LoggerUtils.e("冲正失败，界面返回主菜单");
			pubBean.setResponseCode("EC");
			pubBean.setMessage("联机预处理失败");
			return false;
		}
		
		LoggerUtils.d("111 pinBlock[" + pubBean.getPinBlock() + "]");
		if ( StringUtils.isEmpty(pubBean.getPinBlock()) || "0000000000000000".equals(pubBean.getPinBlock()) ){
			// 输空的pin
			pubBean.setInputMode(pubBean.getInputMode()+"2");
		} else {
			pubBean.setInputMode(pubBean.getInputMode()+"1");
		}
		pubBean.setSecctrlInfo(trans.getSecctrlInfo(pubBean));
		
		//IC卡磁道加密处理,磁条卡磁道加密在K21完成
		pubBean.setTrackData2(trans.dealTrack(pubBean.getCardInputMode(), pubBean.getTrackData2()));
		return true;
	}
	
	/**
	 *  输入卡有效期
	 *  param:emptyFlag
	 *  true-允许为空，false-不允许为空
	 */
	public boolean inputValidDate(PubBean pubBean,boolean emptyFlag){
		CommonBean<String> dateBean = new CommonBean<String>();
		dateBean.setTitle(pubBean.getTransName());
		dateBean.setContent(trans.getText(R.string.choose_date_choose_indate));
		dateBean.setEmptyFlag(emptyFlag);
		dateBean = trans.showUIYearDateChoose(dateBean);
		if(!dateBean.getResult()){
			pubBean.setResponseCode("EC");
			pubBean.setMessage("选择卡片有效期失败");
			return false;
		}
		pubBean.setExpDate(dateBean.getValue());
		return true;
	}
	/** 
	 * 输入原凭证号 
	 * @param oldTransType 原交易类型
	 * @return oldWater 若为空，交易结束，否则，交易下一步
	 */
	public Water inputOldTraceNo(PubBean pubBean, int oldTransType){
		// 1
		InputInfoBean infoBean = new InputInfoBean();
		if(oldTransType == TransType.TRANS_REPRINT){
			pubBean.setTransName(TransUtils.getTransType(oldTransType)[0]);
		}
		if(!StringUtils.isNullOrEmpty(pubBean.getTraceNo())){
			infoBean.setResult(pubBean.getTraceNo());
		}else{
			infoBean.setTitle(pubBean.getTransName());
			infoBean.setContent(trans.getText(R.string.common_pls_input)
					+ trans.getText(R.string.common_old_trace_no));
			infoBean.setShortContent(trans.getText(R.string.common_old_trace_no));
			infoBean.setMode(InputInfoBean.INPUT_MODE_NUMBER);
			infoBean.setMaxLen(6);
			infoBean.setMinLen(1);
			infoBean.setEmptyFlag(false);
			infoBean = trans.showUIInputInfo(infoBean);
			switch (infoBean.getStepResult()) {
			case SUCCESS:
				break;
	
			case BACK:
				pubBean.setResponseCode("EC");
				pubBean.setMessage("交易取消");
			case TIME_OUT:
				pubBean.setResponseCode("EC");
				pubBean.setMessage("交易超时");
			case FAIL:
				pubBean.setResponseCode("EC");
				pubBean.setMessage("交易失败");
			default:
				return null;
			}
		}
		
		// 2
		boolean flag = true;
		
		MessageTipBean tipBean = new MessageTipBean();
		String traceNo = StringUtils.paddingString(infoBean.getResult(), 6, "0", 0);
		WaterService waterService = new WaterServiceImpl(MainActivity.getInstance());
		Water oldWater = waterService.findByTrace(traceNo);
		//重打印任意一笔调用时传入
		if(oldTransType == TransType.TRANS_REPRINT && oldWater != null){
			oldTransType = oldWater.getTransType();
		}
		if (oldWater == null) {
			flag = false;
			tipBean.setContent(trans.getText(R.string.tip_old_trace_no_not_exsits));
			pubBean.setResponseCode("EC");
			pubBean.setMessage(trans.getText(R.string.tip_old_trace_no_not_exsits));
			
		}else if (oldWater.getTransType() != oldTransType){
			flag = false;
			//原交易不符合条件 
			tipBean.setContent(trans.getText(R.string.tip_old_trace_no_not_valid));
			pubBean.setResponseCode("EC");
			pubBean.setMessage(trans.getText(R.string.tip_old_trace_no_not_valid));
			
		}else if (oldWater.getTransStatus() != TransStatus.NORMAL) {
			flag = false;
			if (oldWater.getTransStatus() == TransStatus.ADJUST 
				|| oldWater.getTransStatus() == TransStatus.SEND_AND_ADJ){
				//原交易已经被调整
				tipBean.setContent(trans.getText(R.string.tip_old_trans_adjust));
				pubBean.setResponseCode("EC");
				pubBean.setMessage(trans.getText(R.string.tip_old_trans_adjust));
			} else {
				//原交易已经被撤销
				tipBean.setContent(trans.getText(R.string.tip_old_trans_void));
				pubBean.setResponseCode("EC");
				pubBean.setMessage(trans.getText(R.string.tip_old_trans_void));
			} 	
		} 
		if (flag){
			CommonBean<Water> bean = new CommonBean<Water>();
			bean.setValue(oldWater);
			bean.setTitle(pubBean.getTransName());
			bean.setTimeOut(60);
			bean = trans.showUIConfirmInfo(bean);

			switch (bean.getStepResult()) {
			case SUCCESS:
				// 设置原交易信息到PubBean对象
				pubBean.setAmount(oldWater.getAmount());
				pubBean.setCurrency(oldWater.getCurrency());
				pubBean.setOldRefnum(oldWater.getReferNum());
				pubBean.setPan(oldWater.getPan());
				pubBean.setOldTrace(traceNo);
				if (!StringUtils.isEmpty(oldWater.getAuthCode())) {
					/**预授权完成撤销，38域同原交易请求，而不是原交易响应*/
					if (pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE){
//						|| pubBean.getTransType() == TransType.TRANS_VOID_ORDER_AUTHSALE){
						pubBean.setOldAuthCode(oldWater.getOldAuthCode());
					} else {
						pubBean.setOldAuthCode(oldWater.getAuthCode());
					}
				}
				if (!StringUtils.isEmpty(oldWater.getTelNo())) { 
					pubBean.setMobileNo(oldWater.getTelNo());
				}
				String isoField61 = oldWater.getBatchNum() + oldWater.getTrace();
				if (pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE){
//					|| pubBean.getTransType() == TransType.TRANS_VOID_ORDER_AUTHSALE){
					isoField61 += oldWater.getDate();
				} 
				pubBean.setIsoField61(isoField61);
				return oldWater;
			case BACK:
			case TIME_OUT:
			case FAIL:
			default:
				return null;
			}
			
		}else {
			
			tipBean.setTitle(pubBean.getTransName());
			tipBean.setCancelable(false);
			tipBean.setTimeOut(60);
			trans.showUIMessageTip(tipBean);
			return null;
		}
		
	}
	
	/**
	 * 输入原授权码
	 */
	public boolean inputOldAuthCode(PubBean pubBean){
		
		if(pubBean.getOldAuthCode() != null){
			return true;
		}
		
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(trans.getText(R.string.common_pls_input) + trans.getText(R.string.common_auth_code));
		infoBean.setShortContent(trans.getText(R.string.common_auth_code));
		infoBean.setMode(InputInfoBean.INPUT_MODE_ASCII);
		infoBean.setMaxLen(6);
		infoBean.setMinLen(1);
		infoBean.setTimeOut(60);
		infoBean.setEmptyFlag(false);
		
		infoBean = trans.showUIInputInfo(infoBean);
		switch(infoBean.getStepResult()){
			case SUCCESS:
				// 授权码位数不足前补空格
				pubBean.setOldAuthCode(StringUtils.fill(infoBean.getResult(), " ", 6, false));
				return true;
			case BACK:
				pubBean.setResponseCode("CC");
				pubBean.setMessage("用户取消");
				return false;
			case TIME_OUT:
			case FAIL:
			default:
				pubBean.setResponseCode("EC");
				pubBean.setMessage("获取原授权码失败");
				return false;
		}	
	}
	
	/**
	 * 输入原交易日期
	 */
	public boolean inputOldTransDate(PubBean pubBean,boolean emptyFlag){
		
		if(pubBean.getOldDate() == null){
			CommonBean<String> dateBean = new CommonBean<String>();
				dateBean.setTitle(pubBean.getTransName());
				dateBean.setContent(trans.getText(R.string.choose_date_choose_transaction));
				dateBean.setEmptyFlag(emptyFlag);
				dateBean = trans.showUIDateChoose(dateBean);

				if(!dateBean.getResult()){
					pubBean.setResponseCode("EC");
					pubBean.setMessage("获取日期失败");
					return false;
			}
			pubBean.setOldDate(dateBean.getValue());
		}
		//原始信息域
		pubBean.setIsoField61("000000" + "000000" + pubBean.getOldDate());
		return true;
	}
}
