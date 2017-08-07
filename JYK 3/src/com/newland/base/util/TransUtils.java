package com.newland.base.util;

import java.io.File;

import android.content.Context;

import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.ReverseWaterService;
import com.newland.payment.mvc.service.ScriptResultService;
import com.newland.payment.mvc.service.SettlementService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.EmvFailWaterServiceImpl;
import com.newland.payment.mvc.service.impl.ReverseWaterServiceImpl;
import com.newland.payment.mvc.service.impl.ScriptResultServiceImpl;
import com.newland.payment.mvc.service.impl.SettlementServiceImpl;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.impl.Auth;
import com.newland.payment.trans.impl.AuthSale;
import com.newland.payment.trans.impl.AuthSaleOff;
import com.newland.payment.trans.impl.BalanceQuery;
import com.newland.payment.trans.impl.BonusLogin;
import com.newland.payment.trans.impl.LogOut;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.trans.impl.MagRefund;
import com.newland.payment.trans.impl.Sale;
import com.newland.payment.trans.impl.Settle;
import com.newland.payment.trans.impl.VoidAuth;
import com.newland.payment.trans.impl.VoidAuthSale;
import com.newland.payment.trans.impl.VoidSale;
import com.newland.payment.trans.impl.elecash.ECBalanceQuery;
import com.newland.payment.trans.impl.elecash.ECLoad;
import com.newland.payment.trans.impl.elecash.ECLoadDetail;
import com.newland.payment.trans.impl.elecash.ECRefund;
import com.newland.payment.trans.impl.elecash.ECTransDetail;
import com.newland.payment.trans.impl.elecash.ECVoidCashLoad;
import com.newland.payment.trans.impl.elecash.QPbocSale;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 交易工具类
 * 
 * @author CB
 * @date 2015-6-2
 * @time 下午11:14:37
 */
public class TransUtils {

	public static AbstractBaseTrans getTransBean(int resTitle) {

		AbstractBaseTrans bean = null;

		switch (resTitle) {
		case TransType.TRANS_BALANCE:
			// 余额查询
			bean = new BalanceQuery(TransType.TRANS_BALANCE);
			break;

		case TransType.TRANS_SALE:
			// 消费
			bean = new Sale();
			break;
		case TransType.TRANS_INSERT_CARD_SALE:
			// 消插卡费
			bean = new Sale(TransType.TRANS_INSERT_CARD_SALE);
			break;

		case TransType.TRANS_AUTHSALE:
			// 授权完成请求
			bean = new AuthSale();
			break;

		case TransType.TRANS_AUTHSALEOFF:
			// 授权完成通知
			bean = new AuthSaleOff();
			break;
			
		case TransType.TRANS_PREAUTH:
			// 预授权
			bean = new Auth();
			break;
			
		case TransType.TRANS_REFUND:
			// 退货
			bean = new MagRefund(TransType.TRANS_REFUND);
			break;
			
		case TransType.TRANS_VOID_SALE:
			// 消费撤销
			bean = new VoidSale();
			break;
			
		case TransType.TRANS_VOID_AUTHSALE:
			// 预授权完成撤销
			bean = new VoidAuthSale();
			break;

		case TransType.TRANS_VOID_PREAUTH:
			// 预授权撤销
			bean = new VoidAuth();
			break;

		case TransType.TRANS_EMV_SCRIPE:
			// EMV脚本结果通知
			break;
			
		case TransType.TRANS_EMV_REFUND:
			// EMV脱机退货
			bean = new ECRefund();
			break;
			
		case TransType.TRANS_LOAD_DETAIL:
			// 圈存日志
			bean = new ECLoadDetail();
			break;
			
			
		case TransType.TRANS_EC_BALANCE:
			// 电子现金余额查询
			bean = new ECBalanceQuery();
			break;
			
		case TransType.TRANS_EC_DETAIL:
			// 电子现金明细查询
			bean = new ECTransDetail();
			break;
			
		case TransType.TRANS_QPBOC:
			// 快速支付
			bean = new QPbocSale();
			break;
			
		case TransType.TRANS_EC_PURCHASE:
			// 普通支付
			bean = new Sale(TransType.TRANS_EC_PURCHASE);
			break;
			
		case TransType.TRANS_EC_LOAD:
			// 电子现金圈存 指定帐户
			bean = new ECLoad(TransType.TRANS_EC_LOAD);
			break;
			
		case TransType.TRANS_EC_LOAD_CASH:
			// 电子现金圈存现金充值
			bean = new ECLoad(TransType.TRANS_EC_LOAD_CASH);
			break;
			
		case TransType.TRANS_EC_LOAD_NOT_BIND:
			// 电子现金圈存非指定账户
			bean = new ECLoad(TransType.TRANS_EC_LOAD_NOT_BIND);
			break;
			
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			// 电子现金圈存现金撤销
			bean = new ECVoidCashLoad();
			break;
			
			
		case TransType.TRANS_SALE_EMV:
			// 借贷记消费
			break;
			
		case TransType.TRANS_LOGIN:
			// 签到
			bean = new Login();
			break;
			
		case TransType.TRANS_LOGOUT:
			// 签退
			bean = new LogOut();
			break;
			
		case TransType.TRANS_REVERSAL:
			// 冲正
			break;
			
		case TransType.TRANS_SCRIPT:
			// 脚本结果通知
			break;
			
		case TransType.TRANS_SETTLE:
			// 结算
			bean = new Settle();
			break;
			
		case TransType.TRANS_BATCHUP:
			// 批上送
			break;
			
		case TransType.TRANS_PARAM_TRANSFER:
			// 参数传递
			break;
			
		case TransType.TRANS_STATUS_SEND:
			// 状态上送
			break;
			
		case TransType.TRANS_ECHO:
			// 回响测试
			break;
			
		case TransType.TRANS_CASHIER_LOGIN:
			// 收银员积分签到
			bean = new BonusLogin();
			break;
			
		}

		return bean;
	}

