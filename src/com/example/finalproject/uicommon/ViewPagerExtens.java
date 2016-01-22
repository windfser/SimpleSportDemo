package com.example.finalproject.uicommon;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerExtens extends ViewPager {

	private boolean isScrollable = false;
	private ArrayList<ITabPage> pages = null;
	private PagerAdapter mPagerAdapter = null;
	
	public ViewPagerExtens(Context context) {
		super(context);
		pages = new ArrayList<ITabPage>();
		initPagerAdapter();
	}

	private void initPagerAdapter() {
		mPagerAdapter = new PagerAdapter() {
			
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(pages.get(position).getView());

			}
			
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = pages.get(position).getView();
				container.addView(view);
				return view;
			}
			
			@Override
			public boolean isViewFromObject(View container, Object position) {
				return container == position;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pages.size();
			}
		};
	}
	
	public void setUp(){
		this.setAdapter(mPagerAdapter);
	}
	
	public void setScrollEable(boolean isEable){
		isScrollable = isEable;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}
	
	
	public void addPage(ITabPage v){
		pages.add(v);
	}
	
	

}
