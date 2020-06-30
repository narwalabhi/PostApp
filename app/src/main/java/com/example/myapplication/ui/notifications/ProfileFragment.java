package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Post;
import com.example.myapplication.PostListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView ivProPic;
    private TextView tvUsername;
    private TextView tvNoOfPosts;
    private RecyclerView recyclerView;
    PostListAdapter adapter;
    private ArrayList<Post> posts = new ArrayList<>();
    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences userPrefs = requireContext().getSharedPreferences(String.valueOf(R.string.file_key), Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        ivProPic = root.findViewById(R.id.ivProfile);
        tvUsername = root.findViewById(R.id.tvProfileUsername);
        tvNoOfPosts = root.findViewById(R.id.tvNoOfPosts);
        recyclerView = root.findViewById(R.id.rvProfilePosts);
//        adapter = new PostListAdapter(requireContext(), posts);
//        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
//        recyclerView.setAdapter(adapter);
        tvUsername.setText(userPrefs.getString("username", null));
        Picasso.get()
                .load(userPrefs.getString("imgUrl", null))
                .rotate(-90f)
                .resize(250, 250)
                .centerCrop()
                .into(ivProPic);
        Log.d("profile", "onCreateView: " + userPrefs.getString("username",""));
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        User user = snapshot.toObject(User.class);
                        tvNoOfPosts.setText(MessageFormat.format("{0}", user.getPostCount()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Profile", "onFailure: " + e.getMessage());
                    }
                });
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Post post = dataSnapshot.getValue(Post.class);
//                posts.add(post);
//                adapter.setData(posts);
//                Log.d("posts", "onChildAdded: " + post.toString());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return root;
    }
}
