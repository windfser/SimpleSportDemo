package com.example.finalproject.system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class DownLoad {
	Context context;
	public DownLoad(Context context){
		this.context=context;
	}

	public String downLoadFromNet(String path){
		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			InputStream inputStream=conn.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			String len=null;
			StringBuilder builder=new StringBuilder();
			while((len=reader.readLine())!=null){
				builder.append(len);
			}
			return builder.toString();
		} catch (Exception e) {			
			e.printStackTrace();
			Looper.prepare();
			Toast.makeText(context, "net work error", Toast.LENGTH_SHORT).show();
			Looper.loop();
			return null;
		}
	}

}
