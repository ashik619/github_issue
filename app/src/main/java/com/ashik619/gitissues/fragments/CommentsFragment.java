package com.ashik619.gitissues.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashik619.gitissues.CommentListAdapter;
import com.ashik619.gitissues.R;
import com.ashik619.gitissues.io.HttpServerBackend;
import com.ashik619.gitissues.io.RestAdapter;
import com.ashik619.gitissues.models.Comment;
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
public class CommentsFragment extends Fragment {


    public CommentsFragment() {
        // Required empty public constructor
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this,rootView);
        homeList = EventBus.getDefault().getStickyEvent(HomeList.class);
        getAllCommentsApiCall();
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        getAllCommentsApiCall();
    }
    void getAllCommentsApiCall() {
        Call<JsonArray> call = new RestAdapter().getRestInterface().getComments(String.valueOf(homeList.number));
        new HttpServerBackend(getContext()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, JsonArray data, int message) {
                super.onReturn(success, data, message);
                loadingLayout.setVisibility(View.GONE);
                System.out.println(message);
                if (success) {
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject commentObject = data.get(i).getAsJsonObject();
                        Comment comment = new Comment();
                        comment.userName = commentObject.getAsJsonObject("user").get("login").getAsString();
                        comment.commentText = commentObject.get("body").getAsString();
                        commentArrayList.add(comment);
                        populateListView();
                    }

                } else {
                }
            }
        });
    }

    void populateListView() {
        mainLayout.setVisibility(View.VISIBLE);
        titleView.setText(homeList.title);
        bodyView.setText(homeList.body);
        CommentListAdapter commentListAdapter = new CommentListAdapter(getContext(), commentArrayList);
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
