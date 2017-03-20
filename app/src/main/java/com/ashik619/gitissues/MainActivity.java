package com.ashik619.gitissues;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ashik619.gitissues.io.GetJsonAsyncTask;
import com.ashik619.gitissues.io.GetOnTaskCompleted;
import com.ashik619.gitissues.models.HomeList;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GetOnTaskCompleted {
    ArrayList<HomeList> homeListArrayList = new ArrayList<HomeList>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getAllIssuesApiCall();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                HomeList homeList = (HomeList) listView.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                EventBus.getDefault().postSticky(homeList);
                startActivity(i);
            }
        });

    }

    void getAllIssuesApiCall() {
        try {
            URL url = new URL(Constants.BASE_URL);
            GetJsonAsyncTask task2 = new GetJsonAsyncTask(this, url);
            task2.execute("");
        } catch (Exception e) {
        }
    }

    @Override
    public void GetOnTaskCompleted(String response) {
        if (response != null) {
            try {
                System.out.println(response);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject issue = jsonArray.getJSONObject(i);
                    HomeList homeList = new HomeList();
                    homeList.title = issue.getString("title");
                    homeList.body = issue.getString("body");
                    homeList.commentsUrl = issue.getString("comments_url");
                    homeListArrayList.add(homeList);
                }
                loadingLayout.setVisibility(View.GONE);
                populateListView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void populateListView() {
        HomeListAdapter homeListAdapter = new HomeListAdapter(MainActivity.this,homeListArrayList);
        listView.setAdapter(homeListAdapter);
    }

}
