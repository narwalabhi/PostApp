package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "Setup Profile";
    EditText etName;
    EditText etUsername;
    EditText etMobile;
    EditText etDate;
    Button btnSave;
    ImageView ivProfilePicture;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    ProgressBar mProgressBar;
    String ImageName;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri ImageUri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsename);
        etDate = findViewById(R.id.etDob);
        etMobile = findViewById(R.id.etMob);
        btnSave = findViewById(R.id.btnSave);
        ivProfilePicture = findViewById(R.id.ivDisplayPic);

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUri != null)
                    addProfile();
                else
                    Toast.makeText(ProfileSetupActivity.this, "Select Profile Picture!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void uploadPicture() throws FileNotFoundException {
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");
        if (ImageUri != null) {
            Cursor returnCursor = getContentResolver().query(ImageUri, null, null, null, null);
            int nameIndex = 0;
            if (returnCursor != null) {
                nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            }
//            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            ivProfilePicture.setImageURI(ImageUri);
            ImageName = returnCursor.getString(nameIndex);
            Log.d(TAG, "uploadPicture: " + ImageName);
            Log.d(TAG, "uploadPicture: " + ImageUri);
            mStorageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures/" + ImageName);
            uploadTask = mStorageReference.putFile(ImageUri);
// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "onFailure: " + exception.getMessage());
                    Toast.makeText(ProfileSetupActivity.this, "failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
            returnCursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                ImageUri = data.getData();
            }
            try {
                uploadPicture();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProfile() {
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String date = etDate.getText().toString();
        String mobile = etMobile.getText().toString();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        User user = new User(name, email, username, mobile, date, ImageName);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "addProfile:  " + userID);
        db.collection("users")
                .document(userID)
                .set(user)
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
                }) ;
        Intent NextIntent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(NextIntent);
    }
}