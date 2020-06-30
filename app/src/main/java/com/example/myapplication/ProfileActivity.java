package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DatabaseError";
    TextView tvEmail;
    Button btnLogout;
    TextView tvName;
    TextView tvDob;
    TextView tvMobile;
    TextView tvUsername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        btnLogout = findViewById(R.id.btnLogout);
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvDob = findViewById(R.id.tvDob);
        tvUsername = findViewById(R.id.tvUsername);
        tvMobile = findViewById(R.id.tvMobile);
        String uid = firebaseUser.getUid();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setFields(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });
        btnLogout.setOnClickListener(this);
    }

    private void setFields(DataSnapshot dataSnapshot) {
        tvUsername.append(dataSnapshot.child("username").getValue().toString());
        tvEmail.append(dataSnapshot.child("email").getValue().toString());
        tvDob.append(dataSnapshot.child("dob").getValue().toString());
        tvMobile.append(dataSnapshot.child("mobile").getValue().toString());
        tvName.append(dataSnapshot.child("name").getValue().toString());
    }

    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
