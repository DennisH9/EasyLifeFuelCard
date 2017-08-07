package com.newland.payment.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;

/**
 * 检测设备控件
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午3:15:19
 */
public class CheckView extends FrameLayout{
	
	/** 检查名 */
//	@ViewInject(R.id.txt_content)
	TextView txtContent;
	/** 结果 */
//	@ViewInject(R.id.iv_result)
	ImageView ivResult;
	
	private boolean result;

	public CheckView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CheckView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.check_view, this);
		ViewUtils.inject(this, view);
		txtContent = (TextView)view.findViewById(R.id.txt_content);
		ivResult = (ImageView)view.findViewById(R.id.iv_result);
		result = false;
	}
	
	public void setContent(int resContent) {
		txtContent.setText(resContent);
	}
	
	public void setNormalStyle() {
		txtContent.setTextColor(Color.parseColor("#999999"));
		ivResult.setVisibility(View.INVISIBLE);
		result = false;
	}

	public void setErrorStyle() {

		txtContent.setTextColor(Color.parseColor("#e43737"));
		ivResult.setVisibility(View.VISIBLE);
		ivResult.setImageResource(R.drawable.check_bad);
		result = false;
	}
	
	public void setErrorStyleWithMsg(String msg) {
		txtContent.setText(msg);
		txtContent.setTextColor(Color.parseColor("#e43737"));
		ivResult.setVisibility(View.VISIBLE);
		ivResult.setImageResource(R.drawable.check_bad);
		result = false;
	}
	
	public void setSuccessStyle() {

		txtContent.setTextColor(Color.parseColor("#1ed656"));
		ivResult.setVisibility(View.VISIBLE);
		ivResult.setImageResource(R.drawable.check_good);
		result = true;
	}
	
	public boolean isSuccess() {
		return result;
	}
}
