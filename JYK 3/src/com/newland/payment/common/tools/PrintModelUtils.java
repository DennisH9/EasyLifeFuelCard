package com.newland.payment.common.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.TransUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.jni.type.capk;
import com.newland.emv.jni.type.emvparam;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.PrintType.PrintWaterType;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.SendStatus;
import com.newland.payment.common.TransConst.SettlementTableTypeConst;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.SettlementService;
import com.newland.payment.mvc.service.impl.SettlementServiceImpl;
import com.newland.payment.printer.template.model.PrintAllWaterModel;
import com.newland.payment.printer.template.model.PrintInfoModel;
import com.newland.payment.printer.template.model.PrintSettleModel;
import com.newland.payment.printer.template.model.PrintSysParamModel;
import com.newland.payment.printer.template.model.PrintTotalModel;
import com.newland.payment.printer.template.model.PrintVourcherModel;
import com.newland.payment.trans.bean.field.Field48_Settle;
import com.newland.payment.trans.bean.field.Field_55_PrintData;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.common.TransConst;
import com.newland.pos.sdk.common.TransConst.TransAttr;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 获取打印需要的model数据
 * 
 * @author lst
 * 
 */
public class PrintModelUtils {
	
	/**
	 * 流水对象转为签购单model
	 * @param water
	 * @param type
	 * @param isReprint
	 * @return
	 */
	public static PrintVourcherModel waterToPrinterModel(Water water,
			PrintWaterType type, boolean isReprint) {
		PrintVourcherModel printerModel = new PrintVourcherModel();
		switch (type) {
		case MERCHANT:
			printerModel.setMerchantFlag("1");
			break;
		case CUSTOMER:
			printerModel.setCustomerFlag("1");
			break;
		default:
			printerModel.setBankFlag("1");
			break;
		}
		if (isReprint) {
			printerModel.setReprintFlag("1");
		}
		// 抬头LOGO或中文名
		if (ParamsUtils.getInt(ParamsConst.PARAMS_KEY_BASE_PRINTTITLEMODE) == 0) {
			printerModel.setPrintTopName(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_PRINTTITLECN).trim()+"签购单");
		}
		else {				
			printerModel.setPrintTopImg(Const.PathConst.APPS_DATA+
					Const.FileConst.LOGO_IMG_LARGE);			
		}
		printerModel.setMerchantName(ParamsUtils.getString( ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME));
		printerModel.setShopId(ParamsUtils.getShopId());
		printerModel.setTerminalId(ParamsUtils.getPosId() == null? " ":ParamsUtils.getPosId());
		printerModel.setOperId(water.getOper() == null? " ":water.getOper());
		
