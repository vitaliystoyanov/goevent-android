package com.stoyanov.developer.goevent.ui.adapter;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stoyanov.developer.goevent.R;

import java.util.Arrays;
import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {
    private List<String> items;
    private Context context;

    public CategorySpinnerAdapter(Context context, @ArrayRes int res) {
        this.context = context;
        items = Arrays.asList(context.getResources().getStringArray(res));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.toolbar_spinner_item_in_spinner, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < items.size() ? items.get(position) : "";
    }
}
