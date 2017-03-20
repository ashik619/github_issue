package com.ashik619.gitissues;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ashik619.gitissues.models.HomeList;

import java.util.ArrayList;

/**
 * Created by ashik619 on 20-03-2017.
 */
public class HomeListAdapter extends ArrayAdapter<HomeList> {
    Context context;

    public HomeListAdapter(Context context, ArrayList<HomeList> homeListArrayList) {
        super(context, 0, homeListArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HomeList homeList = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_item, parent, false);
        }
        TextView casetitle = (TextView) convertView.findViewById(R.id.titleView);
        TextView bodyView = (TextView) convertView.findViewById(R.id.bodyView);
        casetitle.setText(homeList.title);
        bodyView.setText(homeList.body.substring(0,140)+"...");


        return convertView;
    }
}