		//收单行
		if (water.getAcqCode() != null) {
			if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_PRINT_CHINESE_ACQUIRER) && BankCodeHelper.getBankCodeCN(water.getAcqCode().substring(0,4)) != null) {
				String bankCodeCN = BankCodeHelper.getBankCodeCN(water.getAcqCode().substring(0,4));
				if(bankCodeCN.equals(water.getAcqCode().substring(0,4))){
					printerModel.setAcqCode(water.getAcqCode());
				}else{
					printerModel.setAcqCode(bankCodeCN);
				}
			}
			else {
				printerModel.setAcqCode(water.getAcqCode());
			}
		}
		//发卡行
		if (water.getIisCode() != null) {
			if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_PRINT_CHINESE_ISSUING_BANK) ) {
				if (BankCodeHelper.getBankCodeCN(water.getIisCode().substring(0,4)) != null) {
					printerModel.setIisCode(BankCodeHelper.getBankCodeCN(water.getIisCode().substring(0,4))); 
				}
				else {
					if (ParamsUtils.getString(ParamsConst.PARAMS_KEY_ISSUING_BANK_NAME)!= null && !(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ISSUING_BANK_NAME).equals(""))) {
						printerModel.setIisCode(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ISSUING_BANK_NAME));
					}
					else {
						printerModel.setIisCode(water.getIisCode());
					}
				}	
			}	
			else {
				printerModel.setIisCode(water.getIisCode());
			}
		}
		if(water.getCardType()!=null){
			printerModel.setCardType(water.getCardType().trim());
		}
		printerModel.setEntryMode(formatInputMode(water.getInputMode()) == null? " ":formatInputMode(water.getInputMode()));
		printerModel.setExpDate(water.getExpDate());
		printerModel.setCardSerialNo(water.getCardSerialNo());
		String[] transName = TransUtils.getTransType(water.getTransType());
		printerModel.setTransType_cn(transName[0] + "/" + transName[1]);
		printerModel.setBatchNo(water.getBatchNum() == null? " ":water.getBatchNum());
		printerModel.setTraceNo(water.getTrace() == null? " ":water.getTrace());
		printerModel.setRefNum(water.getReferNum() == null? " ":water.getReferNum());
		printerModel.setAuthCode(water.getAuthCode());
		printerModel.setDateTime(FormatUtils.timeFormat(water.getDate(),water.getTime()));
		printerModel.setInterOrg(water.getInterOrg());
		printerModel.setAmount(FormatUtils.formatMount(String
				.valueOf(water.getAmount())));
		if(water.getOrderAmount()!= -1){
			LoggerUtils.d("hjh  water.getOrderAmount()"+water.getOrderAmount());
			printerModel.setOrderAmount(FormatUtils.formatMount(String
					.valueOf(water.getOrderAmount())));
		}
		if(water.getDiscountAmount()!= -1){
			LoggerUtils.d("hjh  water.getDiscountAmount()"+water.getDiscountAmount());
			printerModel.setDiscountAmount(FormatUtils.formatMount(String
					.valueOf(water.getDiscountAmount())));
		}
		// 还款币种
			if (water.getCurrency() != null) {
				printerModel.setCurrency(FormatUtils.getCurrencySymbol(water.getCurrency()));
			}
	
		if (water.getEcBalance() != null) {
			 printerModel.setBalance(FormatUtils.formatAmount(water.getEcBalance().toString()));
		}
		
		//设置卡号
		printerModel.setCardNo(TransUtils.getPanByWater(water));

		if (water.getTelNo() != null) {
			printerModel.setTelNo(FormatUtils.formatPhoneNumberWithStar(water.getTelNo()));
		}
		/** 备注 */
		switch (water.getTransType()) {		
		case TransType.TRANS_REFUND:
			//原参考号
			printerModel.setOldRefNum(water.getOldRefNum());
			//原交易日期
			printerModel.setOldDate(water.getOldDate());
			break;
		case TransType.TRANS_EMV_REFUND:
			//原批次
			if (water.getOldBatch() != null && !(water.getOldBatch().equals("000000"))) {
				printerModel.setOldBatchNo(water.getOldBatch());
			}
			//原凭证
			if (water.getOldTrace() != null && !(water.getOldTrace().equals("000000"))) {
				printerModel.setOldTraceNo(water.getOldTrace());
			}
			//原交易日期
			printerModel.setOldDate(water.getOldDate());
			break;
		case TransType.TRANS_AUTHSALE:
		case TransType.TRANS_AUTHSALEOFF:
		case TransType.TRANS_VOID_PREAUTH:
			//原预授权码
			if (water.getOldAuthCode() != null && !(water.getOldAuthCode().equals("000000"))) {
				printerModel.setOldAuthCode(water.getOldAuthCode());
			}
		break;
		case TransType.TRANS_VOID_SALE:
		case TransType.TRANS_VOID_AUTHSALE:
		case TransType.TRANS_EC_VOID_LOAD_CASH:
		
			//原凭证
			if (water.getOldTrace() != null && !(water.getOldTrace().equals("000000"))) {
				printerModel.setOldTraceNo(water.getOldTrace());
			}
			
			//原预授权码
			if (water.getTransType() == TransType.TRANS_VOID_AUTHSALE) {
				if (water.getOldAuthCode() != null && !(water.getOldAuthCode().equals("000000"))) {
					printerModel.setOldAuthCode(water.getOldAuthCode());
				}			
			}
			break;
		default:		
			break;
		} 
		// 转入卡号
		if (water.getCardNoTransIn() != null) {
			printerModel.setCardNoTransIn(water.getCardNoTransIn());
		}
		try{
		LoggerUtils.d("111 发卡行信息[" + water.getIisInfo() + "]");
		printerModel.setIisInfo(water.getIisInfo());
		LoggerUtils.d("111 银联信息[" + water.getCupInfo() + "]");
		printerModel.setCupInfo(water.getCupInfo());
		LoggerUtils.d("111 收单行信息[" + water.getMerAcqInfo() + "]");
		printerModel.setAcqInfo(water.getMerAcqInfo());
		LoggerUtils.d("111 POS终端保留63.2.4[" + water.getTermInfo() + "]");
		printerModel.setAdsInfo(water.getTermInfo());
		} catch (Exception e){
			e.printStackTrace();
		}
		printerModel.setHotLine(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_HOTLINE));
		// IC卡交易emv数据
		if (water.getTransAttr() == TransAttr.ATTR_EMV_STANDARD
				|| water.getTransAttr() == TransAttr.ATTR_qPBOC
				|| water.getTransAttr() == TransAttr.ATTR_EMV_STANDARD_RF) {
			if (water.getEmvAddition() != null) {
				Field_55_PrintData printData = new Field_55_PrintData(
						BytesUtils.hexToBytes(water.getEmvAddition()));
				if (water.getEmvStatus() == EmvStatus.EMV_STATUS_ONLINE_SUCC) {
					printerModel.setArqc(printData.getArqc());
				}
				if (water.getEmvStatus() == EmvStatus.EMV_STATUS_OFFLINE_SUCC) {
					printerModel.setTc(printData.getTC());
				}
				printerModel.setAid(printData.getAid());
				printerModel.setCsn(water.getCardSerialNo());
				printerModel.setCvm(printData.getCvm());
					printerModel.setTvr(printData.getTvr());
					printerModel.setTsi(printData.getTsi());
				printerModel.setTermCap(printData.getTermCap());
				printerModel.setUnpr(printData.getUnpr());
				printerModel.setAip(printData.getAip());
				printerModel.setAtc(printData.getAtc());
				printerModel.setIad(printData.getIad());
				String appName = null;
				try {
					appName = new String(printData.getAppname(),"UTF-8");
					printerModel.setAppName(appName);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				String appLab = null;
				try {
					appLab = new String(printData.getApplable(),"UTF-8");
					printerModel.setApplab(appLab);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				printerModel.setCardProductId(printData.getCardProductId());
			}
		}
		//电子现金免签名
		LoggerUtils.e("qps getTransType:" +  water.getTransType());
		if (water.getTransType() == TransType.TRANS_EC_PURCHASE) {
			printerModel.setIsCashCard("true");
		}
		// 电子签名数据,存入文件名
		long noSignLimit = Long.parseLong(ParamsUtils.getString(ParamsConst.PARAMS_KEY_QPS_NO_SINGNATURE_LIMIT));
		long noPswLimit = Long.parseLong(ParamsUtils.getString(ParamsConst.PARAMS_KEY_QPS_NO_PSW_LIMIT));
		
		if(PrintWaterType.MERCHANT == type || PrintWaterType.BANK == type) {
			
			LoggerUtils.e("qps getNoPswFlag:" +  water.getNoPswFlag());
			LoggerUtils.e("qps getNoSignFlag:" +  water.getNoSignFlag());
			if((water.getNoPswFlag() != null && water.getNoPswFlag())
					&& (water.getNoSignFlag() != null &&water.getNoSignFlag())){
				
				if(water.getAmount() <= noPswLimit){
					
					if(noSignLimit <= noPswLimit){
						printerModel.setAmountLimit(FormatUtils.formatMount(String
								.valueOf(noSignLimit)));
					}else{
						printerModel.setAmountLimit(FormatUtils.formatMount(String
								.valueOf(noPswLimit)));
					}
					printerModel.setIsNoSignAndPsw("true");
				}else{
					printerModel.setAmountLimit(FormatUtils.formatMount(String
							.valueOf(noSignLimit)));
					printerModel.setIsNoSign("true");
				}
			}else if(water.getNoSignFlag() != null &&water.getNoSignFlag()){
				printerModel.setAmountLimit(FormatUtils.formatMount(String
						.valueOf(noSignLimit)));
				printerModel.setIsNoSign("true");
			}else{
				if((water.getNoPswFlag() != null && water.getNoPswFlag())
						&& (water.getAmount() <= noPswLimit)){
					printerModel.setAmountLimit(FormatUtils.formatMount(String
							.valueOf(noPswLimit)));
					printerModel.setIsNoPsw("true");
				}
				
				printerModel.setIsEleSign("1");
				if(water.getSignatureFlag()!=null && true==water.getSignatureFlag()) {
					File file = new File(App.SIGNATURE_BMP_DIR+water.getTrace());
					if (file.exists()) {
						printerModel.setEleSignTureDate(App.SIGNATURE_BMP_DIR+water.getTrace());
					}
				}
				if(StringUtils.isEmpty(printerModel.getEleSignTureDate()) &&
						StringUtils.isEmpty(printerModel.getIsCashCard())) {
					printerModel.setIsSignature("true");
				}
			}		
		}
		return printerModel;
	}


	/**
	 * 获取打印交易控制Model
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static PrintSysParamModel getTransControlModel(Context context) {

		PrintSysParamModel model = new PrintSysParamModel();

		// 是否打印参数打印提示抬头
		model.setISPRINTTRANS("true");

		/** 消费 */
		model.setISSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_SALE).equals("1")) ? "支持" : "不支持");

		/** 消费撤销 */
		model.setISVOIDSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOID).equals("1")) ? "支持" : "不支持");

		/** 退货 */
		model.setISREFUND((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_REFUND).equals("1")) ? "支持"
				: "不支持");

		/** 余额查询 */
		model.setISBALANCE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_BALANCE).equals("1")) ? "支持"
				: "不支持");

		/** 预授权 */
		model.setISPREAUTH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_PREAUTH).equals("1")) ? "支持"
				: "不支持");

		/** 预授权撤销 */
		model.setISVOIDPREAUTH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOIDPREAUTH).equals("1")) ? "支持"
				: "不支持");

		/** 预授权完成请求 */
		model.setISAUTHSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_AUTHSALE).equals("1")) ? "支持"
				: "不支持");

		/** 预授权完成通知 */
		model.setISAUTHSALEOFF((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_AUTHSALEOFF).equals("1")) ? "支持"
				: "不支持");

		/** 预授权完成撤销 */
		model.setISVOIDAUTHSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOIDAUTHSALE).equals("1")) ? "支持"
				: "不支持");

		/** 离线结算 */
		model.setISOFFLINE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_SETTLEOFF).equals("1")) ? "支持"
				: "不支持");

		/** 结算调整 */
		model.setISREJUST((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ADJUSTOFF).equals("1")) ? "支持"
				: "不支持");

		/** 接触电子现金消费 */
		model.setISECSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_EC).equals("1")) ? "支持"
				: "不支持");

		/** 快速支付（非接） */
		model.setISECFASTSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_QPBOC).equals("1")) ? "支持"
				: "不支持");

		/** 指定账户圈存 */
		model.setISECLOAD((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ECPOINTLOAD).equals("1")) ? "支持"
				: "不支持");

		/** 非指定账户圈存 */
		model.setNOTBINDECLOAD((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ECNOPOINTLOAD).equals("1")) ? "支持"
				: "不支持");

		/** 电子现金现金充值 */
		model.setISECLOADCASH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ECMONEYLOAD).equals("1")) ? "支持"
				: "不支持");

		/** 电子现金充值撤销 */
		model.setISECVOIDLOADCASH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ECVOIDMONEYLOAD).equals("1")) ? "支持"
				: "不支持");

		/** 电子现金脱机退货 */
		model.setISECOFFREFUND((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ECREFUND).equals("1")) ? "支持"
				: "不支持");

		/** 电子钱包消费 */
		model.setISEPSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_EPSALE).equals("1")) ? "支持"
				: "不支持");

		/** 分期付款消费 */
		model.setISINSTALL((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_INSTALLSALE).equals("1")) ? "支持"
				: "不支持");

		/** 分期付款消费撤销 */
		model.setISVOIDINSTALL((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_INSTALLVOID).equals("1")) ? "支持"
				: "不支持");

		/** 联盟积分消费 */
		model.setBONUS_ALLIANCE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_UNIONBOINTSALE).equals("1")) ? "支持"
				: "不支持");

		/** 发卡行积分消费 */
		model.setBONUS_IIS_SALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_BANKBOINTSALE).equals("1")) ? "支持"
				: "不支持");

		/** 联盟积分消费撤销 */
		model.setVOIDBONUS_ALL((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOIDUNIONSALE).equals("1")) ? "支持"
				: "不支持");

		/** 发卡行消费撤销 */
		model.setVOID_BONUS_IIS((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOIDBANKSALE).equals("1")) ? "支持"
				: "不支持");

		/** 联盟积分查询 */
		model.setALLIANCE_BALANCE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_UNIONBALANCE).equals("1")) ? "支持"
				: "不支持");

		/** 联盟积分退货 */
		model.setALLIANCE_REFUND((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_UNIONREFUND).equals("1")) ? "支持"
				: "不支持");

		/** 预约消费 */
		model.setISVOIDAPPOINT((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_APPOINTMENT).equals("1")) ? "支持"
				: "不支持");

		/** 预约消费撤销 */
		model.setISORDERSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_APVOIDPOINT).equals("1")) ? "支持"
				: "不支持");

		/** 订购消费 */
		model.setISORDERSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORSALE).equals("1")) ? "支持"
				: "不支持");

		/** 订购消费撤销 */
		model.setISORDERVOIDSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORVOID).equals("1")) ? "支持"
				: "不支持");

		/** 订购退货 */
		model.setISREFUNDORDER((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORREFUND).equals("1")) ? "支持"
				: "不支持");

		/** 订购预授权 */
		model.setISORDERPREAUTH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORPREAUTH).equals("1")) ? "支持"
				: "不支持");

		/** 订购预授权撤销 */
		model.setISORDERVOIDPRE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORVOIDPREAUTH).equals("1")) ? "支持"
				: "不支持");

		/** 订购预授权完成请求 */
		model.setISORDERAUTHSALE((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORAUTHSALE).equals("1")) ? "支持"
				: "不支持");

		/** 订购预授权完成通知 */
		model.setISORDERAUTHOFF((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORAUTHSALEOFF).equals("1")) ? "支持"
				: "不支持");

		/** 订购预授权完成撤销 */
		model.setISORDERVOIDAUTH((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_ORVOIDAUTHSALE).equals("1")) ? "支持"
				: "不支持");

		/** 磁条卡现金充值 */
		model.setISSTRIPECASHLOAD((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_MSCMONEY).equals("1")) ? "支持"
				: "不支持");

		/** 磁条卡账户充值 */
		model.setISSTRIPELOAD((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_MSCACCOUNT).equals("1")) ? "支持"
				: "不支持");

		/** 结算是否自动签退 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT) != null) {
			model.setAUTOLOGOUT((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT).equals("1")) ? "是" : "否");
		}
		
		/** 结算是否打印明细 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_HINT_PRINT_DETAIL)!= null) {
			model.setPRNWATERREC((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_HINT_PRINT_DETAIL).equals("1")) ? "是"
					: "否");
		}		

		/** 离线上送笔数 */
		model.setOFFLINESENDCOUNT(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_MAX_OFFSEND_NUM));
		
		/** 离线重发次数 */
		model.setOFFLINESENDTIME(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_OFFSEND_RESEND_TIMES));
		/** 是否输入主管密码 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_ADMIN_PASSWORD)!= null) {
			model.setADMINPWD((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_ADMIN_PASSWORD).equals("1")) ? "是"
					: "否");
		}
		
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_CARD_INPUT)!= null) {
			/** 是否允许手输卡号 */
			model.setCARDINPUT((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_CARD_INPUT).equals("1")) ? "是" : "否");
		}
		
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_DEFAULT_TRANS_TYPE)!= null) {
			/** 默认刷卡交易 */
			model.setDEFAULTTRANSTYPE((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_DEFAULT_TRANS_TYPE).equals("1")) ? "消费"
					: "预授权");
		}
		

		/** 退货最大限额 */
		model.setMAXREFUNDAMT(FormatUtils.formatAmount(ParamsUtils.getString(
				 ParamsConst.PARAMS_KEY_BASE_REFUNDAMOUNT)));

		return model;

	}

	/**
	 * 获取打印系统控制Model
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static PrintSysParamModel getSysControlModel(Context context) {

		PrintSysParamModel model = new PrintSysParamModel();

		// 是否打印系统控制提示抬头
		model.setISPRINTSYSPARAM("true");

		/** 当前交易凭证号 */
		model.setTRACENO(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_TRACENO));

		/** 当前交易批次号 */
		model.setBATCHNO(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_BATCHNO));

		/** 是否打印中文收单行 */
		model.setPNTCHACQUIRER((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_PRINT_CHINESE_ACQUIRER)).equals("1") ? "是"
				: "否");

		/** 是否打印中文发卡行 */
		model.setPNTCHCARDSCHEME((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_PRINT_CHINESE_ISSUING_BANK)).equals("1") ? "是"
				: "否");

		/** 套打签购单样式 */
		model.setPNTTYPE(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_TAODAPRINTERMODE).equals("1") ? "印制"
				: "空白");

		/** 热敏打印联数 */
		model.setPRINTPAGE(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_PRTCOUNT));

		/** 签购单是否打印英文 */
		model.setTICKETWITHEN((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_PRINT_SIGN_ORDER_ENGLISH)).equals("1") ? "是"
				: "否");

		/** 冲正重发次数 */
		model.setREVERSALNUM(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES));

		/** 最大交易笔数 */
		model.setMAXTRANSCOUNT(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_MAX_TRANS_COUNT));

		/** 小费比例 */
		model.setTIPRATE(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_TIPRATE));

		/** 打印字体 */
		if (ParamsUtils.getInt(ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT) == 0) {
			model.setPNTFONTSIZE("大");
		}
		else if (ParamsUtils.getInt(ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT) == 1) {
			model.setPNTFONTSIZE("中");
		}else {
			model.setPNTFONTSIZE("小");
		}
		/** 是否打印负号 */
		model.setPRINTMINUS((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_PRINT_MINUS)).equals("1") ? "是" : "否");

		/** 是否打印所有交易 */
		model.setPNTALLTRANS((ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_PRINT_ALL_TRANS_DETAIL)).equals("1") ? "是"
				: "否");

		return model;
	}

	/**
	 * 获取打印其他Model
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static PrintSysParamModel getOtherControlModel(Context context) {

		PrintSysParamModel model = new PrintSysParamModel();

		// 是否打印刷卡输密控制提示抬头
		model.setISPRINTINPUTPIN("true");
		/** 消费撤销是否刷卡 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_TRANS_VOID_SWIPE)!= null) {
			model.setSALEVOIDSTRIP((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_TRANS_VOID_SWIPE).equals("1") ? "是" : "否"));
		}
		

		// /**分期付款撤销是否刷卡*/
		// model.setSALEVOIDSTRIP((ParamsUtils.getString(
		// ParamsConst.PARAMS_KEY_TRANS_INSTALLSALE).equals(1)?"是":"否"));

		/** 预授权完成撤销是否刷卡 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_AUTH_SALE_VOID_SWIPE) != null) {
			model.setAUTHSALEVOIDSTRIP((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_AUTH_SALE_VOID_SWIPE).equals("1") ? "是"
					: "否"));
		}
		

		/** 消费撤销是否输密 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_INPUT_TRANS_VOID)!=null) {
			model.setVOIDPIN((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_INPUT_TRANS_VOID).equals("1") ? "是"
					: "否"));
		}
		

		// /**分期付款撤销是否输密*/
		// model.setVOIDPIN((ParamsUtils.getString(
		// ParamsConst.PARAMS_KEY_TRANS_INSTALLSALE).equals(1)?"是":"否"));

		/** 预授权撤销是否输密 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_INPUT_AUTH_VOID) != null) {
			model.setPREAUTHVOIDPIN((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_INPUT_AUTH_VOID).equals("1") ? "是"
					: "否"));
		}
		/** 预授权完成撤销是否输密 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_AUTH_SALE_VOID_PIN)!= null) {
			model.setAUTHSALEVOIDPIN((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_IS_AUTH_SALE_VOID_PIN).equals("1") ? "是"
					: "否"));
		}
		/** 授权完成联机是否输入密码 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_ANEW_PRINT_STATEMENT) != null) {
			
			model.setAUTHSALEPIN((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_ANEW_PRINT_STATEMENT).equals("1") ? "是"
					: "否"));
		}

		

		// 是否打印密钥控制提示抬头
		model.setISPRINTKEY("true");

		/** 主密钥索引号 */
		model.setMAINKEYNO(App.paramsManager.getMtkIndex() + "");

		
		/** 主密钥算法设置 */
		if (ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_ENCRYPT_MODE)  != null) {
			model.setENCYPTMODE((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_ENCRYPT_MODE).equals("1")) ? "双倍长"
					: "单倍长");
		}
		

		return model;
	}

	/**
	 * 获取打印通讯参数Model
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static PrintSysParamModel getCommunicateControlModel(Context context) {

		PrintSysParamModel model = new PrintSysParamModel();

		// 是否打印通讯参数设置提示抬头
		model.setISPRINTCOMM("true");

		int commType = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE);
		
		switch (commType) {
		case 0:
			//cdma
			/** 通讯方式 */
			model.setCOMMTYPE("CDMA");
			/** 长短链接 */
			model.setCOMMMODE(ParamsUtils.getString(ParamsConst.PARAMS_KEY_CDMA_MODE));
			/** 主机域名1 */
			model.setDOMAIN1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_SERVERIP1));
 
			/** 主机端口1 */
			model.setDNSPORT1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_PORT1));

			/** 主机域名2 */
			model.setDOMAIN2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_SERVERIP2));

			/** 主机端口2 */
			model.setDNSPORT2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_PORT2));

			/** TPDU */
			model.setTPDU(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_TPDU));
			/** 用户名*/
			model.setUSERNAME(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_USERNAME));
			/** 密码*/
			model.setUSERPWD(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_PWD));
			/** 接入号码*/
			model.setWIRELESSDIALNUN(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_CDMA_ACCESS_NUMBER));
			break;
		case 1:
			//gprs
			/** 通讯方式 */
			model.setCOMMTYPE("GPRS");
			/** 长短链接 */
			model.setCOMMMODE(ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_MODE));
			/** 主机域名1 */
			model.setDOMAIN1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_SERVERIP1));

			/** 主机端口1 */
			model.setDNSPORT1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_PORT1));

			/** 主机域名2 */
			model.setDOMAIN2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_SERVERIP2));

			/** 主机端口2 */
			model.setDNSPORT2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_PORT2));

			/** TPDU */
			model.setTPDU(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_TPDU));

			/** TPDU */
			model.setTPDU(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_TPDU));
			/** 用户名*/
			model.setUSERNAME(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_USERNAME));
			/** 密码*/
			model.setUSERPWD(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_PWD));
			/** APN*/
			model.setAPN1((ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_APN)));
			/** 接入号码*/
			model.setWIRELESSDIALNUN(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_GPRS_ACCESS_NUMBER));
			break;
		case 2:
			//wifi
			/** 通讯方式 */
			model.setCOMMTYPE("WIFI");
			/** 主机域名1 */
			model.setDOMAIN1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_WIFI_SERVERIP1));

			/** 主机端口1 */
			model.setDNSPORT1(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_WIFI_PORT1));

			/** 主机域名2 */
			model.setDOMAIN2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_WIFI_SERVERIP2));

			/** 主机端口2 */
			model.setDNSPORT2(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_WIFI_PORT2));

			/** TPDU */
			model.setTPDU(ParamsUtils.getString(
					ParamsConst.PARAMS_KEY_WIFI_TPDU));

		
			break;

		default:
			break;
		}
		/** 交易超时时间 */
		model.setTIMEOUT(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_COMM_TIMEOUT));

		return model;
	}

	/**
	 * 获取打印结算小票Model(金额前补11位 数量前补3位)
	 * 
	 * @param
	 * @return
	 */
	public static PrintSettleModel getPrintSettleModel(Context context) {

		PrintSettleModel model = new PrintSettleModel();
		Boolean isPrintAllDetail = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_PRINT_ALL_TRANS_DETAIL);
		model.setMerchantName(fillModel(ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME), 22));
		model.setShopId(fillModel(ParamsUtils.getShopId(), 22));
		model.setOperId(App.USER.getUserNo());
		model.setTerminalId(ParamsUtils.getPosId());
		model.setBatchNo(fillModel(ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_BATCHNO), 24));
