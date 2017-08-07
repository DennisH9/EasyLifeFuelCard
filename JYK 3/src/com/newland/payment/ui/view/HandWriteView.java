package com.newland.payment.ui.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 签名视图
 */
public class HandWriteView extends View {
	
	private static final float PAINT_SIZE = 3.5f;
	private Context context;
	private Paint paint;
	private Canvas cacheCanvas;
	private Bitmap cachebBitmap;
	private Bitmap bg;
	private Path path;
	private int width;
	private int height;
	private float startX, startY;
	private float clickX, clickY;
	
	private String signatureCode = null;
	
	private RectF dirtyRect;
	
	private long count;

	public HandWriteView(Context context) {
		super(context);
		this.context = context;
	}

	public HandWriteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = measureWidth(widthMeasureSpec);
		height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
		init(width, height);
	}

	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}

		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	private void init(int width, int height) {
		requestFocus();
		
		startX = 0;
		startY = 0;
		clickX = 0;
		clickY = 0;
		count = 0;
		
		float scale = context.getResources().getDisplayMetrics().density;
		float pageSize = PAINT_SIZE*scale;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(pageSize);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		paint.setSubpixelText(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		path = new Path();
		cachebBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		cacheCanvas = new Canvas(cachebBitmap);
		
		this.setBackgroundColor(Color.WHITE);
		
		dirtyRect = new RectF();
		
		drawSignatureCode();
		if (App.SCREEN_TYPE == Const.ScreenType.IM_91) {
			bg = BitmapFactory.decodeResource(getResources(),
					R.drawable.signature_bg);
		} else {
			bg = BitmapFactory.decodeResource(getResources(),
					R.drawable.h_signature_bg);
		}
	}

	private void drawBackground(Canvas canvas) {
		int xof = (width - bg.getWidth()) / 2;
		int yof = (height - bg.getHeight()) / 2;
		canvas.drawBitmap(bg, xof, yof, paint);
	}
	
	public void drawSignatureCode(){
		if(StringUtils.isEmpty(signatureCode)){
			return;
		}
		if(cachebBitmap == null || cacheCanvas == null){
			return;
		}
		signatureCode = signatureCode.replaceAll(" ", "");
		signatureCode = formatString(signatureCode);
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		Rect bounds = new Rect();

		cacheCanvas.save();
		float width = 0;
		float height = 0; 
		if(App.SCREEN_TYPE == Const.ScreenType.IM_91){
			width = cachebBitmap.getHeight();
			height = cachebBitmap.getWidth();
			//cacheCanvas.rotate(-90);
			cacheCanvas.rotate(90);
			cacheCanvas.translate(0, - cachebBitmap.getWidth());
			//cacheCanvas.translate(-cachebBitmap.getHeight(), 0);
			paint.setTextSize(70.0f);
		}else{
			width = cachebBitmap.getWidth();
			height = cachebBitmap.getHeight();
			paint.setTextSize(80.0f);
			
		}
		
		paint.getTextBounds(signatureCode, 0, signatureCode.length(), bounds);
		
		cacheCanvas.drawText(signatureCode, (width - bounds.width()) / 2,
				(height + bounds.height()) / 2, paint);
		cacheCanvas.restore();
		
		//cacheCanvas.drawText(text, x, y, paint);
	}

	private String formatString(String signatureCode){
		StringBuffer buffer = new StringBuffer();
		for (int i=0; i<signatureCode.length(); i++) {
			buffer.append(signatureCode.substring(i, i+1));
			buffer.append("  ");
		}
		return buffer.toString();
	}
	
	public void clear() {
		count = 0;
		//startX = 0;
		//startY = 0;
		//clickX = 0;
		//clickY = 0;
		path.reset();
		path.moveTo(startX, startY);
		if (cacheCanvas != null) {
			cachebBitmap.eraseColor(Color.TRANSPARENT);
			cacheCanvas.drawColor(Color.TRANSPARENT);
			//
			drawSignatureCode();
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
		int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;

		curW = Math.max(curW, w);
		curH = Math.max(curH, h);
		Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
				Bitmap.Config.ARGB_8888);
		Canvas newCanvas = new Canvas();
		newCanvas.setBitmap(newBitmap);
		if (cachebBitmap != null) {
			newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
		}
		cachebBitmap = newBitmap;
		cacheCanvas = newCanvas;

	}
	
	private static final int WIDTH = 16*8*2;
	private static final int HEIGHT = 9*8*2;
	public Bitmap getCachebBitmap() {
		Bitmap validBitmap =null;
		if(App.SCREEN_TYPE == Const.ScreenType.IM_91){
			validBitmap = Bitmap.createScaledBitmap(cachebBitmap, HEIGHT, WIDTH, true);
		}else{
			validBitmap = Bitmap.createScaledBitmap(cachebBitmap, WIDTH, HEIGHT, true);
		}
		LoggerUtils.i("width:" + validBitmap.getWidth() + " height:" + validBitmap.getHeight());
		Bitmap outBmp = Bitmap.createBitmap(WIDTH, HEIGHT,
				Config.ARGB_8888);
		Canvas c = new Canvas(outBmp);
		if(App.SCREEN_TYPE == Const.ScreenType.IM_91){
//			c.translate(250, 125);
//			c.rotate(-90);
//			c.translate(-125, -250);
			c.rotate(-90);
			c.translate(-outBmp.getHeight(), 0);
		}
		c.drawColor(Color.WHITE);
		c.drawBitmap(validBitmap, 0, 0, new Paint());
		return outBmp;
	}

	/**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startX = event.getX();
        startY = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touchDown(event);
            return true;
        case MotionEvent.ACTION_MOVE:
            touchMove(event);
            return true;
        case MotionEvent.ACTION_UP:
            touchUp(event);
            return true;
        default:
            break;
        }
        
        return super.onTouchEvent(event);
    }
     
    /**
     * 
     * @param event
     */
    private void touchDown(MotionEvent event){ 
        clickX = startX;
        clickY = startY;
        //path移动,不进行重画
        path.moveTo(startX, startY);
        //invalidate();
    }
     
    /**
     * 
     * @param event
     */
    private void touchMove(MotionEvent event){

        path.quadTo(clickX, clickY, (clickX+startX)/2, (clickY+startY)/2);
        count ++;
        resetDirtyRect();
        clickX = startX;
        clickY = startY;
        
        this.invalidate();
        //局部重画 比较卡时会出现 线条断续.... 除非使用invalidate
        //this.invalidate((int)(dirtyRect.left - 2*PAINT_SIZE), (int)(dirtyRect.top - 2*PAINT_SIZE),
        //				(int)(dirtyRect.right + 2*PAINT_SIZE), (int)(dirtyRect.bottom + 2*PAINT_SIZE));
    }
     
    /**
     * @param event
     */
    private void touchUp(MotionEvent event){
    	//将路径画在bitmap上
    	cacheCanvas.drawPath(path, paint);
        path.reset();
    }
    
    /**
     * 计算脏区矩形
     */
    private void resetDirtyRect(){
    	dirtyRect.left = Math.min(startX, clickX);
    	dirtyRect.right = Math.max(startX, clickX);
    	dirtyRect.top = Math.min(startY, clickY);
    	dirtyRect.bottom = Math.max(startY, clickY);
    }

	public String getSignatureCode() {
		return signatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		this.signatureCode = signatureCode;
	}
	
	public boolean isValid(){
		if(count >= 3){
			return true;
		}
		return false;
	}
}
