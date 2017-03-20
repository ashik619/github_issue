package com.ashik619.gitissues;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashik619.gitissues.io.GetJsonAsyncTask;
import com.ashik619.gitissues.io.GetOnTaskCompleted;
import com.ashik619.gitissues.models.Comment;
import com.ashik619.gitissues.models.HomeList;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements GetOnTaskCompleted {
    ArrayList<Comment> commentArrayList = new ArrayList<Comment>();
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.bodyView)
    TextView bodyView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;
    HomeList homeList;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        homeList = EventBus.getDefault().getStickyEvent(HomeList.class);
        getAllCommentsApiCall();

    }

    void getAllCommentsApiCall() {
        try {
            System.out.println("calling api");

            URL url = new URL(homeList.commentsUrl);
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
                    Comment comment = new Comment();
                    comment.userName = issue.getJSONObject("user").getString("login");
                    comment.commentText = issue.getString("body");
                    commentArrayList.add(comment);
                }
                loadingLayout.setVisibility(View.GONE);
                populateListView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void populateListView() {
        mainLayout.setVisibility(View.VISIBLE);
        titleView.setText(homeList.title);
        bodyView.setText(homeList.body);
        CommentListAdapter commentListAdapter = new CommentListAdapter(DetailsActivity.this, commentArrayList);
        listView.setAdapter(commentListAdapter);
        setListViewHeightBasedOnChildren(listView);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        params.height += listView.getDividerHeight() * listAdapter.getCount();

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
