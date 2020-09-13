package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostsViewHolder> {

    ArrayList<String> postIDs;
    Post post;
    Context mContext;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String TAG = "ProfilePostAdapter";

    public ProfilePostsAdapter(ArrayList<String> postIDs, Context mContext) {
        this.postIDs = postIDs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfilePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.profile_posts_list_item, parent, false);
        return new ProfilePostsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfilePostsViewHolder holder, int position) {
        String postID = postIDs.get(position);
        Log.d(TAG, "onBindViewHolder: " + postID);
        final DocumentReference documentReference = firebaseFirestore.collection("posts").document(postID);
        holder.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        post = documentSnapshot.toObject(Post.class);
                        Intent intent = new Intent(mContext, PostViewActivity.class);
                        intent.putExtra("post", post);
                        mContext.startActivity(intent);
                    }
                });

            }
        });
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.getLocalizedMessage());
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    post = documentSnapshot.toObject(Post.class);
                    Log.d(TAG, "onEvent: " + post.getPostImageUrl());
                    Picasso.get()
                            .load(post.getPostImageUrl())
                            .resize(350, 400)
                            .centerCrop()
//                            .placeholder()
                            .into(holder.ivPostImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postIDs.size();
    }

    public void setData(ArrayList<String> postIds) {
        this.postIDs = postIds;
        notifyDataSetChanged();
    }

    public class ProfilePostsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPostImage;

        public ProfilePostsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = itemView.findViewById(R.id.ivPostImage);
        }
    }
}
