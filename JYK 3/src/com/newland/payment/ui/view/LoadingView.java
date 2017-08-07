package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 加载控件
 *
 * @author CB
 * @date 2015-5-14 
 * @time 上午11:31:09
 */
public class LoadingView extends FrameLayout{
	
	/** 主布局 */
//	@ViewInject(R.id.ll_main)
	LinearLayout llMain;

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadingView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view =View.inflate(context, R.layout.loading_view, this);
		ViewUtils.inject(this, view);
		llMain = (LinearLayout)view.findViewById(R.id.ll_main);
		int count = llMain.getChildCount();
		Animator[] animators = new Animator[count];
		AnimatorSet animatorSet = new AnimatorSet();
		
		for (int i = 0; i < count; i++) {

			ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llMain.getChildAt(i), "translationY", 0, -30, 0, 15, 0);
			
			objectAnimator.setRepeatCount(ValueAnimator.INFINITE);  
			objectAnimator.setStartDelay(i * 140);
			objectAnimator.setDuration(1800);
			animators[i] = objectAnimator;
			
		}
		animatorSet.playTogether(animators);
		animatorSet.start();
		
	}

}
