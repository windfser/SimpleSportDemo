package com.example.finalproject.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityManager {

	private static ActivityManager mInstance = null;
	
	public ActivityManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized ActivityManager instance(){
		if(mInstance == null){
			mInstance = new ActivityManager();
		}
		return mInstance;
	}
	
	public static void showPanel(Activity fromActivity, Class<?> toActivityClass, boolean isFinishSelf){
		Intent intent = new Intent(fromActivity.getApplicationContext(),toActivityClass);
		fromActivity.startActivity(intent);
		if(isFinishSelf){
			fromActivity.finish();
		}
	}

}
