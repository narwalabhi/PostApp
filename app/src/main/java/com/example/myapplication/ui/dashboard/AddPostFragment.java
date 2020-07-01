package com.example.myapplication.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Post;
import com.example.myapplication.ProfileSetupActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;

public class AddPostFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "profileSetup";
    private boolean uploadComplete = false;
    TextInputEditText etTopic;
    TextInputEditText etContent;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//    ImageView ibAddPostImage;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    ProgressBar mProgressBar;
    String ImageName;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri ImageUri;
    private String postImgUrl;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        etTopic = root.findViewById(R.id.etTopic);
        etContent = root.findViewById(R.id.etContent);
        ImageView ibAddPostImage = root.findViewById(R.id.ibAddPostImage);
        MaterialButton btnPost = root.findViewById(R.id.btnPost);
        ibAddPostImage.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibAddPostImage: {
                Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getImageIntent, PICK_IMAGE_REQUEST);
            }break;
            case R.id.btnPost :{
                addPost();
            }
            break;
        }
    }


    private void uploadPicture() {
        StorageReference mStorageReference;
        if (ImageUri != null) {
            Cursor returnCursor = requireActivity().getContentResolver().query(ImageUri, null, null, null, null);
            int nameIndex = 0;
            if (returnCursor != null) {
                nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            }
//            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            Objects.requireNonNull(returnCursor).moveToFirst();
            ImageName = returnCursor.getString(nameIndex);
            Log.d(TAG, "uploadPicture: " + ImageName);
            Log.d(TAG, "uploadPicture: " + ImageUri);
            mStorageReference = FirebaseStorage.getInstance().getReference().child("postImages/" + ImageName);
            uploadTask = mStorageReference.putFile(ImageUri);
            final StorageReference finalMStorageReference = mStorageReference;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "onFailure: " + exception.getMessage());
                    Toast.makeText(requireContext(), "failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    finalMStorageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    postImgUrl = uri.toString();
                                    uploadComplete = true;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });

                }
            });
            returnCursor.close();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ImageUri = data.getData();
                }
                uploadPicture();
            }
        }
    }

    private void addPost() {
        if(uploadComplete && !postImgUrl.equals("")){
            String postContent = Objects.requireNonNull(etContent.getText()).toString();
            String postTitle = Objects.requireNonNull(etTopic.getText()).toString();
            SharedPreferences preferences = requireContext().getSharedPreferences(String.valueOf(R.string.file_key), Context.MODE_PRIVATE);
            String username = preferences.getString("username", null);
            String userImgUrl = preferences.getString("imgUrl", null);
            if (!postContent.equals("") && !postTitle.equals("")) {
                etContent.setText("");
                etTopic.setText("");
                String userId = firebaseUser.getUid();
                Post post = new Post(userId, postContent, username, userImgUrl, postTitle, postImgUrl);
                String postID = db.collection("posts").document().getId();
                db.collection("posts")
                        .document(postID)
                        .set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: ");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                DocumentReference userRef = db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                userRef.update("postIds", FieldValue.arrayUnion(postID))
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                userRef.update("postCount", FieldValue.increment(1))
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
            }else{
                Toast.makeText(requireContext(), "Please Ddon't leave the fields empty", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(requireContext(), "Wait for the image to upload.", Toast.LENGTH_SHORT).show();
        }

    }

}
