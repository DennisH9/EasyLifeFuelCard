package com.newland.payment.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newland.base.CommonThread;
import com.newland.base.util.DisplayUtils;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.UserService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.EmvFailWaterServiceImpl;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.OperatorPasswordDialog.InputEventListener;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 设置界面开关控件
 * 
 * @author CB
 * @date 2015-5-11
 * @time 下午10:46:54
 */
public class SettingEditView extends FrameLayout {

	/** 标题 */
//	@ViewInject(R.id.txt_title)
	private TextView txtTitle;
	/** 内容 */
//	@ViewInject(R.id.et_content)
	private EditText etContent;
	/** 遮罩 */
//	@ViewInject(R.id.v_shade)
	private View vShade;
	/** 最大长度*/
//	@ViewInject(R.id.txt_max_size)
	private TextView txtMaxSize;
	/** 当前长度 */
//	@ViewInject(R.id.txt_current_size)
	private TextView txtCurrentSize;
	
	/** 安全密码弹出窗 */
	private OperatorPasswordDialog operatorPasswordDialog;
	private KeyboardNumber keyboardNumber;
	private StringValueChangeListener stringValueChangeListener;
	private StringValueChangeListener checkSuccessListener;
	private int maxLength = 999;
	private int minLength = 0;
	private int maxCharacter = 999;
	private List<String> valueList;
	/** 最大值 */
	private Long maxValue;
	/** 最小值 */
	private Long minValue;
	/** 指定长度 */
	private Integer fixLength;
	private Context context;
	private InputFilter[] inputFilters;
	private boolean enabled = true;
	/** shareperfence 设置值的KEY */
	private String setKey;
	/** 最小值提示 */
	private Integer resTipMinValue;
	
	/** 是否验证 */
	private boolean isCheck = true;
	
	public SettingEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SettingEditView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initView(context);
		initEvent();
	}

	
	private void initView(Context context) {
		txtTitle = (TextView) findViewById(R.id.txt_title);
		etContent = (EditText) findViewById(R.id.et_content);
		vShade = findViewById(R.id.v_shade);
		txtMaxSize = (TextView) findViewById(R.id.txt_max_size);
		txtCurrentSize = (TextView) findViewById(R.id.txt_current_size);

		valueList = new ArrayList<String>();
		View view = LayoutInflater.from(context).inflate(
				R.layout.setting_edit_view, this, true);
		ViewUtils.inject(this, view);
		inputFilters = new InputFilter[3];
		inputFilters[0] = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				return null;
			}
		};
		inputFilters[1] = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				return null;
			}
		};
		inputFilters[2] = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				
				LoggerUtils.i("source:"+source+",start:"+start+",end:"+end+",dest:"+dest
						+",dstart:"+dstart+",dend:"+dend);
				
				if (getCharacterNum(source.toString()+dest+"") <= maxCharacter) {
					return null;
				} else {
					return "";
				}
				
			}
		};
	}

	private void initEvent() {
		
		etContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (keyboardNumber != null) {
					keyboardNumber.setVisibility(View.VISIBLE);
				}
				setNormalStyle();		
			}
		});
		
		etContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (enabled) {
					if (hasFocus == false) {
						if (stringValueChangeListener != null) {
							stringValueChangeListener.onChange(etContent.getText().toString());
						}
						if (keyboardNumber != null) {
							keyboardNumber.setVisibility(View.GONE);
						} else {
							InputUtils.setKeyBoardStatus(etContent,
									InputUtils.STATUS_CLOSE);
						}
					} else {
						if (keyboardNumber != null) {
							InputUtils.setKeyBoardStatus(etContent,
									InputUtils.STATUS_CLOSE);
							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									keyboardNumber.setVisibility(View.VISIBLE);
								}

								@Override
								public void onBackGround() {
									try {
										Thread.sleep(600);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}).start();
							AbstractKeyBoardListener listener = ((AbstractKeyBoardListener) keyboardNumber
									.getKeyBoardListener());

							if (listener != null) {
								listener.setEditText(etContent);
								listener.setMaxLength(maxLength);
							}
						} else {
							InputUtils.setKeyBoardStatus(etContent,
									InputUtils.STATUS_OPEN);
						}
					}
				}
			}
		});
		
		etContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isCheck = false;
				MainActivity.getInstance().resetProgress();
