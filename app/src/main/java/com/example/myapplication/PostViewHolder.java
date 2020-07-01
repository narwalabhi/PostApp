package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    CardView postCardView;
    MaterialTextView tvPostContent, tvPostTitle, tvUsername;
    TextView tvNoOfLikes, tvNoOfDislikes, tvNoOfComments;
    CircleImageView cIvUserProPic;
    ImageView ivPostPic;
    ImageButton ibLike, ibDislike, ibComment;

    public PostViewHolder(View view) {
        super(view);
        postCardView = view.findViewById(R.id.postCardView);
        tvUsername = view.findViewById(R.id.tvPostUsername);
        tvPostContent = view.findViewById(R.id.tvContent);
        cIvUserProPic = view.findViewById(R.id.ivPostProfilePic);
        ibLike = view.findViewById(R.id.ibLike);
        ibDislike = view.findViewById(R.id.ibDislike);
        ibComment = view.findViewById(R.id.ibComment);
        tvNoOfLikes = view.findViewById(R.id.tvNoOfLikes);
        tvNoOfDislikes = view.findViewById(R.id.tvNoOfDislikes);
        tvNoOfComments = view.findViewById(R.id.tvNoOfComments);
        ivPostPic = view.findViewById(R.id.ivPostPic);
        tvPostTitle = view.findViewById(R.id.tvTitle);

    }
}
