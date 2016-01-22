package com.example.finalproject.uicommon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;



public class DialogManager {
	private static DialogManager mInstance = null;
	
	public DialogManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized DialogManager instance(){
		if(mInstance == null){
			mInstance = new DialogManager();
		}
		return mInstance;
	}
	
	public void showListDialog(Context context,String title, String[] options, DialogInterface.OnClickListener callBack) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setItems(options, callBack);
		builder.create().show();
	}

}