//		model.setDateTime(fillModel(FormatUtils.timeFormat(ParamsUtils.getString( ParamsTrans.PARAMS_SETTLE_TIME).substring(0,4),ParamsUtils.getString( ParamsTrans.PARAMS_SETTLE_TIME).substring(4)),22));
//		if (reprintFlag) {
//			model.setIsReprint("true");
//		}
		LoggerUtils.d("111 内卡对账应答码：" + ParamsUtils.getInt(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING));
		switch (ParamsUtils.getInt(
				ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING)) {
		case 1:
			model.setIsSettleEok0EqualFlag("true");
			break;
		case 2:
			model.setIsSettleEok0NotEqualFlag("true");
			break;
		case 3:
			model.setIsSettleEok0ErrFlag("true");
			break;
		default:
			break;
		}
		LoggerUtils.d("111 外卡对账应答码：" + ParamsUtils.getInt(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING));
		switch (ParamsUtils.getInt(
				ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING)) {
		case 1:
			model.setIsSettleEok1EqualFlag("true");
			break;
		case 2:
			model.setIsSettleEok1NotEqualFlag("true");
			break;
		case 3:
			model.setIsSettleEok1ErrFlag("true");
			break;

		default:
			break;
		}
		SettlementService settleService = new SettlementServiceImpl(App
				.getInstance().getApplicationContext());
		// 内卡
		
		Long saleAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.SALE);
		int saleNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.SALE);
		Long authSaleAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.AUTH_SALE);
		int authSaleNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.AUTH_SALE);
		Long refundAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.REFUND);
		int refundNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.REFUND);
		Long emvRefundAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.EMV_REFUND);
		int emvRefundNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.EMV_REFUND);
		//退货为普通退货加EMV退货
		refundAmt0 = refundAmt0 + emvRefundAmt0; 
		refundNum0 = refundNum0 +emvRefundNum0;
		int authSaleOffNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.AUTH_SALE_OFF);
		Long authSaleOffAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.AUTH_SALE_OFF);
		int voidAuthSaleNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.VOID_AUTH_SALE);
		Long voidAuthSaleAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.VOID_AUTH_SALE);
		int ECSaleNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.EC_SALE);
		Long ECSaleAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.EC_SALE);
		Long magLoadAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.MAG_CASH_LOAD);
		int magLoadNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.MAG_CASH_LOAD);
		Long ecLoadAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.CASH_ECLOAD);
		int ecLoadNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.CASH_ECLOAD);
		Long voidCashECloadAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
		int voidCashECloadNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
		Long loadAmt0 = magLoadAmt0 + ecLoadAmt0
				- voidCashECloadAmt0;
		int loadNum0 = magLoadNum0 + ecLoadNum0
				- voidCashECloadNum0;
		Long voidSaleAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.VOID_SALE);
		int voidSaleNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.VOID_SALE);
		Long offlineAmt0 = settleService
				.getSettleAmount_NK(SettlementTableTypeConst.OFFLINE);
		int offlineNum0 = settleService
				.getSettleNum_NK(SettlementTableTypeConst.OFFLINE);
		
		//如果打印所有
		if (!isPrintAllDetail) {
			saleAmt0 = saleAmt0 - voidSaleAmt0;
			saleNum0 = saleNum0 - voidSaleNum0;
			voidSaleAmt0 = 0L;
			voidSaleNum0 = 0;
			authSaleAmt0 = authSaleAmt0 - voidAuthSaleAmt0;
			authSaleNum0 = authSaleNum0 - voidAuthSaleNum0;
			voidAuthSaleAmt0 = 0L;
			voidAuthSaleNum0 = 0;
		}
		Long totalAmt0 = saleAmt0
				+ authSaleAmt0 + authSaleOffAmt0
				+ ECSaleAmt0 + loadAmt0
				- voidSaleAmt0 - voidAuthSaleAmt0
				- refundAmt0 + offlineAmt0;
		int totalNum0 = saleNum0
				+ authSaleNum0
				+ authSaleOffNum0
				+ ECSaleNum0 + loadNum0
				+ voidSaleNum0
				+ voidAuthSaleNum0
				+ refundNum0 + offlineNum0;
		
		// 外卡
		Long saleAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.SALE);
		int saleNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.SALE);
		Long authSaleAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.AUTH_SALE);
		int authSaleNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.AUTH_SALE);
		Long refundAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.REFUND);
		int refundNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.REFUND);
		Long emvRefundAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.EMV_REFUND);
		int emvRefundNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.EMV_REFUND);
		//退货为普通退货加EMV退货
		refundAmt1 = refundAmt1 + emvRefundAmt1; 
		refundNum1 = refundNum1 +emvRefundNum1;
		int authSaleOffNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.AUTH_SALE_OFF);
		Long authSaleOffAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.AUTH_SALE_OFF);
		int voidAuthSaleNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.VOID_AUTH_SALE);
		Long voidAuthSaleAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.VOID_AUTH_SALE);
		int ECSaleNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.EC_SALE);
		Long ECSaleAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.EC_SALE);
		Long magLoadAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.MAG_CASH_LOAD);
		int magLoadNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.MAG_CASH_LOAD);
		Long ecLoadAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.CASH_ECLOAD);
		int ecLoadNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.CASH_ECLOAD);
		Long voidCashECloadAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
		int voidCashECloadNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
		Long loadAmt1 = magLoadAmt1 + ecLoadAmt1
				- voidCashECloadAmt1;
		int loadNum1 = magLoadNum1 + ecLoadNum1
				- voidCashECloadNum1;
		Long voidSaleAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.VOID_SALE);
		int voidSaleNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.VOID_SALE);
		Long offlineAmt1 = settleService
				.getSettleAmount_WK(SettlementTableTypeConst.OFFLINE);
		int offlineNum1 = settleService
				.getSettleNum_WK(SettlementTableTypeConst.OFFLINE);
		//如果打印所有
				if (!isPrintAllDetail) {
					saleAmt1 = saleAmt1 - voidSaleAmt1;
					saleNum1 = saleNum1 - voidSaleNum1;
					voidSaleAmt1 = 0L;
					voidSaleNum1 = 0;
					authSaleAmt1 = authSaleAmt1 - voidAuthSaleAmt1;
					authSaleNum1 = authSaleNum1 - voidAuthSaleNum1;
					voidAuthSaleAmt1 = 0L;
					voidAuthSaleNum1 = 0;
				}
		Long totalAmt1 = saleAmt1
				+ authSaleAmt1 + authSaleOffAmt1
				+ ECSaleAmt1 + loadAmt1
				- voidSaleAmt1 - voidAuthSaleAmt1
				- refundAmt1 + offlineAmt1;
		int totalNum1 = saleNum1
				+ authSaleNum1
				+ authSaleOffNum1
				+ ECSaleNum1 + loadNum1
				+ voidSaleNum1
				+ voidAuthSaleNum1
				+ refundNum1 + offlineNum1;

		// 内卡
		model.setAuthSaleNum0(StringUtils.fill(String.valueOf(authSaleNum0), " ", 3, true));
		model.setAuthSaleAmt0(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(authSaleAmt0)), " ", 12, true));
		model.setAuthSaleOffNum0(StringUtils
				.fill(String.valueOf(authSaleOffNum0), " ", 3, true));
		model.setAuthSaleOffAmt0(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(authSaleOffAmt0)), " ", 12, true));
		model.setECSaleNum0(StringUtils.fill(String.valueOf(ECSaleNum0), " ", 3, true));
		model.setECSaleAmt0(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(ECSaleAmt0)), " ", 12, true));
		model.setLoadAmt0(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(loadAmt0)),
				" ", 12, true));
		model.setLoadNum0(StringUtils.fill(String.valueOf(loadNum0), " ", 3, true));
		model.setRefundAmt0(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(refundAmt0)), " ", 12, true));
		model.setRefundNum0(StringUtils.fill(String.valueOf(refundNum0), " ", 3, true));
		model.setSaleNum0(StringUtils.fill(String.valueOf(saleNum0), " ", 3, true));
		model.setSaleAmt0(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(saleAmt0)),
				" ", 12, true));
		if (isPrintAllDetail) {
			model.setVoidAuthSaleNum0(StringUtils.fill(String.valueOf(voidAuthSaleNum0), " ", 3,
					true));
			model.setVoidAuthSaleAmt0(StringUtils.fill(
					FormatUtils.formatAmount(String.valueOf(voidAuthSaleAmt0)), " ", 12, true));
			model.setVoidSaleNum0(StringUtils.fill(String.valueOf(voidSaleNum0), " ", 3, true));
			model.setVoidSaleAmt0(StringUtils.fill(
					FormatUtils.formatAmount(String.valueOf(voidSaleAmt0)), " ", 12, true));
		}
		
		model.setOfflineNum0(StringUtils.fill(String.valueOf(offlineNum0), " ", 3, true));
		model.setOfflineAmt0(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(offlineAmt0)), " ", 12, true));
		model.setTotalNum0(StringUtils.fill(String.valueOf(totalNum0), " ", 3, true));
		if (totalAmt0 < 0) {
			totalAmt0 = 0 - totalAmt0;
			model.setTotalAmt0(StringUtils.fill("-"+FormatUtils.formatAmount(String.valueOf(totalAmt0)),
					" ", 12, true));
		}else {
			model.setTotalAmt0(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(totalAmt0)),
					" ", 12, true));
		}

		// 外卡
		model.setAuthSaleNum1(StringUtils.fill(String.valueOf(authSaleNum1), " ", 3, true));
		model.setAuthSaleAmt1(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(authSaleAmt1)), " ", 12, true));
		model.setAuthSaleOffNum1(StringUtils
				.fill(String.valueOf(authSaleOffNum1), " ", 3, true));
		model.setAuthSaleOffAmt1(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(authSaleOffAmt1)), " ", 12, true));
		model.setECSaleNum1(StringUtils.fill(String.valueOf(ECSaleNum1), " ", 3, true));
		model.setECSaleAmt1(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(ECSaleAmt1)), " ", 12, true));
		model.setLoadAmt1(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(loadAmt1)),
				" ", 12, true));
		model.setLoadNum1(StringUtils.fill(String.valueOf(loadNum1), " ", 3, true));
		model.setRefundAmt1(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(refundAmt1)), " ", 12, true));
		model.setRefundNum1(StringUtils.fill(String.valueOf(refundNum1), " ", 3, true));
		model.setSaleNum1(StringUtils.fill(String.valueOf(saleNum1), " ", 3, true));
		model.setSaleAmt1(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(saleAmt1)),
				" ", 12, true));
		
		if (isPrintAllDetail) {
			model.setVoidAuthSaleNum1(StringUtils.fill(String.valueOf(voidAuthSaleNum1), " ", 3, true));
			model.setVoidAuthSaleAmt1(StringUtils.fill(
					FormatUtils.formatAmount(String.valueOf(voidAuthSaleAmt1)), " ", 12, true));
			model.setVoidSaleNum1(StringUtils.fill(String.valueOf(voidSaleNum1), " ", 3, true));
			model.setVoidSaleAmt1(StringUtils.fill(
					FormatUtils.formatAmount(String.valueOf(voidSaleAmt1)), " ", 12, true));
		}
		
		
		model.setOfflineNum1(StringUtils.fill(String.valueOf(offlineNum1), " ", 3, true));
		model.setOfflineAmt1(StringUtils.fill(
				FormatUtils.formatAmount(String.valueOf(offlineAmt1)), " ", 12, true));
		model.setTotalNum1(StringUtils.fill(String.valueOf(totalNum1), " ", 3, true));
		if (totalAmt1 < 0) {
			totalAmt1 = 0 - totalAmt1;
			model.setTotalAmt1(StringUtils.fill("-"+FormatUtils.formatAmount(String.valueOf(totalAmt1)),
					" ", 12, true));
		}else {
			model.setTotalAmt1(StringUtils.fill(FormatUtils.formatAmount(String.valueOf(totalAmt1)),
					" ", 12, true));
		}
		
		

		return model;
	}

	/**
	 * 获取打印交易汇总Model
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static PrintTotalModel getPrintTotalModel(final Context context) {

		PrintTotalModel model = new PrintTotalModel();
		Field48_Settle field48_data = new Field48_Settle();

		model.setCreditAmt_nk(FormatUtils.formatAmount(String
				.valueOf(field48_data.getCreditAmount_N())));
		model.setCreditAmt_wk(FormatUtils.formatAmount(String
				.valueOf(field48_data.getCreditAmount_W())));
		// 前补12位
		model.setCreditNum_nk(StringUtils.fill(
				String.valueOf(field48_data.getCreditNum_N()), " ", 4, true));
		// 前补12位
		model.setCreditNum_wk(StringUtils.fill(
				String.valueOf(field48_data.getCreditNum_W()), " ", 4, true));
		model.setDebitAmt_nk(FormatUtils.formatAmount(String
				.valueOf(field48_data.getDebitAmount_N())));
		model.setDebitAmt_wk(FormatUtils.formatAmount(String
				.valueOf(field48_data.getDebitAmount_W())));
		// 前补12位
		model.setDebitNum_nk(StringUtils.fill( 
				String.valueOf(field48_data.getDebitNum_N()), " ", 4, true));
		// 前补12位
		model.setDebitNum_wk(StringUtils.fill(
				String.valueOf(field48_data.getDebitNum_W()), " ", 4, true));

		return model;
	}

	/**
	 * 获取打印商户信息Model
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static PrintInfoModel getPrintMerchantInfoModel(Context context) {

		PrintInfoModel model = new PrintInfoModel();
		// model.setAdsInfo(fillModel("广告信息", 15));
		model.setAppName(fillModel(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_APPDISPNAME), 23));
		model.setIsPrintVer("t");
		model.setTerminalId(fillModel(ParamsUtils.getPosId(), 23));
		model.setMerchantName(fillModel(ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME), 22));
		model.setShopId(fillModel(ParamsUtils.getShopId(), 23));
		model.setIsPrintShopParam("t");
		return model;
	}

	/**
	 * 获取打印版本信息Model
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static PrintInfoModel getPrintVertionInfoModel(Context context) {

		PrintInfoModel model = new PrintInfoModel();
		
		if (AndroidTools.getApplicationVersionName(context)!=null) {
			model.setAppVer(fillModel(AndroidTools.getApplicationVersionName(context), 17));
			model.setIsPrintVer("t");
		}
		
		return model;
	}

	/**
	 * 获取打印失败流水明细数据
	 * 
	 * @param
	 * @return
	 */
	public static PrintAllWaterModel getPrintFailWaterModel(Boolean title,
			Boolean end, Water water, int isUnSusc) {

		PrintAllWaterModel model = new PrintAllWaterModel();
		if (title) {
			model.setIsDescript("true");
			model.setIsEnd(null);
			if (isUnSusc == SendStatus.FAIL) {
				model.setUnsucc("true");
				model.setDenied(null);
			}
			else if (isUnSusc == SendStatus.DECLINE) {
				model.setDenied("true");
				model.setUnsucc(null);

			}else {
				model.setDenied(null);
				model.setUnsucc(null);
			}
			
		}
		if (end) {
			model.setIsTitleFlag(null);
			model.setUnsucc(null);
			model.setDenied(null);
			model.setIsEnd("true");
		}
		if (water != null) {
			// 前补12位
			model.setAmount(StringUtils.fill(
					FormatUtils.formatMount(String.valueOf(water.getAmount())),
					" ", 12, true));
			// 前补20位
			model.setCardNo(StringUtils.fill(TransUtils.getPanByWater(water), " ", 20, true));
			
			// 前补6位
			model.setTraceNo(StringUtils.fill(water.getTrace(), " ", 6, true));
			
			model.setType(StringUtils.fill(getTransEnType(water), " ", 3, true));
		}
		return model;
	}

	/**
	 * 获取打印交易明细数据
	 * 
	 * @param
	 * @return
	 */
	public static PrintAllWaterModel getPrintAllWaterDate(Boolean title,
			Boolean end, Water water) {

		PrintAllWaterModel model = new PrintAllWaterModel();
		if (title) {
			model.setIsTitleFlag("true");
			model.setIsDescript("true");
			model.setIsExplain(null);
			model.setIsEnd(null);
		}
		else if (end) {
			model.setIsTitleFlag(null);
			model.setIsDescript(null);
			model.setIsExplain("true");
			model.setIsEnd("true");
		}
		else {
			model.setIsTitleFlag(null);
			model.setIsDescript(null);
			model.setIsExplain(null);
			model.setIsEnd(null);
		}
		if (water != null) {
			// 前补12位
			model.setAmount(StringUtils.fill(
					FormatUtils.formatMount(String.valueOf(water.getAmount())),
					" ", 12, true));
			// 前补20位
			model.setCardNo(StringUtils.fill(TransUtils.getPanByWater(water), " ", 20,	true));
			// 前补6位
			model.setAuthNo(StringUtils.fill(water.getAuthCode(), " ", 6, true));
			// 前补6位
			model.setTraceNo(StringUtils.fill(water.getTrace(), " ", 6, true));
			// 前补1
			model.setType(StringUtils.fill(getTransEnType(water),
					" ", 1, true));
		}
		return model;
	}

	/** 格式化text长度到len （由左填充空格） */
	public static String fillModel(String text, int len) {
		return StringUtils.fill(text, " ", len, true);
	}
	
	/**
	 * 获取打印emv参数数据
	 */
	public static String getEMVParamsData() {

		List<emvparam> aidList = EmvAppModule.getInstance().getAIDList();
		List<capk> ridList = EmvAppModule.getInstance().getCAPKList();
		
		//** 打印aid*//*
		StringBuffer buffer = new StringBuffer();
			buffer.append("!NLFONT 6 1 3"+"\n");
			buffer.append("*text l AID_NUM:"+String.valueOf(aidList.size())+"\n");
		for (emvparam it : aidList) {
			buffer.append("!r!n*text l --------------------------------"+"\n");
			buffer.append("!r!n*text l AID:"+BytesUtils.bytesToHex(it._aid,it._aid_len)+"\n");
			buffer.append("!r!n*text l VER:"+BytesUtils.bytesToHex(it._app_ver)+"\n");
			buffer.append("!r!n*text l TAC_Default:"+BytesUtils.bytesToHex(it._tac_default)+"\n");
			buffer.append("!r!n*text l TAC_Decline:"+BytesUtils.bytesToHex(it._tac_denial)+"\n");
			buffer.append("!r!n*text l TAC_Online:"+BytesUtils.bytesToHex(it._tac_online)+"\n");
			buffer.append("!r!n*text l FloorLimit:"+BytesUtils.bytesToHex(it._floorlimit)+"\n");
			buffer.append("!r!n*text l ThresholdValue:"+BytesUtils.bytesToHex(it._threshold_value)+"\n");
			buffer.append("!r!n*text l MaxTargetPercent:"+BytesUtils.byteToHex(it._max_target_percent)+"\n");
			buffer.append("!r!n*text l Default DDOL:"+BytesUtils.bytesToHex(it._default_ddol,it._default_ddol_len)+"\n");
			if (it._default_tdol != null) {
				buffer.append("!r!n*text l Default TDOL:"+BytesUtils.bytesToHex(it._default_tdol,it._default_tdol_len)+"\n");
			}
			buffer.append("!r!n*text l CAP:"+BytesUtils.bytesToHex(it._cap)+"\n");
			if (it._add_cap != null) {
				buffer.append("!r!n*text l ADDCAP:"+BytesUtils.bytesToHex(it._add_cap)+"\n");
			}
			buffer.append("!r!n*text l ASI(PartialAID):"+String.valueOf(it._app_sel_indicator)+"\n");
			buffer.append("!r!n*text l ICS:"+BytesUtils.bytesToHex(it._ics)+"\n");
			buffer.append("!r!n*text l Terminal Type:"+String.valueOf(it._type)+"\n");
			buffer.append("!r!n*text l _limite_exist:"+BytesUtils.byteToHex(it._limit_exist)+"\n");
			buffer.append("!r!n*text l _ec_limit:"+BytesUtils.bytesToHex(it._ec_limit)+"\n");
			if (it._cl_limit != null) {
				buffer.append("!r!n*text l _cl_limit:"+BytesUtils.bytesToHex(it._cl_limit)+"\n");
			}
			buffer.append("!r!n*text l _cl_offline_limit:"+BytesUtils.bytesToHex(it._cl_offline_limit)+"\n");								
		}
		buffer.append("!r!n*text l --------------------------------"+"\n");
	
		//** 打印rid*//*	
		buffer.append("!r!n*text l AID_NUM:"+String.valueOf(ridList.size())+"\n");
		buffer.append("!r!n*text l --------------------------------"+"\n");
		for (capk it : ridList) {
			buffer.append("!r!n*text l Index:"+BytesUtils.byteToHex(it._index)+"\n");
			buffer.append("!r!n*text l RID:"+BytesUtils.bytesToHex(it._rid)+"\n");
			buffer.append("!r!n*text l ExpDate:"+BytesUtils.bytesToHex(it._expired_date)+"\n");
		}
		buffer.append("!r!n*text l --------------------------------"+"\n");
		buffer.append("!r!n*text l   "+"  \n");
		buffer.append("!r!n*text l   "+"  \n");
		buffer.append("!r!n*text l   "+"  \n");
		buffer.append("!r!n*text l   "+"  \n");
		buffer.append("!r!n*text l   "+"  \n");
		buffer.append("!r!n*text l   "+"  \n");
		return buffer.toString();
	
	}
	
	/**
	 * 根据流水获取交易类型英文标示
	 * 
	 * @param
	 * @return
	 */
	public static String getTransEnType(Water water) {
		String type = null;
		switch (water.getTransType()) {
		// 如果是Qpboc和pbocEc交易则为E
		case TransType.TRANS_SALE:
				type = "S";
			break;
		case TransType.TRANS_REFUND:
		case TransType.TRANS_EMV_REFUND:
			type = "R";
			break;
		case TransType.TRANS_AUTHSALE:
			type = "P";
			break;
		case TransType.TRANS_AUTHSALEOFF:
			type = "C";
			break;
		case TransType.TRANS_EC_LOAD:
		case TransType.TRANS_EC_LOAD_CASH:
		case TransType.TRANS_EC_LOAD_NOT_BIND:
		case TransType.TRANS_EC_PURCHASE:
			type = "E";
			break;
		case TransType.TRANS_VOID_AUTHSALE:
			type = "A";
			break;
		case TransType.TRANS_VOID_SALE:
			type = "V";
			break;
		default:
			type = "";
			break;
		}
		return type;
	}
	
	/**
	 * 将流水中输入方式转为打印凭条上的输入方式标识
	 * 
	 * @param mode
	 * @modify by linld
	 */
	public static String formatInputMode(String mode) {

		if (null == mode) {
			return "/N";
		}

		if (mode.startsWith("02")) {
			// 磁卡
			return "/S";
		} else if (mode.startsWith("05") || mode.startsWith("95")) {
			// IC插卡
			return "/I";
		} else if (mode.startsWith("07") || mode.startsWith("91")
				|| mode.startsWith("96") || mode.startsWith("98")) {
			// 非接
			return "/C";
		} else if (mode.startsWith("01")) {
			// 手输卡号,以及不刷卡的撤销类交易
			return "/M";
		} else {
			// 无卡交易
			return "/N";
		}

	}

}
