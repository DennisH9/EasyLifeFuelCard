package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.trans.bean.field.Field48_Settle;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 输入金额
 * 
 * @author CB
 * @time 2015-4-17 下午4:23:40
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class InputMoneyFragment extends BaseFragment {

	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyBoardNumber;
	/** 整数 */
//	@ViewInject(R.id.txt_integer)
	TextView txtInteger;
	/** 小数 */
//	@ViewInject(R.id.txt_decimal)
	TextView txtDecimal;
	/** 提示*/
//	@ViewInject(R.id.txt_input_money_content)
	TextView txtContent;
	/** 小数点*/
//	@ViewInject(R.id.txt_input_money_point)
	TextView txtPoint;
	
	private AmountBean bean;
	
	private String amount;
	
	private long amountDefault;
	/**金额的格式，小数点位置*/
	private int format;
	
	/**小数点的模*/
	private long mod;
	
	/**输入上限金额*/
	private long maxAmount;
	
	/**最小金额*/
	private long minAmount;
	
	/**输入超时时间，单位s*/
	@SuppressWarnings("unused")
	private int timeOut;
	
	/**Title*/
	private String title;
	
	/**提示文本(请输入金额/请输入小费)*/
	private String content;
	
	/**是否确认金额*/
	private Boolean isConfirmAmount;
	
	/**是否检查退货金额*/
	private Boolean isCheckRefundAmount;
	
	/**当前输入的金额, 用于金额校验*/
	private long currentValue;
	
	private AbstractKeyBoardListener keyBoardListen = null;
	
	public InputMoneyFragment(long timeOut) {
		super(timeOut);
	}
	
	public static InputMoneyFragment newInstance(AmountBean amountBean) {
		InputMoneyFragment fragment = new InputMoneyFragment(amountBean.getTimeOut());
		fragment.bean = amountBean;
		return fragment;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.input_money_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		//标题咋设?
		title = bean.getTitle();
		content = bean.getContent();
		maxAmount = bean.getMaxAmount();
		minAmount = bean.getMinAmount();
		format = bean.getFormat();
		timeOut = bean.getTimeOut();
		amountDefault = bean.getAmount();
		
		isConfirmAmount = bean.isConfirmAmount();
		isCheckRefundAmount = bean.isCheckRefundAmount();
		
		setTitle(title);

		if(!StringUtils.isEmpty(content)){
			txtContent.setText(content);
		}
		mod = 1;
		for(int i=0; i<format; i++){
			mod *= 10;
		}
		long decimal = amountDefault % mod;
		long integer = amountDefault / mod;
		
		
		String strInteger = String.valueOf(integer);
		String strDecimal = "";
		
		if(format <=0 ){
			txtPoint.setVisibility(View.GONE);
		}else{
			strDecimal = String.format("%0" + format + "d", decimal);
		}
		txtInteger.setText(strInteger);
		txtDecimal.setText(strDecimal);
		amount = String.valueOf(amountDefault);
		keyBoardNumber.setEnterNotGone();
	
	}

	@Override
	protected void initClickEvent(View view) {

	}

	
	@Override
	protected void initEvent() {
		
		//键盘输入事件
		keyBoardListen = new AbstractKeyBoardListener() {
			
			@Override
			public void onEnter() {
				if (StringUtils.isEmpty(amount)) {
					ToastUtils.show(context, R.string.expense_collection_input_money);
					return;
				}
				currentValue = Long.valueOf(amount);
				if(currentValue < minAmount){
					ToastUtils.show(context, R.string.common_input_amount_not_enough);
					return;
				}
				
				if(currentValue > maxAmount){
					//不应该走到这一步,走到这说明有BUG
					amount = String.valueOf(maxAmount);
					currentValue = maxAmount;
					
				}
				
				/**
				 * 以下是对限额检查，之前放步骤里会导致fragment的层层覆盖,所以移到里面
				 * 逻辑很奇葩
				 */
				//1.POS交易限额检查，超过限额提示先结算
				if (!checkAmountLimit()){
					return;
				}
				
				//2.确认金额
				//3.检验退货最大金额
				//4.返回金额
				if (doConfirmAmount()){
					if (doCheckRefundAmount()){
						doSucces();
						return;
					}
				}	
				
				
			}
			
			
			@Override
			public void onChangeText(String value) {
				LoggerUtils.d("key value:" + value);
				
				String strInteger = "0";
				String strDecimal = "0";
				
				StringBuilder tmp = new StringBuilder();
				
				if(value.length() == 0){
					value = "0";
				}
				
				try{
					currentValue = Long.valueOf(value);
				}catch(Exception e){
					return;
				}
				
				if(currentValue > maxAmount){
					return;
				}
				amount = value;
				
				if (value.length() > format) {
					strInteger = Long.valueOf(value.substring(0,value.length()-format)).toString();
					if(strInteger.length() > 3){
						int end = strInteger.length()%3;
						int len = strInteger.length();
						for(int start=0; end <= len; start=end,end+=3){
							//tmp.strInteger.substring(len-i, 3);
							tmp.append(strInteger.subSequence(start, end));
							if(end < len && start != end){
								tmp.append(",");
							}
						}
						strInteger = tmp.toString();
					}
					strDecimal = value.substring(value.length()-format);
				} else {
					strDecimal = String.format("%0" + format + "d", Integer.parseInt(value));
				}
				
				txtInteger.setText(strInteger);
				txtDecimal.setText(strDecimal);
				
			}
			
			@Override
			public void onClick(int code) {
				String tmp = amount + (char)code;
//				Long currentValue = 0L;
				try{
					currentValue = Long.valueOf(tmp);
				}catch(Exception e){
					return;
				}
				
				if(currentValue > maxAmount){
					return;
				}
				super.onClick(code);
			}
		};
		
		keyBoardNumber.setKeyBoardListener(keyBoardListen);
	}
	
	private Dialog confirmDialog = null;
	
	private synchronized boolean doConfirmAmount(){
		if(!isConfirmAmount){
			return true;
		}
		
		if(confirmDialog != null){
			return false;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(getText(R.string.common_pls_confirm));
		sb.append(getText(R.string.bill_money));
		sb.append(":");
		sb.append(FormatUtils.formatAmount((amount)));

		MainActivity.getInstance().resetProgress();
		confirmDialog = MessageUtils.showCommonDialog(activity, title, sb.toString(),
			new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				dismissDialog();
				if (doCheckRefundAmount()){
					doSucces();
				}
			}
			
		}, new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				dismissDialog();
				keyBoardListen.onClear();
			}
			
		});
		return false;
	}
	
	private boolean doCheckRefundAmount(){
		if(!isCheckRefundAmount){
			return true;
		}
		
		if(confirmDialog != null){
			return false;
		}
		
		long maxAmount = ParamsUtils.getLong(
				ParamsConst.PARAMS_KEY_BASE_REFUNDAMOUNT, 10000L);
		if (Long.valueOf(amount) <= maxAmount) {
			return true;
		}

		MainActivity.getInstance().resetProgress();
		confirmDialog = MessageUtils.showCommonDialog(activity, title,
				getText(R.string.error_refund_amount_limit),
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismissDialog();
						keyBoardListen.onClear();
					}

				}, null);

		return false;
	}
	
	private void doSucces(){
		((AmountBean)bean).setAmount(Long.valueOf(amount));
		onSucess();
	}
	
	/**金额限制检查 */
	private boolean checkAmountLimit(){
		
		if(confirmDialog != null){
			return false;
		}
		
		Field48_Settle settleAmount = new Field48_Settle();
		LoggerUtils.d("dd 输入金额：" + currentValue
					+ "\r\nPOS当前交易总金额：" + settleAmount.getTotalAmount()
					+ "\r\nPOS最大限额：" + TransConst.POS_MAX_AMOUNT);
		if (currentValue + settleAmount.getTotalAmount() > TransConst.POS_MAX_AMOUNT){
			MainActivity.getInstance().resetProgress();
			confirmDialog = MessageUtils.showCommonDialog(activity, title,
					getText(R.string.error_over_limit_amount),
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							dismissDialog();
							keyBoardListen.onClear();
						}

					}, null);
			//金额超限
			return false;	
			
		}
		//金额没超限，直接返回
		return true;

	}
	
	private void dismissDialog(){
		if (confirmDialog != null) {
			confirmDialog.dismiss();
			confirmDialog = null;
		}
	}
	
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		dismissDialog();
	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}
	
}
