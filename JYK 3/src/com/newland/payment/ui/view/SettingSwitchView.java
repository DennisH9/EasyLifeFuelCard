package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.EmvFailWaterServiceImpl;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.listener.BooleanValueChangeListener;


/**
 * 开关
 *
 * @author CB
 * @date 2015-5-11 
 * @time 下午9:35:06
 */
public class SettingSwitchView extends FrameLayout{

	/** 标题 */
	private TextView txtTitle;
	/** 开关 */
	private SwitchView switchView;
	
	private Context context;
	private BooleanValueChangeListener booleanValueChangeListener;
	private String key;
	private boolean enable = true;

	public SettingSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SettingSwitchView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initView(context);
		initdata();
		initEvent();
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.setting_switch_view, this, true);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		switchView = (SwitchView) findViewById(R.id.switch_view);
	}

	private void initdata() {
	}

	private void initEvent() {
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (enable) {

					final boolean value = !switchView.getValue();
					switchView.setValue(value);
					new CommonThread(new ThreadCallBack(){
						@Override
						public void onBackGround() {
							ParamsUtils.setString(key, value ? "1" : "0");
						}

						@Override
						public void onMain() {
							if (booleanValueChangeListener != null) {
								booleanValueChangeListener.onChange(value);
							}
						}
						
					}).start();
				}
			}
		});
	}

	public boolean getValue() {
		return switchView.getValue();
	}

	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		txtTitle.setText(title);
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(int title) {
		txtTitle.setText(title);
	}

	/**
	 * 设置值改变监听器
	 * @param booleanValueChangeListener
	 */
	private void setValueChangeListener( BooleanValueChangeListener booleanValueChangeListener) {
		switchView.setBooleanValueChangeListener(booleanValueChangeListener);
	}
	
	/**
	 * 设置值改变监听器
	 * @param booleanValueChangeListener
	 */
	public void addValueChangeListener( BooleanValueChangeListener booleanValueChangeListener) {
		this.booleanValueChangeListener = booleanValueChangeListener;
	}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int resTitle, final String key) {
		setTitle(resTitle);
		switchView.setValue("1".equals(ParamsUtils.getString(key)) ? true : false); 
		this.key = key;
		setValueChangeListener(new BooleanValueChangeListener() {
			
			@Override
			public void onChange(final boolean value) {
				 

				new CommonThread(new ThreadCallBack(){
					@Override
					public void onBackGround() {
						ParamsUtils.setString(key, value ? "1" : "0");
					}

					@Override
					public void onMain() {
						if (booleanValueChangeListener != null) {
							booleanValueChangeListener.onChange(value);
						}
					}
					
				}).start();
			}
		});
	}

	/**
	 * 设置有流水时不可编辑0
	 */
	public void setWaterEnabled() {
		//终端存在流水，不允许修改批次号和凭证号
		new CommonThread(new ThreadCallBack() {
			int count;
			@Override
			public void onMain() {
				boolean enabled = count > 0 ? false : true;
				enable = enabled;
				if (!enabled) {
					setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ToastUtils.show(context, R.string.has_water_please_first_settlement);
						}
					});
				}
				switchView.setStyleEnabled(enabled);
				if (!enabled) {

					switchView.addOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ToastUtils.show(context, R.string.has_water_please_first_settlement);
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

}
