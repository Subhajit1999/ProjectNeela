package com.subhajitkar.commercial.project_neela.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.adapters.SliderAdapter;
import com.subhajitkar.commercial.project_neela.objects.User;
import com.subhajitkar.commercial.project_neela.utils.NonSwipableViewPager;
import com.subhajitkar.commercial.project_neela.utils.PreferenceManager;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

import pl.droidsonroids.gif.GifImageView;

public class SetupActivity extends AppCompatActivity implements NonSwipableViewPager.OnPageChangeListener{
    private static final String TAG = "SetupActivity";

    private GifImageView splashGif;
    private NonSwipableViewPager sliderPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout dotsLayout;
    private TextView[] mDots;
    private int mCurrentPage;
    private Button prevBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticFields.setStatusBarBackground(this, R.color.blue_5);
        setContentView(R.layout.activity_setup);

        //widgets init
        sliderPager = findViewById(R.id.slider_viewpager);
        dotsLayout = findViewById(R.id.dots_layout);
        sliderAdapter = new SliderAdapter(this, this);
        //setup viewpager with adapter
        sliderPager.setAdapter(sliderAdapter);
        sliderPager.setOnPageChangeListener(this);

        //methods
        setupGifImage();
        addDotsIndicator(0);
        buttonsHandler();
    }

    public void buttonsHandler(){
        Log.d(TAG, "buttonsHandler: handling buttons events");
        prevBtn = findViewById(R.id.bt_prev);
        nextBtn = findViewById(R.id.bt_next);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderPager.setCurrentItem(mCurrentPage - 1);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = StaticFields.userCredentials;
                if (mCurrentPage==0 && !user.getUserName().isEmpty() && user.getUserAge()>0 && !user.getUserGender().isEmpty()){
                    sliderPager.setCurrentItem(mCurrentPage + 1);

                }else if (mCurrentPage==1 && sliderAdapter.getCheckedChips(0)>=3 && sliderAdapter.getCheckedChips(0)<=5){
                    sliderPager.setCurrentItem(mCurrentPage + 1);

                }else if (mCurrentPage==2 && sliderAdapter.getCheckedChips(1)>=3 && sliderAdapter.getCheckedChips(1)<=5){
                    sliderPager.setCurrentItem(mCurrentPage + 1);

                }else if(mCurrentPage==3){
                    //saving user data in prefs
                    new PreferenceManager(SetupActivity.this).setUserPrefs(StaticFields.userCredentials);
                    Intent i = new Intent(SetupActivity.this, MainActivity.class);
                    startActivity(i);
                    SetupActivity.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(), "PLease fill all the required details correctly.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupGifImage(){
        Log.d(TAG, "setupGifImage: setting up gif avatar");
        //widgets init
        splashGif = findViewById(R.id.splash_gif);
    }

    private void addDotsIndicator(int pos){
        Log.d(TAG, "addDotsIndicator: adding dot indicators");
        mDots = new TextView[4];
        dotsLayout.removeAllViews();

        for(int i = 0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(36);
            mDots[i].setTextColor(getResources().getColor(R.color.blue_5));
            dotsLayout.addView(mDots[i]);
        }
        if (mDots.length > 0){
            mDots[pos].setText(Html.fromHtml("&#8226;"));
            mDots[pos].setTextColor(getResources().getColor(R.color.blue_2));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: viewpager pageSelected callback");
        addDotsIndicator(position);
        mCurrentPage = position;
        if (position == 0){
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(false);
            prevBtn.setVisibility(View.INVISIBLE);

            prevBtn.setText("");
            nextBtn.setText(R.string.setup_next);
        }else if (position == mDots.length - 1){
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
            prevBtn.setVisibility(View.VISIBLE);

            prevBtn.setText(R.string.setup_back);
            nextBtn.setText(R.string.setup_finish);
        }else{
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
            prevBtn.setVisibility(View.VISIBLE);

            prevBtn.setText(R.string.setup_back);
            nextBtn.setText(R.string.setup_next);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}