//				etContent.setSelection(etContent.getText().toString().length());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {	
			}
		});

		vShade.setVisibility(View.GONE);
	}
	
	/**
	 * 设置出入值改变监听
	 * 
	 * @param valueChangeListener
	 */
	public void setValueChangeListener(StringValueChangeListener stringValueChangeListener) {
		this.stringValueChangeListener = stringValueChangeListener;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		txtTitle.setTextKeepState(title);
	}

	public String getTitle() {
		return txtTitle.getText().toString();
	}

	public void setHint(String hint) {
		etContent.setHint(hint);
	}

	public void setTitle(int title) {
		setTitle(context.getString(title));
	}

	public void setHint(int hint) {
		etContent.setHint(hint);
	}
	
	/**
	 * 获取输入框的值
	 * @return
	 */
	public String getValue() {
		return etContent.getText().toString();
	}

	public void setKeyboardNumber(KeyboardNumber keyboardNumber) {
		setKeyboardNumber(keyboardNumber, InputType.TYPE_CLASS_TEXT);
	}
	
	public void setKeyboardNumber(KeyboardNumber keyboardNumber, int inputType) {
		this.keyboardNumber = keyboardNumber;
		InputUtils.alwaysHideKeyBoard(etContent, inputType);
	}

	public void setInputType(int inputType) {
    	etContent.setInputType(inputType);
	}
	
	/**
	 * 设置最大长度
	 * @param size
	 */
	public void setMaxLength(int size) {
		maxLength = size;
		txtMaxSize.setTextKeepState("/" + maxLength);
		inputFilters[0] = new InputFilter.LengthFilter(maxLength);
		etContent.setFilters(inputFilters);
	}
	
	/**
	 * 获取焦点
	 */
	public void setFocus(){
		etContent.setCursorVisible(true);
		etContent.setFocusable(true);
		etContent.setFocusableInTouchMode(true);
		etContent.requestFocus();
//		etContent.setSelection(etContent.getText().length());
	}
	
	/**
	 * 设置内容输入框的值
	 * @param content
	 */
	public void setContent(String content) {
		if (etContent != null && content != null) {
			etContent.setTextKeepState(content);
		}
	}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData( final String key, int resTitle) {
		setTitle(resTitle);
		setParamData(key);
	}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(int resTitle,KeyboardNumber keyboardNumber, final String key) {
		setTitle(resTitle);
		setKeyboardNumber(keyboardNumber);
		setParamData(key);
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final String key ) {
		setParamData(key, null);	
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final String key, boolean isShowData) {
		setParamData(key, isShowData, null);	
	}

	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final String key , final StringValueChangeListener listener) {
		setParamData(key, true,listener);
		
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final String key , boolean isShowData, final StringValueChangeListener listener) {
		setParamData(999,key, isShowData,listener);
		
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int length, final String key) {
		setParamData(length, key, true, null);
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int length, final String key, final StringValueChangeListener valueChangeListener) {

		setParamData(length, key, true, valueChangeListener);
	}
	/**
	 * 验证值
	 * @return true-验证成功，false-验证失败
	 */
	public boolean disposeCheck() {
		disposeValue(etContent.getText().toString());
		return isCheck;
	}
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int length, String key, final boolean isShowDate, final StringValueChangeListener valueChangeListener) {
		setMaxLength(length);
		
		if (isShowDate) {
			setContent(ParamsUtils.getString(key)); 
		}

		setKey = key;
		setValueChangeListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(String value) {
				
				disposeValue(value);
				
				if (valueChangeListener != null) {
					valueChangeListener.onChange(value);
				}
			}
		});
	}
	
	
	
	/**
	 * 输入安全密码
	 */
	public void setSafePassword() {

			
			final String safePwd = ParamsUtils.getString(ParamsConst.PARAMS_SAFE_PASSWORD);
			operatorPasswordDialog = new OperatorPasswordDialog(context,R.style.swiping_dialog,6,R.string.setting_password_safe,new InputEventListener() {
				
				@Override
				public void onConfirm(Dialog dialog, String value) {
					if (operatorPasswordDialog.getPassword().equals(safePwd)) {
						operatorPasswordDialog.dismiss();
						vShade.setVisibility(View.GONE);
						setFocus();
						if (keyboardNumber == null) {
							InputUtils.showKeyboard(etContent);
							InputUtils.setKeyBoardStatus(etContent, InputUtils.STATUS_OPEN);
						}
					} else {
						operatorPasswordDialog.dismiss();
						ToastUtils.show(context, R.string.error_password);
					}				
				}
				
				@Override
				public void onCancel() {
					
				}

			});

			vShade.setVisibility(View.VISIBLE);
			vShade.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (enabled) {
						operatorPasswordDialog.show();
						MainActivity.getInstance().commonDialog = operatorPasswordDialog;
						InputUtils.hideKeyboard(etContent);
					}
				}
			});
		
	}
	
	/**
	 * 设置需要输入管理员密码
	 */
	public void setManagePassword() {
		operatorPasswordDialog = new OperatorPasswordDialog(context,R.style.swiping_dialog,6,R.string.setting_password_safe,new InputEventListener() {
			
			@Override
			public void onConfirm(Dialog dialog, String value) {
				
				new CommonThread(new ThreadCallBack() {
					int result;
					@Override
					public void onBackGround() {
						UserService service = new UserServiceImpl(context);
						result = service.checkLogin("00", operatorPasswordDialog.getPassword());
					}

					@Override
					public void onMain() {
						if (result == 2) {
							operatorPasswordDialog.dismiss();
							setFocus();
							if (keyboardNumber == null) {
								InputUtils.showKeyboard(etContent);
								InputUtils.setKeyBoardStatus(etContent, InputUtils.STATUS_OPEN);
							}
						} else {
							operatorPasswordDialog.dismiss();
							ToastUtils.show(context, R.string.error_password);
						}	
					}
					
				}).start();
				
							
			}
			
			@Override
			public void onCancel() {
				
			}

		});
		vShade.setVisibility(View.VISIBLE);
		vShade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (enabled) {
					operatorPasswordDialog.show();
					MainActivity.getInstance().commonDialog = operatorPasswordDialog;
					InputUtils.hideKeyboard(etContent);
				}
			}
		});
	}
	
	/**
	 * 设置错误样式
	 */
	public void setErrorStyle() {
		txtTitle.setTextColor(Color.parseColor("#ff0000"));
		etContent.setTextColor(Color.parseColor("#ff0000"));
	}
	
	/**
	 * 设置正常样式
	 */
	public void setNormalStyle() {
		txtTitle.setTextColor(Color.parseColor("#333333"));
		etContent.setTextColor(Color.parseColor("#333333"));
	}

	/**
	 * 设置最大值
	 */
	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * 设置最小值
	 */
	public void setMinValue(long minValue) {
		this.minValue = minValue;
	}
	/**
	 * 设置最小值
	 */
	public void setMinValue(long minValue,Integer resTip) {
		this.minValue = minValue;
		if (resTip  != null) {
			resTipMinValue = resTip;
		}
	}
	
	public Integer getFixLength() {
		return fixLength;
	}

	public void setFixLength(Integer fixLength) {
		this.fixLength = fixLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	
	public Editable getContent() {
		return etContent.getText();
	}
	
	/**
	 * 是否通过合法性验证
	 * @return
	 */
	public boolean isCheck() {
		return isCheck;
	}
	/**
	 * 
	 * 设置合法参数
	 */
	public void addValueList(String value) {
		valueList.add(value);
	}
	
	private void disposeValue(final String value){

		if (valueList != null && valueList.contains(value)) {
			LoggerUtils.d("disposeValue->值为[" + value + "]直接跳过检查");
			setSuccess(value);
			return;
		}
		
		if (null == value){
			setErrorStyle();
			ToastUtils.show(context, R.string.error_data);
			return ;
		}
			
		//非空检查
		if (minValue != null || minLength > 0){
			if (StringUtils.isNullOrEmpty(value)){
				setErrorStyle();
				ToastUtils.show(context, R.string.error_not_null);
				return;
			}
		}
		
		//指定长度检查
		if (fixLength != null && value.length() != fixLength) {
			setErrorStyle();
			ToastUtils.show(context, context.getString(R.string.error_format_length, fixLength + ""));
			return;
		}
		
		//最小长度检查
		if (minLength > 0){
			if (value.length() < minLength){
				setErrorStyle();
				ToastUtils.show(context, context.getString(R.string.error_min_length, minLength + ""));
				return;
			}
		}
		
		//最大长度检查
		if (getCharacterNum(value) > maxCharacter) {
			setErrorStyle();
			LoggerUtils.i("getCharacterNum:"+getCharacterNum(value));
			ToastUtils.show(context, context.getString(R.string.error_max, maxCharacter+""));
			return;
		}
		
		//最小值检查
		if (minValue != null){
			if (Integer.valueOf(value) < minValue){
				setErrorStyle();
				ToastUtils.show(context, context.getString(resTipMinValue == null ? R.string.error_min : resTipMinValue, minValue + ""));
				return;
			}
		}
		
		//最大值检查
		if (maxValue != null){
			if (Integer.valueOf(value) > maxValue){
				setErrorStyle();
				ToastUtils.show(context, context.getString(R.string.error_max, maxValue+""));
				return;
			}
		}
		
		setSuccess(value);
		
	}
	
	private void setSuccess(final String value) {
		setNormalStyle();
		isCheck = true;
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				
			}
			
			@Override
			public void onBackGround() {
				ParamsUtils.setString(setKey, value);
				
			}
		}).start();
		
		if (checkSuccessListener != null) {
			checkSuccessListener.onChange(value);
		}
	}
	
	public void setTitleWidth(int valueDp) {
		ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtTitle.getLayoutParams();
		params.width = DisplayUtils.dip2px(context, valueDp);
		txtTitle.setLayoutParams(params);
	}
	
	public void setDigits(final String digits) {
		inputFilters[1] = DigitsKeyListener.getInstance(digits);
		etContent.setFilters(inputFilters);
	}
	
	/**
	 * 显示最大长度和当前长度
	 */
	public void showTextSize() {
		txtCurrentSize.setVisibility(View.VISIBLE);
		txtMaxSize.setVisibility(View.VISIBLE);
		txtMaxSize.setTextKeepState("/" + maxLength);
		etContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				txtCurrentSize.setTextKeepState(s.length()+"");
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
	
	/**
	 * 设置输入框是否可用
	 */
	public void setEditTextEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			etContent.setTextColor(Color.parseColor("#333333"));
			etContent.setSelection(etContent.getText().length());
		} else {
			etContent.setTextColor(Color.parseColor("#7B7B7B"));
		}
	}
	
	/**
	 * 设置有流水时不可编辑
	 */
	public void setWaterEnabled() {
		setWaterEnabled(null);
	}

	/**
	 * 设置有流水时不可编辑
	 */
	public void setWaterEnabled(final int message) {
		setWaterEnabled(context.getString(message));
	}
	/**
	 * 设置有流水时不可编辑
	 */
	public void setWaterEnabled(final String message) {
		//终端存在流水，不允许修改批次号和凭证号
		
		new CommonThread(new ThreadCallBack() {
			int count;
			@Override
			public void onMain() {
				boolean enable = count > 0 ? false : true;
				LoggerUtils.i("water count:" + count);
				setEditTextEnabled(enable);
				if (!enable && message != null) {

					vShade.setVisibility(View.VISIBLE);
					vShade.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ToastUtils.show(context, message);
						}
					});
				}
			}
			
			@Override
			public void onBackGround() {
				WaterService waterService = new WaterServiceImpl(context);
				EmvFailWaterService failWaterService = new EmvFailWaterServiceImpl(context);
				count = waterService.getWaterCount();
				count += failWaterService.getCount();
			}
		}).start();
	}
	
	/**
	 * 设置最大字符
	 */
	public void setMaxCharacter(int maxCharacter) {
		this.maxCharacter = maxCharacter;
	}
	
	/**
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     * @param content
     * @return
     */
    private int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
    * @description 返回字符串里中文字或者全角字符的个数
    * @param s
    * @return
    */
    private int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }
    
    /**
     * 是否显示系统键盘
     */
    public boolean isShwoSystemKeyboard() {
    	return InputUtils.isShowKeyBoard(etContent);
    }
    
    /**
     * 关闭系统键盘
     */
    public void hideSystemKeyboard() {
    	InputUtils.hideKeyboard(etContent);
    }

	public void setCheckSuccessListener(
			StringValueChangeListener checkSuccessListener) {
		this.checkSuccessListener = checkSuccessListener;
	}
	
	public void setEditTextEnabledNoChangeColor(boolean enabled) {
		this.enabled = enabled;
		etContent.setEnabled(enabled);
	}
    
}
