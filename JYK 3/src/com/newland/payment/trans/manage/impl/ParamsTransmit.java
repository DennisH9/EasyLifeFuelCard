package com.newland.payment.trans.manage.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.bean.field.ISOField62_Params;
import com.newland.payment.trans.manage.ParamsHelper;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 参数传递
 *
 * @author CB
 * @date 2015-5-20 
 * @time 下午4:58:13
 */
public class ParamsTransmit extends AbstractBaseTrans{


	private class Step {
		public final static int TRANS_START = 1;
		public final static int SEND_STATUS = 2;
		public final static int PARAM_TRANS = 3;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	@Override
	protected void checkPower(){
		super.checkPower();
		checkSingIn = false; // 不检查签到状态
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		checkCardExsit = false;
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		pubBean.setTransName(getText(R.string.setting_other_download_parameter_pass));
		return res;
	}
	
	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_other_download_parameter_passing)};
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}
	
	/** 组包 */
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_packIsoData() {
		
		try {
			initManagePubBean();
			ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
			pubBean.setIsoField60(isoField60);
			
			iso8583.initPack();	
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());	
			iso8583.setField(60, pubBean.getIsoField60().getString());	
			int resCode = dealPackAndComm(false, false, true);
			if (resCode != STEP_CONTINUE){
				if (resCode == STEP_FINISH){
					return Step.TRANS_RESULT;
				}
				return resCode;
			}
			
			return Step.PARAM_TRANS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return FINISH;
		}
		
	}


	@AnnStep(stepIndex = Step.PARAM_TRANS)
	public int step_ParamTrans(){

		try {
			
			String data = iso8583.getField(62);		
			if (StringUtils.isNullOrEmpty(data)){
				transResultBean.setIsSucess(true);
				transResultBean.setContent(getText(R.string.setting_other_download_parameter_pass_success));
				return Step.TRANS_RESULT;
			}
			ISOField62_Params field62 = new ISOField62_Params(data);
			//终端应用类型
			LoggerUtils.d("111 终端应用类型[" + field62.getPosAppType() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_POS_TYPE, field62.getPosAppType());
			//超时时间
			LoggerUtils.d("111 超时时间[" + field62.getTimeout() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_COMMUNICATION_OUT_TIME, field62.getTimeout());
			//重试次数
			LoggerUtils.d("111 重试次数[" + field62.getRetryTime() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_RECONNECT_TIMES, field62.getRetryTime());
			//三个交易电话号码之一
			LoggerUtils.d("111 电话号码1[" + field62.getTransPhone1() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_DIAL_PHONE1, field62.getTransPhone1());
			//三个交易电话号码之二
			LoggerUtils.d("111 电话号码2[" + field62.getTransPhone2() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_DIAL_PHONE2, field62.getTransPhone2());
			//三个交易电话号码之三
			LoggerUtils.d("111 电话号码3[" + field62.getTransPhone3() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_DIAL_PHONE3, field62.getTransPhone3());
			//一个管理电话号码
			LoggerUtils.d("111 管理号码1[" + field62.getManagePhone() + "]");
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_MANAGE_PHONE, field62.getManagePhone());
			//是否支持小费 1 —支持， 0—不支持
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_IS_TIP, field62.getIsSupportTip());
			//小费百分比
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_BASE_TIPRATE, field62.getTipPercent());
			//是否支持手工输入卡号  1 —支持， 0—不支持
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_IS_CARD_INPUT, field62.getIsSupportHandInputCardNo());
			//结算后自动签退 N1 1 —自动， 0—不自动
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT, field62.getIsAutoSignOut());
			//商户名称（中文简称或英文简称）
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME, field62.getShopName());
			//交易重发次数(冲正重发次数)
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES, field62.getTransRetryTime());
			//主密钥
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_MAIN_KEY_INDEX, field62.getMainKey());
			ParamsUtils.setTMKIndex(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAIN_KEY_INDEX));
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
			
			/*支持的交易类型*/
			String supportTransType = field62.getSupportTransType();
			ParamsHelper params = new ParamsHelper(StringUtils.hexToBinary(supportTransType));
			/***********************第一个字节*************************/
			//1.查询
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_BALANCE, params.getData(1));
			//2.预授权/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_PREAUTH, params.getData(1));
			//3.预授权撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_VOIDPREAUTH, params.getData(1));
			//4.预授权完成（请求） /冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_AUTHSALE, params.getData(1));
			//5.预授权完成撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_VOIDAUTHSALE, params.getData(1));
			//6.消费/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_SALE, params.getData(1));
			//7.消费撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_VOID, params.getData(1));
			//8.退货
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_REFUND, params.getData(1));
			/***********************第二个字节*************************/
			//9.离线结算
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_SETTLEOFF, params.getData(1));
			//10.结算调整
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ADJUSTOFF, params.getData(1));
			//11.预授权完成（通知）
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_AUTHSALEOFF, params.getData(1));
			//12.基于 PBOC 借/贷记标准 IC 卡脚本处理结果通知，又设置，但流程不受这个参数控制
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_PBOC_CARD_RESULT_NOTIFICATION, params.getData(1));
			//13.电子现金脱机消费
			String isSupport = "";
			isSupport = params.getData(1);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_EC, isSupport);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_QPBOC, isSupport);
			//同步电子现金开关到AID参数
			EmvModule emvModule = EmvAppModule.getInstance();
			if("1".equals(isSupport)) {
				emvModule.emvSetSupportEc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_EC, true));
			} else {
				emvModule.emvSetSupportEc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_EC, false));
			}
			//跳过第14位
			params.getData(1);
			//基于 PBOC 电子钱包的圈存类交易/冲正
			//15.电子钱包暂时只解析不做处理
			params.getData(1);
			//16.分期付款交易/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_INSTALLSALE, params.getData(1));
			/***********************第三个字节*************************/
			//17.分期付款交易撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_INSTALLVOID, params.getData(1));
			
			//18.积分消费/冲正
			String pointType = params.getData(1);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_UNIONBOINTSALE, pointType);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_BANKBOINTSALE, pointType);
			//19.积分消费撤销/冲正
			pointType = params.getData(1);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_VOIDUNIONSALE, pointType);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_VOIDBANKSALE, pointType);
			//20.借贷记的圈存类交易/冲正
			String loadType = params.getData(1);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ECNOPOINTLOAD, loadType);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ECPOINTLOAD, loadType);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ECMONEYLOAD, loadType);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ECVOIDMONEYLOAD, loadType);
			//21.预约消费/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_APPOINTMENT, params.getData(1));
			//22.预约消费撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_APVOIDPOINT, params.getData(1));
			//23.订购消费/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ORSALE, params.getData(1));
			//24.订购消费撤销/冲正
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_ORVOID, params.getData(1));
			/***********************第四个字节*************************/
			//25.磁条卡现金（账户）充值类交易
			String addTrans = params.getData(1);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_MSCMONEY, addTrans);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_TRANS_MSCACCOUNT, addTrans);
			
			//重置菜单
			App.mainMenuData.checkAll();
			
			//这边说明参数下载完成了
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PARAM_DOWN, false);
			transResultBean.setIsSucess(true);
			transResultBean.setContent(getText(R.string.setting_other_download_parameter_pass_success));
			
		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.setting_other_download_parameter_pass_fail));
		}

		return Step.TRANS_RESULT;
	}

	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_showResult(){
		
		//transResultBean = showUITransResult(transResultBean);
		showToast(transResultBean.getContent());
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
