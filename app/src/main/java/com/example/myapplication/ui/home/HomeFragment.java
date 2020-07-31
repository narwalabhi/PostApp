package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Post;
import com.example.myapplication.PostListAdapter;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FirebaseUser firebaseUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PostListAdapter postListAdapter;
    private RecyclerView PostList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        PostList = root.findViewById(R.id.rvPosts);
        postListAdapter = new PostListAdapter(requireContext(), new ArrayList<Post>());
        PostList.setLayoutManager(new LinearLayoutManager(requireContext()));
        PostList.setAdapter(postListAdapter);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = db.collection("posts");
//        collectionReference.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
//                            Post post = documentSnapshot.toObject(Post.class);
//                            posts.add(post);
//                        }
//                        postListAdapter.setData(posts);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.getMessage());
//                    }
//                });
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.getMessage());
                    return;
                }
                ArrayList<Post> posts = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Post post = documentSnapshot.toObject(Post.class);
                    posts.add(post);
                }
                postListAdapter.setData(posts);
                Log.d(TAG, "onEvent: " + posts.toString());
            }
        });
        return root;
    }
}
