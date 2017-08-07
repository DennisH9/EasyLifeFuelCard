package com.newland.payment.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.listener.LongValueChangeListener;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 设置界面日期控件
 * 
 * @author CB
 * @date 2015-5-11
 * @time 下午10:46:54
 */
public class SettingDateView extends FrameLayout {

	/** 标题 */
//	@ViewInject(R.id.txt_title)
	private TextView txtTitle;
	/** 内容 */
//	@ViewInject(R.id.txt_content)
	private TextView txtContent;
	
	/** 日期选择dialog */
	private Dialog dialog;
	private Context context;
	private LongValueChangeListener valueChangeListener;
	private int dateType;

	public SettingDateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SettingDateView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.setting_date_view, this, true);
		ViewUtils.inject(this, view);
	}

	private void initData() {
		dateType = Const.SettingDateType.DATE;
	}

	private void initEvent() {
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog == null) {
					if (dateType == Const.SettingDateType.TIME) {
						dialog = MessageUtils.showCommonTimeDialog(context, 
								txtTitle.getText().toString(), System.currentTimeMillis(), new LongValueChangeListener() {
									
									@Override
									public void onChange(long value) {
										txtContent.setText(TimeUtils.getFormatTime(value, "HH:mm:ss"));
										if (valueChangeListener != null) {
											valueChangeListener.onChange(value);
										}
									}
								});
					} else {
						dialog = MessageUtils.showCommonDateDialog(context, 
								txtTitle.getText().toString(), new LongValueChangeListener() {
									
									@Override
									public void onChange(long value) {
										txtContent.setText(TimeUtils.getFormatTime(value, "yyyy-MM-dd"));
										if (valueChangeListener != null) {
											valueChangeListener.onChange(value);
										}
									}
								});
					}
					
				} else {
					dialog.show();
				}
			}
		});
	}

	public void dismissDialog(){
		if (dialog!=null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
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

	public void setContent(String title) {
		txtContent.setText(title);
	}
	/**
	 * 设置值改变事件
	 * @param valueChangeListener
	 */
	public void setValueChangeListener(LongValueChangeListener valueChangeListener){
		this.valueChangeListener = valueChangeListener;
	}
	
	public void setTimeStyle() {
		dateType = Const.SettingDateType.TIME;
	}
}
