package com.subhajitkar.commercial.project_neela.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

import pl.droidsonroids.gif.GifImageView;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private GifImageView splashGif;
    private LinearLayout updatesLayout, errorLayout;
    private TextView title;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updatesLayout = view.findViewById(R.id.layout_updates);
        errorLayout = view.findViewById(R.id.layout_error);
        title = view.findViewById(R.id.home_title);
        //set the title with username
        String user;
        if (!StaticFields.userCredentials.getUserName().isEmpty()){
            user = StaticFields.userCredentials.getUserName();
        }else{
            user = "User";
        }
        title.setText(String.format("Welcome back,\n%s", StaticFields.capitalizeString(user)));

        setupGifImage(view);
        checkNetworkAvailability();
    }

    private void setupGifImage(View view){
        Log.d(TAG, "setupGifImage: setting up gif avatar");
        //widgets init
        splashGif = view.findViewById(R.id.splash_gif);
    }

    private void checkNetworkAvailability(){
        Log.d(TAG, "checkNetworkAvailability: log message");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
                NetworkInfo.State state = info.getState();

                if (state == NetworkInfo.State.CONNECTED) {
                    //tasks when connection is back
                    hideError();
                } else {
                    //handle network error when connection is gone
                    showError();
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    private void showError(){
        Log.d(TAG, "showError: showing error layout");
        updatesLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void hideError(){
        Log.d(TAG, "hideError: hiding error layout");
        updatesLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}