package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    CircleImageView cvProfile;
    MaterialTextView tvComment, tvUsername;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        cvProfile = itemView.findViewById(R.id.cvCommentProfile);
        tvComment = itemView.findViewById(R.id.tvCommentText);
        tvUsername = itemView.findViewById(R.id.tvCommentUsername);
    }
}
