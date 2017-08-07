package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.listener.OnListDialogClickListener;
import com.newland.payment.ui.listener.StringValueChangeListener;

/**
 * 选择控件
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午3:39:40
 */
public class SettingChooseView extends FrameLayout{
	
	/** 标题 */
//	@ViewInject(R.id.txt_title)
	TextView txtTitle;
	/** 内容 */
//	@ViewInject(R.id.txt_content)
	TextView txtContent;

	private CommonDialog dialog;
	private Context context;
	private CommonListDialog listDialog;
	private boolean enabled = true;
	private StringValueChangeListener valueChangeListener;

	public SettingChooseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SettingChooseView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.setting_choose_view, this);
		ViewUtils.inject(this, view);
		this.context = context;
		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		txtContent = (TextView) view.findViewById(R.id.txt_content);

		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (enabled) {

					if (dialog != null) {
						dialog.show();
					} else if (listDialog != null) {
						listDialog.show();
					}
				}
			}
		});
	}
	
	public void dismissDialog(){
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		} 
		if (listDialog != null && listDialog.isShowing()) {
			listDialog.dismiss();
			listDialog = null;
		} 
	}
	
	/**
	 * 可设置显示字体大小
	 */
		public void setParamData(final int resTitle, 
				final int resItem1, final String valueItem1,
				final int resItem2, final String valueItem2, 
				final String key,int size) {

			txtTitle.setText(resTitle);

			dialog = new CommonDialog(context);
			dialog.setContent(resTitle);
			dialog.setSureListener(resItem1, new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					dialog.dismiss();
					txtContent.setText(resItem1);
					setParamValue(key, valueItem1);
				}
			},size);
			dialog.setCancelListener(resItem2, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					txtContent.setText(resItem2);
					setParamValue(key, valueItem2);
				}
			},size);
			
			new CommonThread(new ThreadCallBack(){
				String value;
				@Override
				public void onBackGround() {
					value = ParamsUtils.getString(key);
				}

				@Override
				public void onMain() {
					
					if (valueItem1.equals(value)) {
						txtContent.setText(resItem1);
					} else if (valueItem2.equals(value)) {
						txtContent.setText(resItem2);
					}
					
				}
				
			}).start();
			
		}
		
		
		/**
		 * 显示与设置参数值
		 * @param key
		 */
		public void setParamData(final int resContext, final int resItem1,
				final String valueItem1, final int resItem2,
				final String valueItem2, final String key,
				final StringValueChangeListener listener) {
			valueChangeListener = listener;
			txtTitle.setText(resContext);
			
			String[] items = new String[]{
					context.getString(resItem1),
					context.getString(resItem2),
			};
			
			txtTitle.setText(resContext);
			
			
			listDialog = new CommonListDialog(context, resContext, items,
					new OnListDialogClickListener() {
						
						@Override
						public void onClick(int position) {
							switch (position) {
							case 0:

								txtContent.setText(resItem1);
								setParamValue(key, valueItem1);
								break;
							case 1:

								txtContent.setText(resItem2);
								setParamValue(key, valueItem2);
								break;
							}
						}
			});

			new CommonThread(new ThreadCallBack(){
				String value;
				@Override
				public void onBackGround() {
					value = ParamsUtils.getString(key);
				}

				@Override
				public void onMain() {
					
					if (valueItem1.equals(value)) {
						listDialog.setCheck(0);
						txtContent.setText(resItem1);
					} else if (valueItem2.equals(value)) {
						listDialog.setCheck(1);
						txtContent.setText(resItem2);
					}
					
				}
				
			}).start();
			
		}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int resContext, final int resItem1,
			final String valueItem1, final int resItem2,
			final String valueItem2, final int resItem3,
			final String valueItem3, final String key) {
		txtTitle.setText(resContext);
		
		String[] items = new String[]{
				context.getString(resItem1),
				context.getString(resItem2),
				context.getString(resItem3)
		};
		
		listDialog = new CommonListDialog(context, resContext, items,
				new OnListDialogClickListener() {
					
					@Override
					public void onClick(int position) {
						switch (position) {
						case 0:

							txtContent.setText(resItem1);
							setParamValue(key, valueItem1);
							break;
						case 1:

							txtContent.setText(resItem2);
							setParamValue(key, valueItem2);
							break;
						case 2:

							txtContent.setText(resItem3);
							setParamValue(key, valueItem3);
							break;
						}
					}
		});

		new CommonThread(new ThreadCallBack(){
			String value;
			@Override
			public void onBackGround() {
				value = ParamsUtils.getString(key);
			}

			@Override
			public void onMain() {
				
				if (valueItem1.equals(value)) {
					listDialog.setCheck(0);
					txtContent.setText(resItem1);
				} else if (valueItem2.equals(value)) {
					listDialog.setCheck(1);
					txtContent.setText(resItem2);
				} else if (valueItem3.equals(value)) {
					listDialog.setCheck(2);
					txtContent.setText(resItem3);
				}
				
			}
			
		}).start();
		
	}
	
	/**
	 * 显示与设置参数值
	 * @param key
	 */
	public void setParamData(final int resTitle, 
			final int resItem1, final String valueItem1,
			final int resItem2, final String valueItem2, 
			final String key) {

		txtTitle.setText(resTitle);

		dialog = new CommonDialog(context);
		dialog.setContent(resTitle);
		dialog.setSureListener(resItem1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				txtContent.setText(resItem1);
				setParamValue(key, valueItem1);
			}
		});
		dialog.setCancelListener(resItem2, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				txtContent.setText(resItem2);
				setParamValue(key, valueItem2);
			}
		});
		
		new CommonThread(new ThreadCallBack(){
			String value;
			@Override
			public void onBackGround() {
				value = ParamsUtils.getString(key);
			}

			@Override
			public void onMain() {
				
				if (valueItem1.equals(value)) {
					txtContent.setText(resItem1);
				} else if (valueItem2.equals(value)) {
					txtContent.setText(resItem2);
				}
				
			}
			
		}).start();
		
	}
	
	
	

	public void initCommon(final int resTitle, 
			final int resItem1,
			final int resItem2, 
			final OnClickListener listener1, final OnClickListener listener2) {

		txtTitle.setText(resTitle);

		dialog = new CommonDialog(context);
		dialog.setContent(resTitle);
		dialog.setSureListener(resItem1, new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				listener1.onClick(v);
			}
		});
		dialog.setCancelListener(resItem2, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				listener2.onClick(v);
			}
		});
		
	}
	
	/**
	 * 保存值
	 * @param key
	 * @param value
	 */
	private void setParamValue(final String key, final String value) {
		new CommonThread(new ThreadCallBack(){
			@Override
			public void onBackGround() {
				ParamsUtils.setString(key, value );
			}

			@Override
			public void onMain() {
				if (valueChangeListener != null) {
					valueChangeListener.onChange(value);
				}
			}
			
		}).start();
	}
	
	public void setValueChangeListener(StringValueChangeListener valueChangeListener) {
		this.valueChangeListener = valueChangeListener;
	}

	/**
	 * 设置弹出dialog的宽度
	 * @param resDp
	 */
	public void setDialogWidth(int resDp) {
		if (dialog != null) {
			dialog.setWidth(resDp);
		}
	}
	
	public void setDisabled(final String msg) {
		enabled = false;
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.show(context, msg);
			}
		});
	}
	
	public void setContent(String content) {
		txtContent.setText(content);
	}
	
	

	public CommonDialog getDialog() {
		return dialog;
	}

	public void setDialog(CommonDialog dialog) {
		this.dialog = dialog;
	}
	

}
