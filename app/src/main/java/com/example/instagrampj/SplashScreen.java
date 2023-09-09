package com.example.instagrampj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

//    variable declaration
    private ImageView imageIcon;
    private LinearLayout linearLayout;
    private Button registerBtn;
    private Button loginBtn;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageIcon = findViewById(R.id.iconImage);
        linearLayout = findViewById(R.id.linearLayout);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.logInBtn);

        fAuth = FirebaseAuth.getInstance();

//        If user is already logged in go to main activity
        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }

//        Some animations for learning purpose
        linearLayout.animate().alpha(0f).setDuration(1);
        TranslateAnimation animation= new TranslateAnimation(0 ,0 ,0,-1000);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        imageIcon.setAnimation(animation);

//        Choose between login or register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    private  class  MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            imageIcon.clearAnimation();
            imageIcon.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}