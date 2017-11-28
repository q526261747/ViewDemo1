package com.example.viewdemo1;

import java.util.LinkedList;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {

	private Paint paint,paintText,paintPoint,paintTitle;
	
	private int mXPoint = 60;
	private int mYPoint = 200;
	private int mXScale = 45;
	private int mYScale = 50;
	private int mXLength = 325;
	private int mYLength = 200;
	
	private String[] mXLabel = null;
	private String[] mYLabel = null;
	private LinkedList<String> mData = new LinkedList<String>();
	private String mTitle = null;
	
	public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
		// TODO Auto-generated constructor stub
	}
	
	public ChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setInfo( String[] mXLabel, String[] mYLabel,LinkedList<String> mData, String mTitle) {
		this.mXLabel = mXLabel;
		this.mYLabel = mYLabel;
		this.mData = mData;
		this.mTitle = mTitle;
	}
	private void initData(){
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setColor(Color.DKGRAY);
		paint.setTextSize(12);
		
		paintPoint = new Paint();
		paintPoint.setStyle(Paint.Style.STROKE);
		paintPoint.setAntiAlias(true);
		paintPoint.setStyle(Paint.Style.FILL);
		
		paintText = new Paint();
		paintText.setStyle(Paint.Style.STROKE);
		paintText.setAntiAlias(true);
		paintText.setTextSize(12);
		paintText.setColor(Color.DKGRAY);
		
		paintTitle = new Paint();
		paintTitle.setStyle(Paint.Style.STROKE);
		paintTitle.setAntiAlias(true);
		paintTitle.setTextSize(16);
		paintTitle.setColor(Color.DKGRAY);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		caculatemXscale();
		//y
		canvas.drawLine(mXPoint, mYPoint, mXPoint, mYPoint-mYLength, paint);
		for(int i = 0;i*mYScale<mYLength;i++){
			canvas.drawLine(mXPoint, mYPoint-i*mYScale, mXPoint+5, mYPoint-i*mYScale, paint);
			canvas.drawText(mYLabel[i], mXPoint-25, mYPoint-i*mYScale+5, paintText);
		}
		canvas.drawLine(mXPoint, mYPoint-mYLength, mXPoint-3, mYPoint-mYLength+6, paint);
		canvas.drawLine(mXPoint, mYPoint-mYLength, mXPoint+3, mYPoint-mYLength+6, paint);
		//x
		canvas.drawLine(mXPoint, mYPoint, mXPoint+mXLength, mYPoint, paint);
		for(int i = 0;i*mXScale<mXLength;i++){
			canvas.drawLine(mXPoint+i*mXScale, mYPoint, mXPoint+i*mXScale, mYPoint-5, paint);
			canvas.drawText(mXLabel[i], mXPoint+i*mXScale-6, mYPoint+16, paintText);
		}
		canvas.drawLine(mXPoint+mXLength, mYPoint, mXPoint+mXLength-6, mYPoint-3, paint);
		canvas.drawLine(mXPoint+mXLength, mYPoint, mXPoint+mXLength-6, mYPoint+3, paint);
		for (int i = 0; i < mData.size(); i++) {
			if(i>0&&YCoord(mData.get(i-1))!=-999&&YCoord(mData.get(i))!=-999){
				canvas.drawLine(mXPoint+(i-1)*mXScale, YCoord(mData.get(i-1)), mXPoint+i*mXScale, YCoord(mData.get(i)), paint);
			}
			canvas.drawText(mData.get(i), mXPoint+i*mXScale, YCoord(mData.get(i))-15, paintText);
			canvas.drawCircle(mXPoint+i*mXScale, YCoord(mData.get(i)), 6, paintPoint);
		}
		canvas.drawText(mTitle, 200, 20, paintTitle);
	}
	private int caculatemXscale(){
		mXScale = mXLength/6;
		return mXScale;
	}
	private float YCoord(String y0){
		float y;
		try{
			y = Float.parseFloat(y0);
		}catch(Exception e){
			return -999;
		}
		try {
			return mYPoint-y*mYScale/Integer.parseInt(mYLabel[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return y;
	}
}
