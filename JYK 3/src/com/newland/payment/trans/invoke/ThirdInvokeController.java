package com.newland.payment.trans.invoke;

import android.content.Context;
import android.content.Intent;

import com.lidroid.xutils.util.LogUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ThirdTransType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.TransController;
import com.newland.payment.trans.TransResultListener;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.impl.Auth;
import com.newland.payment.trans.impl.AuthSale;
import com.newland.payment.trans.impl.BalanceQuery;
import com.newland.payment.trans.impl.JycMposDown;
import com.newland.payment.trans.impl.JycMposLoad;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.trans.impl.MagRefund;
import com.newland.payment.trans.impl.Reprint;
import com.newland.payment.trans.impl.Sale;
import com.newland.payment.trans.impl.Settle;
import com.newland.payment.trans.impl.VoidAuth;
import com.newland.payment.trans.impl.VoidAuthSale;
import com.newland.payment.trans.impl.VoidSale;
import com.newland.payment.trans.invoke.listener.InvokeResultListener;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 第三方调用的处理工厂
 * @author linchunhui
 * @date 2015年5月28日 上午9:38:12
 *
 */
public class ThirdInvokeController {

	/**交易成功**/
	private final int TRANS_SUCC = 0;

	private Context context;

	private InvokeResultListener listener;

	private String traceNo;
	private Intent intentG;

	public void invoke(Context content, int transType, Intent intent, InvokeResultListener listener) {
		this.listener = listener;

		//TODO临时版本
		PublicLibJNIService.jnicardpowerdown();
		LoggerUtils.d("lxb PublicLibJNIService.jnicardpowerdown()");

		intent.putExtra("transCode", -1);
		switch(transType) {
			case ThirdTransType.TRANS_LOGIN:
				invokeLogin(intent);
				break;

			case ThirdTransType.TRANS_SALE:
				invokeSale(intent);
				break;

			case ThirdTransType.TRANS_VOID_SALE:
				invokeVoidSale(intent);
				break;

			case ThirdTransType.TRANS_BALANCE:
				invokeBalance(intent);
				break;

			case ThirdTransType.TRANS_PREAUTH:
				invokePreauth(intent);
				break;

			case ThirdTransType.TRANS_AUTHSALE:
				invokePreauthSale(intent);
				break;

			case ThirdTransType.TRANS_VOID_AUTHSALE:
				invokePreVoidAuthSale(intent);
				break;

			case ThirdTransType.TRANS_VOID_PREAUTH:
				invokePreVoidAuth(intent);
				break;

			case ThirdTransType.TRANS_REFUND:
				invokeRefund(intent);
				break;

			case ThirdTransType.TRANS_SETTLE:
				invokeSettle(intent);
				break;
			case ThirdTransType.TRANS_REPRINT:
				invokeReprint(intent);
				break;
			case ThirdTransType.TRANS_MPOS_DOWN:
				invokeMposDown(intent);
				break;
			case ThirdTransType.TRANS_LOAD_LOAD:
				invokeMposLoad(intent);
				break;
			default:
				LoggerUtils.e("无此交易");
				listener.fail("无此交易");
		}
	}

