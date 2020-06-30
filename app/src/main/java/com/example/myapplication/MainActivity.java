package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    EditText etPassword;
    Button btnRegister;
    TextView tvSignIn;
    FirebaseAuth firebaseAuth;
    private String TAG = "Authentication";

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.tvSignin);
        btnRegister.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                createUser();
                break;
            case R.id.tvSignin:
                Toast.makeText(getApplicationContext(), "Sign In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,SignInActivity.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG, "onClick: None");
        }
    }

    private void createUser() {
        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    etEmail.setText("");
                    etPassword.setText("");
                    FirebaseUser user = task.getResult().getUser();
                    String uid = user.getUid();
                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),ProfileSetupActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("userId",uid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                if (!task.isSuccessful()){
                    etPassword.setText("");
                    Log.d(TAG, "onComplete: " + task.getException());
                    Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
