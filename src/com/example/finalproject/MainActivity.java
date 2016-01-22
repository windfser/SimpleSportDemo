package com.example.finalproject;

import java.util.ArrayList;

import com.baidu.mapapi.SDKInitializer;
import com.example.finalproject.recordmodule.RecordPage;
import com.example.finalproject.runmodule.RunActivity;
import com.example.finalproject.system.ActivityManager;
import com.example.finalproject.system.ArrayUnits;
import com.example.finalproject.uicommon.ViewPagerExtens;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
	public static final int TAB_MAP = R.id.id_tab_run;
	public static final int TAB_MONENT = R.id.id_tab_moments;
	private static final int[] tabTypeList = {TAB_MAP,TAB_MONENT,TAB_MONENT};
	private static final int[] pagesTitle = {R.string.map,R.string.sport_record};
	private ViewPagerExtens mViewPager = null;
	private TextView mTitle = null;
	private ArrayList<LinearLayout> tabBtnList = null;
	private RunPage mRunPage = null;
	private RecordPage mRecordPage = null;
	private static Handler uiHandler = null;
	public static final int HANDLER_WEATHER_REFRESH = 1; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initViews();
        setUIHandler();
        registerTabBtn();
        initPages();
        mViewPager.setUp();
        mViewPager.setOnPageChangeListener(pageChangeCallBack);
        mRunPage.focusMyLocation();
       
        
    }


    private static void setUIHandler() {
    	uiHandler = new Handler(){
    		@Override
    		public void handleMessage(Message msg){
    			super.handleMessage(msg);
    			switch (msg.what) {
    			case MainActivity.HANDLER_WEATHER_REFRESH:
    				RunPage.updateWeather((String) msg.obj);
    				break;
    			}
    		}
    	};
		
	}


	private void initPages() {
    	mRunPage = new RunPage(R.layout.tab_run, this);
    	RunPage.injectHandler(uiHandler);
    	mViewPager.addPage(mRunPage);
    	mRunPage.setExerciseBtnListener(exerciseBtnCallBack);
    	
    	mRecordPage = new RecordPage(R.layout.tab_moments, this);
    	mViewPager.addPage(mRecordPage);
	}


	private void registerTabBtn() {
    	tabBtnList = new ArrayList<LinearLayout>();
    	for(int i=0;i<tabTypeList.length;++i){
    		tabBtnList.add((LinearLayout) findViewById(tabTypeList[i]));
    		tabBtnList.get(i).setOnClickListener(tabCallBack);
    	}
	}
    
    


	private void initViews() {
    	mViewPager = new ViewPagerExtens(this);
    	mViewPager.setOffscreenPageLimit(3);
    	ViewPager.LayoutParams lp = new LayoutParams();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = 0;
		LinearLayout viewPageContent = (LinearLayout) findViewById(R.id.worldMapWievContent);
		viewPageContent.addView(mViewPager);
		mTitle = (TextView)findViewById(R.id.title);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
	protected void onPause() {
    	mRunPage.getMapView().onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mRunPage.getMapView().onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mRunPage.stopFocurs();
		// 关闭定位图层
		mRunPage.getMapView().getMap().setMyLocationEnabled(false);
		mRunPage.getMapView().onDestroy();
		mRunPage.clearMapView();
		super.onDestroy();
	}
    
    //////////////////////////////////callBack////////////////////////////////////
	private ViewPager.OnPageChangeListener pageChangeCallBack = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int idx) {
			if(ArrayUnits.arrayIndexOf(tabTypeList,TAB_MAP)==idx)
				mViewPager.setScrollEable(false);
			else
				mViewPager.setScrollEable(true);
			mTitle.setText(pagesTitle[idx]);
			if(ArrayUnits.arrayIndexOf(tabTypeList,TAB_MONENT)==idx){
				mRecordPage.refreshData();
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
    private View.OnClickListener exerciseBtnCallBack = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityManager.showPanel(MainActivity.this, RunActivity.class,false);
		}
	};
    private View.OnClickListener tabCallBack = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = ArrayUnits.arrayIndexOf(tabTypeList, v.getId());
			mViewPager.setCurrentItem(index);
		}
	};
}
