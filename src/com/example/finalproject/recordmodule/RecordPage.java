package com.example.finalproject.recordmodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.example.finalproject.R;
import com.example.finalproject.database.DBManager;
import com.example.finalproject.database.TableRecord;
import com.example.finalproject.uicommon.DialogManager;
import com.example.finalproject.uicommon.ITabPage;

public class RecordPage implements ITabPage {
	Context context = null;
	private View view = null;
	private ListView listView;
	private RecordAdapter recordAdapter = null;
	List<RecordItem> lists = null;

	public RecordPage(int layoutId, Context context) {
		this.context = context;
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		view = mLayoutInflater.inflate(layoutId, null);
		listView = (ListView)view.findViewById(R.id.record_list);
		listView.setOnItemLongClickListener(itemCallBack);
	}
	
	public void refreshData(){
		lists = DBManager.getInstance(context).getRecordList();
		recordAdapter = new RecordAdapter(context, R.layout.record_item, lists);
		listView.setAdapter(recordAdapter);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return view;
	}
	
	
	//////////////////////////////call back///////////////////////////////////////
	OnItemLongClickListener itemCallBack = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			DialogManager.instance().showListDialog(context, "是否要删除此条记录？", new String[]{"是的","不是"}, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						RecordItem item = recordAdapter.getItem(position);
						DBManager.getInstance(context).deleteRecord(item);
						if(null!=lists)
							lists.remove(item);
						recordAdapter.notifyDataSetChanged();
					}
					
				}
			});
			return true;
		}
	};
}
