package com.newland.payment.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.newland.base.util.InputUtils;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.RemindViewCloseListener;
import com.newland.payment.ui.view.CardRemindView;
import com.newland.payment.ui.view.CardThreeRemindView;
import com.newland.payment.ui.view.CardTwoRemindView;
import com.newland.payment.ui.view.CommonDialog.TimeOutOper;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.card.CardModule;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.interfaces.CardListener;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 刷卡界面
 *
 * @author CB
 * @time 2015-4-20 下午2:37:21
 */
@SuppressLint("ValidFragment")
public class CardFragment extends BaseFragment implements CardListener {
	
	/** 输入 */
//	@ViewInject(R.id.txt_input)
	TextView txtInput;
	/** 卡号 */
//	@ViewInject(R.id.et_card_no)
	EditText etCardNo;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyBoardNumber;
	
	private CardBean bean;
	private Handler handler;
	private String content;
	private Dialog dialog;
	private List<Integer> supportMode;
	private static CardModule cardModule;

	private final static int CARD_READ_COMFIRM = 0x0A;
	
	public CardFragment(long timeOut) {
		super(0);
	}
	public CardFragment() {
		super(0);
	}
	public static CardFragment newInstance(CardBean bean) {
		CardFragment fragment;
		if (bean.getTimeOut() != null) {
			fragment = new CardFragment(Long.valueOf(bean.getTimeOut()));
		}
		else {
			fragment = new CardFragment();
		}

		fragment.bean = bean;
		return fragment;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.card_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		txtInput = (TextView)mFragmentView.findViewById(R.id.txt_input);
		etCardNo = (EditText)mFragmentView.findViewById(R.id.et_card_no);
		keyBoardNumber = (KeyboardNumber)mFragmentView.findViewById(R.id.key_board_number);
		return mFragmentView;
	}
	
	private FrameLayout remindView;
	
