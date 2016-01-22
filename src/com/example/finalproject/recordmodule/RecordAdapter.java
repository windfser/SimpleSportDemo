package com.example.finalproject.recordmodule;

import java.util.List;

import com.example.finalproject.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordAdapter extends ArrayAdapter<RecordItem> {
	private int resourceId;
	

	public RecordAdapter(Context context, int resource, List<RecordItem> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		RecordItem item = getItem(position);
		View view=LayoutInflater.from(this.getContext()).inflate(resourceId, null);
		TextView date = (TextView)view.findViewById(R.id.txt_date);
		date.setText(item.getDate());
		TextView distance = (TextView)view.findViewById(R.id.txt_distance);
		distance.setText("距离(m):"+item.getDistance());
		TextView time = (TextView)view.findViewById(R.id.txt_time);
		time.setText("总用时(s)："+item.getTime());
		return view;
	}
	
	
	

}
