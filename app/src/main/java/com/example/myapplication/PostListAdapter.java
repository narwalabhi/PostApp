package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private Context mContext;
    private final LayoutInflater layoutInflater;
    private ArrayList<Post> PostList;
    private StorageReference mstorageReference;

    public PostListAdapter(Context mContext, ArrayList<Post> postList) {
        this.mContext = mContext;
        PostList = postList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.post_list_item, parent,false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder postViewHolder, int position) {
        final Post post = PostList.get(position);
        postViewHolder.tvUsername.setText(post.getUsername());
        postViewHolder.tvPostTitle.setText(post.getTitle());
        postViewHolder.tvPostContent.setText(post.getContent());
        postViewHolder.tvNoOfComments.setText(MessageFormat.format("{0}", post.getCommentCount()));
        postViewHolder.tvNoOfLikes.setText(MessageFormat.format("{0}", post.getLikeCount()));
        postViewHolder.tvNoOfDislikes.setText(MessageFormat.format("{0}", post.getDislikeCount()));
        Picasso.get()
                .load(post.getPostImageUrl())
                .resize(60, 60)
//                .rotate(-90)
                .centerCrop()
                .into(postViewHolder.ivPostPic);
        Picasso.get()
                .load(post.getUserImageUrl())
                .rotate(-90f)
                .resize(102, 102)
                .centerCrop()
                .into(postViewHolder.cIvUserProPic);
        postViewHolder.postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostViewActivity.class);
                intent.putExtra("post",post);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }

    public void setData(ArrayList<Post> posts) {
        this.PostList = posts;
        notifyDataSetChanged();
    }
}