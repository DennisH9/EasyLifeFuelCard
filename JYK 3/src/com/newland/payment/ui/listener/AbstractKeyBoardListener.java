package com.newland.payment.ui.listener;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.newland.base.CommonThread;
import com.newland.base.util.InputUtils;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.util.LoggerUtils;


public class AbstractKeyBoardListener implements IKeyBoardListener {
	
	private String value = "";
	private int maxLength = 999;
	private View targetView;

	public AbstractKeyBoardListener() {
	}

	public AbstractKeyBoardListener(int maxLength) {
		this.maxLength = maxLength;
	}

	public AbstractKeyBoardListener(int maxLength, View editText) {
		
		this(editText,InputType.TYPE_CLASS_NUMBER,maxLength);
	}

	public AbstractKeyBoardListener(View editText) {
		this(editText,InputType.TYPE_NUMBER_FLAG_SIGNED,null);
	}
	public AbstractKeyBoardListener(View editText,int inputType) {
		this(editText,inputType,null);
	}
	
	public AbstractKeyBoardListener(View editText1,Integer inputType,Integer maxLength){
		this.targetView = editText1;
		if (editText1 != null && editText1 instanceof EditText) {
			final EditText editText = (EditText) editText1;
			editText.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					value = s.toString();
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			
			if (inputType != null) {
				InputUtils.alwaysHideKeyBoard(editText, inputType);
			}
		}
		if (maxLength != null) {
			this.maxLength = maxLength;
		}
	}
	

	/**
	 * 按下时的事件
	 * 
	 * @param code
	 *            ascill码的值
	 * @return true-自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
	 */
	public void onClick(int code) {
		if (value.length() < maxLength) {
			
			if (targetView instanceof EditText && targetView.isFocused()) {
				int index = ((EditText)targetView).getSelectionStart();

				value = value.substring(0, index) + (char) code + value.substring(index);
				onKeyClick(code);
				onChangeText(value,index + 1);
				return;
			} else {
				value += (char) code;
			}
			
		}
		
		onKeyClick(code);
		if (targetView instanceof EditText) {
			onChangeText(value,value.length());
		}
		else
		{
		onChangeText(value);
		}
	}

	/**
	 * 退格事件
	 * 
	 * @return 自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
	 */
	public boolean onBackspeace() {

		int index = value.length();
		if (value.length() > 0) {
			if ( targetView instanceof EditText) {
				EditText editText = (EditText)targetView;
				LoggerUtils.i("isFocusable:"+editText.isFocusable()
						+",isFocused:"+editText.isFocused());
				if (!editText.isFocused()) {
					//无焦点输入框
					value = value.substring(0, value.length() - 1);
				} else {
					index = editText.getSelectionStart();
					String temp = index > 0 ? value.substring(0, index - 1) : "";
					temp += value.substring(index);
					value = temp;
				}
			} else {
				value = value.substring(0, index - 1);
			}
			
		} else {
			value = "";
		}
		
		if (targetView instanceof EditText) {
			LoggerUtils.i("value:" + ((EditText)targetView).getText().toString() + ",getSelectionStart"
					+ ((EditText) targetView).getSelectionStart()
					+ ",getSelectionEnd: "
					+ ((EditText) targetView).getSelectionEnd());
		}
		onKeyClick(KeyboardNumber.K_BACKSPACE);
		if ( targetView instanceof EditText) {
			index = ((EditText)targetView).getSelectionStart() - 1;
			if(index < 0)
				index = 0;
			if(value.length() < index)
				index = value.length();
			onChangeText(value,index);
		}
		else
		{
		onChangeText(value);
		}
		return false;
	}

	/**
	 * 清除事件
	 * 
	 */
	public void onClear() {
		value = "";
		onKeyClick(KeyboardNumber.K_CLEAN);
		onChangeText(value,0);
	}
	
	public String getValue() {
		return value;
	}
	/**
	 * 取消事件
	 * 
	 */
	public void onCancel() {
		Handler handler = App.getInstance().getAppHanlder();
		Message msg = handler.obtainMessage(0x533);
		msg.obj = 0xd1;
		handler.sendMessage(msg);
		onKeyClick(KeyboardNumber.K_CANCEL);
		
	}

	/**
	 * 设置键盘的值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
		LoggerUtils.i("keyboard value:" + value);
	}

	/**
	 * 设置最大长度
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * 当前输入的所有文本
	 * 
	 * @param text
	 */
	public void onChangeText(String text, int CursorIndex){
		refreshEditText();
	}
	
	/**
	 * 当前输入的单个字符
	 * @param key
	 */
	public void onKeyClick(int key) {
		
	}

	/**
	 * 确认事件
	 */
	public void onEnter(){
		onKeyClick(KeyboardNumber.K_ENTER);
	}
	
	/**
	 * 设置目标输出对象
	 * @param view
	 */
	public void setTargetView(View view){
		targetView = view;
		if (view instanceof EditText) {
			final EditText editText = (EditText) view;
			value = editText.getText().toString();
			
			LoggerUtils.i("editText.getText:" + editText.getText());
			
			//这里必须延迟加载，特殊处理代码控制输入框1改变时焦点跳转输入框2导致值为输入框1的值
			new CommonThread(new ThreadCallBack(){

				@Override
				public void onBackGround() {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onMain() {
					editText.addTextChangedListener(new TextWatcher() {
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							
							try {
								
								LoggerUtils.i("start:" + start + ",before:" + before + ",count:" + count);
								editText.setSelection(before);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {
							
						}
						
						@Override
						public void afterTextChanged(Editable s) {
						}
					});
				}
				
			});
			
		}
	}
	
	/**
	 * 设置输入框监听
	 * @param editText
	 */
	public void setEditText(EditText et) {
		setTargetView(et);
	}
	
	private void refreshEditText() {
		
		if (targetView != null && targetView instanceof EditText) {
			EditText editText = (EditText)targetView;
			int index = editText.getSelectionStart();
			int count = editText.getText().length();
			editText.setTextKeepState(value);
			LoggerUtils.i("refreshEditText -> value:"+value+",index:"+index+",count:"+count);
			if (value.equals("")) {
			} else if(value.length() < count) {
				editText.setSelection(index - 1);
			} else if(value.length() > count) {
				editText.setSelection(index + 1);
			} else {
				editText.setSelection(index);
			}
		}
	}

	public View getTargetView() {
		return targetView;
	}
	@Override
	public void onChangeText(String text) {
		if (targetView != null && targetView instanceof EditText) {
			EditText editText = (EditText)targetView;
			editText.setText(text);
			refreshEditText();
		}
	}
	
	
}
