package com.newland.payment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 在scrollView里面使用的GridView
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午9:53:53
 */
public class GridViewInnerScroll extends GridView{
	public GridViewInnerScroll(Context context, AttributeSet attrs) { 
		super(context, attrs); 
	} 

	public GridViewInnerScroll(Context context) { 
		super(context); 
	} 

	public GridViewInnerScroll(Context context, AttributeSet attrs, int defStyle) { 
		super(context, attrs, defStyle); 
	} 

	@Override 
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
				MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec); 
	} 

}