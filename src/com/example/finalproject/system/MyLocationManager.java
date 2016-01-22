package com.example.finalproject.system;

import android.content.Context;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

/**
 * 定位SDK监听函数
 */
public class MyLocationManager implements BDLocationListener {
	boolean isFirstLoc = true;// 是否首次定位
	private MapView mMapView = null;
	private LocationMode mCurrentMode;
	private LocationClient mLocClient;
	private BitmapDescriptor mCurrentMarker;
	private Context context = null;
	private IMyLocationMgrCallBack firstCallBack = null;
	
	
	
	public MyLocationManager(BaiduMap baiduMap, MapView mapView,Context context){
		this.mBaiduMap = baiduMap;
		this.mMapView = mapView;
		this.context = context;
	}
	
	public void stopFocurs(){
		mLocClient.stop();
	}
	
	public void clear(){
		mBaiduMap = null;
		mMapView = null;
		context = null;
	}
	
	public void focusMyLocation(){
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap
        .setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
		// 定位初始化
		mMapView.getMap().setMyLocationEnabled(true);
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
	}
	
	private BaiduMap mBaiduMap = null;
    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
            if(null!=firstCallBack){
            	firstCallBack.execute(location);
            }
        }
    }

	public void onReceivePoi(BDLocation poiLocation) {
	}
	
	public void setFirstLocCallBack(IMyLocationMgrCallBack cb){
		firstCallBack = cb;
	}
	
	public interface IMyLocationMgrCallBack {
		public void execute(BDLocation location);
	}
}
