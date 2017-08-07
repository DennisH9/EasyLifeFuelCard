package com.newland.payment.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.RemindViewCloseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 两个图标提示界面
 *
 * @author CB
 * @date 2015-5-12 
 * @time 下午9:29:15
 */
@SuppressLint("ViewConstructor")
public class CardTwoRemindView extends FrameLayout{

	/** 抬头提示信息 */
//	@ViewInject(R.id.tv_tip_msg)
	TextView tvTipMsg;
	/** 关闭图标 */
//	@ViewInject(R.id.iv_close)
	ImageView ivClose;
	/** 提示文字 */
//	@ViewInject(R.id.txt_remind)
	TextView txtRemind;
	/** 提示文字图标 */
//	@ViewInject(R.id.iv_remind)
	ImageView ivRemind;
	/** 提示文字 */
//	@ViewInject(R.id.txt_remind_2)
	TextView txtRemind2;
	/** 提示文字图标 */
//	@ViewInject(R.id.iv_remind_2)
	ImageView ivRemind2;
	/** 提示图标 */
//	@ViewInject(R.id.iv_icon)
	ImageView ivIcon;
	/** 提示图标 */
//	@ViewInject(R.id.iv_icon_2)
	ImageView ivIcon2;

	private List<Integer> resIcon;
	private List<Integer> resString;
	private List<Integer> resStringIcon;
	
	public CardTwoRemindView(Context context, 
			int inputModel,
			String content,
			boolean isSupportHandInput, 
			final RemindViewCloseListener listener) {
		super(context);
		
		View view = View.inflate(context, R.layout.card_two_remind_view, this);
		ViewUtils.inject(this, view);

		tvTipMsg = (TextView) view.findViewById(R.id.tv_tip_msg);
		ivClose = (ImageView) view.findViewById(R.id.iv_close);
		txtRemind = (TextView) view.findViewById(R.id.txt_remind);
		ivRemind = (ImageView) view.findViewById(R.id.iv_remind);
		txtRemind2 = (TextView) view.findViewById(R.id.txt_remind_2);
		ivRemind2 = (ImageView) view.findViewById(R.id.iv_remind_2);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		ivIcon2 = (ImageView) view.findViewById(R.id.iv_icon_2);

		if(isSupportHandInput) {
			tvTipMsg.setText(content+getResources().getString(R.string.swipe_card_how_to_input_or_hand_input));
			
		} else {
			tvTipMsg.setText(content);
			ivClose.setVisibility(View.GONE);
		}
		resIcon = new ArrayList<Integer>();
		resString = new ArrayList<Integer>();
		resStringIcon = new ArrayList<Integer>();
		
		//提示输入类型
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			if ((inputModel & ReadcardType.RFCARD) != 0) {
				resIcon.add(R.drawable.h_swipe_card_rf_2);
				resStringIcon.add(R.drawable.h_swipe_card_rf_txt_2);
				resString.add(R.string.swipe_card_rf_hint);
			} 
			if ((inputModel & ReadcardType.ICCARD) != 0) {
				resIcon.add(R.drawable.h_swipe_card_ic_2);
				resStringIcon.add(R.drawable.h_swipe_card_ic_txt_2);
				resString.add(R.string.swipe_card_ic_hint);
			} 
			
			if ((inputModel & ReadcardType.SWIPE) != 0) {
				resIcon.add(R.drawable.h_swipe_card_2);
				resStringIcon.add(R.drawable.h_swipe_card_txt_2);
				resString.add(R.string.swipe_card_hint);
			}
		} else {
			if ((inputModel & ReadcardType.RFCARD) != 0) {
				resIcon.add(R.drawable.swipe_card_rf_23);
				resString.add(R.string.swipe_card_rf_hint);
				resStringIcon.add(R.drawable.swipe_card_rf_txt_23);
			} 
			if ((inputModel & ReadcardType.ICCARD) != 0) {
				resIcon.add(R.drawable.swipe_card_ic_23);
				resString.add(R.string.swipe_card_ic_hint);
				resStringIcon.add(R.drawable.swipe_card_ic_txt_23);
			} 
			
			if ((inputModel & ReadcardType.SWIPE) != 0) {
				resIcon.add(R.drawable.swipe_card_23);
				resString.add(R.string.swipe_card_hint);
				resStringIcon.add(R.drawable.swipe_card_txt_23);
			}
		}
		

		ivIcon.setImageResource(resIcon.get(0));
		txtRemind.setText(resString.get(0));
		ivRemind.setImageResource(resStringIcon.get(0));
		ivIcon2.setImageResource(resIcon.get(1));
		txtRemind2.setText(resString.get(1));
		ivRemind2.setImageResource(resStringIcon.get(1));
		
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

}
