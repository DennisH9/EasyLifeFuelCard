package com.newland.payment.ui.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.pos.sdk.util.LoggerUtils;

public class AmountEditText extends FrameLayout {

	private KeyboardNumber keyboardNumber;
	private AbstractKeyBoardListener keyBoardListen = null;
	private String amount = "";
	private long maxAmount = 200000;
	private String shareKay;

	/** 当前输入的金额, 用于金额校验 */
	private long currentValue;

//	@ViewInject(R.id.et_amount)
	private EditText etAmount;

//	@ViewInject(R.id.txt_title)
	private TextView tvtitle;

	public AmountEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setTitle(String title) {
		tvtitle.setText(title);
	}

	public void setTitle(int title) {
		tvtitle.setText(title);
	}

	public void setParam(Context context, final String key,
			KeyboardNumber keyboardNumber) {
		this.keyboardNumber = keyboardNumber;
		this.shareKay = key;
		amount = ParamsUtils.getString(key);
		currentValue = Long.parseLong(amount);
		initView(context);
		initEent();
	}

	private void initView(Context context) {

		View view = LayoutInflater.from(context).inflate(
				R.layout.amount_edit_text, this, true);
		ViewUtils.inject(this, view);
		updateValue(currentValue);
	}

	private void initEent() {

		InputUtils.alwaysHideKeyBoard(etAmount,
				InputType.TYPE_NUMBER_FLAG_SIGNED);
		etAmount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				keyboardNumber.setVisibility(View.VISIBLE);
			}
		});

		etAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					keyboardNumber.setVisibility(View.VISIBLE);
					keyboardNumber.setKeyBoardListener(keyBoardListen);
					keyBoardListen.setTargetView(AmountEditText.this);
				} else {
					keyboardNumber.setVisibility(View.GONE);
				}
			}
		});

		keyBoardListen = new AbstractKeyBoardListener() {

			@Override
			public void onKeyClick(int key) {
				super.onKeyClick(key);
				switch (key) {
				case KeyboardNumber.K_CANCEL:
					break;

				case KeyboardNumber.K_CLEAN:
					break;

				case KeyboardNumber.K_BACKSPACE:
					LoggerUtils.e("amount :" + amount);
					if (amount.length() <= 0) {
						break;
					}

					if (amount.length() > 0) {
						amount = amount.substring(0, amount.length()-1);
						if(amount.equals("")){
							updateValue(0);
						}else{
							updateValue(Long.parseLong(amount));
						}
					}
					break;

				case KeyboardNumber.K_ENTER:
					keyboardNumber.setVisibility(View.GONE);
					etAmount.setFocusable(false);
					etAmount.setFocusableInTouchMode(false);
					etAmount.requestFocus();
					ParamsUtils.setString(shareKay,amount);
					break;

				case KeyboardNumber.K_DOT:
					break;

				default:
					if(Long.parseLong(amount + (char)key) > maxAmount){
						break;
					}else{
						amount += (char)key;
					}
					LoggerUtils.e("key :" + key);
					LoggerUtils.e("amount :" + amount);
					updateValue(Long.parseLong(amount));
					break;
				}
			}
		};
	}

	public void setMaxValue(long value) {
		maxAmount = value;
	}

	private void updateValue(long amount) {

		int temp = (int) amount;
		String strInteger = String.valueOf(temp / 100);
		String strDecimal = String.format("%02d", temp % 100);
		etAmount.setText(strInteger + "." + strDecimal);
		etAmount.setSelection(etAmount.getText().length());
	}

}
