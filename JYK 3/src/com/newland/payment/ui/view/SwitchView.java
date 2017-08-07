package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.BooleanValueChangeListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 开关
 *
 * @author CB
 * @date 2015-5-11 
 * @time 下午9:35:06
 */
public class SwitchView extends FrameLayout{
	/** 按钮背景 */
	private ImageView ivSwitchBg;
	/** 按钮 */
	private ImageView ivSwitch;
	
	private BooleanValueChangeListener booleanValueChangeListener;
	private OnClickListener addOnClickListener;
	private boolean value;
	private boolean enabled = true;

	public SwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SwitchView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		initView(context);
		initdata();
		initEvent();
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.switch_view, this, true);
		ivSwitch = (ImageView) findViewById(R.id.iv_switch);
		ivSwitchBg = (ImageView) findViewById(R.id.iv_switch_bg);
	}

	private void initdata() {
		value = false;
	}

	private void initEvent() {
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (enabled) {

					if (value) {
						ivSwitchBg.setImageResource(R.drawable.switch_off);

						AnimatorSet animatorSet = new AnimatorSet();
						if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
							animatorSet.playTogether(ObjectAnimator.ofFloat(
									ivSwitch, "translationX", 29, 0));
						} else {

							animatorSet.playTogether(ObjectAnimator.ofFloat(
									ivSwitch, "translationX", 29, 0));
						}
						animatorSet.setDuration(200);
						animatorSet.start();
					} else {
						ivSwitchBg.setImageResource(R.drawable.switch_on);

						AnimatorSet animatorSet = new AnimatorSet();
						animatorSet.playTogether(ObjectAnimator.ofFloat(ivSwitch, "translationX", 0, 29));
						animatorSet.start();
					}
					value = !value;
					if (booleanValueChangeListener != null) {
						booleanValueChangeListener.onChange(value);
					}
				} 
				if (addOnClickListener != null) {
					addOnClickListener.onClick(v);
				}
			}
		});
	}

	public boolean getValue() {
		return value;
	}
	

	public void setValue(boolean value) {
		ivSwitchBg.setImageResource(value ? R.drawable.switch_on : R.drawable.switch_off);
		if (!this.value && value) {
			ObjectAnimator.ofFloat(
					ivSwitch, "translationX", 0, 29).setDuration(200).start();
		} else if (this.value && !value) {
			ObjectAnimator.ofFloat(
					ivSwitch, "translationX", 0).setDuration(200).start();
		}
		

		this.value = value;
	}

	/**
	 * 设置值改变监听器
	 * @param booleanValueChangeListener
	 */
	public void setBooleanValueChangeListener(
			BooleanValueChangeListener booleanValueChangeListener) {
		this.booleanValueChangeListener = booleanValueChangeListener;
	}
	
	public void setStyleEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void addOnClickListener(OnClickListener listener) {
		addOnClickListener = listener;
	}
}
