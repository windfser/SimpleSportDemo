package com.example.finalproject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.example.finalproject.system.MyLocationManager;
import com.example.finalproject.system.MyLocationManager.IMyLocationMgrCallBack;
import com.example.finalproject.uicommon.ITabPage;
import com.example.finalproject.system.DownLoad;
import com.example.finalproject.weather.JsonForReader;



public class RunPage implements ITabPage {
	static Context context = null;
	private View view = null;
	private MapView mMapView = null;
	private ImageButton button = null; 
	private BaiduMap mBaiduMap = null;
	private static Handler handler = null;
	private static TextView tvWeather = null;
	
	public MyLocationManager myLocationManager = null;
	
	
	public RunPage(int layoutId, Context context) {
		RunPage.context = context;
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		view = mLayoutInflater.inflate(layoutId, null);
		mMapView = (MapView) view.findViewById(R.id.bmapView1);
		mBaiduMap = mMapView.getMap();
		myLocationManager = new MyLocationManager(mBaiduMap,mMapView,context);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
		button = (ImageButton)view.findViewById(R.id.begin_run_btn);
		tvWeather = (TextView)view.findViewById(R.id.tv_weather);
		myLocationManager.setFirstLocCallBack(new IMyLocationMgrCallBack() {
			@Override
			public void execute(BDLocation location) {
				requestWeather(location.getCity().replace("市", ""));
			}
		});
	}
	
	public static void updateWeather(String weather){
		tvWeather.setText(weather);
	}
	
	public static void injectHandler(Handler handler){
		RunPage.handler = handler;
	}
	
	public void focusMyLocation(){
		myLocationManager.focusMyLocation();
	}

	@Override
	public View getView() {
		return view;
	}
	
	public void setExerciseBtnListener(View.OnClickListener l){
		button.setOnClickListener(l);
	}
	
	public MapView getMapView(){
		return this.mMapView;
	}
	
	public void clearMapView(){
		mMapView = null;
	}
	
	public void stopFocurs(){
		myLocationManager.stopFocurs();
	}
	
	public static void requestWeather(final String city){
			new Thread(){
				@Override
				public void run() {
					DownLoad downLoad=new DownLoad(context);
					String url="http://wthrcdn.etouch.cn/weather_mini?city=";
					try {
						if(city!=null){
							String args =URLEncoder.encode(city, "UTF-8");
							url=url+args;
						}else{
							String args=URLEncoder.encode("珠海", "UTF-8");
							url=url+args;
						}
						String jsonMessage=downLoad.downLoadFromNet(url);
						JsonForReader reader= new JsonForReader(jsonMessage);
						reader.read();
						Message msg= handler.obtainMessage();
						msg.what = MainActivity.HANDLER_WEATHER_REFRESH;
						msg.obj = reader.getTips();
						handler.sendMessage(msg);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}.start();
	}
	
	
	
	

}
