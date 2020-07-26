package com.example.myapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private ArrayList<Comment> comments;
    private Context mContext;

    public CommentsAdapter(Context mContext, ArrayList<Comment> comments) {
        this.mContext = mContext;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.comment_list_item, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvUsername.setText(comment.getUsername());
        holder.tvComment.setText(comment.getComment());
        Picasso.get()
                .load(comment.getImgUrl())
                .into(holder.cvProfile);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setData(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }
}
