package com.example.finalproject.runmodule;

import android.util.Log;



public class CalorieListener implements IStepListener {
	
	public interface Listener {
		public void valueChanged(double value);
		public void passValue();
	}
	
	private Listener mListener;
	private double mCalories = 0.0f;
	double mStepLength;
	double mBodyWeight;

	public CalorieListener(Listener listener, double bodyweight,
			double steplength) {
		mListener = listener;
		mBodyWeight = bodyweight;
		mStepLength = steplength;
		reloadSettings();
	}
	
	public void reloadSettings() {
		notifyListener();
	}
	
	private void notifyListener() {
		mListener.valueChanged(mCalories);
	}

	@Override
	public void onStep() {
		mCalories += mBodyWeight
				* mStepLength / 100.0;// centimeters
		Log.d("CalorieNotifier.onStep", "mCalories = " + mCalories);
		notifyListener();
	}

	@Override
	public void passValue() {
		// TODO Auto-generated method stub

	}
	
	public void setStepLength(float stepLength) {
		mStepLength = stepLength;
	}

	public void setCalories(float calories) {
		mCalories = calories;
		notifyListener();
	}
	
	public void resetValues() {
		mCalories = 0;
		notifyListener();
	}

}
