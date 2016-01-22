package com.example.finalproject.database;

import java.util.ArrayList;

import com.example.finalproject.recordmodule.RecordItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private Context context = null;
	private static DBManager dataBaseManager = null;
	public static String DATABASE_NAME = "data.db";
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	
	public DBManager(Context context) { 
		this.context = context;
		dbHelper = new MyDatabaseHelper(context, DATABASE_NAME);
		db = dbHelper.getWritableDatabase();  
		createRecordTable();
	}
	
	public static synchronized DBManager getInstance(Context context){
		if(dataBaseManager==null&&context!=null){
			dataBaseManager = new DBManager(context);
		}
		return dataBaseManager;
	}
	
	public void createRecordTable(){
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ TableRecord.TABLE_NAME + "("+TableRecord.CLOUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TableRecord.CLOUMN_DATE+" VARCHAR, "+TableRecord.CLOUMN_DISTANCE+" VARCHAR, "+TableRecord.CLOUMN_TIME+" VARCHAR)";  		
		db.execSQL(CREATE_TABLE);
	}
	
	public  ArrayList<RecordItem> getRecordList(){
		ArrayList<RecordItem> list = new ArrayList<RecordItem>();
		Cursor c = db.rawQuery("SELECT * FROM "+TableRecord.TABLE_NAME+"", null); 
		while (c.moveToNext()) {  
			RecordItem item = new RecordItem();  
			item.setDate(c.getString(c.getColumnIndex(TableRecord.CLOUMN_DATE)));
			item.setDistance(c.getString(c.getColumnIndex(TableRecord.CLOUMN_DISTANCE)));
			item.setTime(c.getString(c.getColumnIndex(TableRecord.CLOUMN_TIME)));
			item.setId(c.getString(c.getColumnIndex(TableRecord.CLOUMN_ID)));
			list.add(item);
		}
		c.close();
		return list;
		
	}
	
	
	public long addRecord(RecordItem item){
		ContentValues values = new ContentValues();
		values.put(TableRecord.CLOUMN_DATE, item.getDate());
		values.put(TableRecord.CLOUMN_DISTANCE, item.getDistance());
		values.put(TableRecord.CLOUMN_TIME, item.getTime());
		long id = db.insert(TableRecord.TABLE_NAME, null, values);
		item.setId(String.valueOf(id));
		return id;
	}
	
	public int deleteRecord(RecordItem item){
		String whereClause = TableRecord.CLOUMN_ID+"=?";
		String whereArgs[] = {item.getId()};
		int rows = db.delete(TableRecord.TABLE_NAME,whereClause,whereArgs);
		return rows;
	}
	
	public void closeDB() {  
		db.close();  
		context = null;
		dataBaseManager = null;
	}

}
