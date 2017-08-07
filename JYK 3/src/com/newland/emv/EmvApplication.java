package com.newland.emv;

import java.util.List;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.SoundUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.SoundType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.EcLoadBean;
import com.newland.pos.sdk.bean.PbocDetailBean;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.common.EmvResult;
import com.newland.pos.sdk.common.EmvTransType;
import com.newland.pos.sdk.common.TransConst.AmountBeanConst;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.emv.EmvProcess;


/**
 * Emv交易接口
 * 
 * @version 1.0
 * @author spy
 * @date 2015年5月27日
 * @time 下午9:19:31
 */
public class EmvApplication{

	private AbstractBaseTrans trans = null;
	private EmvAppModule emvAppModule = null;
	private EmvProcess emvProcess;

	public EmvApplication(AbstractBaseTrans trans) {
		this.trans = trans;
		emvAppModule = EmvAppModule.getInstance();
		emvAppModule.setUiTrans(trans);
		emvProcess = new EmvProcess();
		emvProcess.init(emvAppModule, emvAppModule);
		emvProcess.setDispTvrTsi(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_DISPLAY_TRV_TSI,false));
	}

	/**
	 * 简易流程
	 * 
	 * @param pubBean
	 * @return
	 */
	public int simpleProcess(PubBean pubBean) {
		int ret;

		trans.showUIProgressFragment("正在读卡\r\n请勿拿卡");
		
		ret = emvProcess.simpleProcess(pubBean.getEmvBean());

		return ret;

	}

	/**
	 * qPboc流程
	 * 
	 * @param pubBean
	 * @return
	 */
	public int qPbocProcess(PubBean pubBean) {
		int ret;
		
		//因qPboc交易走到这里时，交易已经完成，所以，IC卡参数以及公钥的判断不能放这里，其他操作也一样
	
		ret = emvProcess.qPbocProcess(pubBean.getEmvBean());

		return ret;
	}

	/**
	 * 
	 * @param pubBean
	 * @return
	 */
	public int initEmvApp(PubBean pubBean) /* throws EmvException */{
		int ret;
		
		// 加入判断参数是否下载
		if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_AID_DOWN)) {
			showTipMsg("请下载AID参数");
			return EmvResult.EMV_FAIL;
		}

		pubBean.getEmvBean().setTransMode(pubBean.getTransType());
		pubBean.getEmvBean().setEnableOnlyEc(true);
		pubBean.getEmvBean().setL2_9CTransType(BytesUtils.hexStringToBytes(trans.getProcessCode(pubBean.getTransType()))[0]);
		
		switch (pubBean.getTransType()) {
		case TransType.TRANS_BALANCE:         	/**< 余额查询*/
		case TransType.TRANS_AUTHSALE:			/**< 授权完成请求*/
		case TransType.TRANS_AUTHSALEOFF:		/**< 授权完成通知*/
		case TransType.TRANS_PREAUTH:			/**< 预授权*/
		case TransType.TRANS_REFUND:			/**< 退货*/
		case TransType.TRANS_VOID_SALE:			/**< 消费撤销*/
		case TransType.TRANS_VOID_AUTHSALE:		/**< 完成撤销*/
		case TransType.TRANS_VOID_PREAUTH:		/**< 授权撤销*/
			pubBean.getEmvBean().setEnableOnlyEc(false);
			break;
		default:
			break;
		}
		
		ret = emvProcess.initEmvApp(pubBean.getEmvBean());
		
		/** <电子现金余额查询 */
		if (EmvTransType.TRANS_EC_BALANCE == pubBean.getTransType()) {
			SoundUtils.getInstance().play(SoundType.BEEP); // 喇叭响
		}
		return ret;
	}

	public int processApp(PubBean pubBean) {

		int ret;
		trans.showUIProgressFragment("正在读卡\r\n请勿拿卡");
		
		pubBean.setCurrency(AmountBeanConst.CURRENCY);

		ret = emvProcess.processApp(pubBean.getEmvBean());
		if(ret != 0 && ret != EmvConst.EMV_TRANS_ACCEPT ){
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV一次授权失败");
		}
		return ret;
	}

	public int completeApp(PubBean pubBean, boolean cOnlineResult,
			boolean isReversal) {

		int ret;
		boolean cReSuccFlag = false;
		String responseCode = pubBean.getResponseCode();
		LoggerUtils.d("111 后台响应码[" + responseCode + "]");
		LoggerUtils.d("111 completeApp cOnlineResult1:" + cOnlineResult);
		if (cOnlineResult == true) {
			if ("00".equals(responseCode)
					|| "11".equals(responseCode)
					|| "A2".equals(responseCode)
					|| "A4".equals(responseCode)
					|| "A5".equals(responseCode)
					|| "A6".equals(responseCode)) {
				pubBean.setResponseCode("00");
			} 
		}
		
		ret = emvProcess.completeApp(pubBean.getEmvBean(),cOnlineResult);
		
		if(pubBean.isTSIComleted()){
			if (TransType.TRANS_EC_VOID_LOAD_CASH == pubBean.getTransType()) {
				pubBean.getIsoField60().setFuncCode("00");
			}
			LoggerUtils.d("111 保存脚本通知数据");
			// 保存脚本
			pubBean.setIsoField55(emvAppModule.packScriptField55());
			ScriptResult scriptResult = trans.getScriptResult(pubBean);
			trans.addScriptResult(scriptResult);
		}

		if (true == isReversal) {
			if (true == cOnlineResult) {
				/** <收到应答之后冲正的55域内容需要再次打包保存 */
				String field55 = emvAppModule.packReversalField55(true);
				trans.changeReverseField55(field55);
			}
		}

		if (ret != 0) {
			/** <这种情况要构成冲正 */
			if (true == cOnlineResult && "00".equals(responseCode)) {
				LoggerUtils.d("111 后台批准,卡片拒绝的情况~~~~");
				cReSuccFlag = true;
			} else if (true == cOnlineResult
					&& !"00".equals(responseCode)) {
				if ("11".equals(responseCode)
						|| "A2".equals(responseCode)
						|| "A4".equals(responseCode)
						|| "A5".equals(responseCode)
						|| "A6".equals(responseCode)) {
					trans.displayResponse(true, responseCode);
				} else {
					if (true == isReversal) {
						/** 后台有返回错误码,不冲正,lld,2014-2-19,szBctc,修改空格响应码引起冲正问题 */
						trans.deleteReverse();

					}
					trans.displayResponse(false, responseCode);
				}
			}
			
			/**
			 * <脱机拒绝的记录流水
			 */
			if (false == cOnlineResult && ret == EmvConst.EMV_TRANS_DENIAL) {
				// 存失败流水
				LoggerUtils.d("dd completeApp脱机拒绝-存失败流水");
				// 脱机流水加1
				trans.addTraceNo();
				pubBean.setInternationOrg(getEmvOrgSheet());
				pubBean.setEmv_Status(EmvStatus.EMV_STATUS_OFFLINE_FAIL);
				emvAppModule.SaveEmvFailWater(App.getInstance().getApplicationContext(), pubBean);
			}
			if (ret == EmvConst.EMV_TRANS_DENIAL) {
				if (true == cReSuccFlag) {
					LoggerUtils.d("111 提示请联系发卡行1");
					// 发卡行已承兑,卡片拒绝，提示联系发卡行
					trans.setResultContent("交易拒绝:" + "请联系发卡行");
					DispOutICC(pubBean.getTransName(), "交易拒绝", "请联系发卡行");
					pubBean.setResponseCode("EC");//存在卡片拒绝发卡行等情况
					pubBean.setMessage("发卡行认证失败");
				} else {
					LoggerUtils.d("111 不提示请联系发卡行2");
						trans.setResultContent("交易拒绝:" + getEmvErrMsg());
						DispOutICC(pubBean.getTransName(), "交易拒绝", getEmvErrMsg());
						LoggerUtils.d("completeApp:交易拒绝");
				}
			} else {
				if (true == cReSuccFlag) {
					LoggerUtils.d("111 提示请联系发卡行2");
					trans.setResultContent("交易拒绝:" + "请联系发卡行");
					DispOutICC(pubBean.getTransName(), "交易失败", "请联系发卡行");
					pubBean.setResponseCode("EC");//存在卡片拒绝发卡行等情况
					pubBean.setMessage("发卡行认证失败");
				} else {
					LoggerUtils.d("111 不提示请联系发卡行2");
						trans.setResultContent("交易拒绝:" + getEmvErrMsg());
						DispOutICC(pubBean.getTransName(), "交易失败",getEmvErrMsg());
				}
			}
			pubBean.setResponseCode("EC");//存在卡片拒绝发卡行等情况
			pubBean.setMessage("二次授权失败");
			return EmvResult.EMV_FAIL;
		} else if ("11".equals(responseCode)
				|| "A2".equals(responseCode)
				|| "A4".equals(responseCode)
				|| "A5".equals(responseCode)
				|| "A6".equals(responseCode)) // 特殊成功的提示
		{
			trans.displayResponse(true, pubBean.getResponseCode());
		}
		return 0;

	}

	/**
	 * 取EMV Pboc日志
	 * 
	 * @return
	 */
	public List<PbocDetailBean> emvGetPbocLog(PubBean pubBean) {

		List<PbocDetailBean> list ; 

		if (pubBean.getCardInputMode() == ReadcardType.ICCARD) {
			list = emvProcess.emvGetPbocLog(false);
		} else {
			list = emvProcess.emvGetPbocLog(true);
		}
		return list;
	}

	/**
	 * 取电子现金 圈存日志
	 * 
	 * @return
	 */
	public List<EcLoadBean> emvGetEcLoadLog(PubBean pubBean) {

		List<EcLoadBean> list ;

		if (pubBean.getCardInputMode() == ReadcardType.ICCARD) {
			list = emvProcess.emvGetEcLoadLog(false);
		} else {
			list = emvProcess.emvGetEcLoadLog(true);
		}

		return list;
	}

	/**
	 * @brief 检测是否是电子现金卡
	 */
	public boolean checkIsEC() {
		return emvProcess.checkIsEC();
	}

	/**
	 * @brief 纯电子 现金卡不允许进行联机交易
	 */
	public boolean checkIsOnlyEC() {
		return emvProcess.checkIsOnlyEC();
	}

	/**
	 * 检查非接是否需要联机pin
	 * 
	 * @return
	 */
	public boolean checkIsQpbocOnlinePin() {
		return emvProcess.checkIsQpbocOnlinePin();
	}

	/**
	 * 检查qPboc是否发生扣款
	 */
	public boolean checkIsPay() {
		return emvProcess.checkIsPay();
	}

	/**
	 * @brief 从标签读取主账号
	 * @param [in] char *pszPan
	 * @param [out] char *pszPan
	 * @return
	 * @li APP_SUCC
	 * @li APP_FAIL
	 **/

	public String getPanFrom5A() {
		return emvProcess.getPanFrom5A();
	}

	/**
	 * @brief 从标签读取二磁
	 * @return
	 **/

	public String getTrack2From57() {
		return emvProcess.getTrack2From57();
	}

	public void DispOutICC(String title, String content, String content2) {
		
		emvProcess.DispOutICC(title, content, content2);
	}

	public String getEmvErrMsg() {
		return emvProcess.getEmvErrMsg();
	}

	public EmvAppModule getEmvAppModule() {
		return emvAppModule;
	}
	
	private void showTipMsg(final String msg) {

		MainActivity.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtils.show(MainActivity.getInstance()
						.getApplicationContext(), msg);
			}
		});
	}


	/**
	 * @brief 检索国际组织代码
	 * @param in
	 *            pRid RID
	 * @param out
	 *            pOrg 国际组织代码
	 * @return
	 * @li APP_SUCC
	 * @li APP_FAIL
	 */
	public String getEmvOrgSheet() {
		return emvProcess.getEmvOrgSheet();
	}


	public String getMsgCheckEcOnly(int transType) {
		switch (transType) {
		case TransType.TRANS_SALE:
			return trans.getText(R.string.only_ec_sale);
		case TransType.TRANS_PREAUTH:
		case TransType.TRANS_AUTHSALE:
		case TransType.TRANS_AUTHSALEOFF:
			return trans.getText(R.string.only_ec_auth);
		default:
			return trans.getText(R.string.only_ec_decline);
		}
	}

	public boolean checkInputPin() {
		return emvProcess.checkInputPin();
	}
}
