package com.newland.payment.ui.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.util.InputUtils;
import com.newland.payment.R;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.pos.sdk.util.LoggerUtils;

public class IpEditText extends FrameLayout {
	
	/** IP1 */
//	@ViewInject(R.id.et_ip_1)
	private EditText etIp1;
	/** IP2 */
//	@ViewInject(R.id.et_ip_2)
	private EditText etIp2;
	/** IP3 */
//	@ViewInject(R.id.et_ip_3)
	private EditText etIp3;
	/** IP4 */
//	@ViewInject(R.id.et_ip_4)
	private EditText etIp4;
	
	private int currentIndex;
	private String currentIpPoint;
	private KeyboardNumber keyboardNumber;
	private String currentIp;
	
	private EditText[] ets;
	
	
	private StringValueChangeListener stringValueChangeListener;
	
	public IpEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initCommon(Context context, String defaultIp, KeyboardNumber keyboardNumber,
			StringValueChangeListener stringValueChangeListener) {
		this.keyboardNumber = keyboardNumber;
		this.stringValueChangeListener = stringValueChangeListener;
		currentIp = defaultIp;
		init(context);
	}

	
	private void init(Context context) {
		initView(context);
		initEvent();
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.ip_edit_text, this, true);
		ViewUtils.inject(this, view);

		ets = new EditText[]{etIp1, etIp2, etIp3, etIp4};
		for (final EditText et : ets) {
			et.setSelected(false);
			InputUtils.alwaysHideKeyBoard(et, InputType.TYPE_NUMBER_FLAG_SIGNED);
			et.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					keyboardNumber.setVisibility(View.VISIBLE);
				}
			});
			
			
		}
	}
	private String getRealIpPoint(String ipPoint) {
		if (ipPoint == null) {
			return "";
		}
		try {
			return String.valueOf(Integer.parseInt(ipPoint));
		} catch (Exception e) {
			return "";
		}
	}
	
	private boolean focusNext() {
		if (currentIndex < 3) {
			EditText et = ets[currentIndex + 1];
			et.setFocusable(true);
			et.setFocusableInTouchMode(true);
			et.requestFocus();
			et.setTextKeepState(et.getText());
			et.setSelection(et.getText().length());
			return true;
		}
		return false;
	}
	
	private boolean focusBack() {
		if (currentIndex > 0) {
			EditText et = ets[currentIndex - 1];
			et.setFocusable(true);
			et.setFocusableInTouchMode(true);
			et.requestFocus();
			et.setSelection(et.getText().toString().length());
			return true;
		}
		return false;
	}
	
	
	private void initEvent() {
		
		String[] points = this.currentIp.split("\\.");
		int index = 0;
		for (String s : points) {
			ets[index].setText(this.getRealIpPoint(s));
			index++;
		}
		
		for (index = 0; index < ets.length; index++) {
			ets[index].setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						
						keyboardNumber.setVisibility(View.VISIBLE);
						AbstractKeyBoardListener listener = (AbstractKeyBoardListener) keyboardNumber.getKeyBoardListener();
						listener.setTargetView(IpEditText.this);
						
						if (v.getId() == R.id.et_ip_1) {
							currentIndex = 0;
						} else if (v.getId() == R.id.et_ip_2) {
							currentIndex = 1;
						} else if (v.getId() == R.id.et_ip_3) {
							currentIndex = 2;
						} else if (v.getId() == R.id.et_ip_4) {
							currentIndex = 3;
						}
						LoggerUtils.i("currentIpPoint:"+currentIpPoint);
						currentIpPoint = ((EditText)v).getText().toString();
						LoggerUtils.i("currentIpPoint:"+currentIpPoint);
					} else {
						currentIp = getIp();
						stringValueChangeListener.onChange(currentIp);
					}
					
				}
			});
			
		}
		
	}
	
	public void onKeyClick(int code) {
		switch (code) {
		case KeyboardNumber.K_CANCEL:
			break;
		case KeyboardNumber.K_CLEAN:
			currentIpPoint = "";
			onChangeText(currentIpPoint);
			break;
		case KeyboardNumber.K_BACKSPACE:
			if (currentIpPoint.equals("")) {
				focusBack();
			}
			if (currentIpPoint.length() > 0) {
				
				int index = ets[currentIndex].getSelectionStart();
				String temp = index > 0 ? currentIpPoint.substring(0, index - 1) : "";
				temp += currentIpPoint.substring(index);
				currentIpPoint = temp;
				
				onChangeText(currentIpPoint);
			
			}
			break;
		case KeyboardNumber.K_ENTER:
			keyboardNumber.setVisibility(View.GONE);
			
			// 这一步是用来取消焦点的，不知道是否可行
			ets[currentIndex].setFocusable(false);
			ets[currentIndex].setFocusableInTouchMode(false);
			ets[currentIndex].requestFocus();
			break;
		case KeyboardNumber.K_DOT:
			focusNext();
			break;
		default:
				int index = ets[currentIndex].getSelectionStart();
				String value = currentIpPoint.substring(0, index) + (char) code + currentIpPoint.substring(index);
			
				if (currentIpPoint.length() < 3 && Integer.parseInt(value) <= 255) {

					onChangeText(value);
				} else if (currentIpPoint.length() == 3) {
					if (focusNext()) {
						onChangeText("" + (char)code);
					}
				}
			break;
		
		}
	}
	
	private void onChangeText(String text) {
		
		int index = ets[currentIndex].getSelectionStart();
		int count = ets[currentIndex].getText().length();
		ets[currentIndex].setTextKeepState(text);
		LoggerUtils.i("refreshEditTextSelection:"+index+",text:"+ets[currentIndex].getText().toString());
		if (text.length() > 0) {
			if(text.length() < count ) {
			ets[currentIndex].setSelection(text.length());
		} else if(text.length() > count) {
			ets[currentIndex].setSelection(index + 1);
		} else {
			ets[currentIndex].setSelection(index);
			}
		} else {
			ets[currentIndex].setSelection(0);
		}
		
		
		currentIpPoint = text;
//		ets[currentIndex].setTextKeepState(text);
//		ets[currentIndex].setSelection(text.length());
	}
	
	
	public String getIp() {
		return etIp1.getText().toString() + "." + 
				etIp2.getText().toString() + "." + 
				etIp3.getText().toString() + "." + 
				etIp4.getText().toString();
	}
	
	/**
	 * 设置IP（格式：192.168.1.1）
	 * @param ip
	 */
	public void setIp(String ip) {
		if (ip != null) {

			String[] ips = ip.split("."); 
			if (ips.length == 4) {
				etIp1.setText(ips[0]);
				etIp2.setText(ips[1]);
				etIp3.setText(ips[2]);
				etIp4.setText(ips[3]);
			}
		}
	}
	
	public void setFocus() {
		for (EditText et : ets) {
			if (et.getText().toString().trim().equals("") || et.getId() == R.id.et_ip_4) {
				et.setFocusable(true);
				et.setFocusableInTouchMode(true);
				et.requestFocus();
				et.setTextKeepState(et.getText().toString());
//				et.setSelection(et.getText().toString().length());
				break;
			}
		}
	}
}
