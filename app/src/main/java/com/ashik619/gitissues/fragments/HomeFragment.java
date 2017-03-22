package com.ashik619.gitissues.fragments;


import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.ashik619.gitissues.HomeListAdapter;
import com.ashik619.gitissues.R;
import com.ashik619.gitissues.io.HttpServerBackend;
import com.ashik619.gitissues.io.RestAdapter;
import com.ashik619.gitissues.models.HomeList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    CommentsFragment commentsFragment;
    public HomeFragment() {
        // Required empty public constructor
    }
    ArrayList<HomeList> homeListArrayList = new ArrayList<HomeList>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                HomeList homeList = (HomeList) listView.getItemAtPosition(position);
                EventBus.getDefault().postSticky(homeList);
                inflateCommentsFragment();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        getAllIssuesApiCall();
    }
    void getAllIssuesApiCall() {

        Call<JsonArray> call = new RestAdapter().getRestInterface().getAllIssues();
        new HttpServerBackend(getContext()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, JsonArray data, int message) {
                super.onReturn(success, data, message);
                loadingLayout.setVisibility(View.GONE);
                System.out.println(message);
                if (success) {
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject issue = data.get(i).getAsJsonObject();
                        HomeList homeList = new HomeList();
                        homeList.title = issue.get("title").getAsString();
                        homeList.body = issue.get("body").getAsString();
                        homeList.number = issue.get("number").getAsInt();
                        homeListArrayList.add(homeList);
                        populateListView();
                    }
                } else {
                    if(message == -50){
                        showNoInternetDialog();
                    }
                }
            }
        });
    }

    void populateListView() {
        HomeListAdapter homeListAdapter = new HomeListAdapter(getContext(),homeListArrayList);
        listView.setAdapter(homeListAdapter);
    }
    void showNoInternetDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.setCancelable(false);
        dialog.show();
        Button databutton = (Button) dialog.findViewById(R.id.data);
        Button wifibutton = (Button) dialog.findViewById(R.id.wifi);
        databutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                dialog.dismiss();
                startActivity(intent);
            }
        });
        wifibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                dialog.dismiss();
                startActivity(intent);

            }
        });
    }
    void inflateCommentsFragment(){
        commentsFragment = new CommentsFragment();
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction homeTransaction = manager.beginTransaction();
        homeTransaction.replace(R.id.container, commentsFragment);
        homeTransaction.commit();
    }

}
