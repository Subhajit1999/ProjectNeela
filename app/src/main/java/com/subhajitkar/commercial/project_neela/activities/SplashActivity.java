package com.subhajitkar.commercial.project_neela.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.subhajitkar.commercial.project_neela.R;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private GifImageView splashGif;
    private ImageView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        setupGifImage();
        setTitlePathView();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, SetupActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void setTitlePathView(){
        title = findViewById(R.id.splash_title);
        // gif image anim
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        title.startAnimation(aniFade);
    }

    private void setupGifImage(){
        //widgets init
        splashGif = findViewById(R.id.splash_gif);
        // gif image anim
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        splashGif.startAnimation(aniFade);
    }
}