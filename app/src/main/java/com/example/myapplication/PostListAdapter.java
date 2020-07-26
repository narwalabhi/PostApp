package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCanceledListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private Context mContext;
    private final LayoutInflater layoutInflater;
    private ArrayList<Post> PostList;
    int count;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        Picasso.get()
                .load(post.getPostImageUrl())
                .resize(60, 60)
//                .rotate(-90)
                .centerCrop()
                .into(postViewHolder.ivPostPic);
        Picasso.get()
                .load(post.getUserImageUrl())
//                .rotate(-90f)
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
        final DocumentReference documentReference = db.collection("posts").document(post.getPostId());
        postViewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.update("likedIds", FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: " + FirebaseAuth.getInstance().getUid());
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Log.d(TAG, "onCanceled: cancelled");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                documentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                List<String> ids = (List<String>) snapshot.get("likedIds");
                                count = ids.size();
                                postViewHolder.tvNoOfLikes.setText(String.format("%d", ids.size()));
                                udpateCount(post.getPostId());
                            }
                        });


            }
        });
        postViewHolder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postID", post.getPostId());
                mContext.startActivity(intent);
            }
        });
    }

    private void udpateCount(String postId) {
        final DocumentReference documentReference = db.collection("posts").document(postId);
        documentReference.update("likeCount", count)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: success " + count);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: " + count);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
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