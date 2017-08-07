package com.newland.payment.trans;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;

import com.newland.base.util.AndroidTools;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.SoundUtils;
import com.newland.base.util.ToastUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.Const.ElecSignType;
import com.newland.payment.common.Const.SoundType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransConst.ReverseReasonCode;
import com.newland.payment.common.TransConst.SettlementTableTypeConst;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.DuplicatedScriptResultException;
import com.newland.payment.mvc.model.BlackCard;
import com.newland.payment.mvc.model.CardBinA;
import com.newland.payment.mvc.model.CardBinB;
import com.newland.payment.mvc.model.CardBinC;
import com.newland.payment.mvc.model.EmvFailWater;
import com.newland.payment.mvc.model.ReverseWater;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.BlackCardService;
import com.newland.payment.mvc.service.ReverseWaterService;
import com.newland.payment.mvc.service.ScriptResultService;
import com.newland.payment.mvc.service.SettlementService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.BlackCardServiceImpl;
import com.newland.payment.mvc.service.impl.CardBinAServiceImpl;
import com.newland.payment.mvc.service.impl.CardBinBServiceImpl;
import com.newland.payment.mvc.service.impl.CardBinCServiceImpl;
import com.newland.payment.mvc.service.impl.ReverseWaterServiceImpl;
import com.newland.payment.mvc.service.impl.ScriptResultServiceImpl;
import com.newland.payment.mvc.service.impl.SettlementServiceImpl;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.bean.CommHead;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.trans.bean.NewRefundBean;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.SignatureBean;
import com.newland.payment.trans.bean.TransResultBean;
import com.newland.payment.trans.bean.TransStepBeanException;
import com.newland.payment.trans.bean.field.ISOField44;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.impl.ElecSignSend;
import com.newland.payment.trans.impl.LogOut;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.trans.impl.Reverse;
import com.newland.payment.trans.impl.ScriptResultAdvise;
import com.newland.payment.trans.impl.Settle;
import com.newland.payment.trans.impl.VoidPartialSale;
import com.newland.payment.trans.impl.elecash.QPbocSale;
import com.newland.payment.trans.impl.offline.SendOffline;
import com.newland.payment.trans.impl.payQuery;
import com.newland.payment.trans.manage.impl.EmvParamsDownload;
import com.newland.payment.trans.manage.impl.ParamsTransmit;
import com.newland.payment.trans.manage.impl.StatusSend;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.fragment.BaseFragment;
import com.newland.payment.ui.fragment.BillFragment;
import com.newland.payment.ui.fragment.CardFragment;
import com.newland.payment.ui.fragment.ChooseDateFragment;
import com.newland.payment.ui.fragment.CommunicationFragment;
import com.newland.payment.ui.fragment.CommunicationFragment2;
import com.newland.payment.ui.fragment.ConfirmInfoFragment;
import com.newland.payment.ui.fragment.EcLoadLogListFragment;
import com.newland.payment.ui.fragment.InputInfoFragment;
import com.newland.payment.ui.fragment.InputMoneyFragment;
import com.newland.payment.ui.fragment.InputPinFragment;
import com.newland.payment.ui.fragment.InputPinOfflineFragment;
import com.newland.payment.ui.fragment.MenuSelectFragment;
import com.newland.payment.ui.fragment.MessageTipFragment;
import com.newland.payment.ui.fragment.NewBalanceReadCardFragment;
import com.newland.payment.ui.fragment.NewConsumeInfoFragment;
import com.newland.payment.ui.fragment.NewInputInfoFragment;
import com.newland.payment.ui.fragment.NewInputPinFragment;
import com.newland.payment.ui.fragment.NewRefundReadCardFragment;
import com.newland.payment.ui.fragment.NewSaleReadCardFragment;
import com.newland.payment.ui.fragment.NewVoidSaleReadCardFragment;
import com.newland.payment.ui.fragment.OperatorPasswordFragment;
import com.newland.payment.ui.fragment.PbocLogListFragment;
import com.newland.payment.ui.fragment.PrintFragment;
import com.newland.payment.ui.fragment.ProgressFragment;
import com.newland.payment.ui.fragment.SignatureFragment;
import com.newland.payment.ui.listener.OnDateEnterListener;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.bean.EcLoadBean;
import com.newland.pos.sdk.bean.EmvBean;
import com.newland.pos.sdk.bean.MenuSelectBean;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.bean.PasswordBean;
import com.newland.pos.sdk.bean.PbocDetailBean;
import com.newland.pos.sdk.card.CardModule;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.common.TransConst.StepResult;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.ISO8583;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TLVUtils;
import com.newland.pos.sdk.util.TimeUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 业务执行的抽象层
 * @author checnkh
 * @date 2015-05-06
 * @edit by linchunhui at 2015-05-07 添加了异步转同步的代码
 * @edit by linchunhui at 2015-05-07 下午 取消了 ITrans的接口
 *
 */
@SuppressLint("DefaultLocale")
public abstract class AbstractBaseTrans {
	/**
	 * 结束流程：单步业务返回
	 */
	public final static int FINISH = -10000;
	
	/**
	 * 成功
	 */
	public final static int SUCC = 0;
	
	/**
	 * 流程继续 
	 */
	public final static int STEP_CONTINUE = 1;
	
	/**
	 * 步骤结束,与FINISH同效果
	 * 定义这个变量,用于dealPackAndComm过滤出网络连接失败的错误
	 */
	public final static int STEP_FINISH = -10;

	/**
	 * 按取消返回,这个用于连续上送时,按取消退出上送
	 */
	public final static int STEP_CANCEL = -2;
	
	
	/**
	 * 是否检查签到
	 */
	protected boolean checkSingIn;
	/**
	 * 是否检查交易笔数上限
	 */
	protected boolean checkWaterCount;
	/**
	 * 是否检查结算状态
	 */
	protected boolean checkSettlementStatus;
	/**
	 * 是否检查存在流水
	 */
	protected boolean checkIsExistWater;
	/**
	 * 是否验证主管密码
	 */
	protected boolean checkManagerPassword;
	
	/**
	 * 交易结束是否检卡
	 */
	protected boolean checkCardExsit;
	/**
	 * 交易是否开启事务（独占底层资源）
	 */
	protected boolean transcationManagerFlag;
	
	/**
	 * 交易开启事务超时时间
	 */
	protected int transcationTimeOut = 180;

	/** 输金额 */
	private InputMoneyFragment inputMoneyFragment;
	/** 输密 */
	private InputPinFragment passwordFragment;
	/** 输密(脱机) */
	private InputPinOfflineFragment passwordOfflineFragment;
	/** 检卡 */
	private CardFragment cardFragment;
	/** 显示结果 */
	private BillFragment billFragment;
	/** 通讯界面 */
	private CommunicationFragment communicationFragment;
	/** 通讯界面 */
	private CommunicationFragment2 communicationFragment2;
	/** 信息输入 */
	private InputInfoFragment inputInfoFragment;
	/** 信息提示 */
	private MessageTipFragment messageTipFragment;
	/** 菜单选择 */
	private MenuSelectFragment menuSelectFragment;
	/** 电子签名 */
	private SignatureFragment signatureFragment;
	/** 信息确认 */
	private ConfirmInfoFragment confirmInfoFragment;
	/** 加油卡信息 */
	private NewConsumeInfoFragment consumeInfoFragment;

	protected TransResultBean transResultBean;
	protected ReverseWaterService reverseWaterService = null;
	protected ScriptResultService scriptResultService = null;
	
	protected CommunicationBean communicationBean = null;
	
	protected MainActivity activity;
	

	/**
	 * 交易过程中fragment放入栈中管理
	 */
	public static int TRANS_FRAGMENT_COUNT = 0;
	
	protected static ISO8583 iso8583 = new ISO8583(App.getInstance().getApplicationContext());
	protected static ISO8583 mposIso8583 = new ISO8583(App.getInstance().getApplicationContext());
	static{
		try {
			iso8583.loadXmlFile(Const.FileConst.CUPS8583);
			mposIso8583.loadXmlFile(Const.FileConst.MPOSCUPS8583);
		} catch (Exception e) {
			LoggerUtils.e("初始化 iso8583失败");
			e.printStackTrace();
		}
	}
	
	protected PubBean pubBean;
	
