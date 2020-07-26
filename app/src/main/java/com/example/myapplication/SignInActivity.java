package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import javax.annotation.Nullable;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Signin";
    TextInputEditText etEmailSigin;
    TextInputEditText etPasswordSignin;
    MaterialButton btnSignIn;
    TextView tvSignup;
    FirebaseAuth firebaseAuth;
    private String username, imgUrl;
    SharedPreferences loginPrefs;
    SharedPreferences.Editor editor;
    User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        etEmailSigin = findViewById(R.id.etEmailSignIn);
        etPasswordSignin = findViewById(R.id.etPasswordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignup = findViewById(R.id.tvSignup);
        tvSignup.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn: {
                login();
            }
            break;
            case R.id.tvSignup: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void login() {
        String email = Objects.requireNonNull(etEmailSigin.getText()).toString();
        String password = Objects.requireNonNull(etPasswordSignin.getText()).toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setPrefs();
                    Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (!task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Sign in unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPrefs() {
        loginPrefs = this.getSharedPreferences(String.valueOf(R.string.file_key), Context.MODE_PRIVATE);
        editor = loginPrefs.edit();
        final String UserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "setPrefs: " + firebaseUser.getUid());
        DocumentReference documentReference = db.document("users/" + firebaseUser.getUid());
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        Log.d(TAG, "onSuccess: " + user);
                        editor.putString("username",user.getUsername());
                        editor.apply();
                        getImageUrl(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: ");
                    }
                });
        editor.putString("imgUrl", imgUrl);
//        editor.apply();
//        databaseReference.child("Users").child(UserID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//
//                editor.apply();
//                String ImageName = Objects.requireNonNull(dataSnapshot.child("proPicName").getValue()).toString();
//                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        SharedPreferences.Editor editor1 = loginPrefs.edit();
//                        imgUrl = uri.toString();
//                        editor1.putString("imgUrl", imgUrl);
//                        editor1.apply();
//                        Log.d("url", "onSuccess: " + uri.toString());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("url", "onFailure: " + e.getMessage());
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void getImageUrl(User user) {
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures/" + user.getProPicName());
        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                SharedPreferences.Editor editor = loginPrefs.edit();
                imgUrl = uri.toString();
                editor.putString("imgUrl", imgUrl);
                editor.apply();
                Log.d("url", "onSuccess: " + uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("url", "onFailure: " + e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
