package com.example.finalproject.runmodule;

import java.util.Calendar;
import android.util.Log;

public class DistanceListener implements IStepListener {
	
	public interface Listener {
		public void valueChanged(double value, double v2);

		public void passValue();
	}
	
	private Listener mListener;
	double mDistance = 0;
	double mVelocity = 0.0f;
	double mStepLength;

	public DistanceListener(Listener listener, double stepLength) {
		mListener = listener;
		mStepLength = stepLength;
		reloadSettings();
	}

	@Override
	public void onStep() {
			mDistance += mStepLength / 100.0;
			Log.d("distanceNotifier.onStep", "distance = " + mDistance);
			
			Calendar c = Calendar.getInstance();
			int hour2 = c.get(Calendar.HOUR_OF_DAY); 
			int minute2 = c.get(Calendar.MINUTE);
			int second2 = c.get(Calendar.SECOND);
			
			int ended = hour2*60*60 + minute2*60 + second2; 
			double mtime = ended - StepService.startedTime + StepService.totalTime; 
			if (mtime != 0)
				mVelocity = mDistance/mtime;  //v:m/s
			else
				mVelocity = 0;
			notifyListener();
	}

	@Override
	public void passValue() {
		// TODO Auto-generated method stub

	}
	
	public void reloadSettings() {
		notifyListener();
	}
	
	private void notifyListener() {
		mListener.valueChanged(mDistance, mVelocity);
	}
	
	public void setDistance(float distance, float velocity) {
		mDistance = distance;
		mVelocity = velocity;
		notifyListener();
	}

}
