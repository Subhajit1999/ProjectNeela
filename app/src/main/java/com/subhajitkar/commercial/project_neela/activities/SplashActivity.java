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

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        setupImageViews();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, SetupActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.tran_slide_up,R.anim.tran_fade_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void setupImageViews(){
        //widgets init
        ImageView avatar = findViewById(R.id.iv_splash_avatar);
        ImageView dialogflow = findViewById(R.id.iv_splash_dialogflow);

        //images entrance anim
        Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        avatar.startAnimation(animFade);
        dialogflow.startAnimation(animFade);
    }
}