	protected TransStepProvider stepProvider = new TransStepProvider(this);
	/**
	 * 暂停步骤，需要等待过程方法调用回调函数，参见 {@link TransStepListener}
	 */
	protected void stopStep(BaseBean bean) {
		try {
			synchronized(bean.getWaitObj()) {
				bean.getWaitObj().wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 继续步骤，用于dialog界面调用
	 */
	protected void goOnStep(BaseBean bean) {
		try {
			synchronized(bean.getWaitObj()) {
				bean.getWaitObj().notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void initPubBean(){
		pubBean.setTraceNo(ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_TRACENO));
		pubBean.setCurrentOperNo(App.USER.getUserNo());
		pubBean.setPosID(ParamsUtils.getPosId());
		pubBean.setShopID(ParamsUtils.getShopId());
		
		pubBean.setDate(TimeUtils.getCurrentDate());
		pubBean.setTime(TimeUtils.getCurrentTime());
		
		//初始化消息ID、交易处理码、服务条件码
		pubBean.setMessageID(getMessageID(pubBean.getTransType()));
		pubBean.setProcessCode(getProcessCode(pubBean.getTransType()));
		pubBean.setServerCode(getF25ServerCode(pubBean.getTransType()));
	}
	
	public void initManagePubBean(){
		pubBean.setPosID(ParamsUtils.getPosId());
		pubBean.setShopID(ParamsUtils.getShopId());
		pubBean.setMessageID(getMessageID(pubBean.getTransType()));
	}
	
	public CommunicationBean initCommunicationBean(){
		CommunicationBean communicationBean = new CommunicationBean();
		//communicationBean.setHttps("POST /mjc/webtrans/VPB_lb HTTP/1.1\r\nHOST: 145.4.206.244:5000\r\nUser-Agent: Donjin Http 0.1\r\nCache-Control: no-cache\r\nContent-Type:x-ISO-TPDU/x-auth\r\nAccept: */*\r\nContent-Length: 93\r\n\r\n");
		String tpdu = null;
		if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 0){
			tpdu = ParamsUtils.getString(ParamsConst.PARAMS_KEY_CDMA_TPDU);
		}else if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 1){
			tpdu = ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_TPDU);		
		}else{
			tpdu = ParamsUtils.getString(ParamsConst.PARAMS_KEY_WIFI_TPDU);			
		}
		communicationBean.setTpdu(tpdu);
		communicationBean.setHead(new CommHead(activity).getString());
		if (pubBean.getTransType() == TransType.TRANS_MPOS_DOWN)
			communicationBean.setMposDwnm(true);
		else
			communicationBean.setMposDwnm(false);
		return communicationBean;
	}
	
	
	/**
	 * 初始化交易参数
	 * <li>-1: 初始化异常 </li>
	 */
	protected int init() {
		pubBean = new PubBean();
		transResultBean = new TransResultBean();
		reverseWaterService = new ReverseWaterServiceImpl(activity);
		scriptResultService = new ScriptResultServiceImpl(activity);
		return 0;
	}
	
	/**
	 * 释放资源
	 */
	protected void release() {
		LoggerUtils.d("111 " + getClass().getSimpleName()+"->release()");
//		pubBean = null;
		transResultBean = null;
		reverseWaterService = null;
		/** 交易结束检查是否未拔卡 */
		if (checkCardExsit){
LoggerUtils.d("111 检查卡片是否存在");			
			transEndCheckCardExist();
		}
		
		backToTransMenu();
		
	}
	
	/**
	 * 权限检查
	 * @return
	 */
	protected void checkPower() {
		this.activity = MainActivity.getInstance();
		this.checkSingIn = true; 
		this.checkWaterCount = true;
		this.checkSettlementStatus = true;
		this.checkIsExistWater = false;
		this.checkManagerPassword = false;
		this.checkCardExsit = true;
		this.transcationManagerFlag = true; // 默认开启事务，管理类交易可不开启事务
	}
	
	/**
	 * 锁对象
	 * 当value值为1时，解锁
	 * 确保上锁之后再解锁
	 * @author chenkh
	 * @date 2015-6-15
	 * @time 下午2:38:31
	 */
	private class LockObj {
		public int value;
		public Object object;
		public LockObj(){
			this.value = 0;
			this.object = new Object();
		}
	}

	/**
	 * 返回菜单，并且移除栈中界面
	 */
	public void backToTransMenu() {
		final LockObj lockObj = new LockObj();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					LoggerUtils.d("交易结束，返回主菜单，fragment num ："+TRANS_FRAGMENT_COUNT  +" AbstractBaseTrans:"+AbstractBaseTrans.this);
					if ((AbstractBaseTrans.this instanceof EmvParamsDownload
							|| AbstractBaseTrans.this instanceof ParamsTransmit
							|| AbstractBaseTrans.this instanceof StatusSend)
							&& activity.isThirdInvoke() ) {
						//第三方下载EMV参数
						LoggerUtils.d("lxb    1111111");
						if (TRANS_FRAGMENT_COUNT>0) {
							activity.popBackFragment(TRANS_FRAGMENT_COUNT);
						} 
						LoggerUtils.e(" 第三方调用，参数自动下载后...开始交易");
						activity.doThirdInvoke();
				
					}else if (AbstractBaseTrans.this instanceof Login
						&& TimeUtils.getCurrentDate().equals(ParamsUtils.getString(ParamsConst.PARAMS_RUN_LOGIN_DATE))
						&& ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN)) {
					
						if (TRANS_FRAGMENT_COUNT>0) {
							LoggerUtils.d("lxb    444444");
							activity.popBackFragment(TRANS_FRAGMENT_COUNT);
						} 
						if (!activity.isThirdInvoke()) {
							LoggerUtils.d("lxb    555555");
							LoggerUtils.e("非第三方调用...返回主菜单");
							activity.returnFirstMainMenu();
						} else if(activity.getTransType() != TransType.TRANS_LOGIN){
							LoggerUtils.d("lxb 第三方调用不为签到...开始交易");
							LoggerUtils.e("第三方调用不为签到...开始交易");
							activity.doThirdInvoke();
						}

					} else if((AbstractBaseTrans.this instanceof LogOut || AbstractBaseTrans.this instanceof Settle) 
								&& !ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN )){
						//签退成功
						if (TRANS_FRAGMENT_COUNT>0) {
							activity.popBackFragment(TRANS_FRAGMENT_COUNT);
						} 
						if (!activity.isThirdInvoke()) {
							// 非第三方调用，返回登陆界面
							activity.returnLogin(false);
						}
					} else {
						/** 除签到成功之外，其他交易结束时返回交易入口的fragment界面 */
						if (TRANS_FRAGMENT_COUNT>0) {
							activity.popBackFragment(TRANS_FRAGMENT_COUNT);
							BaseFragment topFragment = (BaseFragment) AndroidTools.getTopFragment(activity);
							if (topFragment==null && !activity.isThirdInvoke()) {
								// 非第三方调用，且栈顶无界面时，强制退回主菜单
								
								activity.returnFirstMainMenu();
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				TRANS_FRAGMENT_COUNT = 0; // 置0
				try {
					LoggerUtils.d("完成界面结束");
					synchronized (lockObj.object) {
						lockObj.value = 1;
						lockObj.object.notify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			LoggerUtils.d("开始界面退出");
			synchronized (lockObj.object) {
				if (lockObj.value != 0) {
					
				} else {
					lockObj.object.wait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LoggerUtils.e("完成交易......");
	}
	
	
	/**
	 * 交易页面切换，并且计数
	 */
	public void transSwitchContent(final Fragment fragment) {
		LoggerUtils.d("222 transSwitchContent");
		if (activity == null) {
			activity = MainActivity.getInstance();
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.switchContent(fragment);
				TRANS_FRAGMENT_COUNT++; // 
			}
		});
	}
	
	
	/**
	 * 执行输入金额
	 * @param amountBean
	 * @return
	 */
	public AmountBean showUIInputAmount(AmountBean amountBean){
		inputMoneyFragment = InputMoneyFragment.newInstance(amountBean);
		transSwitchContent(inputMoneyFragment);
		stopStep(amountBean);
		return amountBean;
	}
	public CommonBean<PubBean> showConsumeInfo(CommonBean<PubBean> CommonBean){
		consumeInfoFragment = NewConsumeInfoFragment.newInstance(CommonBean);
		transSwitchContent(consumeInfoFragment);
		stopStep(CommonBean);
		return CommonBean;
	}

	/**
	 * 执行读卡操作
	 */
	protected CardBean showReadCard(CardBean swipeCardBean){
		if (swipeCardBean.getTranstype() == EmvConst.EMV_TRANS_RF_SALE) {	// 消费
			
		}
		
		this.cardFragment =  CardFragment.newInstance(swipeCardBean);
		transSwitchContent(cardFragment);
		stopStep(swipeCardBean);
		return swipeCardBean;
	}
	
	protected CardBean showReadCard(CardBean swipeCardBean, PubBean pubBean) {
		LoggerUtils.d("tradeType = "+pubBean.getTransType());
		Fragment readCardFragment = null;
		switch (pubBean.getTransType()) {
			case TransType.TRANS_SALE:	// 消费
				readCardFragment =  NewSaleReadCardFragment.newInstance(swipeCardBean,pubBean);
				break;
				
			case TransType.TRANS_BALANCE:	// 余额查询
				readCardFragment = NewBalanceReadCardFragment.newInstance(swipeCardBean);
				break;
				
			case TransType.TRANS_VOID_SALE:	// 撤销
				readCardFragment = NewVoidSaleReadCardFragment.newInstance(swipeCardBean,pubBean);
				break;

			default:
				readCardFragment = CardFragment.newInstance(swipeCardBean);
				break;
		}
		
		transSwitchContent(readCardFragment);
		stopStep(swipeCardBean);
		return swipeCardBean;
	}

	/**
	 * 退货交易读卡
	 * @param refundBean
     */
	protected void showReadForRefund(NewRefundBean refundBean) {
		transSwitchContent(NewRefundReadCardFragment.newInstance(refundBean));
		stopStep(refundBean.getCardBean());
	}
	
	/**
	 * 信息输入
	 * @param inputInfoBean
	 * @return
	 */
	protected InputInfoBean showUIInputInfo(InputInfoBean inputInfoBean){
//		this.inputInfoFragment = InputInfoFragment.newInstance(inputInfoBean);
//		transSwitchContent(inputInfoFragment);
		transSwitchContent(NewInputInfoFragment.newInstance(inputInfoBean));
		stopStep(inputInfoBean);
		return inputInfoBean;
	}
	
	/**
	 * 菜单选择框UI
	 * @param menuSelectBean
	 * @return
	 */
	public MenuSelectBean showUIMenuSelect(final MenuSelectBean menuSelectBean){
		menuSelectFragment = MenuSelectFragment.newInstance(menuSelectBean);
		transSwitchContent(menuSelectFragment);
		stopStep(menuSelectBean);
		return menuSelectBean;
	}
	
	/**
	 * 消息提示UI
	 * @param msgTipBean
	 * @return
	 */
	public MessageTipBean showUIMessageTip(final MessageTipBean msgTipBean){
		this.messageTipFragment = MessageTipFragment.newInstance(msgTipBean);
		if (msgTipBean.getContent() != null) {
			transSwitchContent(messageTipFragment);	
			stopStep(msgTipBean);
		}
		return msgTipBean;
	}
	
	/**
	 * 消息提示UI,
	 * @param msgTipBean
	 * @param isAddCount 是否计算该fragment个数
	 * @return
	 */
	public MessageTipBean showUIMessageTip(final MessageTipBean msgTipBean, boolean isAddCount){
		if (msgTipBean.getContent() == null) {
			return msgTipBean;
		}
		this.messageTipFragment = MessageTipFragment.newInstance(msgTipBean);
		if (isAddCount) {
			transSwitchContent(messageTipFragment);	
		} else {
			if (activity == null) {
				activity = MainActivity.getInstance();
			}
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					activity.switchContent(messageTipFragment);
				}
			});
		}
		stopStep(msgTipBean);
		return msgTipBean;
	}

	/**
	 * 执行通讯交互
	 */
	protected CommunicationBean showUICommunication(CommunicationBean communicationBean){		
		communicationFragment = CommunicationFragment.newInstance(communicationBean);
		transSwitchContent(communicationFragment);	
		stopStep(communicationBean);
		return communicationBean;
	}
	protected CommunicationBean showUICommunication2(CommunicationBean communicationBean){
		communicationFragment2 = CommunicationFragment2.newInstance(communicationBean);
		transSwitchContent(communicationFragment2);
		stopStep(communicationBean);
		return communicationBean;
	}
	/**
	 * 执行输密操作（脱机）
	 */
	public PasswordBean showUIPinOfflineInput(PasswordBean passwordBean){
		this.passwordOfflineFragment =  InputPinOfflineFragment.newInstance(passwordBean);
		transSwitchContent(passwordOfflineFragment);
		stopStep(passwordBean);
		return passwordBean;
	}
	
	/**
	 * 执行输密操作
	 */
	public PasswordBean showUIPinInput(PasswordBean passwordBean){
//		this.passwordFragment = InputPinFragment.newInstance(passwordBean);
		NewInputPinFragment passwordFragment =  NewInputPinFragment.newInstance(passwordBean);
		transSwitchContent(passwordFragment);
		stopStep(passwordBean);
		return passwordBean;
	}
	
	/**
	 * 
	 * @param comBean
	 * @return
	 */
	protected CommonBean<List<PbocDetailBean>> showUIPbocLog( CommonBean<List<PbocDetailBean>> comBean) {
		PbocLogListFragment fragment = PbocLogListFragment.newInstance(comBean);
		transSwitchContent(fragment);
		stopStep(comBean);
		return comBean;
	}
	
	protected CommonBean<List<EcLoadBean>> showUIEcLoadLog( CommonBean<List<EcLoadBean>> comBean) {
		EcLoadLogListFragment fragment = EcLoadLogListFragment.newInstance(comBean);
		transSwitchContent(fragment);
		stopStep(comBean);
		return comBean;
	}
	
	/**
	 * 执行显示流水
	 */
	protected TransResultBean showUITransResult(TransResultBean transResultBean) {
		this.billFragment =  BillFragment.newInstance(transResultBean);
		transSwitchContent(billFragment);
		stopStep(transResultBean);
		return transResultBean;
	}
	
	/**
	 * 执行信息确认
	 */
	protected CommonBean<Water> showUIConfirmInfo(CommonBean<Water> bean) {
		
		this.confirmInfoFragment = new ConfirmInfoFragment(bean);
		transSwitchContent(confirmInfoFragment);
		stopStep(bean);
		return bean;
	}
	
	/**
	 * 签名视图
	 * @param signatureBean
	 * @return
	 */
	protected SignatureBean showUISignatureView(final SignatureBean signatureBean){
		this.signatureFragment = SignatureFragment.newInstance(signatureBean);
		transSwitchContent(signatureFragment);
		stopStep(signatureBean);
		return signatureBean;
	}
	
	/**
	 * 快速支付 
	 * @return
	 */
	protected boolean doQpboc(PubBean pubBean){
		// 脚本执行结果上送
		final CommonBean<PubBean> commonBean = new CommonBean<PubBean>();
		commonBean.setValue(pubBean);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new QPbocSale(commonBean), new TransResultListener() {
					@Override
					public void succ() {
						LoggerUtils.d("lxb QPbocSale Succ");
						//commonBean.setResult(true);
					}
					
					@Override
					public void fail(String message) {
						LoggerUtils.d("lxb QPbocSale Fail");
						//commonBean.setResult(false);
					}
				});
			}
		});
		
		stopStep(commonBean);
//		//TODO
//		if (pubBean.getResponseCode() == null ) {
//			commonBean.setResult(false);
//		} else if (pubBean.getResponseCode().equals("00")) {
//			commonBean.setResult(true);
//		}
//		LoggerUtils.d("lxb getPubBean111:" + pubBean.getResponseCode());
		
		return commonBean.getResult();
	}
	
	
	/**
	 * 末笔交易上送
	 * @return
	 */
