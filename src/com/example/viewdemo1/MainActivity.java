package com.example.viewdemo1;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.example.httppost.HttpUtils;
import com.example.json.JsonParse;
import com.example.notification.NotificationActivity;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.util.Log;
import android.view.KeyEvent;

@SuppressLint("HandlerLeak") public class MainActivity extends Activity {
	private ChartView chartView;
//	private Random random = new Random();
	private int count = 0;
	private Handler mHandler;
	private Handler mHandler2;
	private Thread thread;
	//"http://192.168.1.119:8080/transportservice/type/jason/action/GetAllSense.do"
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
//					String r = random.nextInt(4)*100+"";
					chartView.setRange(200);
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
        mHandler2 = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		if(msg.what==0x111){
        			notifyMessage();
        			setVabrator();
        		}
        	}
        };
        setThread();
        setContentView(R.layout.activity_main);
        initData();
        
    }
    /**
     * 启动震动
     */
    private void setVabrator(){
    	Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    	vibrator.vibrate(1500);
    }
    /**
     * 通知栏发送消息
     */
    protected void notifyMessage() {
    final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// TODO Auto-generated method stub
    	//大视图文本通知
	      //创建消息构造器,在扩展包
	      NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
	      //设置当有消息是的提示，图标和提示文字
	      builder.setSmallIcon(R.drawable.ic_launcher).setTicker("有新消息了");
	      //需要样式
	      BigTextStyle style = new BigTextStyle();
	      style.setBigContentTitle("警戒值通知");//通知的标题
	      style.bigText("PM2.5已超警戒值！！");//通知的文本内容
	      //大视图文本具体内容
	      style.setSummaryText("PM2.5已超警戒值！！尽量少出门噢！");
	      builder.setStyle(style);
	      //显示消息到达的时间，这里设置当前时间
	      builder.setWhen(System.currentTimeMillis());
	      //获取一个通知对象
	      Notification notification = builder.build();
	      notification.flags = Notification.FLAG_AUTO_CANCEL;
	      //发送(显示)通知
	      //notify()第一个参数id An identifier for this notification unique within your application
	      //get?意思说，这个通知在你的应用程序中唯一的标识符
	      count++;
	      manager.notify(count, notification);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//退出时弹出对话框询问是否退出
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			isExit.setTitle("提示");
			isExit.setMessage("确定退出吗");
			isExit.setButton(DialogInterface.BUTTON_POSITIVE, "确定",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			isExit.show();
		}
		return false;
	}
    public void initData(){
    	chartView = new ChartView(MainActivity.this);
    	chartView = (ChartView) findViewById(R.id.chart_view);
    	chartView.setHandler(mHandler2);
    	mList = new LinkedList<String>();
    }
    public void setThread(){
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				while(!Thread.currentThread().isInterrupted()){
					//String json = "{\"CarId\":" + 2 + "}";
					String json_str = HttpUtils.HttpPost(URL,null);
					//Log.i("TAG1","--------->"+json_str);
					try {
					List<String> list = JsonParse.Parse(json_str);
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("text", list.get(0));
					message.what = 0x123;
					message.setData(bundle);
					mHandler.sendMessage(message);
					
					Thread.sleep(3000);
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