	/**
	 * 根据所给的交易类型获得交易的中英文名称
	 * 
	 * @param transType
	 * @return 中英文标题数组
	 */
	public static String[] getTransType(int transType) {
		String[] transName = new String[2];
		switch (transType) {

		case TransType.TRANS_SALE:// 消费
			transName[0] = "消费";
			transName[1] = "SALE";
			return transName;
		case TransType.TRANS_VOID_SALE:// 消费撤销
			transName[0] = "消费撤销";
			transName[1] = "VOID";
			return transName;
		case TransType.TRANS_REFUND:// 退货
			transName[0] = "退货";
			transName[1] = "REFUND";
			return transName;
		case TransType.TRANS_PREAUTH:// 预授权
			transName[0] = "预授权";
			transName[1] = "AUTH";
			return transName;
		case TransType.TRANS_AUTHSALE:// 预授权完成请求
			transName[0] = "预授权完成（请求）";
			transName[1] = "AUTH COMPLETE";
			return transName;
		case TransType.TRANS_AUTHSALEOFF:// 预授权完成通知
			transName[0] = "预授权完成（通知）";
			transName[1] = "AUTH SETTLEMENT";
			return transName;
		case TransType.TRANS_VOID_PREAUTH:// 预授权撤销
			transName[0] = "预授权撤销";
			transName[1] = "CANCEL";
			return transName;
		case TransType.TRANS_VOID_AUTHSALE:// 预授权完成撤销
			transName[0] = "预授权完成撤销";
			transName[1] = "COMPLETE VOID";
			return transName;
			// 电子现金
		case TransType.TRANS_EMV_REFUND:
			transName[0] = "电子现金退货";
			transName[1] = "EC REFUND";
			return transName;
		case TransType.TRANS_EC_PURCHASE:// 电子现金消费
			transName[0] = "电子现金消费";
			transName[1] = "EC SALE";
			return transName;
		case TransType.TRANS_EC_LOAD_CASH:// 电子现金圈存现金
			transName[0] = "电子现金现金充值";
			transName[1] = "EC LOAD";
			return transName;
		case TransType.TRANS_EC_LOAD_NOT_BIND:// 电子现金非指定账户圈存
			transName[0] = "电子现金非指定账户圈存";
			transName[1] = "EC LOAD";
			return transName;
		case TransType.TRANS_EC_LOAD:// 电子现金圈存
			transName[0] = "电子现金指定账户圈存";
			transName[1] = "EC LOAD";
			return transName;
		case TransType.TRANS_EC_VOID_LOAD_CASH:// 电子现金圈存现金撤销
			transName[0] = "电子现金充值撤销";
			transName[1] = "VOID";
			return transName;
		case TransType.TRANS_REPRINT:// 重打印
			transName[0] = "重打印";
			transName[1] = "REPRINT";
			return transName;
		default:
			transName[0] = "未定义的交易";
			transName[1] = "NOT DIFINE TRANS";
			return transName;
		}

	}

	public static String getPanByWater(Water water) {
		String pan = "";
		
		if (water.getTransType() == TransType.TRANS_PREAUTH) {
			if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_PREAUTH_SHIELD_PAN)) {
				pan = water.getPan();
			} else {
				pan = FormatUtils.formatCardNoWithStar(water.getPan());
			}

		} else if (water.getEmvStatus() == EmvStatus.EMV_STATUS_OFFLINE_FAIL
				|| water.getEmvStatus() == EmvStatus.EMV_STATUS_OFFLINE_SUCC) {
			pan = water.getPan();
		} else {
			pan = FormatUtils.formatCardNoWithStar(water.getPan());
		}
		return pan;
	}

	/**
	 * 清除流水，并将相关状态置位
	 * 
	 * @author linld
	 * @date 2015-06-06
	 */
	public static void clearWater(Context context) {

		// 清流水
		WaterService waterService = new WaterServiceImpl(context);
		waterService.clearWater();
		// 清EMV失败流水
		EmvFailWaterService emvFailWaterService = new EmvFailWaterServiceImpl(
				context);
		emvFailWaterService.clearEmvFailWater();
		//清EMV日志[注:文件名写死"emv.log"]
		LoggerUtils.d("EMVLog文件路径:" + App.getInstance().getApplicationContext().getFilesDir() + File.separator + "emv" + File.separator + "emv.log");
		File file = new File(App.getInstance().getApplicationContext().getFilesDir() + File.separator + "emv" + File.separator + "emv.log");
		file.delete();
		// 清结算
		SettlementService settleService = new SettlementServiceImpl(context);
		settleService.clearSettlement();
		// 清冲正数据
		ReverseWaterService reverseService = new ReverseWaterServiceImpl(
				context);
		reverseService.delete();
		// 清脚本数据
		ScriptResultService scriptService = new ScriptResultServiceImpl(context);
		scriptService.delete();
		// 清电子签名图片
		FileUtils.clearDir(new File(App.SIGNATURE_BMP_DIR), false);
		FileUtils.clearDir(new File(App.SIGNATURE_JBIG_DIR), false);

		// 电子签名上送位置覆位1
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_OFFLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_SETTLE_HALT, 1);
		// 离线上送位置覆位1
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_SEND_HALT, 1);

		// 结算上送位置覆位
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_OFFLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_SUCC_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_ONLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_INFORM_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_FAIL_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, 0);

		// 未上送笔数置0
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, 0);
		// 已上送次数置0
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_REVERSAL_HAVE_SEND, 0);
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_SCRIPT_HAVE_SEND, 0);

	}
	
}