//	protected boolean doSendInfo(PubBean pubBean){
//		// 脚本执行结果上送
//		final CommonBean<PubBean> commonBean = new CommonBean<PubBean>();
//		commonBean.setValue(pubBean);
//		activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				TransController transController = new TransController(activity);
//				transController.start(new SendInfo(commonBean), new TransResultListener() {
//					@Override
//					public void succ() {
//						LoggerUtils.d("lxb SendInfo Succ");
//					}
//					
//					@Override
//					public void fail(String message) {
//						LoggerUtils.d("lxb SendInfo Fail");
//					}
//				});
//			}
//		});
//		stopStep(commonBean);
//		return commonBean.getResult();
//	}
	
	
	/** 
	 * 脚本执行结果上送
	 * <li>true-上送正常完成,交易继续</li>
	 * <li>false-上送连后台失败，交易结束</li>
	 */
	protected boolean doScriptAdvise(){
		// 脚本执行结果上送
		final CommonBean<Integer> commonBean = new CommonBean<Integer>();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new ScriptResultAdvise(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	
	/** 
	 * 执行冲正 
	 * @return 
	 * <li>true-冲正正常完成,交易继续</li>
	 * <li>false-冲正连后台失败，交易结束</li>
	 */
	protected boolean doReverseWater(){
		final CommonBean<Integer> commonBean = new CommonBean<Integer>();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new Reverse(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	
	/**
	 * 上送签名
	 * @param commonBean
	 * @param fileName
	 * @return
	 */
	protected boolean doElecSignSend(Integer elecSignSendType){
		final CommonBean<Integer> commonBean = new CommonBean<Integer>();
		commonBean.setValue(elecSignSendType);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new ElecSignSend(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	
	/**
	 * 处理签名板流程
	 * @param water
	 * @return
	 */
	protected boolean doElecSign(Water water){
		if(water == null){
			return false;
		}
		boolean supportSignature = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_ELECSIGN);
		if(!supportSignature){
			return true;
		}
		int timeOut = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_ELEC_TIMEOUT);
		
		String block1 = water.getSettleDate();
		String block2 = water.getReferNum();
		LoggerUtils.d("电子签名特征码：block1=" + block1 + "block2=" + block2);
		
		String signatureCode = getFeatureCode(block1, block2);
		
		SignatureBean signatureBean = new SignatureBean();
		signatureBean.setTimeOut(timeOut);
		signatureBean.setTraceCode(water.getTrace());
		signatureBean.setSignatureCode(signatureCode);
		signatureBean = showUISignatureView(signatureBean);
		LoggerUtils.d("signature result:" + signatureBean.getStepResult());
		
		switch (signatureBean.getStepResult()) {
		case SUCCESS:
			//是否输入手机号
			if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_ELECSIGN_INPUT_PHONE, false)){
				InputInfoBean inputInfoBean = new InputInfoBean();
				inputInfoBean.setTitle(getText(R.string.elec_sign_title));
				inputInfoBean.setContent(getText(R.string.common_pls_input)
						+getText(R.string.appoint_phone_no));
				inputInfoBean.setMinLen(11);
				inputInfoBean.setMaxLen(11);
				inputInfoBean.setEmptyFlag(true);
				inputInfoBean.setMode(InputInfoBean.INPUT_MODE_NUMBER);
				
				// 执行输入业务流程
				inputInfoBean = showUIInputInfo(inputInfoBean);
				// 对业务处理结果进行判断	
				if (StepResult.SUCCESS == inputInfoBean.getStepResult()){
					if (!StringUtils.isEmpty(inputInfoBean.getResult())){
						LoggerUtils.d("电子签名输入手机号:" + inputInfoBean.getResult());
						water.setCardHolderTelphone(inputInfoBean.getResult());
					}
				}
			}
						
			LoggerUtils.d("设置流水签名标志");
			water.setSignatureFlag(true);
			try {
				updateWater(water);
			} catch (Exception e) {
				LoggerUtils.e("update water fial:" + water +" traceNo:" + water.getTrace());
				e.printStackTrace();
			}
			transResultBean.setWater(water);
			//电子签名未上送笔数加1
			incElecSignUnSendNum();
			return true;
			
		default:
			return false;
		}
	}
	
	/**
	 * 电子签名版获取特征码
	 * 
	 * @param strDate
	 * @param strRefNum
	 * @return
	 */
	private String getFeatureCode(String strDate, String strRefNum) {
		byte[] data = new byte[16 + 1];
		int i;

		if (StringUtils.isEmpty(strDate)) {
			strDate = "0000";
		}
		if (StringUtils.isEmpty(strRefNum)) {
			strRefNum = "000000000000";
		}

		byte[] date = strDate.getBytes();
		byte[] refNum = strRefNum.getBytes();

		Arrays.fill(data, (byte) 0x00);
		System.arraycopy(date, 0, data, 0, date.length);
		System.arraycopy(refNum, 0, data, 4, refNum.length);

		StringBuilder result = new StringBuilder();
		
		for (i = 0; i < 8; i++) {
			data[i] = (byte) ((data[i] ^ data[i + 8]) & 0xFF);
			result.append(String.format("%X", data[i]));
		}
		return result.toString();
	}
	
	/**
	 * 离线上送
	 * @return
	 */
	protected boolean doOfflineSend(){
		final CommonBean<Integer> commonBean = new CommonBean<Integer>();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new SendOffline(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	 
	/**
	 * 部分扣款撤销
	 * @param commonBean
	 */
	protected boolean doVoidPartialSale(PubBean pubBean){
		final CommonBean<PubBean> commonBean = new CommonBean<PubBean>();
		commonBean.setValue(pubBean);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new VoidPartialSale(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	
	/**
	 * 获取通讯界面提示信息，不重写该方法，为默认提示语
	 * @return
	 */
	protected String[] getCommunitionTipMsg(){
		return null;
	}
	
	/**
	*  打包、通讯、解包
	* @author linld
	* @date 2015-05-21
	*/
	@SuppressWarnings("incomplete-switch")
	protected int dealPackAndComm(boolean isAddMac, boolean isReversal, boolean isCheckResp) {
		//打包
		String requst = null;
		try {
			requst = iso8583.pack();
			if (isAddMac){
				// 替换64域
				requst = replaceMac(requst);
			}
		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack) + e.getMessage());
			return TransConst.STEP_FINAL;
		}
		if (isReversal){
			// 保存冲正数据
			try {
				LoggerUtils.d("dd 保存冲正流水---------------");
				addReverseWater(getReverseWater(pubBean));
			} catch (Exception e1) {
				LoggerUtils.e("保存冲正数据异常");
				transResultBean.setIsSucess(false);
				transResultBean.setContent("FAIL:"+e1.getMessage());
				return TransConst.STEP_FINAL;
			}
		}
		// 流水号增1
		addTraceNo();
		
		/** 通讯 */
		communicationBean = initCommunicationBean();
		communicationBean.setData(requst);
		communicationBean.setContent(getCommunitionTipMsg());
		communicationBean = showUICommunication(communicationBean);
		switch(communicationBean.getStepResult()) {
		case BACK:
			LoggerUtils.d("通讯BACK");
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.result_trans_cancel));
			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
				//连接出错
				if (isReversal){
					LoggerUtils.d("dd 连接失败,back,删除冲正流水---");
					deleteReverse();
				}
			}
			return TransConst.STEP_FINAL;
				
		case FAIL:
			LoggerUtils.d("通讯Fail");
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.comm_fail));
			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
				transResultBean.setContent(getText(R.string.comm_connect_fail));
				//连接出错
				if (isReversal){
					LoggerUtils.d("dd 连接失败,删除冲正流水-------");
					deleteReverse();
				}
				return STEP_FINISH; 
			}
			
			//接收失败,冲正不上送9F36
			if (isReversal){
				if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
					String field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
					reverseWaterService.changeField55(field55);
				}
			}
			return TransConst.STEP_FINAL;
			
		case TIME_OUT:
			LoggerUtils.d("通讯timeOut");
			// 超时的业务
			transResultBean.setContent(getText(R.string.comm_connect_timeout));
			//超时,冲正不上送9F36
			if (isReversal){
				if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
					String field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
					reverseWaterService.changeField55(field55);
				}
			}
			showToast(getText(R.string.comm_connect_timeout));
			return FINISH;
			
		case SUCCESS:
			LoggerUtils.d("通讯succ");
			if (isReversal){
				// 收到应答，冲正改06
				LoggerUtils.d("111 修改冲正应答码为06");
				reverseWaterService.changeField39Result(ReverseReasonCode.OTHER_REASON);
			}
			//包校验,解包，存流水，删冲正
			try {
				iso8583.initPack();
				iso8583.unpack(communicationBean.getData());
				int resCode = checkRespons(iso8583, pubBean);
				switch (resCode) {
				case 0:
					// 正常情况
					break;
				case 1:
					String data = communicationBean.getData();
					String resMac = data.substring(data.length()-16);
					String mac = getMac(data.substring(0, data.length()-16));
					if (!resMac.equals(mac)) {
						//MAC错 需要重新自动签到
						ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
						
						// MAC校验不对
						if (isReversal){
							String field55 = null;
							String field61 = null;
							//MAC错,55域不上送9F36
							if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
								field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
							}
							if (pubBean.getTransType() == TransType.TRANS_VOID_PREAUTH
								|| pubBean.getTransType() == TransType.TRANS_AUTHSALE
								|| pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE){
								//冲正61.3原交易日期,取后台回的交易日期,lld,2015-10-20
								ReverseWater reverseWater = reverseWaterService.getReverseWater();
								int len = reverseWater.getField61().length();				
								LoggerUtils.d("111 冲正原61域值[" + reverseWater.getField61() + "],长度[" + len + "]");
								LoggerUtils.d("111 后台回的交易日期[" + pubBean.getDate() + "]");
								if (len >= 12){
									field61 = reverseWater.getField61().substring(0, 12) + pubBean.getDate();
								}
								
								if (len > 16){
									field61 += reverseWater.getField61().substring(16, len);
								}
								LoggerUtils.d("111 冲正最终上送61域:" + field61);
							}
							reverseWaterService.changeReverseWater(ReverseReasonCode.MAC_ERROR, field55, field61);
						}
						transResultBean.setIsSucess(false);
						transResultBean.setContent(getText(R.string.response_check_fail_mac_error));
						return TransConst.STEP_FINAL;
					} 
					break;
				default:
					// 校验返回数据其他情况
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getErrorInfo(resCode));
					return TransConst.STEP_FINAL;
				}
				if (!StringUtils.isNullOrEmpty(pubBean.getIsoField63())){
					if(pubBean.getIsoField63().length()<3){
						pubBean.setInternationOrg("   ");
					} else{
						pubBean.setInternationOrg(pubBean.getIsoField63().substring(0, 3));
					}
						
				} else {
					pubBean.setInternationOrg("   ");
				}
				if (isCheckResp){
					String responseCode = pubBean.getResponseCode();
					LoggerUtils.d("111 响应码[" + responseCode+ "]");
					if("00".equals(responseCode) 
//							||"11".equals(responseCode)
							|| "A2".equals(responseCode)
							|| "A4".equals(responseCode)
							|| "A5".equals(responseCode)
							|| "A6".equals(responseCode) ){
						
						transResultBean.setIsSucess(true);
						displayResponse(true, pubBean);
					} else { 
						//收到应答码,不冲正,删除冲正数据
						if (isReversal){
							reverseWaterService.delete();
						}							 
						transResultBean.setIsSucess(false);
						
						displayResponse(false, pubBean);
						return TransConst.STEP_FINAL;
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.common_receive_exception));
				return TransConst.STEP_FINAL;
			}
			break;  //如果不是最后一步，可不处理，直接跳转下一步处理。
		}
		return STEP_CONTINUE;
	}
	/**
	 *  打包、通讯、解包
	 * @author linld
	 * @date 2015-05-21
	 */
	@SuppressWarnings("incomplete-switch")
	protected int dealPackAndComm2(boolean isAddMac, String requst, boolean isCheckResp) {
		//打包

		try {
			if (isAddMac){
				// 替换64域
				LoggerUtils.d("hjh  replaceMac requst:"+requst);
				requst +="|"+replaceMac2(StringUtils.strToHex(requst));
				requst = String.format("%04d", requst.length())+ requst;
				LoggerUtils.d("hjh  replaceMac requst:"+requst);
			}
		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack) + e.getMessage());
			return TransConst.STEP_FINAL;
		}
		addTraceNo();
		/** 通讯 */
		communicationBean = initCommunicationBean();
		communicationBean.setData(requst);
		communicationBean.setContent(getCommunitionTipMsg());
		communicationBean = showUICommunication2(communicationBean);
		switch(communicationBean.getStepResult()) {
			case BACK:
				LoggerUtils.d("通讯BACK");
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.result_trans_cancel));
				return TransConst.STEP_FINAL;
			case FAIL:
				LoggerUtils.d("通讯Fail");
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.comm_fail));
				if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					return STEP_FINISH;
				}

				return TransConst.STEP_FINAL;

			case TIME_OUT:
				LoggerUtils.d("通讯timeOut");
				// 超时的业务
				transResultBean.setContent(getText(R.string.comm_connect_timeout));
				showToast(getText(R.string.comm_connect_timeout));
				return FINISH;

			case SUCCESS:
				LoggerUtils.d("通讯succ  data:"+communicationBean.getData());
				//TODO 返回报文处理
				break;  //如果不是最后一步，可不处理，直接跳转下一步处理。
		}
		return STEP_CONTINUE;
	}


	protected int dealPackAndComm3(boolean isAddMac, boolean isReversal, boolean isCheckResp) {
		//打包
		String requst = null;
		try {
			requst = mposIso8583.pack();
			if (isAddMac){
				// 替换64域
				requst = replaceMac(requst);
			}
		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack) + e.getMessage());
			return TransConst.STEP_FINAL;
		}
		if (isReversal){
			// 保存冲正数据
			try {
				LoggerUtils.d("dd 保存冲正流水---------------");
				addReverseWater(getReverseWater(pubBean));
			} catch (Exception e1) {
				LoggerUtils.e("保存冲正数据异常");
				transResultBean.setIsSucess(false);
				transResultBean.setContent("FAIL:"+e1.getMessage());
				return TransConst.STEP_FINAL;
			}
		}
		// 流水号增1
		addTraceNo();

		/** 通讯 */
		communicationBean = initCommunicationBean();
		communicationBean.setData(requst);
		communicationBean.setContent(getCommunitionTipMsg());
		communicationBean = showUICommunication(communicationBean);
		switch(communicationBean.getStepResult()) {
			case BACK:
				LoggerUtils.d("通讯BACK");
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.result_trans_cancel));
				if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
					//连接出错
					if (isReversal){
						LoggerUtils.d("dd 连接失败,back,删除冲正流水---");
						deleteReverse();
					}
				}
				return TransConst.STEP_FINAL;

			case FAIL:
				LoggerUtils.d("通讯Fail");
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.comm_fail));
				if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					//连接出错
					if (isReversal){
						LoggerUtils.d("dd 连接失败,删除冲正流水-------");
						deleteReverse();
					}
					return STEP_FINISH;
				}

				//接收失败,冲正不上送9F36
				if (isReversal){
					if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
						String field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
						reverseWaterService.changeField55(field55);
					}
				}
				return TransConst.STEP_FINAL;

			case TIME_OUT:
				LoggerUtils.d("通讯timeOut");
				// 超时的业务
				transResultBean.setContent(getText(R.string.comm_connect_timeout));
				//超时,冲正不上送9F36
				if (isReversal){
					if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
						String field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
						reverseWaterService.changeField55(field55);
					}
				}
				showToast(getText(R.string.comm_connect_timeout));
				return FINISH;

			case SUCCESS:
				LoggerUtils.d("通讯succ");
				if (isReversal){
					// 收到应答，冲正改06
					LoggerUtils.d("111 修改冲正应答码为06");
					reverseWaterService.changeField39Result(ReverseReasonCode.OTHER_REASON);
				}
				//包校验,解包，存流水，删冲正
				try {
					mposIso8583.initPack();
					mposIso8583.unpack(communicationBean.getData());
					int resCode = checkRespons(mposIso8583, pubBean);
					switch (resCode) {
						case 0:
							// 正常情况
							break;
						case 1:
							String data = communicationBean.getData();
							String resMac = data.substring(data.length()-16);
							String mac = getMac(data.substring(0, data.length()-16));
							if (!resMac.equals(mac)) {
								//MAC错 需要重新自动签到
								ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);

								// MAC校验不对
								if (isReversal){
									String field55 = null;
									String field61 = null;
									//MAC错,55域不上送9F36
									if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
										field55 = new EmvApplication(this).getEmvAppModule().packReversalField55(false);
									}
									if (pubBean.getTransType() == TransType.TRANS_VOID_PREAUTH
											|| pubBean.getTransType() == TransType.TRANS_AUTHSALE
											|| pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE){
										//冲正61.3原交易日期,取后台回的交易日期,lld,2015-10-20
										ReverseWater reverseWater = reverseWaterService.getReverseWater();
										int len = reverseWater.getField61().length();
										LoggerUtils.d("111 冲正原61域值[" + reverseWater.getField61() + "],长度[" + len + "]");
										LoggerUtils.d("111 后台回的交易日期[" + pubBean.getDate() + "]");
										if (len >= 12){
											field61 = reverseWater.getField61().substring(0, 12) + pubBean.getDate();
										}

										if (len > 16){
											field61 += reverseWater.getField61().substring(16, len);
										}
										LoggerUtils.d("111 冲正最终上送61域:" + field61);
									}
									reverseWaterService.changeReverseWater(ReverseReasonCode.MAC_ERROR, field55, field61);
								}
								transResultBean.setIsSucess(false);
								transResultBean.setContent(getText(R.string.response_check_fail_mac_error));
								return TransConst.STEP_FINAL;
							}
							break;
						default:
							// 校验返回数据其他情况
							transResultBean.setIsSucess(false);
							transResultBean.setContent(getErrorInfo(resCode));
							return TransConst.STEP_FINAL;
					}
					if (!StringUtils.isNullOrEmpty(pubBean.getIsoField63())){
						if(pubBean.getIsoField63().length()<3){
							pubBean.setInternationOrg("   ");
						} else{
							pubBean.setInternationOrg(pubBean.getIsoField63().substring(0, 3));
						}

					} else {
						pubBean.setInternationOrg("   ");
					}
					if (isCheckResp){
						String responseCode = pubBean.getResponseCode();
						LoggerUtils.d("111 响应码[" + responseCode+ "]");
						if("00".equals(responseCode)
//							||"11".equals(responseCode)
								|| "A2".equals(responseCode)
								|| "A4".equals(responseCode)
								|| "A5".equals(responseCode)
								|| "A6".equals(responseCode) ){

							transResultBean.setIsSucess(true);
							displayResponse(true, pubBean);
						} else {
							//收到应答码,不冲正,删除冲正数据
							if (isReversal){
								reverseWaterService.delete();
							}
							transResultBean.setIsSucess(false);

							displayResponse(false, pubBean);
							return TransConst.STEP_FINAL;
						}
					}

				}catch(Exception e){
					e.printStackTrace();
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.common_receive_exception));
					return TransConst.STEP_FINAL;
				}
				break;  //如果不是最后一步，可不处理，直接跳转下一步处理。
		}
		return STEP_CONTINUE;
	}

	/**
	 * 凭证号增加1
	 * @return
	 */
	public synchronized void addTraceNo(){
		String traceNo = ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_TRACENO);
		int currenNo = 1;
		try{
			currenNo = Integer.parseInt(traceNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		int nextNo = (currenNo+1)%1000000;
		ParamsUtils.setString( 
				ParamsConst.PARAMS_KEY_BASE_TRACENO, String.format("%06d", nextNo));
	}
	
	/**
	 * 加密磁道数据
	 * @param trackData
	 * @return
	 */
	protected String encryptTrackData(String track) {
		if (StringUtils.isNullOrEmpty(track)) {
			return null;
		}
//		if(track.length() % 2 != 0){
		LoggerUtils.d("444 磁道加密前：" + track);
		int len = (track.length() + 1)/2;
		int end = (len - 1)*2;
		int start =(len - 1 - 8)*2;
		LoggerUtils.d("444 len:" + len + ",start:" + start + ", end:" + end);
		if(start <= 0){
			LoggerUtils.e("encryptTrackData length error:" + track);
			return null;
		}
		String TDB = track.substring(start, end);
		LoggerUtils.d("444 TDB:" + TDB);
		SecurityModule sm = SecurityModule.getInstance();
		//设置主密钥索引
		sm.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex());
		String ENC_TDB = sm.des3(Const.WorkKeyType.TDK, TDB);
		
		String encTrack = track.substring(0, start) + ENC_TDB + track.substring(end);
		LoggerUtils.i("444 TDB :" + TDB);
		LoggerUtils.i("444 ENC_TDB :" + ENC_TDB);
		LoggerUtils.i("444 track :" + track);
		LoggerUtils.i("444 encTrack :" + encTrack);
		
		return encTrack;
	}
	
	/** F53 安全控制信息 */
	protected String getSecctrlInfo(PubBean pubBean){
		String secctrlInfo = "";
		if ('2' == (pubBean.getInputMode().charAt(2))){
			// pin不出现
			secctrlInfo += "0";
		}else {
			secctrlInfo += "2";
		}
		// 1-双倍长，0-单倍长
		if (Const.DesMode.DES3.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))){
			secctrlInfo += "6";
		} else {
			secctrlInfo += "0";
		}
		// 1-加密，0-不支持加密
		if ("1".equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))
				&& (!StringUtils.isEmpty(pubBean.getTrackData2()) || 
						!StringUtils.isEmpty(pubBean.getTrackData3()))){
			if (pubBean.getTransType() == TransType.TRANS_EC_LOAD
				||	(pubBean.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND && pubBean.getCardInputMode() != ReadcardType.SWIPE)){
				//指定账户圈存不上送磁道信息，所以不置磁道加密位
				//非指定账户圈存，转出卡为IC卡时，不上送磁道，不置加密位
				secctrlInfo += "0";
			} else {
				secctrlInfo += "1";
			}	
		} else {
			secctrlInfo += "0";
		}
		secctrlInfo += "0000000000000";
		return secctrlInfo;
	}
	
	/** 保存冲正 */
	protected void addReverseWater(ReverseWater reverseWater)throws Exception{
		if (reverseWater!=null){
			reverseWaterService.add(reverseWater);
			LoggerUtils.d("冲正保存成功");
		} else {
			throw new TransStepBeanException("冲正流水为空");
		}
	}
	
	/** 修改冲正55域内容*/
	public void changeReverseField55(String field55){
		if (null != field55){
			reverseWaterService.changeField55(field55);
		}
	}
	
	/**
	 * 获取冲正
	 */
	protected ReverseWater getReverseWater(PubBean pubBean){
		ReverseWater reverseWater = new ReverseWater();
		String field61 = "";
		try {
			reverseWater.setTransType(pubBean.getTransType());
			reverseWater.setTransAttr(pubBean.getTransAttr());
			reverseWater.setPan(pubBean.getPan());
			reverseWater.setProcCode(pubBean.getProcessCode());
			reverseWater.setAmount(pubBean.getAmount());
			reverseWater.setTrace(pubBean.getTraceNo());
			reverseWater.setExpDate(pubBean.getExpDate());
			reverseWater.setInputMode(pubBean.getInputMode());
			reverseWater.setCardSerialNo(pubBean.getCardSerialNo());
			reverseWater.setServerCode(pubBean.getServerCode());
			reverseWater.setOldAuthCode(pubBean.getOldAuthCode());
			reverseWater.setResponse(TransConst.ReverseReasonCode.NO_ANSWER);
			reverseWater.setInputModeForTransIn(pubBean.getInputModeForTransIn());
			reverseWater.setCurrencyCode(pubBean.getCurrency());
			reverseWater.setField55(pubBean.getIsoField55());
			reverseWater.setField60(pubBean.getIsoField60().getString());
			//冲正61域
			if (pubBean.getTransType() == TransType.TRANS_VOID_SALE){
//					|| pubBean.getTransType() == TransType.TRANS_VOID_INSTALMENT
//					|| pubBean.getTransType() == TransType.TRANS_VOID_BONUS_IIS_SALE
//					|| pubBean.getTransType() == TransType.TRANS_VOID_BONUS_ALLIANCE
//					|| pubBean.getTransType() == TransType.TRANS_VOID_APPOINTMENT_SALE
//					|| pubBean.getTransType() == TransType.TRANS_VOID_ORDER_SALE){
				//61.1取原撤消交易请求的批次号
				//61.2取原撤消交易请求的流水号11域
				field61 = pubBean.getIsoField60().getBatchNum() + pubBean.getTraceNo();

			} else if (pubBean.getTransType() == TransType.TRANS_VOID_PREAUTH
					|| pubBean.getTransType() == TransType.TRANS_AUTHSALE
					|| pubBean.getTransType() == TransType.TRANS_VOID_AUTHSALE){
//					|| pubBean.getTransType() == TransType.TRANS_VOID_ORDER_PREAUTH
//					|| pubBean.getTransType() == TransType.TRANS_ORDER_AUTHSALE
//					|| pubBean.getTransType() == TransType.TRANS_VOID_ORDER_AUTHSALE){
				//61.1取原撤消交易请求的批次号
				//61.2取原撤消交易请求的流水号11域
				//61.3取原预授权撤消交易请求交易日期为0000
				field61 = pubBean.getIsoField60().getBatchNum() + pubBean.getTraceNo() + "0000";
			} else {
				field61 = pubBean.getIsoField61();
			}
			reverseWater.setField61(field61);
			// 附件数据在每个交易中单独设置
//			reverseWater.setAddition();
		}catch (Exception e) {
			LoggerUtils.e("冲正转化异常！");
			e.printStackTrace();
			return null;
		}
		return reverseWater;
	}
	
	protected boolean reverseToPubBean(ReverseWater reverseWater, PubBean pubBean){
		
		pubBean.setTransType(reverseWater.getTransType());
		pubBean.setTransAttr(reverseWater.getTransAttr());
		pubBean.setPan(reverseWater.getPan());
		pubBean.setProcessCode(reverseWater.getProcCode());
		pubBean.setAmount(reverseWater.getAmount());
		pubBean.setTraceNo(reverseWater.getTrace());
		pubBean.setExpDate(reverseWater.getExpDate());
		pubBean.setInputMode(reverseWater.getInputMode());
		pubBean.setCardSerialNo(reverseWater.getCardSerialNo());
		pubBean.setServerCode(reverseWater.getServerCode());
		pubBean.setOldAuthCode(reverseWater.getOldAuthCode());
		pubBean.setResponseCode(reverseWater.getResponse());
		pubBean.setInputModeForTransIn(reverseWater.getInputModeForTransIn());
		pubBean.setCurrency(reverseWater.getCurrencyCode());
		pubBean.setIsoField55(reverseWater.getField55());
		pubBean.setIsoField60(new ISOField60(reverseWater.getField60()));
		pubBean.setIsoField61(reverseWater.getField61());
		
		return true;
	}
	
	
	protected boolean scriptToPubBean(ScriptResult scriptWater, PubBean pubBean){
		
		pubBean.setTransType(scriptWater.getTransType());
		pubBean.setTransAttr(scriptWater.getTransAttr());
		pubBean.setPan(scriptWater.getPan());
		LoggerUtils.d("111 脚本结果通知3域[" + scriptWater.getProcCode() + "]");
		pubBean.setProcessCode(scriptWater.getProcCode());
		if (scriptWater.getAmount() != null){
		pubBean.setAmount(scriptWater.getAmount());
		}
		pubBean.setInputMode(scriptWater.getInputMode());
		pubBean.setCardSerialNo(scriptWater.getCardSerialNo());
		pubBean.setServerCode(scriptWater.getServerCode());
		pubBean.setAcqCenterCode(scriptWater.getAcqCenterCode());
		pubBean.setSystemRefNum(scriptWater.getRefNum());
		LoggerUtils.d("111 脚本结果通知上送38域：" + scriptWater.getAuthCode());
		pubBean.setOldAuthCode(scriptWater.getAuthCode());
		pubBean.setCurrency(scriptWater.getCurrency());
		pubBean.setIsoField55(scriptWater.getField55());
		LoggerUtils.d("111 脚本上送设置60域:" + scriptWater.getIsoField60().getString());
		pubBean.setIsoField60(scriptWater.getIsoField60());
		pubBean.setIsoField61(scriptWater.getField61());
		
		return true;
	}
	
	
	/** 保存交易流水 */
	protected void addWater(Water water) throws Exception{
		if (water!=null) {
			WaterService waterService = new WaterServiceImpl(activity);
			waterService.addWater(water);
			LoggerUtils.i("流水保存成功！");
		} else {
			throw new TransStepBeanException("交易流水为空");
		}
	}
	
	/** 更新交易流水 */
	protected void updateWater(Water water) throws Exception{
		if (water!=null) {
			WaterService waterService = new WaterServiceImpl(activity);
			waterService.updateWater(water);
			LoggerUtils.i("更新交易流水成功！");
		} else {
			throw new TransStepBeanException("交易流水为空");
		}
	}
	
	/**
	 * 获取交易流水
	 * @param pubBean
	 * @return
	 */
	public Water getWater(PubBean pubBean){
		Water water = new Water();
		try {
			if(pubBean.getDiscountAmount()!=null){
				water.setDiscountAmount(pubBean.getDiscountAmount());
			}
			if(pubBean.getOrderAmount()!=null){
				water.setOrderAmount(pubBean.getOrderAmount());
			}
			water.setTransType(pubBean.getTransType());
			water.setOldTransType(pubBean.getOldTransType()); 
			water.setTransAttr(pubBean.getTransAttr());
			water.setTransStatus(0);
			water.setEmvStatus(pubBean.getEmv_Status());
			water.setPan(pubBean.getPan());
			water.setAmount(pubBean.getAmount());
			water.setTrace(pubBean.getTraceNo());
			water.setTime(pubBean.getTime());
			water.setDate(pubBean.getDate());
			water.setExpDate(pubBean.getExpDate());
			water.setSettleDate(pubBean.getSettleDate());
			water.setInputMode(pubBean.getInputMode());
			water.setCardSerialNo(pubBean.getCardSerialNo());
			if (null != pubBean.getTrackData2()){
				water.setTrack2(pubBean.getTrackData2());
			}
			
			if (null != pubBean.getTrackData3()){
				water.setTrack3(pubBean.getTrackData3());
			}
			water.setReferNum(pubBean.getSystemRefNum());
			
			
			if (StringUtils.isDigital(pubBean.getAuthCode())) {
				LoggerUtils.d("lxb getAuthCode222:" + pubBean.getAuthCode());
				water.setAuthCode(pubBean.getAuthCode());
			}
			
			water.setRespCode(pubBean.getResponseCode());
			if(pubBean.getIsoField60() != null && pubBean.getIsoField60().getString().length() >= 8) {
				water.setBatchNum(pubBean.getIsoField60().getBatchNum());
				water.setFuncCode(pubBean.getIsoField60().getFuncCode());
			}
			if (!StringUtils.isEmpty(pubBean.getIsoField61())) {
				String field61 = pubBean.getIsoField61();
				if (field61.length()>=6) {
					water.setOldBatch(field61.substring(0, 6));
				}
				if (field61.length()>=12) {
					water.setOldTrace(field61.substring(6, 12));
				}
			}
			water.setOldDate(pubBean.getOldDate());
			water.setOldAuthCode(pubBean.getOldAuthCode());  
			water.setOldRefNum(pubBean.getOldRefnum());
			water.setTelNo(pubBean.getMobileNo());
			water.setAppointmentNo(pubBean.getAppointmentNo());
			water.setOper(pubBean.getCurrentOperNo());
			water.setInterOrg(pubBean.getInternationOrg());
			if (pubBean.getIsoField44()!=null) {
				water.setIisCode(pubBean.getIsoField44().getIisCode());
				water.setAcqCode(pubBean.getIsoField44().getAcqCode());
				water.setCardType(pubBean.getIsoField44().getCardType());
			}
			water.setAcqCenterCode(pubBean.getAcqCenterCode());
			if (pubBean.getTipAmount()!=null)
				water.setTipAmount(pubBean.getTipAmount());
			if (pubBean.getIsoField63()!=null) {
				try{
					LoggerUtils.d("111 63域[" + pubBean.getIsoField63() + "],长度[" + pubBean.getIsoField63().length() + "]");
					byte[] btField63 = pubBean.getIsoField63().getBytes("GBK");
					LoggerUtils.d("111 63域[" + btField63 + "],长度[" + btField63.length + "]");
					if (btField63.length >= 23){
						LoggerUtils.d("111 63域发卡行信息[" + new String(btField63,3,20, "GBK") + "]");
						water.setIisInfo(new String(btField63, 3, 20, "GBK"));
					} else if (btField63.length > 3){
						LoggerUtils.d("111 63域发卡行信息[" + new String(btField63,3,btField63.length-3, "GBK") + "]");
						water.setIisInfo(new String(btField63, 3, btField63.length-3, "GBK"));
				}
					if (btField63.length >= 43){
						LoggerUtils.d("111 63域银联信息[" + new String(btField63, 23, 20, "GBK") + "]");
						water.setCupInfo(new String(btField63, 23, 20, "GBK"));
					} else if (btField63.length > 23){
						LoggerUtils.d("111 63域银联信息[" + new String(btField63, 23, btField63.length-23, "GBK") + "]");
						water.setCupInfo(new String(btField63, 23, btField63.length-23, "GBK"));
				}
					if (btField63.length >= 63){
						LoggerUtils.d("111 63收单行信息[" + new String(btField63, 43, 20, "GBK") + "]");
						water.setMerAcqInfo(new String(btField63, 43, 20, "GBK"));
					} else if (btField63.length > 43){
						LoggerUtils.d("111 63收单行信息[" + new String(btField63, 43, btField63.length-43, "GBK") + "]");
						water.setMerAcqInfo(new String(btField63, 43, btField63.length-43, "GBK"));
				}
					if (btField63.length >= 123){
						LoggerUtils.d("111 63POS终端信息[" + new String(btField63, 63, 60, "GBK") + "]");
						water.setTermInfo(new String(btField63, 63, 60, "GBK"));
					} else if (btField63.length > 63){
						LoggerUtils.d("111 63POS终端信息[" + new String(btField63, 63, btField63.length-63, "GBK") + "]");
						water.setTermInfo(new String(btField63, 63, btField63.length-63, "GBK"));
					}
				}catch(UnsupportedEncodingException e){
					LoggerUtils.d("111 63域转码错误");
					e.printStackTrace();
				}
			}
			water.setOffSendFlag(0);
			water.setBatchUpFlag(0);
			water.setSignatureFlag(false);
			water.setSignSendFlag(0);
			
			if (null != pubBean.getOldTerminalId()){
				water.setOldTerminalId(pubBean.getOldTerminalId());
			}
			
			water.setField55(pubBean.getIsoField55());
//			water.setEmvAddition(pubBean.getEmvAddition()); //不在这里设置,emvAddition外面setEmvAdditionInfo补存
			
			//
			if(pubBean.getEcBalance() != null){
				water.setEcBalance(pubBean.getEcBalance());
			}
			
			
			//转入卡号
			if (pubBean.getCardNoTransIn() != null){
				water.setCardNoTransIn(pubBean.getCardNoTransIn());
			}
			
			// 附加数据在交易中单独设置
			water.setAddition("");
			water.setCurrency(pubBean.getCurrency());
			
			//外部订单号
			if (MainActivity.getInstance().isThirdInvoke()) {
				water.setOutOrderNo(MainActivity.getInstance().getOutOrderNo());	
			}
			
			//交易流水详情 1
			water.setTransCode(1);
			
		}catch (Exception e) {
			LoggerUtils.e("流水转化异常！");
			e.printStackTrace();
			return null;
		}
		return water;
	}
	
	/**
	 * 流水记录转化成系统数据,注意60域,61域得自己处理
	 * @param water
	 * @return 
	 */
	public boolean waterToPubBean(Water water, PubBean pubBean){
		try {
			pubBean.setTransType(water.getTransType());
			pubBean.setOldTransType(water.getOldTransType());
			pubBean.setTransAttr(water.getTransAttr());
			pubBean.setEmv_Status(water.getEmvStatus());
			pubBean.setPan(water.getPan());
			pubBean.setAmount(water.getAmount());
//			pubBean.setOldAmount(water.getAmount());
			pubBean.setTraceNo(water.getTrace());
			pubBean.setTime(water.getTime());
			pubBean.setDate(water.getDate());
			pubBean.setExpDate(water.getExpDate());
			pubBean.setSettleDate(water.getSettleDate());
			pubBean.setInputMode(water.getInputMode());
			pubBean.setCardSerialNo(water.getCardSerialNo());
			if (null != water.getTrack2()){
				pubBean.setTrackData2(water.getTrack2());
			}
			LoggerUtils.d("555 退货类流水36域[" + pubBean.getTrackData3() + "]");
			if (null != water.getTrack3()){
				pubBean.setTrackData3(water.getTrack3());
			}
			LoggerUtils.d("555 退货类36域2[" + pubBean.getTrackData3() + "]");
			pubBean.setSystemRefNum(water.getReferNum());
			pubBean.setAuthCode(water.getAuthCode());
			pubBean.setResponseCode(water.getRespCode());
			
			pubBean.setIsoField60(new ISOField60(water.getFuncCode() + water.getBatchNum()));

			
			pubBean.setIsoField61(water.getOldBatch() + water.getOldTrace());
			
			pubBean.setOldAuthCode(water.getOldAuthCode());  
			pubBean.setOldRefnum(water.getOldRefNum());
			pubBean.setMobileNo(water.getTelNo());
			pubBean.setAppointmentNo(water.getAppointmentNo());
			pubBean.setCurrentOperNo(water.getOper());
			pubBean.setInternationOrg(water.getInterOrg());
			
			ISOField44 isoField44 = new ISOField44();
			isoField44.setIisCode(water.getIisCode());
			isoField44.setAcqCode(water.getAcqCode());
			pubBean.setIsoField44(isoField44);
			pubBean.setIsoField55(water.getField55());
			
			String strField63 = water.getIisInfo() + water.getCupInfo() 
					+ water.getMerAcqInfo() + water.getTermInfo();
			pubBean.setIsoField63(strField63);
			
			pubBean.setOldDate(water.getOldDate());
			pubBean.setCurrency(water.getCurrency());
			if (null != water.getOldTerminalId()){
				pubBean.setOldTerminalId(water.getOldTerminalId());
			}
			
		}catch (Exception e) {
			LoggerUtils.e("数据转化异常！");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 流水记录转化成系统数据,注意60域,61域得自己处理
	 * @param
	 * @return 
	 */
	public boolean failWaterToPubBean(EmvFailWater failWater, PubBean pubBean){
		try {
			pubBean.setTransType(failWater.getTransType());
			pubBean.setTransAttr(failWater.getTransAttr());
			pubBean.setEmv_Status(failWater.getEmvStatus());
			pubBean.setPan(failWater.getPan());
			pubBean.setAmount(failWater.getAmount());
			LoggerUtils.d("111 失败交易流水号[" + failWater.getTrace() + "]");
			pubBean.setTraceNo(failWater.getTrace());
			pubBean.setTime(failWater.getTime());
			pubBean.setDate(failWater.getDate());
			pubBean.setExpDate(failWater.getExpDate());
			pubBean.setInputMode(failWater.getInputMode());
			pubBean.setCardSerialNo(failWater.getCardSerialNo());
			if (null != failWater.getTrack2()){
				pubBean.setTrackData2(failWater.getTrack2());
			}
			
			if (null != failWater.getTrack3()){
				pubBean.setTrackData3(failWater.getTrack3());
			}
			pubBean.setIsoField55(failWater.getField55());
							
			pubBean.setCurrentOperNo(failWater.getOper());
			pubBean.setInternationOrg(failWater.getInterOrg());
			
			pubBean.setCurrency(failWater.getCurrency());
			
		}catch (Exception e) {
			LoggerUtils.e("数据转化异常！");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
		
	
	/** 保存脚本 
	 * @throws DuplicatedScriptResultException */
	public void addScriptResult(ScriptResult scriptResult) {
			//ScriptResultService scriptResultService = new ScriptResultServiceImpl(activity);
			scriptResultService.addScriptResult(scriptResult);
			LoggerUtils.i("脚本保存成功！");
	}
	
	/** 更新脚本 */
	public void updateScriptResult(ScriptResult scriptResult) throws Exception{
		if (scriptResult!=null) {
			//ScriptResultService scriptResultService = new ScriptResultServiceImpl(activity);
			scriptResultService.updateScriptResult(scriptResult);
			LoggerUtils.i("更新交易脚本成功！");
		} else {
			throw new TransStepBeanException("交易脚本为空");
		}
	}
	
	
	
	/**
	 * 获取脚本通知结果
	 * @param pubBean
	 * @return
	 */
	public ScriptResult getScriptResult(PubBean pubBean){
		ScriptResult scriptResult = new ScriptResult();
		try {
			scriptResult.setTransType(pubBean.getTransType());
			scriptResult.setTransAttr(pubBean.getTransAttr());
			
			scriptResult.setPan(pubBean.getPan());
			scriptResult.setProcCode(pubBean.getProcessCode());
			if (pubBean.getAmount() != null
				&& pubBean.getAmount() != 0){
			scriptResult.setAmount(pubBean.getAmount());
			}
			
			scriptResult.setInputMode(pubBean.getInputMode());
			scriptResult.setCardSerialNo(pubBean.getCardSerialNo());
			scriptResult.setServerCode(pubBean.getServerCode());
			
			scriptResult.setAcqCenterCode(pubBean.getAcqCenterCode());
			scriptResult.setRefNum(pubBean.getSystemRefNum());
			LoggerUtils.d("111 脚本通知保存38域原授权码:" + pubBean.getAuthCode());
			scriptResult.setAuthCode(pubBean.getAuthCode());
			scriptResult.setCurrency(pubBean.getCurrency());
			scriptResult.setField55(pubBean.getIsoField55());
			LoggerUtils.d("111 脚本通知保存原交易60域[" + pubBean.getIsoField60().getString() + "]");
			scriptResult.setIsoField60(pubBean.getIsoField60());
			
			String field61 = "";
			field61 += pubBean.getIsoField60().getBatchNum();
			field61 += pubBean.getTraceNo();
			if (StringUtils.isNullOrEmpty(pubBean.getDate())){
				field61 += "0000";
			} else {
				field61 += pubBean.getDate();
			}
			LoggerUtils.d("111 脚本通知保存61域[" + field61 + "]");
			scriptResult.setField61(field61);
		}catch (Exception e) {
			LoggerUtils.e("脚本转化异常！");
			e.printStackTrace();
			return null;
		}
		return scriptResult;
	}
	/**
	 * 获取资源文本
	 * @param stringId
	 * @return
	 */
	public String getText(int stringId){
		return activity.getResources().getString(stringId);
	}
	
	/**
	 * 获取资源文本数组
	 * @param arrayId
	 * @return
	 */
	public String[] getStringArray(int arrayId) {
		return activity.getResources().getStringArray(arrayId);
	}
	/**
	 * 获取资源Int数组
	 * @param arrayId
	 * @return
	 */
	public int[] getIntArray(int arrayId) {
		return activity.getResources().getIntArray(arrayId);
	}
	/**
	 * 发送报文前计算并替换64域
	 * @param iso8583
	 * @return
	 */
	protected String replaceMac(String iso8583){
		String data = iso8583.substring(0, iso8583.length()-16);
		String mac = getMac(data);
		LoggerUtils.d("MAC    data"+data);
		LoggerUtils.d("MAC    "+mac);
		return data + mac;
	}
	protected String replaceMac2(String macData){
		LoggerUtils.d("MAC    macData"+macData);
		String mac = getMac2(macData);
		LoggerUtils.d("MAC    "+mac);
		return mac;
	}
	/**
	 * 报文计算mac
	 * @param iso8583
	 * @return
	 */
	protected String getMac(String iso8583){
		SecurityModule sm = SecurityModule.getInstance();
		// 设置主密钥索引
		sm.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex());
		String mac = sm.calcMac(Const.MacType.MAC_TYPE_9606, iso8583);
		return mac;
	}
	protected String getMac2(String iso8583){
		SecurityModule sm = SecurityModule.getInstance();
		// 设置主密钥索引
		sm.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex()+1);
		String mac = sm.calcMac(Const.MacType.MAC_TYPE_9606, iso8583);
		return mac;
	}
	public static final int MUST_CHECK_MAC = 1;//有MAC值，必须校验MAC
	public static final int MSG_ID_ERR = -1; //返回消息类型不符
	public static final int PROC_CODE_ERR = -2;//返回处理码不符
	public static final int AMOUNT_ERR = -3;//返回金额不符
	public static final int TRACE_NO_ERR = -4;//返回流水号不符
	public static final int SERVER_CODE_ERR = -5;//服务点条件码不符
	public static final int ACQ_CENTER_CODE_ERR = -6;//未返回系统参考号
	public static final int REFNUM_ERR = -7;//未返回系统参考号
	public static final int POS_ID_ERR = -8;//返回终端号不符
	public static final int SHOP_ID_ERR = -9;//返回商户号不符
	public static final int CURRENCY_CODE_ERR = -10;//返回货币代码不符

	public int checkRespons(ISO8583 isoMsg, PubBean pubBean) throws UnsupportedEncodingException {
		String strFieldData = "";
		byte[] ReqMsgId;
		byte[] RespMsgId;
		byte[] bitmap = isoMsg.getBitmap();
		 
		RespMsgId = BytesUtils.hexToBytes(isoMsg.getField(0));
		ReqMsgId = BytesUtils.hexToBytes(pubBean.getMessageID());
		ReqMsgId[1] |= 0x10;
		if(!BytesUtils.compareBytes(RespMsgId, ReqMsgId)) {
			return MSG_ID_ERR;
		}
		
		for(int index=2; index<=bitmap.length; index++) {
			if(bitmap[index-1] == '0') {
				continue;
			}
			strFieldData = isoMsg.getField(index);
			switch(index) {
			case 2:
				if (!StringUtils.isNullOrEmpty(strFieldData)){
					pubBean.setPan(strFieldData); //卡号
				}
				break;
			case 3:
				if(!strFieldData.equals(pubBean.getProcessCode())) {
					return PROC_CODE_ERR;
				}
				break;
			case 4:
				long amount = Long.parseLong(strFieldData);
				if((isoMsg.getField(39) != null) && (isoMsg.getField(39).equals("10"))) {
					pubBean.setAmount(amount);
				}
				if(amount != pubBean.getAmount()) {
					return AMOUNT_ERR;
				}
				break;
			case 11:
				if(false == StringUtils.isDigital(strFieldData)) { /**<检查流水号的合法性*/
					continue;/**<如果流水号不是数字的就不比对，针对posp的错误*/
				}
				if(!strFieldData.equals(pubBean.getTraceNo())
						&& pubBean.getTransType() != TransType.TRANS_MPOS_DOWN) {
					return TRACE_NO_ERR;
				}
				break;
			case 12:
				pubBean.setTime(strFieldData);
				break;
			case 13:
				pubBean.setDate(strFieldData);
				break;
			case 14:
				pubBean.setExpDate(strFieldData);
				break;
			case 15:
				pubBean.setSettleDate(strFieldData);
				break;
			case 25:
				if(!strFieldData.equals(pubBean.getServerCode())) {
					return SERVER_CODE_ERR;
				}
				break;
			case 32:
				if(strFieldData.length() == 0) {
					return ACQ_CENTER_CODE_ERR;
				}
				pubBean.setAcqCenterCode(strFieldData);
				break;
			case 37:
				if(strFieldData.length() == 0) {
					return REFNUM_ERR;
				}
				pubBean.setSystemRefNum(strFieldData);
				break;
			case 38:
				pubBean.setAuthCode(strFieldData);
				break;
			case 39:
				pubBean.setResponseCode(strFieldData);
				if(strFieldData.equals("A0")){
					//MAC错 需要重新自动签到
					ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
				}
				break;
			case 41:
				if(!strFieldData.equals(pubBean.getPosID())
						&& pubBean.getTransType() != TransType.TRANS_MPOS_DOWN) {
					return POS_ID_ERR;
				}
				break;
			case 42:
				if(!strFieldData.equals(pubBean.getShopID())
						&& pubBean.getTransType() != TransType.TRANS_MPOS_DOWN) {
					return SHOP_ID_ERR;
				}
				break;
			case 44:
				if(strFieldData.length() >=22) {
					ISOField44 isoField44 = new ISOField44();
					isoField44.setField(strFieldData);
					pubBean.setIsoField44(isoField44);
				}
				break;
			case 49:
				if(!strFieldData.equals(pubBean.getCurrency())) {
					return CURRENCY_CODE_ERR;
				}
				break;
			case 63:
				LoggerUtils.d("111 63域解包[" + strFieldData + "]");
				pubBean.setIsoField63(strFieldData);
				break;
			case 64:
				return MUST_CHECK_MAC;
			default:
				break;
			}
		}
		return 0;
	}
	
	/** 获取错误信息 */
	protected String getErrorInfo(int resCode){
		switch (resCode) {
		case MSG_ID_ERR:
			return getText(R.string.response_check_fail_msg_type_error);
		case PROC_CODE_ERR:
			return getText(R.string.response_check_fail_process_code_error);
		case AMOUNT_ERR:
			return getText(R.string.response_check_fail_amount_error);
		case TRACE_NO_ERR:
			return getText(R.string.response_check_fail_trace_no_error);
		case SERVER_CODE_ERR:
			return getText(R.string.response_check_fail_sevice_code_error);
		case ACQ_CENTER_CODE_ERR:
			return getText(R.string.response_check_fail_no_acq_center_code_error);
		case REFNUM_ERR:
			return getText(R.string.response_check_fail_no_refnum_error);
		case POS_ID_ERR:
			return getText(R.string.response_check_fail_posid_error);
		case SHOP_ID_ERR:
			return getText(R.string.response_check_fail_shopid_error);
		case CURRENCY_CODE_ERR:
			return getText(R.string.response_check_fail_cerrency_error);
		default:
			// 校验返回数据其他情况
			return getText(R.string.response_check_fail_other_error);
		}
	}

	/** 显示进度条 */
	public void showUIProgressFragment(final String message){
		transSwitchContent(ProgressFragment.newInstance(message));
	}
	
	/** 打印 */
	public PrintBean showUIPrintFragment(PrintBean bean){
		final PrintFragment fragment = PrintFragment.newInstance(bean);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.switchContent(fragment);
			}
		});
		try {
			synchronized(bean.getWaitObj()) {
				bean.getWaitObj().wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/** Toast提示 */
	public void showToast(final String message){
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ToastUtils.show(activity, message);
			}
		});
	}
	
	/**
	 * 显示输入主管密码对话框UI
	 * @param commonBean
	 * @return
	 */
	protected CommonBean<Integer> showUIOperatorPasswordInput(final CommonBean<Integer> commonBean) {
		
		BaseFragment fragment = OperatorPasswordFragment.newInstance(commonBean);
		transSwitchContent(fragment);
		
		stopStep(commonBean);
		return commonBean;
	}
	
	/**
	 * 日期选择,没有年份
	 * @param dateBean
	 * @return
	 */
	public CommonBean<String> showUIDateChoose(final CommonBean<String> dateBean) {
		LoggerUtils.d("222 showUIDateChoose1");
		transSwitchContent(new ChooseDateFragment(dateBean, Const.DateWheelType.MOUTH|Const.DateWheelType.DAY, new OnDateEnterListener() {
			@Override
			public void onEnter(Long value) {
				dateBean.setResult(true);
				dateBean.setValue(value == null ? "" : TimeUtils.getFormatTime(
						value, "MMdd"));
				dateBean.setStepResult(StepResult.SUCCESS);
				goOnStep(dateBean);
			}
		}));
		LoggerUtils.d("222 showUIDateChoose2");
		stopStep(dateBean);
		
		return dateBean;
	}
	
	/**
	 * 年月选择
	 * @param dateBean
	 * @return
	 */
	protected CommonBean<String> showUIYearDateChoose(final CommonBean<String> dateBean) {
		
		transSwitchContent(new ChooseDateFragment(dateBean, Const.DateWheelType.MOUTH|Const.DateWheelType.YEAR, new OnDateEnterListener() {
			
			@Override
			public void onEnter(Long value) {
				
				dateBean.setResult(true);
				dateBean.setValue(value == null ? "" : TimeUtils.getFormatTime(
						value, "yyMM"));
				dateBean.setStepResult(StepResult.SUCCESS);
				goOnStep(dateBean);

			}
		}));
		stopStep(dateBean);
		return dateBean;
	}
	
	/**
	 * 处理磁道加密
	 * @author linld
	 * @date 2015-05-21
	 */
	public String dealTrack(int cardInputMode, String track){
		if (StringUtils.isEmpty(track)){
			return null;
		}
		LoggerUtils.d("444 磁道加密前：" + track);
		track = track.replace("=", "D");
		LoggerUtils.d("444 cardInputMode:" + cardInputMode);
		if (cardInputMode == ReadcardType.ICCARD || cardInputMode == ReadcardType.RFCARD){
			if(DesMode.DES3.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))
					&& Const.YES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))){
				LoggerUtils.d("444 进行磁道加密");
				track = encryptTrackData(track);
				LoggerUtils.d("444 磁道加密后：" + track);
			}
		}
		//}
		return track;
	}
	
	/**
	 *  处理刷卡方式
	 *  @author linld
	 *  @date 2015-05-21
	 */
	protected Integer dealCardInputMode(Integer inputMode){
		LoggerUtils.d(String.format("dealCardInputMode->in:%d", inputMode));
		if (ReadcardType.SWIPE == inputMode){
			return inputMode;
		}
		
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)){
			inputMode &= ~ReadcardType.ICCARD;
			inputMode &= ~ReadcardType.RFCARD;
		}
		
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_RF)){
			inputMode &= ~ReadcardType.RFCARD;
		}
		
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_CARD_INPUT)){
			inputMode &= ~ReadcardType.HAND_INPUT;
		}
		LoggerUtils.d(String.format("dealCardInputMode->out:%d", inputMode));
		return inputMode;
	}
	
	/**
	 * 交易结束提示拔卡
	 */
	protected void transEndCheckCardExist(){

LoggerUtils.d("111 transEndCheckCardExist->1");		
//		CardModule cardModule = CardModule.getInstance();
		CardModule cardModule = new CardModule(null, null);
		LoggerUtils.d("111 transEndCheckCardExist->2");				
		LoggerUtils.d("111 transEndCheckCardExist->3");				
		boolean isShowDialog = false;
		int checkResult = -1;
		do {
			LoggerUtils.d("111 transEndCheckCardExist->4");		
			/** 交易结束检卡类型参数 */
			checkResult = cardModule.checkCardIsExist();
			LoggerUtils.d("111 checkCardIsExist = " + checkResult);
			if (checkResult > 0) {
				LoggerUtils.d("111 检测到有卡");
				if (!isShowDialog) {
					showUIProgressFragment(getText(R.string.pull_card));
					isShowDialog = true;
				}
				if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) { // N900
					try{
					SoundUtils.getInstance().play(SoundType.BEEP); // 喇叭响
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
				} else { // im81
					PublicLibJNIService.jnisystimebeep(1, 200);// 蜂鸣器响
				}
				
			} else {
				LoggerUtils.d("111 没有检测到卡片");
				break;
			}
		} while (checkResult>0);
		LoggerUtils.d("交易结束提示拔卡方法执行完毕....");
	}
	
	/**
	 * 根据交易类型获取标题
	 * @param transType
	 * @return 标题
	 */
	protected String getTitle(int transType){
		switch (transType){
		case TransType.TRANS_EC_BALANCE:
		case TransType.TRANS_BALANCE: //余额查询
			return getText(R.string.common_check_balance);
		case TransType.TRANS_SALE:// 消费
			return getText(R.string.common_consumption);
		case TransType.TRANS_VOID_SALE:// 消费撤销
			return getText(R.string.common_revocation);
		case TransType.TRANS_REFUND:// 退货
			return getText(R.string.common_return_goods);
		case TransType.TRANS_PREAUTH:// 预授权
			return getText(R.string.common_pre_author);
		case TransType.TRANS_AUTHSALE:// 预授权完成请求
			return getText(R.string.common_pre_author_finish);
		case TransType.TRANS_AUTHSALEOFF:// 预授权完成通知
			return getText(R.string.common_pre_author_finish_notice);
		case TransType.TRANS_VOID_PREAUTH:// 预授权撤销
			return getText(R.string.common_pre_author_revocation);
		case TransType.TRANS_VOID_AUTHSALE:// 预授权完成撤销
			return getText(R.string.common_pre_author_finish_revocation);
			// 管理	
		case TransType.TRANS_LOGIN: //签到
			return getText(R.string.common_sign_in);
		case TransType.TRANS_CASHIER_LOGIN: //收银员积分签到
			return getText(R.string.common_operator_sign_in_score);
		case TransType.TRANS_LOGOUT: //签退
			return getText(R.string.common_sign_out);
		case TransType.TRANS_SETTLE://结算
			return getText(R.string.common_settlement);
			// 电子现金
		case TransType.TRANS_EC_PURCHASE://电子现金消费
			return getText(R.string.common_elec_cash) + getText(R.string.common_consumption);
		case TransType.TRANS_EC_LOAD_CASH://电子现金圈存现金
			return getText(R.string.trans_ecmoneyload);
		case TransType.TRANS_EC_LOAD_NOT_BIND://电子现金非指定账户圈存
			return getText(R.string.trans_ecnopointload);
		case TransType.TRANS_EC_LOAD://电子现金圈存
			return getText(R.string.trans_ecpointload);
		case TransType.TRANS_EC_VOID_LOAD_CASH://电子现金圈存现金撤销
			return getText(R.string.trans_ecvoidmoneyload);
		case TransType.TRANS_EMV_REFUND:
			return getText(R.string.offline_return);	
		case TransType.TRANS_INSERT_CARD_SALE:
			return getText(R.string.common_insert_consumption);
		case TransType.TRANS_MPOS_DOWN:
			return getText(R.string.setting_mpos_download);
		case TransType.TRANS_LOAD_LOAD:
			return getText(R.string.setting_mpos_load);
			default:
			return getText(R.string.trans_nodifine);
		}
		
	}
	
	/**
	 *  消息类型整理
	 *  @author linld
	 *  @date 2015-05-21
	 */
	protected String getMessageID(int transType){
		switch (transType){
			case TransType.TRANS_BALANCE:
			case TransType.TRANS_SALE:
			case TransType.TRANS_VOID_SALE:
			case TransType.TRANS_AUTHSALE:
			case TransType.TRANS_VOID_AUTHSALE:
			case TransType.TRANS_EC_PURCHASE:
			case TransType.TRANS_EC_LOAD_CASH:
			case TransType.TRANS_EC_LOAD_NOT_BIND:
			case TransType.TRANS_EC_LOAD:
			case TransType.TRANS_EC_VOID_LOAD_CASH:
				return "0200";
				
			case TransType.TRANS_PREAUTH:
			case TransType.TRANS_VOID_PREAUTH:
				return "0100";
				
			case TransType.TRANS_REFUND:
			case TransType.TRANS_AUTHSALEOFF:
			case TransType.TRANS_EMV_REFUND:
				return "0220";
				
			case TransType.TRANS_REVERSAL:
				return "0400";
				
			case TransType.TRANS_BATCHUP:
				return "0320";
				
			case TransType.TRANS_SCRIPT:
				return "0620";
				
			case TransType.TRANS_SETTLE:
				return "0500";
			
			case TransType.TRANS_LOGIN:
			case TransType.TRANS_PARAM_TRANSFER:
			case TransType.TRANS_MPOS_DOWN:
			case TransType.TRANS_LOAD_LOAD:
				return "0800";
				
			case TransType.TRANS_LOGOUT:
			case TransType.TRANS_CASHIER_LOGIN:
			case TransType.TRANS_STATUS_SEND:
			case TransType.TRANS_ECHO:
				return "0820";	
				
			default:
				return "0200";
		}
	}
		
	/**
	 *  交易处理码整理
	 *  @author linld
	 *  @date 2015-05-21
	 */
	public String getProcessCode(int transType){
		switch (transType) {
		case TransType.TRANS_BALANCE:
			return "310000";
			
		case TransType.TRANS_SALE:
		case TransType.TRANS_AUTHSALE:
		case TransType.TRANS_AUTHSALEOFF:
		case TransType.TRANS_EC_PURCHASE:
			return "000000";
			
		case TransType.TRANS_VOID_SALE:
		case TransType.TRANS_REFUND:
		case TransType.TRANS_VOID_AUTHSALE:
		case TransType.TRANS_VOID_PREAUTH:
		case TransType.TRANS_EMV_REFUND:
			return "200000";
			
		case TransType.TRANS_PREAUTH:
			return "030000";
			
		case TransType.TRANS_EC_LOAD_CASH:
			return "630000";
		
		case TransType.TRANS_EC_LOAD_NOT_BIND:
			return "620000";
			
		case TransType.TRANS_EC_LOAD:
			return "600000";
			
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			return "170000";
		default:
			return "000000";
		}
	}
	
	/**
	 * 根据交易类型或者25域服务点条件码
	 * @param transType
	 * @return 服务点条件码
	 */
	protected static String getF25ServerCode(int transType) {

		switch (transType) {
		case TransType.TRANS_BALANCE:
		case TransType.TRANS_SALE:
		case TransType.TRANS_VOID_SALE:
		case TransType.TRANS_REFUND:
		case TransType.TRANS_EC_PURCHASE:
		case TransType.TRANS_EMV_REFUND:	
			return "00";
			
		case TransType.TRANS_PREAUTH:
		case TransType.TRANS_AUTHSALE:
		case TransType.TRANS_VOID_AUTHSALE:
		case TransType.TRANS_VOID_PREAUTH:
		case TransType.TRANS_AUTHSALEOFF:
			return "06";
			

		case TransType.TRANS_EC_LOAD:
		case TransType.TRANS_EC_LOAD_NOT_BIND:
		case TransType.TRANS_EC_LOAD_CASH:
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			return "91";
			
		default:
			return "00";
		}
	}
	
	/**
	 * 根据交易类型获取60.1域消息类型码
	 * @param transType
	 * @return 消息类型码
	 */
	protected static String getF601FuncCode(int transType) {
		   
		switch (transType) {
		case TransType.TRANS_BATCHUP:
		case TransType.TRANS_SCRIPT:
		case TransType.TRANS_LOGIN:
		case TransType.TRANS_LOGOUT:
		case TransType.TRANS_SETTLE:
		case TransType.TRANS_CASHIER_LOGIN:
		case TransType.TRANS_PARAM_TRANSFER:
		case TransType.TRANS_STATUS_SEND:
		case TransType.TRANS_ECHO:
			return "00";
			
		case TransType.TRANS_BALANCE:	
			return "01";
			
		case TransType.TRANS_PREAUTH:
        	return  "10";
        	
   		case TransType.TRANS_VOID_PREAUTH:
       		return "11";
			
		case TransType.TRANS_AUTHSALE:
			return  "20";
		 
		case TransType.TRANS_VOID_AUTHSALE:
			return  "21";
			 
		case TransType.TRANS_SALE: 
		case TransType.TRANS_EC_PURCHASE:
			return  "22";
			
		case TransType.TRANS_VOID_SALE:	
			return  "23";
			
		case TransType.TRANS_AUTHSALEOFF:
			return  "24";
			
		case TransType.TRANS_REFUND:
			return  "25";
			
		case TransType.TRANS_EMV_REFUND:
			return "27";
			
		case TransType.TRANS_EC_LOAD:
			return "45";
			
		case TransType.TRANS_EC_LOAD_CASH:
			return "46";
			
		case TransType.TRANS_EC_LOAD_NOT_BIND:
			return "47";
			
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			return "51";
			
		default:
			return "00";
		}
	}
	
	/**
	 * 金额确认
	 * @author linld
	 * @date 2015-05-22
	 */
	protected boolean confirmAmount(String title, long amount){
		MessageTipBean tipBean = new MessageTipBean();
		tipBean.setTitle(title);
		StringBuffer sb = new StringBuffer();
		sb.append(getText(R.string.common_pls_confirm));
		sb.append(getText(R.string.bill_money));
		sb.append(":");
		sb.append(FormatUtils.formatAmount(String.valueOf(amount)));
		tipBean.setContent(sb.toString());
		tipBean.setCancelable(true);
		tipBean = showUIMessageTip(tipBean);
		
		if(!tipBean.getResult()){
			return false;
		}
		return true;
	}
	
	/**
	 * 退货金额限制
	 * @author linld
	 * @date 2015-05-22
	 */
	protected boolean checkRefundAmtLimit(String title, long amount){
		
		long maxAmount = ParamsUtils.getLong(ParamsConst.PARAMS_KEY_BASE_REFUNDAMOUNT, 10000L);
		if (amount > maxAmount){
			MessageTipBean tipBean = new MessageTipBean();
			tipBean.setTitle(title);
			tipBean.setContent(getText(R.string.error_refund_amount_limit));
			tipBean.setCancelable(false);		
			tipBean = showUIMessageTip(tipBean);
	
			return false;
		}
		
		return true;
	}	
	


	public PubBean getPubBean() {
		return pubBean;
	}
	
	
	
	/**
	 * 脱机消费 上送打包
	 * @author linld
	 * @param water
	 * @param type 0-离线上送, 1-批上送
	 * @return
	 */
	protected boolean packSaleOffline(Water water, int type){
	

		pubBean.setTransType(TransType.TRANS_SALE);
		initPubBean();
		waterToPubBean(water, pubBean);
		
		if (1 == type){
			//批上送
			pubBean.setMessageID("0320");
		}
		ISOField60 isoField60 = new ISOField60(water.getTransType(), false);
		//离线交易填36,lld,2015-10-23
		isoField60.setFuncCode("36");
		isoField60.setPartSaleflag("");
		pubBean.setIsoField60(isoField60);
		
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(2, pubBean.getPan());
		iso8583.setField(3, pubBean.getProcessCode());
		iso8583.setField(4, String.format("%012d", water.getAmount()));
		iso8583.setField(11, water.getTrace());
		if (water.getInputMode().startsWith("01")){
			if (null != water.getExpDate() && water.getExpDate().length() > 0){
				iso8583.setField(14, water.getExpDate());
			}
		}
		iso8583.setField(22, water.getInputMode());
LoggerUtils.d("111 离线上送22域值[" + water.getInputMode() + "]");			
		if (!StringUtils.isNullOrEmpty(pubBean.getCardSerialNo())){
LoggerUtils.d("111 离线上送23域值[" + pubBean.getCardSerialNo() + "]");			
			iso8583.setField(23, pubBean.getCardSerialNo());
		}
LoggerUtils.d("111 离线上送25域值[" + pubBean.getServerCode() + "]");		
		iso8583.setField(25, pubBean.getServerCode());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(49, water.getCurrency());
		iso8583.setField(55, water.getField55());
		iso8583.setField(60, pubBean.getIsoField60().getString());
		iso8583.setField(63, water.getInterOrg());
		LoggerUtils.d("111 packSaleOffline InternationOrg:" + water.getInterOrg());
		if (0 == type){
			iso8583.setField(64, "0");
		}
		
		return true;
	}

	/**
	 * 退货 打包
	 * @author linld
	 * @param pubBean
	 * @param type 0-交易打包, 1-批上送
	 * @return
	 */
	protected boolean packRefund(PubBean pubBean, int type){
		
		
		if (1 == type){
			//批上送
			pubBean.setMessageID("0320");
		}
		
		/** 组包*/
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		if (!pubBean.getInputMode().startsWith("02")){
			iso8583.setField(2, pubBean.getPan());
		}
		iso8583.setField(3, pubBean.getProcessCode());
		iso8583.setField(4, String.format("%012d",pubBean.getAmount()));
		iso8583.setField(11, pubBean.getTraceNo());
		if (!StringUtils.isEmpty(pubBean.getExpDate())) {
			iso8583.setField(14, pubBean.getExpDate());
		}
		iso8583.setField(22, pubBean.getInputMode());
		
		if (!StringUtils.isEmpty(pubBean.getCardSerialNo())){
			iso8583.setField(23, pubBean.getCardSerialNo());
		}
		
		iso8583.setField(25, pubBean.getServerCode());
		
		if (pubBean.getTrackData2() != null) {
			iso8583.setField(35, pubBean.getTrackData2());
		}
		if (pubBean.getTrackData3() != null) {
			iso8583.setField(36, pubBean.getTrackData3());
		}
		if (pubBean.getTransType() != TransType.TRANS_EMV_REFUND){
			iso8583.setField(37, pubBean.getOldRefnum());
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
			iso8583.setField(53, getSecctrlInfo(pubBean));
		}
		iso8583.setField(60, pubBean.getIsoField60().getString());

		iso8583.setField(61, pubBean.getIsoField61());
		if (pubBean.getTransType() == TransType.TRANS_EMV_REFUND){
			iso8583.setField(62, pubBean.getIsoField62());
		}
		iso8583.setField(63, pubBean.getIsoField63());
		
		if (0 == type){
			iso8583.setField(64, "0");
		}
		
		return true;
	}
	
	/**
	 * 预授权完成通知 打包
	 * @author linld
	 * @param pubBean
	 * @param type 0-交易打包, 1-批上送
	 * @return
	 */
	protected boolean packAuthSaleOff(PubBean pubBean, int type){
		
		
		if (1 == type){
			//批上送
			pubBean.setMessageID("0320");
		}
		
		/** 组包*/
		iso8583.initPack();	
		iso8583.setField(0, pubBean.getMessageID());
		if (!pubBean.getInputMode().startsWith("02")) {
			iso8583.setField(2, pubBean.getPan());
		}
		iso8583.setField(3, pubBean.getProcessCode());
		iso8583.setField(4, String.format("%012d",pubBean.getAmount()));
		iso8583.setField(11, pubBean.getTraceNo());
		if (!StringUtils.isEmpty(pubBean.getExpDate())) {
			iso8583.setField(14, pubBean.getExpDate());
		}

		iso8583.setField(22, pubBean.getInputMode());
		if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
			iso8583.setField(23, pubBean.getCardSerialNo());
		}
		iso8583.setField(25, pubBean.getServerCode());
		if (pubBean.getTrackData2() != null) {
			iso8583.setField(35, pubBean.getTrackData2());
		}
		if (pubBean.getTrackData3() != null) {
			iso8583.setField(36, pubBean.getTrackData3());
		}
		iso8583.setField(38, pubBean.getOldAuthCode());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(49, pubBean.getCurrency());

		if ('1' == pubBean.getInputMode().charAt(2)) {
			iso8583.setField(52, pubBean.getPinBlock());
		}
		if ('1' == pubBean.getInputMode().charAt(2)
				|| !StringUtils.isEmpty(pubBean.getTrackData2())
				|| !StringUtils.isEmpty(pubBean.getTrackData3())){
			iso8583.setField(53, getSecctrlInfo(pubBean));
		}
		iso8583.setField(60, pubBean.getIsoField60().getString());
		iso8583.setField(61, pubBean.getIsoField61());
		
		if (0 == type){
			iso8583.setField(64, "0");
		}
		
		return true;
	}
	
	/**

	 * 删除冲正数据
	 */
	public void deleteReverse(){
		reverseWaterService.delete();
		LoggerUtils.d("dd after删除冲正流水");
	}
	
	/**
	 * 记录结算数据
	 * @author linld
	 * @param pubBean
	 */
	
	protected void changeSettle(PubBean pubBean){
		
		SettlementService settleService = new SettlementServiceImpl(activity);
		String settleType = "";
		
		switch (pubBean.getTransType()){
		case TransType.TRANS_SALE:
			settleType = SettlementTableTypeConst.SALE;
			break;
		case TransType.TRANS_VOID_SALE:
			settleType = SettlementTableTypeConst.VOID_SALE;
			break;
		case TransType.TRANS_AUTHSALE:
			settleType = SettlementTableTypeConst.AUTH_SALE;
			break;
		case TransType.TRANS_VOID_AUTHSALE:
			settleType = SettlementTableTypeConst.VOID_AUTH_SALE;
			break;
		case TransType.TRANS_REFUND:
			settleType = SettlementTableTypeConst.REFUND;
			break;
		case TransType.TRANS_AUTHSALEOFF:
//		case TransType.TRANS_ORDER_AUTHSALEOFF:
//		case TransType.TRANS_PHONE_AUTHSALEOFF:
			settleType = SettlementTableTypeConst.AUTH_SALE_OFF;			
			break;
		case TransType.TRANS_EC_LOAD_CASH:
			settleType = SettlementTableTypeConst.CASH_ECLOAD;
			break;
		case TransType.TRANS_EMV_REFUND:
			settleType = SettlementTableTypeConst.EMV_REFUND;
			break;
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			settleType = SettlementTableTypeConst.VOID_CASH_ECLOAD;
			break;
		case TransType.TRANS_EC_PURCHASE:
			settleType = SettlementTableTypeConst.EC_SALE;
			break;
		default:
			return;
		}
		
		LoggerUtils.d("卡组织：" + pubBean.getInternationOrg());
//		if ("CUP".equals(pubBean.getInternationOrg())){
		if (true){
			LoggerUtils.d("dd 内卡:[" + settleType + "] + [增加" + pubBean.getAmount() + "]");
			settleService.addSettleAmount_NK(settleType, pubBean.getAmount());
			settleService.addSettleNum_NK(settleType, 1);
		}

		
	}
	
	
	
	/**
	* @brief 对返回码进行解释
	* @param in const char *szRespCode 返回码
	* @return 返回
	*/
	public void displayResponse(boolean isSucc, String pszRespCode)
	{
		if("00".equals(pszRespCode)){
			return;
		}
		
		MessageTipBean msgTipBean = new MessageTipBean();
		if (isSucc){
			msgTipBean.setTitle(getText(R.string.trans_succse));
		}else{
			msgTipBean.setTitle(getText(R.string.trans_fail));
		}
		msgTipBean.setTimeOut(0);
		
		String content = getText(R.string.common_respon) + pszRespCode + "\r\n" + AnswerCodeHelper.getAnswerCodeCN(pszRespCode);
		
		msgTipBean.setCancelable(false);
		msgTipBean.setContent(content);
		showUIMessageTip(msgTipBean);
	}
	public void displayResponse(boolean isSucc, PubBean pubBean)
	{
		String pszRespCode = pubBean.getResponseCode();
		if("00".equals(pszRespCode)){
			return;
		}
		MessageTipBean msgTipBean = new MessageTipBean();
		if (isSucc){
			msgTipBean.setTitle(getText(R.string.trans_succse));
		}else{
			msgTipBean.setTitle(getText(R.string.trans_fail));
		}
		msgTipBean.setTimeOut(0);

		String content = "";
		if(pubBean.getIsoField63()!=null){
			content = getText(R.string.common_respon) + pszRespCode + "\r\n" + pubBean.getIsoField63();
		}else{
			content = getText(R.string.common_respon) + pszRespCode + "\r\n" + AnswerCodeHelper.getAnswerCodeCN(pszRespCode);
		}

		msgTipBean.setCancelable(false);
		msgTipBean.setContent(content);
		showUIMessageTip(msgTipBean);
	}
		
	
	public void setResultContent(String content){
		transResultBean.setContent(content);;
	}
	
	
	/**
	 * 离线未上送次数 +1
	 */
	public synchronized void incOfflineUnSendNum(){
		int num = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, 0);
		num++;
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, num);
	}

	/**
	 * 离线未上送次数 -1
	 */
	public synchronized void delOfflineUnSendNum(){
		int num = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, 0);
		num--;
		if (num < 0){
			num = 0;
		}
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, num);
	}
	
	/**
	 * 电子签名未上送次数 +1
	 */
	public synchronized void incElecSignUnSendNum(){
		int num = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
		num++;
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, num);
	}

	/**
	 * 电子签名未上送次数 -1
	 */
	public synchronized void delElecSignUnSendNum(){
		int num = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
		num--;
		if (num < 0){
			num = 0;
		}
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, num);
	}
	
	
	/**
	 * 判断是否在黑名单中
	 * @param cardBin 需要进行判断的卡号
	 * @return true 不在黑名单中
	 * 		   false 在黑名单中
	 */
	public boolean CheckIsNotInBlk(String cardBin) {
		BlackCardService blackCardService = new BlackCardServiceImpl(activity);
		List<BlackCard> cardBinList = blackCardService.findAll();
		if (null == cardBinList) { 
			return true;
		}
		try{
			LoggerUtils.d("111 检查卡bin[" + cardBin + "]" );
			for(int i=0;i<cardBinList.size();i++) {
				LoggerUtils.d("111 黑名单列表" + i + "[" + cardBinList.get(i).getCardBin() + "]");
				if (cardBin.startsWith(cardBinList.get(i).getCardBin(), 0)){
					LoggerUtils.d("111 列入黑名单");
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean dealSendAfterTrade(){
	
LoggerUtils.d("111 电子签名联机上送->");		
		//联机电子签名上送
		doElecSignSend(ElecSignType.ONLINE);
LoggerUtils.d("111 离线上送->");		
		//离线上送
		doOfflineSend();
LoggerUtils.d("111 离线电子签名上送->");		
		//离线电子签名上送
		doElecSignSend(ElecSignType.OFFLINE);
		
		return true;
	}
	
	/**
	 * 签退
	 * @return
	 */
	protected boolean doLogOut(){
		final CommonBean<Integer> commonBean = new CommonBean<Integer>();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new LogOut(commonBean), new TransResultListener() {
					
					@Override
					public void succ() {
						commonBean.setResult(true);
						goOnStep(commonBean);
					}
					
					@Override
					public void fail(String message) {
						commonBean.setResult(false);
						goOnStep(commonBean);
					}
				});
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	protected boolean doQuery(final CommonBean<PubBean> commonBean) {
		if (commonBean == null) {
			return false;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TransController transController = new TransController(activity);
				transController.start(new payQuery(commonBean));
			}
		});
		stopStep(commonBean);
		return commonBean.getResult();
	}
	protected boolean quickPayNoPsw(PubBean pubBean, EmvAppModule emvAppModule){
		
		byte _9F6c[];
		byte aid[];
		boolean isInside = false;
		long noPswLimit = 0;
		int cardType = 0;
		
		_9F6c = emvAppModule.getEmvData(0x9F6C);
		aid = emvAppModule.getEmvData(0x4F);   
		LoggerUtils.e(" qps 9f6c:" + BytesUtils.bytesToHex(_9F6c,2));
		LoggerUtils.e(" qps aid:" + BytesUtils.bytesToHex(aid,8));
		LoggerUtils.e(" qps amount:" + pubBean.getAmount());
		noPswLimit = ParamsUtils.getLong(ParamsConst.PARAMS_KEY_QPS_NO_PSW_LIMIT, 0);
		
		//cdcvm判断
		if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_CDCVM)){
			if(_9F6c != null){
				if(((_9F6c[0]&0x80) != 0x80) && ((_9F6c[1]&0x80) == 0x80)){
					return true;
				}
			}
		}
		
		//判断是否开始免密
		if(!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW, false)){
			return false;
		}
		
		cardType = pubBean.getEmvBean().getCardType();
		LoggerUtils.e(" qps cardtype:"  + cardType);
		if(cardType == EmvBean.INSIDECARD || cardType == EmvBean.UNKNOWCARD){
			isInside = true;
			LoggerUtils.e(" qps cardtype:inside");
		}else{
			isInside = false;
			LoggerUtils.e(" qps cardtype:outside");
		}
		
		//境内卡和境外卡的判断
		if(isInside){
			
			//大于限额的全部拒绝
			if(pubBean.getAmount() > noPswLimit){
				return false;
			}
			
			if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_CARD_BIN_A)){
				return CheckCardInCardBinA(pubBean.getPan());
			}else if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_CARD_BIN_B)){
				
				if(aid == null){
					return false;
				}
				
				if(aid[7] == 0x02 || aid[7] == 0x03){
					LoggerUtils.e(" qps creadit card");
					return true;
				}else{
					return CheckCardInCardBinB(pubBean.getPan());
				}
			}else{
				if(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_CARD_BIN_C)){
					if(CheckCardInCardBinC(pubBean.getPan())){
						return false;
					}else{
						return true;
					}
				}
			}
			
		}else{
			
			if(aid == null){
				return false;
			}
			
			if(aid[7] == 0x02 || aid[7] == 0x03){
				if(pubBean.getAmount() <= noPswLimit){
					return true;
				}
			}
			
			if(_9F6c == null){
				return false;
			}else{
				if((_9F6c[0]&0x80) == 0x80){
					return false;
				}else{
					return true;
				}
			}
		}
		return true;
	}
	
	protected boolean CheckCardInCardBinA(String cardNo){
		
		CardBinAServiceImpl serviceImpl = new CardBinAServiceImpl(activity);
		int len = cardNo.length();
		String value = len + cardNo.substring(0, 6);
		CardBinA cardBinA = serviceImpl.findByCardBin(value);
		if(cardBinA == null){
			LoggerUtils.e(" qps not in card bin A");
			return false;
		}
		LoggerUtils.e(" qps in card bin A");
		return true;
	}
	
	
	protected boolean CheckCardInCardBinB(String cardNo){
		
		CardBinBServiceImpl serviceImpl = new CardBinBServiceImpl(activity);
		int len = cardNo.length();
		String value = len + cardNo.substring(0, 6);
		
		LoggerUtils.e(" qps value:" + value);
		CardBinB cardBinB = serviceImpl.findByCardBin(value);
		if(cardBinB == null){
			LoggerUtils.e(" qps not in card bin B");
			return false;
		}
		LoggerUtils.e(" qps in card bin B");
		return true;
	}


	protected boolean CheckCardInCardBinC(String cardNo){
		
		CardBinCServiceImpl serviceImpl = new CardBinCServiceImpl(activity);
		int len = cardNo.length();
		String value = len + cardNo.substring(0, 6);
		CardBinC cardBinC = serviceImpl.findByCardBin(value);
		if(cardBinC == null){
			LoggerUtils.e(" qps not in card bin C");
			return false;
		}
		LoggerUtils.e(" qps in card bin C");
		return true;
	}
	
	
	protected boolean CheckIsNoSign(PubBean pubBean){
		
		if(!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_QPS_IS_NO_SINGNATURE)){
			return false;
		}
		
		if(pubBean.getTransType() == TransType.TRANS_SALE
				|| pubBean.getTransType() == TransType.TRANS_PREAUTH){
			Long noSignLimit = Long.parseLong(ParamsUtils.getString(ParamsConst.PARAMS_KEY_QPS_NO_SINGNATURE_LIMIT));
			if(pubBean.getAmount() <= noSignLimit){
				return true;
			}
		}
		return false;
	}
	public HashMap<String, String> getTlvMap(String tlValue){
		HashMap<String, String> res = new HashMap<String, String>();
		int curDex = 0;
		int tagLen = 0;
		int dataLen = 0;
		int pre = 0;
		byte[] data = BytesUtils.hexStringToBytes(tlValue);
		while(curDex < data.length){
			tagLen = TLVUtils.getTagLen(data, curDex);
			curDex += tagLen;
			if((data[curDex]&0x80) == 0x80){
				int tmpLen = data[curDex]&0x7F;
				curDex += tmpLen + 1;
			} else{
				curDex +=1;
			}
			dataLen = Integer.parseInt(BytesUtils.bytesToHex(data, pre+tagLen,1),16);
			LoggerUtils.d("getTlvMap Tv "+ BytesUtils.bytesToHex(data, pre, tagLen)+"     "+BytesUtils.bytesToHex(data, pre+tagLen+1, dataLen));
			res.put(BytesUtils.bytesToHex(data, pre, tagLen),BytesUtils.bytesToHex(data, pre+tagLen+1, dataLen));
			curDex += dataLen;
			pre = curDex;
		}
		if(res.get("5A")!=null){
			String pan = res.get("5A");
			if(pan.endsWith("F")){
				pan = pan.substring(0,pan.length()-1);
			}
			res.put("5A",pan);
		}
		return res;
	}
		
}

	
