package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 输入凭证号
 * 
 * @author CB
 * @time 2015-4-17 下午4:23:40
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class InputInfoFragment extends BaseFragment {

	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyBoardNumber;
	/** 提示*/
//	@ViewInject(R.id.txt_input_info_content)
	TextView txtContent;
	/** 输入框*/
//	@ViewInject(R.id.et_input_info_input)
	EditText editInput;
	
//	@ViewInject(R.id.txt_short_content)
	TextView txtShortContent;
	
	private InputInfoBean bean;
	
	/**输出数据*/
	private String result = "";
	
	/**输入最大长度*/
	private int maxLen;
	
	/**最小长度*/
	private int minLen;
	
	/**输入超时时间，单位s*/
	@SuppressWarnings("unused")
	private int timeOut;
	
	/**Title*/
	private String title;
	
	/**引导信息(请输入流水号)*/
	private String content;
	
	/**简短提示*/
	private String shortContent;
	
	/**输入模式*/
	private int mode;
	
	/**允许空标记*/
	private boolean emptyFlag;
	
	private AbstractKeyBoardListener listener ;
	
	public InputInfoFragment(long timeOut) {
		super(timeOut);
	}

	public static InputInfoFragment newInstance(InputInfoBean infoBean) {
		InputInfoFragment fragment = new InputInfoFragment(infoBean.getTimeOut());
		fragment.bean = infoBean;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.input_info_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		title = bean.getTitle();
		content = bean.getContent();
		maxLen = bean.getMaxLen();
		minLen = bean.getMinLen();
		timeOut = bean.getTimeOut();
		mode = bean.getMode();
		emptyFlag = bean.getEmptyFlag();
		shortContent = bean.getShortContent();
		
		setTitle(title);
		txtContent.setText(content);
		editInput.setHint(content);
		editInput.setCursorVisible(true);
		editInput.setSelection(0);
		editInput.setText(result);
		InputUtils.setMaxLength(editInput, maxLen);
		
		InputFilter[] filters = {new InputFilter.LengthFilter(maxLen)};
		editInput.setFilters(filters);
		
		if(StringUtils.isEmpty(shortContent)){
			txtShortContent.setVisibility(View.GONE);
		} else{
			txtShortContent.setText(shortContent);
		}
		
		switch(mode){
		case InputInfoBean.INPUT_MODE_NUMBER:
			//editInput.setInputType(InputType.TYPE_NULL);
			hideSoftInputMethod(editInput); 
			keyBoardNumber.setVisibility(View.VISIBLE);
			keyBoardNumber.setEnterNotGone();

			//键盘输入事件
			listener = new AbstractKeyBoardListener() {
				
				@Override
				public void onEnter() {
					if (!emptyFlag) {
						if (StringUtils.isEmpty(result)) {
							ToastUtils.show(activity, getString(R.string.common_input_not_allow_empty));
							return;
						}
					}
					if (!StringUtils.isEmpty(result) && result.length() < minLen) {
						if(mode != InputInfoBean.INPUT_MODE_PASSWD){
							// show something
							ToastUtils.show(activity, getString(R.string.common_input_not_enough));
						}
						return;
					}
					if (result.length() > maxLen){
						return;
					}
					bean.setResult(result);
					onSucess();
				}
				
				@Override
				public void onChangeText(String value, int CursorIndex) {
					LoggerUtils.d("key value:" + value);
					result = value;
					editInput.setText(result);
					editInput.setSelection(CursorIndex);
				}
				
				@Override
				public void onClick(int code) {
					String tmp = result + (char)code;
					if(tmp.length() > maxLen){
						return;
					}
					super.onClick(code);
				}
				
//				@Override
//				public void onCancel() {
//					onBack();
//					//this.onClear();
//				}
				
			};
			listener.setEditText(editInput);
			keyBoardNumber.setKeyBoardListener(listener);
			//keyBoardNumber.setTelephoneKeyDisplay(false);
			break;
			
		case InputInfoBean.INPUT_MODE_PASSWD:
			//editInput.setPressed(true);
			editInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			//不break接着走
		case InputInfoBean.INPUT_MODE_ASCII:
			editInput.addTextChangedListener(new TextWatcher() {
				private String tmp;
				//private static final String validStr = "[\u007F-\uFFFF]";
				Pattern pattern = Pattern.compile("[\\u007F-\\uFFFF]+");
		
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					tmp = arg0.toString();
					activity.resetProgress();
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					String s = arg0.toString();
					if(s.equals(tmp)){
						return;
					}
					if(pattern.matcher(s).find()){
						editInput.setText(tmp);
					}
				}
			});
			
			editInput.setFocusable(true);
			editInput.setFocusableInTouchMode(true);
			editInput.requestFocus();
			InputUtils.setKeyBoardStatus(editInput, InputUtils.STATUS_OPEN);
			break;
			
		case InputInfoBean.INPUT_MODE_STRING:
			
			editInput.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					activity.resetProgress();
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
				}
			});
			
			editInput.setFocusable(true);
			editInput.setFocusableInTouchMode(true);
			editInput.requestFocus();
			InputUtils.setKeyBoardStatus(editInput, InputUtils.STATUS_OPEN);
			break;
		}

	}

	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	protected void initEvent() {
		
		editInput.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView view, int action, KeyEvent event) {
				
				LoggerUtils.d("onEditorAction action:" + action + " event:" + event);
				if ( action == EditorInfo.IME_ACTION_DONE || 
						action == EditorInfo.IME_ACTION_SEND || 
						(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					LoggerUtils.d("click done");
					result = editInput.getText().toString();
					if (!emptyFlag) {
						if (StringUtils.isEmpty(result)) {
							ToastUtils.show(context, getString(R.string.common_input_not_allow_empty));
							return true;
						}
					}
					if (!StringUtils.isEmpty(result) && StringUtils.length(result) < minLen) {
						LoggerUtils.d("result.length() < minLen");
						if(mode != InputInfoBean.INPUT_MODE_PASSWD){
							// show something
							ToastUtils.show(context, getString(R.string.common_input_not_enough));
						}
						return true;
					}
					if (StringUtils.length(result) > maxLen){
						LoggerUtils.d("StringUtils.length(result) > maxLen :" + StringUtils.length(result));
						ToastUtils.show(context, getString(R.string.common_input_too_long));
						return true;
					}
					bean.setResult(result);
					onSucess();
					return true;
				}
				
				return false;
			}
		});
		
		
	}
	
	private void doEndThing(){
		InputMethodManager imm = (InputMethodManager)editInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(editInput.getWindowToken(), 0);
		}
		editInput.clearFocus();
	}
	
	@Override
	public void onSucess() {
		doEndThing();
		super.onSucess();
	}
	@Override
	public void onBack() {
		
		doEndThing();
		super.onBack();
	};
	
	@Override
	public void onTimeOut() {
		doEndThing();
		super.onTimeOut();
	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}

	public void hideSoftInputMethod(EditText ed){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if(currentVersion >= 16){
            methodName = "setShowSoftInputOnFocus";
        }
        else if(currentVersion >= 14){
            methodName = "setSoftInputShownOnFocus";
        }
        if(methodName == null){
            ed.setInputType(InputType.TYPE_NULL);  
        }
        else{
            Class<EditText> cls = EditText.class;  
            Method setShowSoftInputOnFocus;  
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);  
                setShowSoftInputOnFocus.invoke(ed, false); 
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }  
        }  
    }
}
