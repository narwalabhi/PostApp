package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PostViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("post");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(Objects.requireNonNull(post).getTitle());
        MaterialTextView tvTitle = findViewById(R.id.tvPostTitle);
        TextView tvPostContent = findViewById(R.id.tvPostContent);
        ImageView postImage = findViewById(R.id.postImage);
        Picasso.get()
                .load(post.getPostImageUrl())
                .into(postImage);
        tvTitle.setText(post.getTitle());
        tvPostContent.setText(post.getContent());
    }
}