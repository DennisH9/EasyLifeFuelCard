package com.newland.payment.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.newland.base.util.AmountUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.NewRefundBean;
import com.newland.payment.ui.listener.OnDateEnterListener;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.card.CardModule;
import com.newland.pos.sdk.common.TransConst;
import com.newland.pos.sdk.interfaces.CardListener;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 退货读卡
 */
public class NewRefundReadCardFragment extends BaseFragment implements CardListener {

//	@ViewInject(R.id.et_refer_no)
	private EditText inputReferNo;   // 参考号
//	@ViewInject(R.id.et_amount)
	private EditText inputAmount;    // 金额
//	@ViewInject(R.id.tv_card_no)
	private TextView cardNo;    // 卡号
//	@ViewInject(R.id.et_refund)
	private EditText inputDate;    // 日期

	private NewRefundBean refundBean;
    private CardBean cardBean;
	private Handler handler;
	private String content;
	private Dialog dialog;
	private List<Integer> supportMode;
	private static CardModule cardModule;

	private final static int CARD_READ_COMFIRM = 0x0A;

	public NewRefundReadCardFragment(long timeOut) {
		super(0);
	}
	public NewRefundReadCardFragment() {
		super(0);
	}
	public static NewRefundReadCardFragment newInstance(NewRefundBean refundBean) {
		NewRefundReadCardFragment fragment;
		if (refundBean.getCardBean().getTimeOut() != null) {
			fragment = new NewRefundReadCardFragment(Long.valueOf(refundBean.getCardBean().getTimeOut()));
		}
		else {
			fragment = new NewRefundReadCardFragment();
		}

		fragment.refundBean = refundBean;
		fragment.cardBean = refundBean.getCardBean();
		return fragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mFragmentView = inflater.inflate(R.layout.layout_refund_readcard, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return mFragmentView;
    }

	private void initView() {
		inputReferNo = (EditText) mFragmentView.findViewById(R.id.et_refer_no);
		inputAmount = (EditText) mFragmentView.findViewById(R.id.et_amount);
		cardNo = (TextView) mFragmentView.findViewById(R.id.tv_card_no);
		inputDate = (EditText) mFragmentView.findViewById(R.id.et_refund);

		inputReferNo.setText(refundBean.getOldRefNo());
		inputAmount.setText(FormatUtils.formatAmount(String.valueOf(refundBean.getOldAmount())));
		inputDate.setText(refundBean.getOldDate());
	}

	@Override
	public void ToastShow(Object info) {
		ToastUtils.show(context, info);
	}

	@Override
	public void onCardBack() {
		LoggerUtils.d("onCardBack Start!!!");
		onBack();
	}

	@Override
	public void onCardComfrim() {
		LoggerUtils.d("onCardComfrim Start!!!");
		onSucess();
	}

	@Override
	public void onCardFail() {
		LoggerUtils.d("onCardFail Start!!!");
		onFail();
	}

	@Override
	public void onCardSucess() {
		LoggerUtils.d("onCardSucess Start!!!");
		System.out.println("22222-------onCardSucess");
		if (!checkInput()) {
			cardModule.cancelCheckCard();
			return;
		}
		onSucess();
	}

	@Override
	public void onCardTimeOut() {
		LoggerUtils.d("onCardTimeOut Start!!!");
		onTimeOut();		
	}

	@Override
	public void onRefreshDate() {
		refreshDate();
	}

	@Override
	protected void initData() {
		initView();
		inputAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
		inputAmount.setFilters(new InputFilter[] { new InputAmountFilter() });
		inputAmount.addTextChangedListener(new InputWatcher());
		inputReferNo.addTextChangedListener(new InputWatcher());
		inputDate.addTextChangedListener(new InputWatcher());
		handler = getCardInputHandler();
		cardBean.setIsPosSupportIc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC, true));
		cardModule = new CardModule(cardBean,this);
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
		setTitle(cardBean.getTitle());
//		InputUtils.setMaxLength(etCardNo, 19);
		refreshDate();
//		etCardNo.setTextKeepState(etCardNo.getText().toString());
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	protected void initEvent() {
		cardModule.startCheckCard();		
	}

	@Override
	protected BaseBean getBean() {
		return cardBean;
	}
	
	@Override
	protected boolean doClickBackEvent() {
		cancelCardReader();
		return false;
	}
	
	@Override
	public void onPause() {
		cardModule.cancelCheckCard();
		super.onPause();
	}
	
