package com.newland.payment.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.RemindViewCloseListener;

/**
 * 一个图标提示界面
 *
 * @author CB
 * @date 2015-5-12 
 * @time 下午9:29:15
 */
@SuppressLint("ViewConstructor")
public class CardRemindView extends FrameLayout{

	/** 抬头提示信息 */
//	@ViewInject(R.id.tv_tip_msg)
	TextView tvTipMsg;
	/** 关闭图标 */
//	@ViewInject(R.id.iv_icon)
	ImageView ivIcon;
	/** 提示文字图片 */
//	@ViewInject(R.id.iv_remind)
	ImageView ivRemind;
	/** 提示图标 */
//	@ViewInject(R.id.iv_close)
	ImageView ivClose;
	/** 提示文字 */
//	@ViewInject(R.id.txt_remind)
	TextView txtRemind;
	
	public CardRemindView(Context context, 
			int inputModel,
			String content,
			boolean isSupportHandInput, 
			final RemindViewCloseListener listener) {
		super(context);
		
		View view = View.inflate(context, R.layout.card_remind_view, this);
		ViewUtils.inject(this, view);

		tvTipMsg = (TextView) view.findViewById(R.id.tv_tip_msg);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		ivRemind = (ImageView) view.findViewById(R.id.iv_remind);
		ivClose = (ImageView) view.findViewById(R.id.iv_close);
		txtRemind = (TextView) view.findViewById(R.id.txt_remind);

		if(isSupportHandInput) {
			tvTipMsg.setText(content+getResources().getString(R.string.swipe_card_how_to_input_or_hand_input));
			
		} else {
			tvTipMsg.setText(content);
			ivClose.setVisibility(View.GONE);
		}
		
		//提示输入类型
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {

			if ((inputModel & ReadcardType.ICCARD) != 0) {
				ivIcon.setImageResource(R.drawable.h_swipe_card_ic_1);
				ivRemind.setImageResource(R.drawable.h_swipe_card_ic_txt);
				txtRemind.setText(R.string.swipe_card_ic_hint);
			} else if ((inputModel & ReadcardType.RFCARD) != 0) {
				ivIcon.setImageResource(R.drawable.h_swipe_card_rf_1);
				ivRemind.setImageResource(R.drawable.h_swipe_card_rf_txt);
				txtRemind.setText(R.string.swipe_card_rf_hint);
			} else if ((inputModel & ReadcardType.SWIPE) != 0) {
				ivIcon.setImageResource(R.drawable.h_swipe_card_1);
				ivRemind.setImageResource(R.drawable.h_swipe_card_txt);
				txtRemind.setText(R.string.swipe_card_hint);
			}
		} else {
			if ((inputModel & ReadcardType.ICCARD) != 0) {
				ivIcon.setImageResource(R.drawable.swipe_card_ic_1);
				ivRemind.setImageResource(R.drawable.swipe_card_ic_txt_1);
				txtRemind.setText(R.string.swipe_card_ic_hint);
			} else if ((inputModel & ReadcardType.RFCARD) != 0) {
				ivIcon.setImageResource(R.drawable.swipe_card_rf_1);
				ivRemind.setImageResource(R.drawable.swipe_card_rf_txt_1);
				txtRemind.setText(R.string.swipe_card_rf_hint);
			} else if ((inputModel & ReadcardType.SWIPE) != 0) {
				ivIcon.setImageResource(R.drawable.swipe_card_1);
				ivRemind.setImageResource(R.drawable.swipe_card_txt_1);
				txtRemind.setText(R.string.swipe_card_hint);
			}
		}
		
		MainActivity.getInstance().hideTitle();
		
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
				MainActivity.getInstance().showTitle();
				listener.onCloseEvent();
			}
		});
		
	}
	
	public void setMessageContent(int id){
		txtRemind.setText(id);
	}

}
