package com.newland.payment.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.RemindViewCloseListener;

/**
 * 三个图标提示界面
 *
 * @author CB
 * @date 2015-5-12 
 * @time 下午9:29:15
 */
@SuppressLint("ViewConstructor")
public class CardThreeRemindView extends FrameLayout{
	/** 抬头提示信息 */
//	@ViewInject(R.id.tv_tip_msg)
	TextView tvTipMsg;
	/** 关闭图标 */
//	@ViewInject(R.id.iv_close)
	ImageView ivClose;
	
	public CardThreeRemindView(Context context, String content,
			boolean isSupportHandInput, 
			final RemindViewCloseListener listener) {
		super(context);
		
		View view = View.inflate(context, R.layout.card_three_remind_view, this);
		ViewUtils.inject(this, view);
		tvTipMsg = (TextView)view.findViewById(R.id.tv_tip_msg);
		ivClose = (ImageView)view.findViewById(R.id.iv_close);

		MainActivity.getInstance().hideTitle();
		
		if(isSupportHandInput) {
			tvTipMsg.setText(content+getResources().getString(R.string.swipe_card_how_to_input_or_hand_input));
			
		} else {
			tvTipMsg.setText(content);
			ivClose.setVisibility(View.GONE);
		}
		
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				MainActivity.getInstance().showTitle();
				listener.onCloseEvent();
			}
		});
	}

}
