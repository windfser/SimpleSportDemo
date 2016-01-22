package com.example.finalproject.runmodule;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.example.finalproject.R;
import com.example.finalproject.system.MyLocationManager;
import com.example.finalproject.system.MyLocationManager.IMyLocationMgrCallBack;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RunActivity extends Activity {

	public MyLocationManager myLocationManager = null;
	private	static TextView tvSteps;
	private	static TextView tvDistance;
	private	static TextView tvVelocity;
	private static	TextView tvCalorie;
	private static	TextView tvTime;
	private	static TextView tvBack;
	private ImageView ivBack;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	public RunActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		initViews();
		registerListener();
		startFocursMyLocation();
	}

	private void startFocursMyLocation() {
		myLocationManager = new MyLocationManager(mBaiduMap, mMapView, RunActivity.this);
		myLocationManager.focusMyLocation();
		myLocationManager.setFirstLocCallBack(new IMyLocationMgrCallBack() {
			@Override
			public void execute(BDLocation location) {
				startRun();
			}
		});
	}
	
	public void startRun(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		int startedTime = hour * 60 * 60 + minute * 60 + second;
		int totalTime = 0;
		StepService.startedTime = startedTime;
		StepService.totalTime = totalTime;
		//startService(new Intent(RunActivity.this, StepService.class));
		bindService(new Intent(RunActivity.this, StepService.class),
				mConnection, Context.BIND_AUTO_CREATE
						| Context.BIND_DEBUG_UNBIND);
	}
	
	private static void updateTime(int value) {
		tvTime.setText(value + "");
	};

	private void registerListener() {
		tvBack.setOnClickListener(backListener);
		ivBack.setOnClickListener(backListener);
		
	}

	private void initViews() {
		tvDistance = (TextView) findViewById(R.id.distance_value);
		tvDistance.setText("0");
		tvSteps = (TextView) findViewById(R.id.step_value);
		tvSteps.setText("0");
		tvCalorie = (TextView) findViewById(R.id.calory_value);
		tvCalorie.setText("0");
		tvVelocity = (TextView) findViewById(R.id.velocity_value);
		tvVelocity.setText("0");
		tvTime = (TextView) findViewById(R.id.time_value);
		tvTime.setText("0");
		tvBack = (TextView)findViewById(R.id.back);
		ivBack = (ImageView)findViewById(R.id.role_back);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
		
	}
	

	private void stopRun() {
		myLocationManager.stopFocurs();
		if (mService != null) {
			mService.resetValues();
			unbindService(mConnection);
			stopService(new Intent(RunActivity.this, StepService.class));
		}
	}
	
	@Override
	protected void onDestroy() {
		stopRun();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		myLocationManager.clear();
		myLocationManager = null;
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		stopRun();
		mMapView.onPause();
		super.onPause();
	}
	
	private static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bb = msg.getData();
			String type = bb.getString("type");
			DecimalFormat decimal = new DecimalFormat("#.####");
			if (type.equals("steps")) {
				int steps = bb.getInt(type, 0);
				tvSteps.setText(steps + "");
			} else if (type.equals("distance")) {
				double dist = bb.getDouble("distance");
				tvDistance.setText(decimal.format(dist));
				Log.d("handler", "in handler distance");
			} else if (type.equals("velocity")) {
				double velocity = bb.getDouble(type);
				Log.d("handler", "calorite = " + velocity);
				tvVelocity.setText(decimal.format(velocity));
			} else if (type.equals("calorie")) {
				double calorie = bb.getDouble(type);
				Log.d("handler", "calorite = " + calorie);
				tvCalorie.setText(decimal.format(calorie));
			} else {
				tvSteps.setText("0");
				tvDistance.setText("0");
				tvVelocity.setText("0");
				tvCalorie.setText("0");
			}
		}
	};
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((StepService.StepBinder) service).getService();
			mService.reloadSettings();
			mService.registerCallback(mCallback);
			mService = null;
		}
	};
	
	
	
	///////////////////////////callBack Function/////////////////////////////
	OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.role_back:
				stopRun();
				RunActivity.this.finish();
				break;
			case R.id.back:
				stopRun();
				RunActivity.this.finish();
				break;
			}
		}
	};
	
	private StepService mService;
	//stepService Icallback
	public static StepService.ICallback mCallback = new StepService.ICallback() {
		public void stepChanged(int value) {
			Bundle b = new Bundle();
			b.putString("type", "steps");
			b.putInt("steps", value);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
		}

		public void distanceChanged(double value) {
			Bundle b = new Bundle();
			b.putString("type", "distance");
			b.putDouble("distance", value);

			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
			Log.d("pedometer", "detect distance changed!!");
		}

		public void caloriesChanged(double value) {
			Bundle b = new Bundle();
			b.putString("type", "calorie");
			b.putDouble("calorie", value);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
			Log.d("pedometer", "calorite = " + value);
		}

		public void velocityChanged(double value) {
			Bundle b = new Bundle();
			b.putString("type", "velocity");
			b.putDouble("velocity", value);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
			Log.d("pedometer", "velocity = " + value);
		}
		
		public void timeChanged(int value){
			updateTime(value);
		}
	};

}