	private void refreshDate(){
		supportMode = new ArrayList<Integer>();
		int readCardType = cardBean.getInputMode();
		if ((readCardType&ReadcardType.SWIPE) == ReadcardType.SWIPE) {
			supportMode.add(ReadcardType.SWIPE);
		}
		if ((readCardType&ReadcardType.ICCARD) == ReadcardType.ICCARD) {
			supportMode.add(ReadcardType.ICCARD);
		}
		if ((readCardType&ReadcardType.RFCARD) == ReadcardType.RFCARD) {
			supportMode.add(ReadcardType.RFCARD);
		}
	}
	
	private Handler getCardInputHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				
				case CARD_READ_COMFIRM:
					System.out.println("11111-------CARD_READ_COMFIRM");
					if (!checkInput()) {
						cardModule.cancelCheckCard();
						return;
					}
					if (cardBean.getIsComfrim()) {
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
	
	private void cancelCardReader(){
		/** 取消检卡 */
		new Thread(){
			public void run() {
				LoggerUtils.d("111 cancel reader card");
//				DeviceController.getInstance().reset();
				cardModule.cancelCheckCard();
				LoggerUtils.d("111 cancel reader card end");
				onBack();
			}
		}.start();
	}
	
	@Override
	protected boolean doClickHomeEvent() {
		return doClickBackEvent();
	}

	private boolean checkInput() {
		String referNo = inputReferNo.getText().toString();
		if (TextUtils.isEmpty(referNo)) {
			ToastUtils.showLong(context, "请输入参考号");
			return false;
		}

		String amount = inputAmount.getText().toString();
		if (TextUtils.isEmpty(amount)) {
			ToastUtils.showLong(context, "请输入金额");
			return false;
		}
		String oldDate = inputDate.getText().toString();
		if (TextUtils.isEmpty(oldDate)) {
			ToastUtils.showLong(context, "请输入原交易日期");
			return false;
		}


		refundBean.setOldRefNo(referNo);
		refundBean.setOldAmount(AmountUtils.yuanStr2fen(amount));
		refundBean.setOldDate(oldDate);

		return true;
	}

	/**
	 * 限制金额输入数值
	 */
	private class InputAmountFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			if (source.equals(".")) {
				if (dest.toString().length() == 0) {
					return "0.";
				} else if (Double.parseDouble(dest.toString()) >= 9999999999.00D) {
					return "";
				}
			}
			if (dest.toString().contains(".")) {
				int index = dest.toString().indexOf(".");
				int mlength = dest.toString().substring(index).length();

				if (mlength == 3) {
					return "";
				}
			} else if (!TextUtils.isEmpty(dest.toString()) && Double.parseDouble(dest.toString()+source.toString()) > 9999999999.00D) {
				return "";
			}
			return null;
		}
	}

	private class InputWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (inputReferNo.getText().toString().length() > 0
					|| inputAmount.getText().length() > 0) {
				cardModule.startCheckCard();
			} else {
				cardModule.cancelCheckCard();
			}
		}
	}
	public String inputOldTransDate(){
		final CommonBean<String> dateBean = new CommonBean<String>();
		LoggerUtils.d("222 onClick0");
		AbstractBaseTrans abstractBaseTrans = new AbstractBaseTrans() {
			@Override
			protected void stopStep(BaseBean bean) {
				super.stopStep(bean);
			}
		};
		LoggerUtils.d("222 onClick1");

		dateBean.setTitle("退货");
		dateBean.setContent("请选择原交易日期");
		dateBean.setEmptyFlag(false);
		LoggerUtils.d("222 onClick2");
		activity.switchContent(new ChooseDateFragment(dateBean, Const.DateWheelType.MOUTH|Const.DateWheelType.DAY, new OnDateEnterListener() {
			@Override
			public void onEnter(Long value) {
				dateBean.setResult(true);
				dateBean.setValue(value == null ? "" : TimeUtils.getFormatTime(
						value, "MMdd"));
				dateBean.setStepResult(TransConst.StepResult.SUCCESS);
				try {
					synchronized(dateBean.getWaitObj()) {
						dateBean.getWaitObj().notify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
		try {
			synchronized(dateBean.getWaitObj()) {
				dateBean.getWaitObj().wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		activity.popBackFragment(1);
		LoggerUtils.d("222 onClick3");
		if(!dateBean.getResult()){
			return "";
		}
		return dateBean.getValue();
	}
}
