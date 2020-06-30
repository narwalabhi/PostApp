package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class  PostActivity extends AppCompatActivity {

//    FirebaseUser firebaseUser;
//    Button postButton;
//    TextView tvName;
//    EditText etPost;
//    ImageView ivProfile;
//    RecyclerView PostList;
//    DatabaseReference databaseReference;
//    private ArrayList<Post> posts = new ArrayList<>();
//    PostListAdapter postListAdapter;
//    String ImageName;
//    StorageReference mStorageReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        PostList = findViewById(R.id.PostList);
//        tvName = findViewById(R.id.tvPostName);
//        postButton = findViewById(R.id.btnPost);
//        etPost = findViewById(R.id.etPost);
//        ivProfile = findViewById(R.id.ivDisplayPic);
//        setProfile();
//        postListAdapter = new PostListAdapter(PostActivity.this,posts);
//        PostList.setLayoutManager(new LinearLayoutManager(PostActivity.this));
//        PostList.setAdapter(postListAdapter);
//        postButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addPost();
//            }
//        });
//        doInBackground();
//    }
//
//    private void setProfile() {
//        String UserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.child("Users").child(UserID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tvName.setText(Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString());
//                ImageName = Objects.requireNonNull(dataSnapshot.child("proPicName").getValue()).toString();
//                Log.d("picName", "onDataChange: " + ImageName);
//                mStorageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures/"+ImageName);
//                Log.d("picName", "onDataChange: " + mStorageReference.toString());
//                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Glide.with(PostActivity.this).load(uri).into(ivProfile);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("proPic", "onFailure: "+ e.getMessage());
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private boolean addPost() {
//        final String postData = etPost.getText().toString();
//        if (postData != "") {
//            databaseReference = FirebaseDatabase.getInstance().getReference();
//            etPost.setText("");
//            String userId = firebaseUser.getUid();
//            databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.d("postFunction", "onDataChange: " + dataSnapshot.getChildrenCount());
//                    post(dataSnapshot,postData);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            return true;
//        }
//        return false;
//    }
//
//    private void post(DataSnapshot dataSnapshot,String postData) {
////        dataSnapshot.child("name").getValue().toString()
//        Post post = new Post(firebaseUser.getUid(), postData,dataSnapshot.child("username").getValue().toString() , dataSnapshot.child("proPicName").getValue().toString(), 0);
//        String key = databaseReference.child("Posts").push().getKey();
////        databaseReference.child("Posts").child(key).setValue(post);
//        Map<String, Object> postValues = post.toMap();
//        Log.d("toMap", "post: "+post.toMap());
//        Map<String, Object> childUpdates = new HashMap<>();
//        Log.d("postValues", "post: " + postValues);
//        childUpdates.put("/Posts/" + "/" + key, postValues);
//        databaseReference.updateChildren(childUpdates);
//    }
//
//    private void doInBackground() {
//            posts = new ArrayList<>();
//            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            tvName = findViewById(R.id.tvPostName);
//            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
//            databaseReference.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    Post post = dataSnapshot.getValue(Post.class);
//                    posts.add(post);
//                    postListAdapter.setData(posts);
//                    Log.d("posts", "onChildAdded: " + post.toString());
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//    }
}