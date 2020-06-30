package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_SCREEN_TIME_OUT = 2000;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user != null){
                            Log.d("user", "onAuthStateChanged: " + user.getUid() + user.getDisplayName());
                            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                firebaseAuth.addAuthStateListener(authStateListener);

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
