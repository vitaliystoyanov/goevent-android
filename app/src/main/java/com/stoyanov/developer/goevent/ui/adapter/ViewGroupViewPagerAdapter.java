package com.stoyanov.developer.goevent.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanov.developer.goevent.R;

public abstract class ViewGroupViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewGroupViewPager";
    private String[] titles;
    private int pageCount;
    private Context context;

    public ViewGroupViewPagerAdapter(Context context, int pageCount) {
        titles = context.getResources().getStringArray(R.array.item_titles_of_tab);
        this.context = context;
        this.pageCount = pageCount;
        if (titles.length != pageCount) {
            throw new RuntimeException("Titles of tabs != page count");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: pos - " + position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup view = inflatePage(inflater, container, position);
        container.addView(view);
        return view;
    }

    public abstract ViewGroup inflatePage(LayoutInflater inflater, ViewGroup container, int position);

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
