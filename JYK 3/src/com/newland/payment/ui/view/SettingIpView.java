package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.CommonThread;
import com.newland.base.util.DisplayUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.listener.StringValueChangeListener;

/**
 * 设置IP控件
 * 
 * @author CB
 * @date 2015-5-11
 * @time 下午10:46:54
 */
public class SettingIpView extends FrameLayout {

	/** 标题 */
//	@ViewInject(R.id.txt_title)
	private TextView txtTitle;
	/** Ip控件 */
//	@ViewInject(R.id.ip_edit_text)
	private IpEditText ipEditText;
	
	private KeyboardNumber keyboardNumber;
	private Context context;
	private String tempKey;

	public SettingIpView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingIpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SettingIpView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initView(context);
		initEvent();
	}

	private void initEvent() {
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ipEditText.setFocus();
			}
		});
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.setting_ip_view, this, true);
		ViewUtils.inject(this, view);
		

	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		txtTitle.setText(title);
	}

	public void setTitle(int title) {
		txtTitle.setText(title);
	}
	
	public String getIp() {
		return ipEditText.getIp();
	}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final String key, KeyboardNumber keyboard) {
		tempKey = key;
		keyboardNumber = keyboard;
		
		new CommonThread(new ThreadCallBack() {
			String value;
			@Override
			public void onMain() {
				
				ipEditText.initCommon(context, value, keyboardNumber, new StringValueChangeListener() {
					
					@Override
					public void onChange(String value) {
						new CommonThread(new ThreadCallBack() {
							
							@Override
							public void onMain() {
								
							}
							
							@Override
							public void onBackGround() {
								ParamsUtils.setString(tempKey, getIp());
							}
						}).start();
					}
				});
			}
			
			@Override
			public void onBackGround() {

				value = ParamsUtils.getString(key); 
			}
		}).start();
		
	}
	
	public void onKeyClick(int code) {
		ipEditText.onKeyClick(code);
	}

	public void setTitleWidth(int valueDp) {
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) txtTitle.getLayoutParams();
		params.width = DisplayUtils.dip2px(context, valueDp);
		txtTitle.setLayoutParams(params);
	}
}
