package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "AddComment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Post post;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar =  findViewById(R.id.toolbarComments);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences preferences = this.getSharedPreferences(String.valueOf(R.string.file_key), Context.MODE_PRIVATE);
        final String imgUrl = preferences.getString("imgUrl",null);
        final String username = preferences.getString("username", null);
        CircleImageView cvUser = findViewById(R.id.cvProfileAddComment);
        RecyclerView rvComments = findViewById(R.id.rvComments);
        commentsAdapter = new CommentsAdapter(getBaseContext(), new ArrayList<Comment>());
        rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvComments.setAdapter(commentsAdapter);
        Picasso.get()
                .load(imgUrl)
                .into(cvUser);
        final String postID = getIntent().getStringExtra("postID");
        ImageButton btnAddComment = findViewById(R.id.btnAddComment);
        final TextInputEditText etCommentContent = findViewById(R.id.etCommentContent);
        setComments(postID);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentData = etCommentContent.getText().toString();
                if(!commentData.isEmpty()){
                    etCommentContent.setText("");
                    Comment comment = new Comment(commentData, imgUrl, username);
                    DocumentReference postReference = db.collection("posts").document(postID);
                    postReference.update("comments", FieldValue.arrayUnion(comment))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                    postReference.update("commentCount", FieldValue.increment(1))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                }
            }
        });
    }

    private void setComments(final String postID) {
        DocumentReference documentReference = db.collection("posts").document(postID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.getMessage());
                    return;
                }
                post =  documentSnapshot.toObject(Post.class);
                commentsAdapter.setData(post.getComments());
            }
        });
    }
}