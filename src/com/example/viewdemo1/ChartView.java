package com.example.viewdemo1;

import java.util.LinkedList;

import com.example.notification.NotificationActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ChartView extends View {
	
	private Handler mHandler = new Handler();
	private Thread thread;
	private boolean exit=false;
	private Paint paint;
	private Paint paintText;
	private Paint paintPoint;
	private Paint paintTitle;
	private Paint paintData;
	
	private int mXPoint = 50;
	private int mYPoint = 200;
	private int mXScale = 45;
	private int mYScale = 50;
	private int mXLength = 325;
	private int mYLength = 200;
	
	private String[] mXLabel = null;
	private String[] mYLabel = null;
	private LinkedList<String> mData = new LinkedList<String>();
	private String mTitle = null;
	
	private int mRange=0; 
	
    public void setRange(int range) {  
        mRange = range;  
    }  
	public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}
	
	public ChartView(Context context) {
		super(context);
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
		paint.setColor(Color.BLUE);
		paint.setTextSize(12);
		
		paintPoint = new Paint();
		paintPoint.setStyle(Paint.Style.STROKE);
		paintPoint.setAntiAlias(true);
		paintPoint.setStyle(Paint.Style.FILL);
		
		paintText = new Paint();
		paintText.setStyle(Paint.Style.STROKE);
		paintText.setAntiAlias(true);
		paintText.setTextSize(12);
		
		paintData = new Paint();
		paintData.setStyle(Paint.Style.STROKE);
		paintData.setAntiAlias(true);
		paintData.setTextSize(12);
		
		paintTitle = new Paint();
		paintTitle.setStyle(Paint.Style.FILL);
		paintTitle.setAntiAlias(true);
		paintTitle.setTextSize(18);
		paintTitle.setColor(Color.BLACK);
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
			//警戒值设置
			if(Float.parseFloat(mData.get(i))>=mRange) { 
				//颜色
            	paintPoint.setColor(Color.parseColor("#FF0000"));  
            	paintData.setColor(Color.parseColor("#FF0000"));
            	exit = true;
            	//if(Thread.currentThread().isInterrupted()||thread==null){
            		setThread();
            	//}
            }else {
            	exit = false;
//            	if(!Thread.currentThread().isInterrupted()){
//            	Thread.currentThread().interrupt();
//            	}
				paintPoint.setColor(Color.GREEN);
				paintData.setColor(Color.GREEN);
			}
			canvas.drawText(mData.get(i), mXPoint+i*mXScale, YCoord(mData.get(i))-15, paintText);
			canvas.drawCircle(mXPoint+i*mXScale, YCoord(mData.get(i)), 6, paintPoint);
		}
		canvas.drawText(mTitle, 190, 20, paintTitle);
	}
	private int caculatemXscale(){
		mXScale = mXLength/6;
		return mXScale;
	}
	public void setHandler(Handler handler){
		this.mHandler = handler;
	}
	public void setThread(){
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					while(exit){
					//Log.i("TAG!","------------------>hh");
					mHandler.sendEmptyMessage(0x111);
					exit = false;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		thread.start();
	}
	private float YCoord(String y0){
		float y;
		try{
			y = Float.parseFloat(y0);
		}catch(Exception e){
			return -999;
		}
		try {
			//计算y坐标在画布上显示的位置
			return mYPoint-y*mYScale/Integer.parseInt(mYLabel[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return y;
	}
}