	/**
	 * 调用母pos下载
	 * @param intent
	 * @return
	 */
	private void invokeMposDown(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final JycMposDown sale = new JycMposDown(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			@Override
			public void succ() {
				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				listener.fail(message);
			}
		});
	}



	/**
	 * 调用母pos灌装
	 * @param intent
	 * @return
	 */
	private void invokeMposLoad(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final JycMposLoad sale = new JycMposLoad(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			@Override
			public void succ() {
				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				listener.fail(message);
			}
		});
	}

	/**
	 * 调用重打印业务
	 * @param intent
	 * @return
	 */
	private void invokeReprint(final Intent intent) {
		// TODO Auto-generated method stub

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final Reprint reprint = new Reprint(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(reprint, new TransResultListener() {
			@Override
			public void succ() {
				setIntentValue(intent, reprint.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				listener.fail(message);
			}
		});
		//重打印
	}
	/**
	 * 调用结算的业务
	 * @param intent
	 * @return
	 */
	private void invokeSettle(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final Settle settle = new Settle(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(settle, new TransResultListener() {
			@Override
			public void succ() {
				setIntentValue(intent, settle.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				listener.fail(message);
			}
		});
	}

	/**
	 * 调用消费的业务
	 * @param intent
	 * @return
	 */
	private void invokeSale(final Intent intent) {

//		CardBean cardBean = getCardBean(intent);
//		AmountBean amountBean = getAmountBean(intent);
		// TODO 需要调整金额的入口
//		final Sale sale = new Sale(cardBean, amountBean);


		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final Sale sale = new Sale(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {
				LoggerUtils.d("lxb invokeSale succ");
				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				//TODO 交易成功没构成冲正？？
//				if(sale.getPubBean().getResponseCode() == null){
//					LoggerUtils.d("lxb invokeSale --- 交易初始化失败？？");
//					//sale.getPubBean().setResponseCode("EC");
//				} else if(sale.getPubBean().getResponseCode().equals("00")){
//					LoggerUtils.d("lxb invokeSale fail --- 交易成功没构成冲正");
//					sale.getPubBean().setResponseCode("EC");
//				}
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 调用消费撤销的业务
	 * @param intent
	 * @return
	 */
	private void invokeVoidSale(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final VoidSale sale = new VoidSale(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());

				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 启动余额查询
	 * @param intent
	 */
	private void invokeBalance(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final BalanceQuery sale = new BalanceQuery(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());

				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}


	/**
	 * 预授权
	 * @param intent
	 */
	private void invokePreauth(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final Auth sale = new Auth(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {
				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 预授权完成请求
	 * @param intent
	 */
	private void invokePreauthSale(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final AuthSale sale = new AuthSale(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 预授权撤销
	 * @param intent
	 */
	private void invokePreVoidAuth(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final VoidAuth sale = new VoidAuth(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 预授权完成撤销
	 * @param intent
	 */
	private void invokePreVoidAuthSale(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final VoidAuthSale sale = new VoidAuthSale(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}

	/**
	 * 退货
	 * @param intent
	 */
	private void invokeRefund(final Intent intent) {

		ThirdInvokeBean thirdInvokeBean = new ThirdInvokeBean(intent);
		final MagRefund sale = new MagRefund(thirdInvokeBean);

		TransController controller = new TransController(context);
		controller.start(sale, new TransResultListener() {
			public void succ() {

				setIntentValue(intent, sale.getPubBean());
				listener.succ(intent);
			}

			@Override
			public void fail(String message) {
				setIntentValue(intent, sale.getPubBean());
				listener.fail(message);
			}
		});
	}



	/**
	 * 调用签到的业务
	 * @param intent
	 * @return
	 */
	public void invokeLogin(final Intent intent) {
		if (intent != null) {
			if(!StringUtils.isNullOrEmpty(intent.getStringExtra("operatorNo"))){
				App.USER.setUserNo(intent.getStringExtra("operatorNo"));
			}else if(App.USER.getUserNo() == null){
				App.USER.setUserNo("01");
			}
		}

		final Login login = new Login();
		TransController controller = new TransController(MainActivity.getInstance());
		controller.start(login, new TransResultListener() {
			@Override
			public void succ() {
				LoggerUtils.d("lxb invokeLogin succ");
				if (intent != null) {
					setIntentValue(intent, login.getPubBean());
					listener.succ(intent);
				}
			}

			@Override
			public void fail(String message) {
				LoggerUtils.d("lxb invokeLogin fail");
				if (intent != null) {
					LoggerUtils.d("lxb invokeLogin11111");
					setIntentValue(intent, login.getPubBean());
					listener.fail(message);
				}else {
					//TODO 自动签到怎么返回数据
					LoggerUtils.d("lxb invokeLogin22222");
					//	setIntentValue(intent, login.getPubBean());
					MainActivity.getInstance().finish();
				}
			}
		});
	}




	private AmountBean getAmountBean(Intent intent) {
		AmountBean amountBean = new AmountBean();
		amountBean.setThirdInvoke(true);
		long amount = intent.getLongExtra("amount", -1);
		LoggerUtils.d("lxb getAmonut :"+ amount);
		if (amount == -1) {
			return null;
		}

		amountBean.setAmount(amount);

		String currency = intent.getStringExtra("currenty");
		if (currency == null) {
			currency = "156";
		}
		amountBean.setCurrency(currency);

		int decimalsNum = intent.getIntExtra("decimalsNum", 2);
		if (decimalsNum < 0) {
			decimalsNum = 2;
		}
		amountBean.setFormat(decimalsNum);


		return amountBean;

	}

	/**
	 * 获取卡数据对象
	 * @param intent
	 * @return
	 */
	private CardBean getCardBean(Intent intent) {
		CardBean cardBean = new CardBean();
		cardBean.setThirdInvoke(true);

		String pan = intent.getStringExtra("pan");
		if (pan == null) {
			return null;
		}
		cardBean.setPan(pan);

		cardBean.setTk1(intent.getStringExtra("track1"));
		cardBean.setTk2(intent.getStringExtra("track2"));
		cardBean.setTk3(intent.getStringExtra("track3"));


		return cardBean;
	}

	/**
	 * 返回数据
	 */
	private void setIntentValue(Intent intent, PubBean pubBean)
	{
		LoggerUtils.d("hjh setIntentValue");
		if (pubBean == null) {
			LoggerUtils.d("lxb 自检退出或者错误");
			intent.putExtra("responseCode", "QX");
//			intent.putExtra("message", "");
			intent.putExtra("transCode", -1);
			return;
		}
		//TODO 需要处理下
		if (intent == null) {
			LoggerUtils.d("lxb 自检退出或者错误2");
			MainActivity.getInstance().doThirdInvokeLoginFail();
			return ;
		}
		if (pubBean.getTransType() == TransType.TRANS_LOGIN ) {
			LogUtils.d("lxb GetTrans111:" + ThirdTransType.TRANS_LOGIN);
			intent.putExtra("transType", ThirdTransType.TRANS_LOGIN);
		}else{
			LogUtils.d("lxb GetTrans222:" + intent.getIntExtra("transType", -1) );
			intent.putExtra("transType", intent.getIntExtra("transType", -1));
		}

		//返回消息

		if (pubBean.getResponseCode() == null) {
			LoggerUtils.d("lxb 用户取消交易");
			intent.putExtra("responseCode", "QX");
			intent.putExtra("transCode", -1);
			return;

		}else if (pubBean.getResponseCode().equals("EC")) {
			intent.putExtra("responseCode", pubBean.getResponseCode());
			intent.putExtra("message", pubBean.getMessage());
		}else {
			intent.putExtra("responseCode", pubBean.getResponseCode());
			intent.putExtra("message", pubBean.getMessage());
			//TODO 加上年份
			intent.putExtra("transTime", TimeUtils.getTimeNow().substring(0, 4)
					+ pubBean.getDate()+pubBean.getTime());
			LoggerUtils.d("lxb getTransTime:" + TimeUtils.getTimeNow().substring(0, 4)
					+ pubBean.getDate()+pubBean.getTime());
		}

		LoggerUtils.d("lxb getResponseCode:" + pubBean.getResponseCode());
		LoggerUtils.d("lxb message:" + pubBean.getMessage());


		intent.putExtra("transResult", TRANS_SUCC);

		intent.putExtra("cardSerialNo", pubBean.getCardSerialNo());
		intent.putExtra("expDate", pubBean.getExpDate());
		intent.putExtra("amount", pubBean.getAmount());

		//TODO 实际金额在部分承兑上存在问题
		intent.putExtra("transAmount", pubBean.getAmount());


		intent.putExtra("cardNo", pubBean.getPan());
		intent.putExtra("traceNo", pubBean.getTraceNo());
		intent.putExtra("referenceNo", pubBean.getSystemRefNum());
		intent.putExtra("authCode", pubBean.getAuthCode());

		intent.putExtra("batchNum", ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_BASE_BATCHNO));
		intent.putExtra("time", pubBean.getTime());
		intent.putExtra("date", pubBean.getDate());
		intent.putExtra("interOrg", pubBean.getInternationOrg());

		//
		intent.putExtra("voucherNo", pubBean.getTraceNo());
		LoggerUtils.d("lxb voucherNo：" + pubBean.getTraceNo());

		intent.putExtra("batchNo", ParamsUtils.getString( ParamsConst.PARAMS_KEY_BASE_BATCHNO));
		LoggerUtils.d("lxb vourcherNo：" + ParamsUtils.getString( ParamsConst.PARAMS_KEY_BASE_BATCHNO));


		//商户号终端号
//		intent.putExtra("merchantId", pubBean.getPosID());
//		intent.putExtra("terminalId", pubBean.getShopID());

		intent.putExtra("merchantId", pubBean.getShopID());
		intent.putExtra("terminalId", pubBean.getPosID());

		//TODO　160708增加银行卡名称　cardType

//		if (pubBean.getInternationOrg() != null ) {
//			LoggerUtils.d("lxb getInternationOrg：" + pubBean.getInternationOrg());
//
//	//		intent.putExtra("cardType", BankCodeHelper.getBankCodeCN(pubBean.getInternationOrg().substring(0,4)));
//			LoggerUtils.d("lxb cardType：" + BankCodeHelper.getBankCodeCN(pubBean.getInternationOrg().substring(0,4)));
//		}
//		intent.putExtra("cardType", pubBean.getField48().getCardInfoCn());
//		LoggerUtils.d("lxb cardType：" + pubBean.getField48().getCardInfoCn());


		intent.putExtra("cardInfo",pubBean.getBack_cardInfo());
		intent.putExtra("isDiscount",pubBean.getBack_isDiscount());
		intent.putExtra("discountType",pubBean.getBack_discountType());
		intent.putExtra("discountRate",pubBean.getBack_discountRate());
		intent.putExtra("bonus",pubBean.getBack_bonus());
		intent.putExtra("name",pubBean.getBack_name());
//		LoggerUtils.d("back _ name"+pubBean.getBack_name());
		//TODO 161219 TransCode
		if(pubBean.getResponseCode().equals("00")){
			intent.putExtra("transCode", 1);
		}else {
			intent.putExtra("transCode", pubBean.getTransCode());
		}

	}


}
