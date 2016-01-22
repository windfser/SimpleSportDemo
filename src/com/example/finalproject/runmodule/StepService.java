package com.example.finalproject.runmodule;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.finalproject.R;
import com.example.finalproject.database.DBManager;
import com.example.finalproject.recordmodule.RecordItem;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepService extends Service {
	
	public static int startedTime = 0;
	public static int totalTime = 0;
	public static int passTime = 0;
	private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	private SensorManager mSensorManager;
	private NotificationManager mNM;
	private StepCounter mStepCounter;
	public static double mDistance; 
	public static double mCalories; 
	public static double mVelocity; 
	public static int mSteps;
	private double mStepLength = 70; 
	private double mWeight = 65; 
	private double mHeight = 175; 
	private int mPerTime = 1000;
	
	private static CalorieListener mCaloriesListener;
	private static StepListener mStepListener;
	private static DistanceListener mDistanceListener;
	
	static Handler mTimeHandler = new Handler();


	public StepService() {
		// TODO Auto-generated constructor stub
	}
	
	public class StepBinder extends Binder {
		public StepService getService() {
			return StepService.this;
		}
	}
	
	public interface ICallback {
		public void distanceChanged(double value);

		public void stepChanged(int value);

		public void caloriesChanged(double value);

		public void velocityChanged(double value);
		
		public void timeChanged(int value);
		
	}
	
	private ICallback mCallback;
	
	/**
	 * Receives messages from activity.
	 */
	private final IBinder mBinder = new StepBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mStepCounter = new StepCounter();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mCallback = RunActivity.mCallback;
		registSensorEvent();
		
		mStepLength = mHeight/3;
		
		mStepListener = new StepListener(stepListener);
		mStepListener.setStep(0);
		mStepCounter.addListener(mStepListener);
		mCaloriesListener = new CalorieListener(calorieListener, mWeight,
				mStepLength);
		mCaloriesListener.setCalories(0.0f);
		mStepCounter.addListener(mCaloriesListener);
		mDistanceListener = new DistanceListener(distanceListener,
				mStepLength);
		mDistanceListener.setDistance(0, 0);
		mStepCounter.addListener(mDistanceListener);
		startTimer();
	}
	
	private void startTimer() {
		passTime = 0;
		mTimeHandler.postDelayed(timeRunnable, mPerTime); //每隔1s执行 
	}

	public void registSensorEvent() {
		Sensor mSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(mStepCounter, mSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public void unregistSensorEvent() {
		mSensorManager.unregisterListener(mStepCounter);
	}
	
	@Override
	public void onDestroy() {
		unregistSensorEvent();
		mTimeHandler.removeCallbacks(timeRunnable);
		mNM.cancel(R.string.app_name);
		RecordItem item = new RecordItem();
		item.setTime(String.valueOf(passTime));
		item.setDistance(String.valueOf(new DecimalFormat("#.##").format(mDistance)));
		item.setDate((String.valueOf(date.format(new Date(System.currentTimeMillis())))));
		DBManager.getInstance(getApplicationContext()).addRecord(item);
		Toast.makeText(getApplicationContext(), "运动记录已完成", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	public void resetValues() {
		mDistanceListener.setDistance(0,0);
		mCaloriesListener.setCalories(0.0f);
		mStepListener.setStep(0);
		passTime = 0;

	}
	
	public void reloadSettings() {

		if (mStepCounter != null) {
			mStepCounter.setSensitivity(Float.valueOf(10));
		}
		if (mDistanceListener != null)
			mDistanceListener.reloadSettings();
		if (mCaloriesListener != null)
			mCaloriesListener.reloadSettings();
	
	}
	
	public void registerCallback(ICallback cb) {
		mCallback = cb;
	}
	
	////////////////////////callback listener////////////////////////////////////////
	Runnable timeRunnable = new Runnable() {
		@Override  
        public void run() {  
			passTime+=1;
			if (mCallback != null) {
				mCallback.timeChanged(passTime);
			}
            try {  
            	mTimeHandler.postDelayed(this, mPerTime);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        } 
	};
	
	private DistanceListener.Listener distanceListener = new DistanceListener.Listener() {
		
		@Override
		public void valueChanged(double value, double v2) {
			mDistance = value;
			mVelocity = v2;
			passValue();
		}
		
		@Override
		public void passValue() {
			if (mCallback != null) {
				mCallback.distanceChanged(mDistance);
				mCallback.velocityChanged(mVelocity);
			}
		}
	};
	
	private CalorieListener.Listener calorieListener = new CalorieListener.Listener() {
		
		@Override
		public void valueChanged(double value) {
			mCalories = value;
			passValue();
		}
		
		@Override
		public void passValue() {
			if (mCallback != null){
				mCallback.caloriesChanged(mCalories); 
			}
		}
	};
	
	private StepListener.Listener stepListener = new StepListener.Listener() {

		@Override
		public void valueChanged(int value) {
			mSteps = value;
			passValue();
		}

		@Override
		public void passValue() {
			if (mCallback != null) {
				mCallback.stepChanged(mSteps);
			}
		}
	};

}