	@SuppressLint("NewApi")
	@Override
	protected void initData() { 
		handler = getCardInputHandler();
		bean.setIsPosSupportIc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC, true));
		cardModule = new CardModule(bean,this);
		cardModule.initData();
		CardModule.setTMkIndex(ParamsUtils.getTMkIndex());
		if(DesMode.DES3.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))){
			CardModule.setEncryptMode(DesMode.DES3);
		}
		else{
			CardModule.setEncryptMode(DesMode.DES);
		}
		if(Const.YES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))){
			CardModule.setIs_encrypt_track(Const.YES);
		}
		else{
			CardModule.setIs_encrypt_track(Const.NO);
		}
		setTitle(bean.getTitle());
		InputUtils.setMaxLength(etCardNo, 19);
		refreshDate();
		etCardNo.setTextKeepState(etCardNo.getText().toString());
	}
	
	/** 刷新界面数据 */
	private void refreshDate(){
		content = bean.getContent();
		if (StringUtils.isEmpty(content)) {
			content = getString(R.string.swipe_card_how_to_input);
		}
		ViewUtils.setGoneTextView(bean.getContent(), txtInput);
		supportMode = new ArrayList<Integer>();
		boolean isSupportHandInput = false;
		int readCardType = bean.getInputMode();
		if ((readCardType&ReadcardType.HAND_INPUT) != 0) {
			setHandInputStyle();
			isSupportHandInput = true;
		}
		if ((readCardType&ReadcardType.SWIPE) == ReadcardType.SWIPE) {
			supportMode.add(ReadcardType.SWIPE);
		}
		if ((readCardType&ReadcardType.ICCARD) == ReadcardType.ICCARD) {
			supportMode.add(ReadcardType.ICCARD);
		}
		if ((readCardType&ReadcardType.RFCARD) == ReadcardType.RFCARD) {
			supportMode.add(ReadcardType.RFCARD);
		}
		
		if (remindView!=null){
			((ViewGroup)mFragmentView).removeView(remindView);
		}
		
		int num = supportMode.size();
		if(bean.getTranstype() == (byte)EmvConst.EMV_TRANS_RF_SALE
				&& ParamsUtils.getBoolean(ParamsConst.PARAM_IS_HIDING_SWING)
				&& readCardType != 12){
			LoggerUtils.d("lxb 插卡隐藏开关---优先非接");
			if(num == 1){
				LoggerUtils.d("lxb 插卡隐藏开关---优先非接1111");
				remindView = new CardRemindView(context, readCardType, content, isSupportHandInput, listener);
			}else{
				
				LoggerUtils.d("lxb CardType:"+ readCardType);
				readCardType = readCardType & 0xfa;
				LoggerUtils.d("lxb 插卡隐藏开关---优先非接2222");
				
				remindView = new CardTwoRemindView(context, readCardType, content, isSupportHandInput, listener);
			}
		}else{
			LoggerUtils.d("lxb 插卡隐藏开关---可插卡");
			if (num == 1){
				remindView = new CardRemindView(context, readCardType, content, isSupportHandInput, listener);
			} else if (num == 2){
				remindView = new CardTwoRemindView(context, readCardType, content, isSupportHandInput, listener);
			} else if (num == 3){
				remindView = new CardThreeRemindView(context, content, isSupportHandInput, listener);
			} else {
			}

		}
		
		if (remindView != null) {
			remindView.setClickable(true);
			((ViewGroup)mFragmentView).addView(remindView);
		}
	}
	
	@Override
	protected void onTransCancel() {
	}
	
	@Override
	public void onPause() {
		cardModule.cancelCheckCard();
		super.onPause();
	}
	
	private boolean isCancelCheckCard = false;
	private RemindViewCloseListener listener = new RemindViewCloseListener() {
		
		@Override
		public void onCloseEvent() {
			LoggerUtils.d("onCloseEvent start!!!");
			/** 切换手输卡号 */
			isCancelCheckCard = true;
//			cardModule.cancelCheckCard();
//			handler = getCardInputHandler();
		}
	};	
	
	@SuppressLint("HandlerLeak")
	private Handler getCardInputHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				
				case CARD_READ_COMFIRM:
					if (bean.getIsComfrim()) {
						onCardComfrim();
						} else {
						onSucess();
					}
					break;
				default:
					break;
				}
			}
		};
	}
	@Override
	protected void initEvent() {
		
		etCardNo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				etCardNo.setSelection(s.length());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		AbstractKeyBoardListener listener = new AbstractKeyBoardListener(etCardNo) {

			@Override
			public void onEnter() {
				if (bean.getInputMinLen() != null) {

					if (this.getValue().length() < bean.getInputMinLen()) {
						ToastUtils.show(context, context.getString(
								R.string.swipe_card_min_length,
								bean.getInputMinLen()));
					} else if (this.getValue().length() > bean.getInputMaxLen()) {
						ToastUtils.show(context, context.getString(
								R.string.swipe_card_max_length,
								bean.getInputMaxLen()));
					} else {
						bean.setPan(etCardNo.getText().toString());
						bean.setTrueInput(ReadcardType.HAND_INPUT);
						handler.sendEmptyMessage(CARD_READ_COMFIRM);
					}
				}

			}

		};
		
		//键盘输入事件
		keyBoardNumber.setKeyBoardListener(listener);
		listener.setTargetView(etCardNo);
		listener.setMaxLength(bean.getInputMaxLen());
		keyBoardNumber.setEnterNotGone();
		startCheckCard();
	}
	
	private void startCheckCard(){
		if (remindView != null){
		cardModule.startCheckCard();
		}
	}
	
	@Override
	public CardBean getBean() {
		return bean;
	}
	
	/**
	 * 设置手输模式
	 */
	public void setHandInputStyle() {
		etCardNo.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		etCardNo.setTextIsSelectable(true);
		keyBoardNumber.setTelephoneKeyDisplay(true);
		keyBoardNumber.setVisibility(View.VISIBLE);
	}


	@Override
	protected void initClickEvent(View view) {
		
	}

	private void dissmiss(){
		if (dialog!=null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/** 读取磁道数据 */
	@SuppressWarnings("unused")
	private void readTrackData(CardBean cardBean){
		cardModule.readTrackData(cardBean);
	}
	
	/**
	 * 判断是否 IC卡
	 */
	protected boolean isIcCard(String serviceCode){
		if (StringUtils.isEmpty(serviceCode)) {
			return false;
		}
		if(serviceCode.startsWith("2") || serviceCode.startsWith("6")){
			return true;
		}
		return false;
	}
	
	@Override
	public void onFragmentShow() {
		
		super.onFragmentShow();
		if (supportMode.size() != 0) {
			activity.hideTitle();
		}
	}
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		dissmiss();
		dismissTipDialog();
	}
	
	
	private void cancelCardReader(){
		if (remindView!=null &&remindView.getVisibility()==View.VISIBLE) {
			/** 取消检卡 */
			new Thread(){
				public void run() {
					LoggerUtils.d("111 cancel reader card");
//					DeviceController.getInstance().reset();
					cardModule.cancelCheckCard();
					LoggerUtils.d("111 cancel reader card end");
				}
			}.start();
		} else {
			onTransCancel();
			onBack();
		}
	}
	private Dialog tipDialog = null;
	private void dismissTipDialog(){
		if(tipDialog != null){
			tipDialog.dismiss();
			tipDialog = null;
		}
	}
	
	@Override
	protected boolean doClickBackEvent() {
		tipDialog = MessageUtils.showCommonDialog(
				activity, 
				activity.getResources().getString(R.string.common_cancel_trans_tip_msg), 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 确认
						cancelCardReader();
						dismissTipDialog();
					}
				}, 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 取消
						dismissTipDialog();
					}
				});
		return false;
	}
	
	@Override
	protected boolean doClickHomeEvent() {
		return doClickBackEvent();
	}
	
	@Override
	public void onCardSucess() {
		// TODO Auto-generated method stub
		LoggerUtils.d("onCardSucess Start!!!");
		onSucess();
	}
	@Override
	public void onCardFail() {
		// TODO Auto-generated method stub
		LoggerUtils.d("onCardFail Start!!!");
		onFail();
	}
	@Override
	public void onCardBack() {
		// TODO Auto-generated method stub
		LoggerUtils.d("onCardBack Start!!!");
		if(!isCancelCheckCard) {
			onBack();
		}
	}
	@Override
	public void onCardTimeOut() {
		// TODO Auto-generated method stub
		LoggerUtils.d("onCardTimeOut Start!!!");
		onTimeOut();
	}
	@Override
	public void onCardComfrim() {
		// TODO Auto-generated method stub
		dialog = MessageUtils.showCommonDialog(
				activity, 
				activity.getText(R.string.common_cardno_comfirm).toString(),
				bean.getPan(), 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 确认
						dissmiss();
						onSucess();
					}
				}, 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 取消
						dissmiss();
						onFail();
					}
				},
				30,
				TimeOutOper.CANCEL
				);
	}

	@Override
	public void onRefreshDate() {
		// TODO Auto-generated method stub
		refreshDate();
	}
	/**
	 * 显示toast提示
	 */
	@Override
	public void ToastShow(Object info) {
		// TODO Auto-generated method stub
		ToastUtils.show(context, info);
	}
}
