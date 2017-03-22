package com.ashik619.gitissues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ashik619.gitissues.models.Comment;
import com.ashik619.gitissues.models.HomeList;

import java.util.ArrayList;

/**
 * Created by ashik619 on 20-03-2017.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {
    Context context;

    public CommentListAdapter(Context context, ArrayList<Comment> commentArrayList) {
        super(context, 0, commentArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }
        TextView casetitle = (TextView) convertView.findViewById(R.id.titleView);
        TextView bodyView = (TextView) convertView.findViewById(R.id.bodyView);
        casetitle.setText(comment.userName);
        bodyView.setText(comment.commentText);


        return convertView;
    }
}
