package com.example.httppost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.text.InputFilter.LengthFilter;
import android.util.Log;

public class HttpUtils {
	public static String HttpPost(String url){
//		String res = null;
//		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost(url);
//		HttpResponse hr;
//		try {
//			hr = client.execute(post);
//			if(hr.getStatusLine().getStatusCode()==200){
//				res = EntityUtils.toString(hr.getEntity(),"UTF-8");
//			}else{
//				Log.i("HttpPost", "HttpPost请求失败！");
//			}
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return res;
	ByteArrayOutputStream bao = new ByteArrayOutputStream();
	try {
		URL mUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
		InputStream is = connection.getInputStream();
		byte[] data = new byte[1024];
		int len = 0;
		while((len=is.read(data))!=-1){
			bao.write(data, 0, len);
		}
		is.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new String(bao.toByteArray());
	}
}
