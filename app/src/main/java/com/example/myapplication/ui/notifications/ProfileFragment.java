package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.myapplication.GridItemDecoration;
import com.example.myapplication.Post;
import com.example.myapplication.PostListAdapter;
import com.example.myapplication.ProfilePostsAdapter;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {


    boolean isSelectedUser;

    private ImageView ivProPic;
    private TextView tvUsername;
    private TextView tvNoOfPosts;
    private RecyclerView recyclerView;
    ProfilePostsAdapter adapter;
    String uID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "ProfileFrag";
    private String imgUrl;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ivProPic = root.findViewById(R.id.ivProfile);
        tvUsername = root.findViewById(R.id.tvProfileUsername);
        tvNoOfPosts = root.findViewById(R.id.tvNoOfPosts);
        recyclerView = root.findViewById(R.id.rvProfilePosts);
        adapter = new ProfilePostsAdapter(new ArrayList<String>(), requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
        recyclerView.addItemDecoration(new GridItemDecoration(3, 0, false));
        recyclerView.setAdapter(adapter);
        Bundle bundle = this.getArguments();
        if(bundle != null && !bundle.isEmpty()){
            uID = bundle.getString("uID");
            isSelectedUser = true;
            setUpData(uID);
        }else{
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uID = firebaseUser.getUid();
            isSelectedUser = false;
            setUpData(uID);
        }
        return root;
    }

    private void setUpData(String uID) {
        final DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d(TAG, "onEvent: " + e.getLocalizedMessage());
                }
                if (documentSnapshot != null && documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    populateFrag(user);
                }else{
                    Log.d(TAG, "onEvent: no data");
                }
            }
        });
    }

    private void populateFrag(User user) {
        SharedPreferences userPrefs = requireContext().getSharedPreferences(String.valueOf(R.string.file_key), Context.MODE_PRIVATE);
        tvNoOfPosts.setText(MessageFormat.format("{0}", user.getPostCount()));
        adapter.setData(user.getPostIds());
        String username;
        if(isSelectedUser){
            username = user.getUsername();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures/" + user.getProPicName());
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgUrl = uri.toString();
                            Picasso.get()
                                    .load(imgUrl)
                                    .resize(250, 250)
                                    .centerCrop()
                                    .into(ivProPic);
                        }
                    });
        }else{
            username = userPrefs.getString("username", null);
            imgUrl = userPrefs.getString("imgUrl", null);
            Picasso.get()
                    .load(imgUrl)
//                .rotate(-90f)
                    .resize(250, 250)
                    .centerCrop()
                    .into(ivProPic);
        }
        tvUsername.setText(username);
    }

}
