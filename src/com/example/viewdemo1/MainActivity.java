package com.example.viewdemo1;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.example.httppost.HttpUtils;
import com.example.json.JsonParse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak") public class MainActivity extends Activity {
	private ChartView chartView;
//	private Random random = new Random();
	private Handler mHandler;
	private Thread thread;
	private final String URL = "http://192.168.1.119:8080/transportservice/type/jason/action/GetAllSense.do";
	private LinkedList<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		if (msg.what == 0x123) {
					Bundle bundle = msg.getData();
//					Log.i("TAG","------------->"+bundle.getString("text"));
					//String r = random.nextInt(4)*100+"";
					if (mList.size()<7) {
						mList.add(bundle.getString("text"));
						chartView.setInfo(new String[]{"0","1","2","3","4","5","6"},   //X轴刻度  
	                            new String[]{"","100","200","300","400"},   //Y轴刻度  
	                            mList, //数据  ***
	                            "PM2.5");
						chartView.invalidate();
					}else{
						mList.poll();
						mList.add(bundle.getString("text"));
						chartView.setInfo(new String[]{"0","1","2","3","4","5","6"},   //X轴刻度  
	                            new String[]{"","100","200","300","400"},   //Y轴刻度  
	                            mList, //数据  ***
	                            "PM2.5");
						chartView.invalidate();
					}
				}
        	}
        };
        setThread();
        setContentView(R.layout.activity_main);
        initData();
    }
    @Override
    protected void onResume() {
     /**
      * 设置为横屏
      */
    if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
     super.onResume(); 
    }
    public void initData(){
    	chartView = new ChartView(MainActivity.this);
    	chartView = (ChartView) findViewById(R.id.chart_view);
    	mList = new LinkedList<String>();
    }
    public void setThread(){
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				while(!Thread.currentThread().isInterrupted()){
					String json_str = HttpUtils.HttpPost(URL);
					try {
					List<String> list = JsonParse.Parse(json_str);
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("text", list.get(0));
					message.what = 0x123;
					message.setData(bundle);
					mHandler.sendMessage(message);
					
					Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
					}
					// TODO Auto-generated method stub
					
				}
			});
    	thread.start();
    }
}
