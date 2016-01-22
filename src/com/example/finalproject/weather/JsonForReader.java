package com.example.finalproject.weather;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonForReader {
	String josnMessage;
	String tips = "";
	
	public JsonForReader(String josnMessage) {
		this.josnMessage=josnMessage;
	}
	
	public String getTips(){
		return tips;
	}
	
	public void read(){
		try {
			JSONObject weatherInfo=new JSONObject(josnMessage);
			JSONObject allData=weatherInfo.getJSONObject("data");
			JSONArray forecastInfo=allData.getJSONArray("forecast");
			JSONObject weather=forecastInfo.getJSONObject(0);
			tips = weather.getString("type")+" " + allData.getString("wendu")+ "℃";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
